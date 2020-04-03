package com.example.bookingmobile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

//import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyRecyclerViewAdapterBookings extends
        RecyclerView.Adapter<MyRecyclerViewAdapterBookings.ViewHolder> {

    private ArrayList<String[]> mData;
    private LayoutInflater mInflater;
    private MyRecyclerViewAdapterBookings.ItemClickListener mClickListener;
    //private Context context;

    MyRecyclerViewAdapterBookings(Context context, ArrayList<String[]> data)
    {
        //this.context = context;
        this.mData = data;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_bookings, parent, false);
        return new MyRecyclerViewAdapterBookings.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyRecyclerViewAdapterBookings.ViewHolder holder, int position) {
        String[] listBookings = mData.get(position);

        holder.hotelName.setText((position + 1) + " " + listBookings[2]);
        holder.hotelCity.setText(listBookings[3]);
        holder.roomType.setText(listBookings[4]);
        holder.dateCheckIn.setText(listBookings[5]);
        holder.dateCheckOut.setText(listBookings[6]);
    }

    @Override
    public int getItemCount() {  return mData.size();  }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView hotelName;
        TextView hotelCity;
        TextView roomType;
        TextView dateCheckIn;
        TextView dateCheckOut;

        ViewHolder(View itemView) {
            super(itemView);

            hotelName = itemView.findViewById(R.id.txtBookingHotelName);
            hotelCity = itemView.findViewById(R.id.txtBookingHotelCity);
            roomType = itemView.findViewById(R.id.txtBookingRoomType);
            dateCheckIn = itemView.findViewById(R.id.txtBookingDateIn);
            dateCheckOut = itemView.findViewById(R.id.txtBookingDateOut);
        }

        @Override
        public void onClick(View view) {
        }
    }

    // allows clicks events to be caught
    void setClickListener(MyRecyclerViewAdapterBookings.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}
