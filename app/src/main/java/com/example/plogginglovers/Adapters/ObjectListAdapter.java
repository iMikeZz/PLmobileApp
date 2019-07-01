package com.example.plogginglovers.Adapters;

import android.app.Activity;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.plogginglovers.Helpers.InputFilterMinMax;
import com.example.plogginglovers.Model.RubbishParcelable;
import com.example.plogginglovers.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ObjectListAdapter extends ArrayAdapter<RubbishParcelable> {

    private static final long REP_DELAY = 50; //aumentar ou diminuir o rate

    private String activity_state;

    //region Interface
    private PointsListener pointsListener;

    public interface PointsListener {
        void onPointsAddedListener(long points);

        void onPointsRemovedListener(long points);
    }

    public void setPointsListener(PointsListener pointsListener) {
        this.pointsListener = pointsListener;
    }

    //endregion

    private Activity mContext;
    private Handler repeatUpdateHandler = new Handler();
    private boolean mAutoIncrement = false;
    private boolean mAutoDecrement = false;

    //region ViewHolder

    private class ViewHolder {
        private TextView name;
        private ImageView image;
        private TextView quantity;
        private ImageView buttonPlus;
        private ImageView buttonMinus;
        private TextView txtPoints;

        public ViewHolder(View v) {
            name = (TextView) v.findViewById(R.id.txtObjectName);
            image = (ImageView) v.findViewById(R.id.objectImage);
            quantity = (TextView) v.findViewById(R.id.editTextNumber);
            buttonPlus = (ImageView) v.findViewById(R.id.btnPlus);
            buttonMinus = (ImageView) v.findViewById(R.id.btnMinus);
            txtPoints = (TextView) v.findViewById(R.id.txtPointsObject);
        }
    }

    //endregion


    public ObjectListAdapter(@NonNull Activity context, int resource, @NonNull List<RubbishParcelable> objects, String activity_state) {
        super(context, resource, objects);
        this.mContext = context;
        this.activity_state = activity_state;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder = null;
        final RubbishParcelable dataModel = getItem(position);

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.object_list_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        final ViewHolder finalHolder = holder;

        holder.quantity.setFilters(new InputFilter[]{new InputFilterMinMax("0", "999")});

        holder.name.setText(dataModel.getName());

        holder.quantity.setText(String.valueOf(dataModel.getQuantity()));

        if (dataModel.getImage() == null){
            Picasso.get().load("http://46.101.15.61/storage/misc/item-default.jpg").into(holder.image);
        }else {
            Picasso.get().load("http://46.101.15.61/storage/items/" + dataModel.getImage()).into(holder.image);
        }
        //Picasso.get().load("http://46.101.15.61/storage/misc/item-default.jpg").into(holder.image);
        //holder.image.setImageResource(R.drawable.bootle);

        holder.txtPoints.setText(dataModel.getScore() + " pts");

        if (activity_state.equals("pending_accepted") || activity_state.equals("terminated_accepted")){
            holder.buttonMinus.setEnabled(false);
            holder.buttonPlus.setEnabled(false);
        }

        if (holder.quantity.getText().toString().equals("0")){
            holder.buttonMinus.setEnabled(false);
        }


        holder.quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //System.out.println(s);
                if (s.length() > 0 && Integer.parseInt(s.toString()) > 0 && Integer.parseInt(s.toString()) <= 999){
                    finalHolder.buttonMinus.setEnabled(true);
                } else {
                    finalHolder.buttonMinus.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        holder.buttonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrement(finalHolder, dataModel);
            }
        });

        holder.buttonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increment(finalHolder, dataModel);
            }
        });

        holder.buttonPlus.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mAutoIncrement = true;
                repeatUpdateHandler.post( new RptUpdater(finalHolder, dataModel) );
                return false;
            }
        });


        holder.buttonPlus.setOnTouchListener( new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if( (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
                        && mAutoIncrement ){
                    mAutoIncrement = false;
                }
                return false;
            }
        });

        holder.buttonMinus.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mAutoDecrement = true;
                repeatUpdateHandler.post(new RptUpdater(finalHolder, dataModel));
                return false;
            }
        });


        holder.buttonMinus.setOnTouchListener( new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if( (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
                        && mAutoDecrement){
                    mAutoDecrement = false;
                }
                return false;
            }
        });

        return convertView;
    }

    public class RptUpdater implements Runnable {
        private ViewHolder holder;
        private RubbishParcelable dataModel;

        public RptUpdater(ViewHolder holder, RubbishParcelable dataModel) {
            this.holder = holder;
            this.dataModel = dataModel;
        }

        @Override
        public void run() {
            if(mAutoIncrement){
                increment(holder, dataModel);
                repeatUpdateHandler.postDelayed( new RptUpdater(holder, dataModel), REP_DELAY );
            } else if(mAutoDecrement){
                decrement(holder, dataModel);
                repeatUpdateHandler.postDelayed( new RptUpdater(holder, dataModel), REP_DELAY );
            }
        }
    }

    private void decrement(ViewHolder holder, RubbishParcelable dataModel) {
        int quantity = dataModel.getQuantity();
        if (quantity == 0){
            mAutoDecrement = false;
        } else {
            dataModel.setQuantity(--quantity);
            holder.quantity.setText(String.valueOf(quantity));
            //System.out.println("Removi 100 pontos");
            if (pointsListener != null) {
                pointsListener.onPointsRemovedListener(dataModel.getScore());
            }
        }
    }

    private void increment(ViewHolder holder, RubbishParcelable dataModel){
        int quantity = dataModel.getQuantity();
        if (quantity == 999){
            mAutoIncrement = false;
        } else {
            dataModel.setQuantity(++quantity);
            holder.quantity.setText(String.valueOf(quantity));
            //System.out.println("Adicionei 100 pontos");
            if (pointsListener != null) {
                pointsListener.onPointsAddedListener(dataModel.getScore());
            }
        }
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}
