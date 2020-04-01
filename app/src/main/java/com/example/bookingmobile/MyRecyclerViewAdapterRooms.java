package com.example.bookingmobile;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
    private RadioButton lastCheckedRB = null;
    private CHotel hotel;
    private MyRecyclerViewAdapterRooms.ItemClickListener mClickListener;
    private Context context;
    private SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    // data is passed into the constructor
    MyRecyclerViewAdapterRooms(Context context, List<String[]> data, CHotel hotel) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.hotel= hotel;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String[] roomType = mData.get(position);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPref.edit();

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

                // update the room selected into SharedPreferences - value default was -1
                editor.putInt("ROOM_SELECTED", position);
                editor.apply();
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

                ArrayList<String> arrayListFacilities = hotel.getArrayRooms().
                        get(getAdapterPosition()).getFacilitiesRoom();

                // inflate the layout of the popup window
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.popup_info_room, null);

                TextView txtPopupHotelName = popupView.findViewById(R.id.txtPopupRoomType);
                txtPopupHotelName.setText(hotel.getArrayRooms().get(getAdapterPosition()).getType());

                TextView txtFacilities = popupView.findViewById(R.id.txtFacilitiesRoom);
                String facilities = "** Facilities **\n ";

                for (int i=0; i< arrayListFacilities.size(); i++)
                        facilities += arrayListFacilities.get(i) + "\n";

                txtFacilities.setText(facilities);

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
            else if (view.getId() == iconPhoto.getId()) {
                // **************************************************
                // PLACE TO CALL THE BEDROOM'S PHOTOGRAPHS POP-UP !!!
                // **************************************************
                // Toast.makeText(view.getContext(), "PHOTO PRESSED => " +
                //        String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
            }
            else {

            }
        }
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
