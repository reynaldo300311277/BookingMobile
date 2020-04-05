package com.example.bookingmobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyRecyclerViewAdapterHotels extends  RecyclerView.Adapter<MyRecyclerViewAdapterHotels.ViewHolder>
{
    private ArrayList<CHotel> arrayListHotels;
    private List<String[]> mData;
    private LayoutInflater mInflater;
    private MyRecyclerViewAdapterHotels.ItemClickListener mClickListener;

    private Context context;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    private static int clickedItem = -1;

    // data is passed into the constructor
    MyRecyclerViewAdapterHotels(Context context, List<String[]> data, ArrayList<CHotel> arrayListHotels) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.arrayListHotels = arrayListHotels;

        this.sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        this.editor = sharedPref.edit();
    }

    // inflates the row layout from xml when needed
    @Override
    public MyRecyclerViewAdapterHotels.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_hotel, parent, false);
        return new MyRecyclerViewAdapterHotels.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyRecyclerViewAdapterHotels.ViewHolder holder, int position) {
        String[] hotelInfoDisplay = mData.get(position);

        holder.myHotel.setText(hotelInfoDisplay[0]);
        holder.myPrice.setText("$ " + hotelInfoDisplay[1] + " ");
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myPrice;
        TextView myHotel;
        ImageView iconInfo;

        ViewHolder(View itemView) {
            super(itemView);

            myHotel = itemView.findViewById(R.id.txtHotelName);
            myPrice = itemView.findViewById(R.id.txtPrice);
            iconInfo = itemView.findViewById(R.id.iconInfo);

            iconInfo.setOnClickListener(this);
            myHotel.setOnClickListener(this);
            myPrice.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null)
                mClickListener.onItemClick(view, getAdapterPosition());

            if (view.getId() == iconInfo.getId()) {

                ArrayList<CSightseeing> arrayListSightseeing = arrayListHotels.get(getAdapterPosition()).getSightseeing();
                ArrayList<String[]> listSightseeing = new ArrayList<>();

                for (int i=0; i< arrayListSightseeing.size(); i++) {
                    String sightseeing = arrayListSightseeing.get(i).getName();
                    String distance = String.valueOf(arrayListSightseeing.get(i).getDistance());

                    listSightseeing.add(new String[] {sightseeing, distance});
                }

                ArrayList<String> arrayListFacilities = arrayListHotels.get(getAdapterPosition()).getFacilitiesHotel();

                // inflate the layout of the popup window
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.popup_info_hotel, null);

                TextView txtPopupHotelName = popupView.findViewById(R.id.txtPopupHotelName);
                txtPopupHotelName.setText(arrayListHotels.get(getAdapterPosition()).getName());

                TextView txtFacilities = popupView.findViewById(R.id.txtFacilities);
                String facilities = "** Facilities **\n ";

                for (int i=0; i< arrayListFacilities.size(); i++)
                        facilities += arrayListFacilities.get(i) + "\n";

                facilities += "\n** Sightseeing **";

                txtFacilities.setText(facilities);

                ListView listView = popupView.findViewById(R.id.lstViewSightseeing);
                listView.setAdapter(new MyPopupHotelAdapter(listSightseeing));

                // create the popup window
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                final PopupWindow popupWindow = new PopupWindow(popupView, 900, height, true);

                // show the popup window
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                // dismiss the popup window when touched
                popupView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();
                        return true;
                    }
                });
            }
            else
            {
                Intent intent = new Intent(context, ActivityRoomFilter.class);
                intent.putExtra("CHOTEL", arrayListHotels.get(getAdapterPosition()));
                context.startActivity(intent);
            }

        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id).toString();
    }

    // allows clicks events to be caught
    void setClickListener(MyRecyclerViewAdapterHotels.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
