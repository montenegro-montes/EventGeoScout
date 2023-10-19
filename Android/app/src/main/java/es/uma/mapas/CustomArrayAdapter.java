package es.uma.mapas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import es.uma.Utils.Row;

/**
 * Created by monte on 23/11/15.
 */
public class CustomArrayAdapter extends ArrayAdapter<Row>  {

    Integer selected_position = -1;
    public ArrayList<Row> layerList;
    private LayoutInflater layoutInflater;

    public CustomArrayAdapter(Context context, List<Row> objects)
    {
        super(context, 0, objects);
        layoutInflater = LayoutInflater.from(context);
        layerList = new ArrayList<Row>();
        layerList.addAll(objects);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // holder pattern
        Holder holder = null;
        if (convertView == null)
        {

            holder = new Holder();

            convertView = layoutInflater.inflate(R.layout.row, null);
            holder.setTextViewLayer((TextView) convertView.findViewById(R.id.Layers));
            holder.setTextViewType((TextView) convertView.findViewById(R.id.Type));
            holder.setTextViewNelements((TextView) convertView.findViewById(R.id.Elements));
            holder.setCheckBox((CheckBox) convertView.findViewById(R.id.checkBox));
            convertView.setTag(holder);


        }
        else
        {
            holder = (Holder) convertView.getTag();
        }

        Row row = getItem(position);
        holder.getTextViewLayer().setText(row.getLayer());

        switch (row.getType()){
            case 0: holder.getTextViewType().setText("Point");break;
            case 1: holder.getTextViewType().setText("LineString"); break;
            case 2: holder.getTextViewType().setText("Polygon"); break;
        }

        holder.getTextViewNelements().setText("" + row.getNelements());

        holder.getCheckBox().setTag(row);

        if(position==selected_position)
            holder.getCheckBox().setChecked(true);
        else
            holder.getCheckBox().setChecked(false);

        final CheckBox chkbox=(CheckBox) convertView.findViewById(R.id.checkBox);

        chkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) view).isChecked())
                {
                    int size=getCount();
                    for (int i=0;i<size;i++){
                        getItem(i).setChecked(false);
                    }

                    Row positions = (Row) view.getTag();
                    positions.setChecked(true);
                    selected_position= position;

               }
                else
                {
                    selected_position=-1;
                }

                notifyDataSetChanged();
            }
        });


        return convertView;
    }

    static class Holder {
        TextView mtextViewLayer,  mtextViewType, mtextViewNelements;
        CheckBox mcheckBox;

        public TextView getTextViewLayer()
        {
            return mtextViewLayer;
        }

        public void setTextViewLayer(TextView textViewLayer)
        {
            mtextViewLayer = textViewLayer;
        }

        public TextView getTextViewType()
        {
            return mtextViewType;
        }

        public void setTextViewType(TextView textViewType)
        {
            mtextViewType = textViewType;
        }

        public CheckBox getCheckBox()
        {
            return mcheckBox;
        }

        public void setCheckBox(CheckBox checkBox)
        {
            mcheckBox = checkBox;
        }

        public TextView getTextViewNelements()
        {
            return mtextViewNelements;
        }

        public void setTextViewNelements(TextView textViewNelements)
        {
            mtextViewNelements = textViewNelements;
        }

    }
}