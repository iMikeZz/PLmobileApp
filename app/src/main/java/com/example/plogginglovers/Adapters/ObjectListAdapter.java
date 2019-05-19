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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.plogginglovers.Helpers.InputFilterMinMax;
import com.example.plogginglovers.Model.Rubbish;
import com.example.plogginglovers.R;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.List;

public class ObjectListAdapter extends ArrayAdapter<Rubbish> {

    private static final long REP_DELAY = 50;

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


    public ObjectListAdapter(@NonNull Activity context, int resource, @NonNull List<Rubbish> objects) {
        super(context, resource, objects);
        this.mContext = context;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder = null;
        final Rubbish dataModel = getItem(position);

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

        holder.image.setImageResource(dataModel.getImage());

        holder.txtPoints.setText(dataModel.getScore() + " pts");

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
                if (s.length() > 0 && Integer.parseInt(s.toString()) > 0){
                    finalHolder.buttonMinus.setEnabled(true);
                } else {
                    finalHolder.buttonMinus.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && Integer.parseInt(s.toString()) > 0){
                    dataModel.setQuantity(Integer.parseInt(s.toString()));
                    /*
                    if (pointsListener != null){
                        pointsListener.onPointsAddedListener(dataModel.getScore() * Integer.parseInt(s.toString()));
                    }
                    */
                }
            }
        });

        //System.out.println(holder.quantity.getText().toString());
        /*

        if (holder.quantity.getText().toString().equals("100")){
            //todo set clickable false no botao plus
            holder.buttonPlus.setEnabled(false);
        }
        */

        /*
        KeyboardVisibilityEvent.setEventListener(mContext,
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        // some code depending on keyboard visiblity status
                        if (isOpen){
                            System.out.println("1");
                            // Toast.makeText(getApplicationContext(), "abri", Toast.LENGTH_SHORT).show();
                        } else {
                            //Toast.makeText(getApplicationContext(), "fechei", Toast.LENGTH_SHORT).show();
                            //finalHolder.quantity.clearFocus();
                            System.out.println("0 " + position + " " + finalHolder.quantity.getText() + " " + dataModel.getQuantity());
                            if (pointsListener != null){
                                pointsListener.onPointsAddedListener(dataModel.getScore() * dataModel.getQuantity());
                            }
                        }
                    }
                });
                */


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
        private Rubbish dataModel;

        public RptUpdater(ViewHolder holder, Rubbish dataModel) {
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

    private void decrement(ViewHolder holder, Rubbish dataModel) {
        long quantity = dataModel.getQuantity();
        if (quantity == 0){
            mAutoDecrement = false;
        } else {
            dataModel.setQuantity(--quantity);
            holder.quantity.setText(String.valueOf(quantity));
            System.out.println("Removi 100 pontos");
            if (pointsListener != null) {
                pointsListener.onPointsRemovedListener(dataModel.getScore());
            }
        }
    }

    private void increment(ViewHolder holder, Rubbish dataModel){
        long quantity = dataModel.getQuantity();
        if (quantity == 999){
            mAutoIncrement = false;
        } else {
            dataModel.setQuantity(++quantity);
            holder.quantity.setText(String.valueOf(quantity));
            System.out.println("Adicionei 100 pontos");
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
