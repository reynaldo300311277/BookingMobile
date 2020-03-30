package com.example.bookingmobile;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyRecyclerViewAdapterRooms extends
        RecyclerView.Adapter<MyRecyclerViewAdapterRooms.ViewHolder>
{
    private List<String[]> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private RadioButton lastCheckedRB = null;



    // data is passed into the constructor
    MyRecyclerViewAdapterRooms(Context context, CHotel cHotel) {//List<String[]> data) {
        this.mInflater = LayoutInflater.from(context);


        ArrayList<CRoom> cRooms = cHotel.getArrayRooms();

        //ArrayList<String[]> rooms  = new ArrayList<>();
        this.mData = new ArrayList<>();

        for (int i=0; i<cRooms.size(); i++)
        {
            String roomType = cRooms.get(i).getType();
            String price = String.valueOf(cRooms.get(i).getPrice());

            this.mData.add(new String[]{roomType, price});
        }
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String[] roomType = mData.get(position);

        holder.rdRoomType.setText(roomType[0]);
        holder.myPrice.setText("$ " + roomType[1] + " ");

        holder.rdGroupRooms.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checked_rb = (RadioButton) group.findViewById(checkedId);
                if (lastCheckedRB != null) {
                    lastCheckedRB.setChecked(false);
                }
                //store the clicked radiobutton
                lastCheckedRB = checked_rb;
            }
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myPrice;
        RadioButton rdRoomType;
        ImageView iconInfo;
        ImageView iconPhoto;
        RadioGroup rdGroupRooms;

        ViewHolder(View itemView) {
            super(itemView);

            rdGroupRooms = itemView.findViewById(R.id.radioGroupRoom);
            rdRoomType = itemView.findViewById(R.id.rdbtnRoomType);
            myPrice = itemView.findViewById(R.id.txtPrice);
            iconInfo = itemView.findViewById(R.id.iconInfo);
            iconPhoto = itemView.findViewById(R.id.iconPhoto);

            iconPhoto.setOnClickListener(this);
            iconInfo.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null)
                mClickListener.onItemClick(view, getAdapterPosition());

            if (view.getId() == iconInfo.getId()) {

                // *********************************************************
                // LUGAR PARA CHAMAR O POP-UP DE INFORMAÇÕES DO QUARTO !!!!!
                // *********************************************************

                Toast.makeText(view.getContext(), "INFO PRESSED => " +
                        String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
            } else {

                // *********************************************************
                // LUGAR PARA CHAMAR O POP-UP DE FOTOGRAFIAS DO QUARTO !!!!!
                // *********************************************************

                Toast.makeText(view.getContext(), "PHOTO PRESSED => " +
                        String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
            }
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id).toString();
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
