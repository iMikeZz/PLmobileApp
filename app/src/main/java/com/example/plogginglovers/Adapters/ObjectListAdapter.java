package com.example.plogginglovers.Adapters;

import android.content.Context;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.plogginglovers.Helpers.InputFilterMinMax;
import com.example.plogginglovers.Model.RecyclingManager;
import com.example.plogginglovers.Model.Rubbish;
import com.example.plogginglovers.R;

public class ObjectListAdapter extends ArrayAdapter<Rubbish> {

    private PointsListener pointsListener;

    public interface PointsListener {
        void onPointsAddedListener(int points);

        void onPointsRemovedListener(int points);
    }

    public void setPointsListener(PointsListener pointsListener) {
        this.pointsListener = pointsListener;
    }

    private Context mContext;
    private int qty = 0;

    private class ViewHolder {
        private TextView name;
        private ImageView image;
        private EditText quantity;
        private ImageView buttonPlus;
        private ImageView buttonMinus;

        public ViewHolder(View v) {
            name = (TextView) v.findViewById(R.id.txtObjectName);
            image = (ImageView) v.findViewById(R.id.objectImage);
            quantity = (EditText) v.findViewById(R.id.editTextNumber);
            buttonPlus = (ImageView) v.findViewById(R.id.btnPlus);
            buttonMinus = (ImageView) v.findViewById(R.id.btnMinus);
        }
    }


    public ObjectListAdapter(@NonNull Context context, int resource/*, @NonNull List<Rubbish> objects*/) {
        super(context, resource/*, objects*/);
        this.mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.object_list_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        holder.quantity.setFilters(new InputFilter[]{new InputFilterMinMax("0", "100")});

        if (holder.quantity.getText().toString().equals("0")){
            //todo set clickable false no botao menos
        }

        final ViewHolder finalHolder = holder;
        holder.buttonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalHolder.quantity.setText(String.valueOf(++qty));
                System.out.println("Adicionei 100 pontos");
                if (pointsListener != null){
                    pointsListener.onPointsAddedListener(100);
                }
            }
        });

        holder.buttonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalHolder.quantity.setText(String.valueOf(--qty));
                System.out.println("Removi 100 pontos");
                if (pointsListener != null){
                    pointsListener.onPointsRemovedListener(100);
                }
            }
        });

        return convertView;
    }

    @Override
    public int getCount() {
        return 5;
    }
}
