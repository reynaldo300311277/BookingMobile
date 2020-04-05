package com.example.bookingmobile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyPopupHotelAdapter extends BaseAdapter {

    ArrayList<String[]> listSightseeing = new ArrayList<>();

    public MyPopupHotelAdapter(ArrayList<String[]> mSightseeing) {
        this.listSightseeing = mSightseeing;
    }

    @Override
    public int getCount() {
        return listSightseeing.size();
    }

    @Override
    public Object getItem(int position) {
        return listSightseeing.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_row_sightseeing, parent, false);
        }

        TextView txtSightseeing = convertView.findViewById(R.id.txtSightseeing);
        TextView txtDistance = convertView.findViewById(R.id.txtDistances);

        txtSightseeing.setText(listSightseeing.get(position)[0]);
        txtDistance.setText(listSightseeing.get(position)[1] + " Km");

        return convertView;
    }
}
