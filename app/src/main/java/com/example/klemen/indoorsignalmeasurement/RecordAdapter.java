package com.example.klemen.indoorsignalmeasurement;

import android.app.Activity;
import android.content.Context;
import android.icu.text.AlphabeticIndex;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class RecordAdapter extends BaseAdapter {

    Context recordContext;
    List<Record> recordList;

    public RecordAdapter(Context context, List<Record> records) {
        recordList = records;
        recordContext = context;
    }

    @Override
    public int getCount() {
        return recordList.size();
    }

    @Override
    public Object getItem(int i) {
        return recordList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        RecordAdapter.RecordViewHolder holder;
        if (view ==null){

            LayoutInflater recordInflater = (LayoutInflater) recordContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = recordInflater.inflate(R.layout.record, null);
            holder = new RecordAdapter.RecordViewHolder();
            holder.ageView = (TextView) view.findViewById(R.id.record_datetime);
            holder.nameView = (TextView) view.findViewById(R.id.record_cell_id);
            holder.positionView = (TextView) view.findViewById(R.id.record_SS);
            holder.addressView = (TextView) view.findViewById(R.id.record_SQ);
            view.setTag(holder);
        }else {
            holder = (RecordAdapter.RecordViewHolder) view.getTag();
        }

        Record record = (Record) getItem(i);
        holder.ageView.setText(record.datetime);
        holder.nameView.setText(record.cell_id);
        holder.positionView.setText(record.SS);
        holder.addressView.setText(record.SS);

        return view;
    }

    public void add(Record record){
        recordList.add(record);
        notifyDataSetChanged();
    }

    private static class RecordViewHolder {

        public TextView nameView;
        public TextView positionView;
        public TextView ageView;
        public TextView addressView;
    }
}




