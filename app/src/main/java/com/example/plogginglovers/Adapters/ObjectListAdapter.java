package com.example.plogginglovers.Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
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
import com.example.plogginglovers.Model.Contact;
import com.example.plogginglovers.Model.RecyclingManager;
import com.example.plogginglovers.Model.Rubbish;
import com.example.plogginglovers.R;

import java.util.List;

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


    public ObjectListAdapter(@NonNull Context context, int resource, @NonNull List<Rubbish> objects) {
        super(context, resource, objects);
        this.mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
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

        if (holder.quantity.getText().toString().equals("0")){
            //todo set clickable false no botao menos
            holder.buttonMinus.setEnabled(false);
        }


        holder.quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println(s);
                if (s.length() > 0 && Integer.parseInt(s.toString()) > 0){
                    finalHolder.buttonMinus.setEnabled(true);
                } else {
                    finalHolder.buttonMinus.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        /*

        if (holder.quantity.getText().toString().equals("100")){
            //todo set clickable false no botao plus
            holder.buttonPlus.setEnabled(false);
        }
        */
        holder.buttonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = dataModel.getQuantity();
                dataModel.setQuantity(--quantity);
                finalHolder.quantity.setText(String.valueOf(quantity));
                System.out.println("Removi 100 pontos");
                if (pointsListener != null) {
                    pointsListener.onPointsRemovedListener(dataModel.getScore());
                }
            }
        });

        holder.buttonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = dataModel.getQuantity();
                dataModel.setQuantity(++quantity);
                finalHolder.quantity.setText(String.valueOf(quantity));
                System.out.println("Adicionei 100 pontos");
                if (pointsListener != null){
                    pointsListener.onPointsAddedListener(dataModel.getScore());
                }
            }
        });



        return convertView;
    }
}
