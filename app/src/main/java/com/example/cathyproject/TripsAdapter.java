package com.example.cathyproject;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.ViewHolder> {
    private OnNoteListener onNoteListener;
    private List<Trips> retrievedResponses;
    private Context context;

    public TripsAdapter(List<Trips> retrievedResponses, OnNoteListener onNoteListener) {
        this.retrievedResponses = retrievedResponses;
        this.onNoteListener = onNoteListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        OnNoteListener onNoteListener;
        private TextView txttripid;
        private TextView txtamount;
        private TextView txttripname;
        private TextView last_name_field;

        public ViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);



            txttripid = itemView.findViewById(R.id.txt_tripid);
            txttripname = itemView.findViewById(R.id.txt_Tripname);
            txtamount = itemView.findViewById(R.id.txt_amount);
            itemView.setOnClickListener(this);
            this.onNoteListener = onNoteListener;

        }
        @Override
        public void onClick(View v) {
            this.onNoteListener.onNoteClick(getAdapterPosition());
        }
        public void setdestinationid(String TripId) {
            txttripid.setText(TripId);
        }

        public void settripname(String Tripname) {
            txttripname.setText(Tripname);
        }

        public void setamount(String Amount) {
            txtamount.setText(Amount);
        }


    }

    @NonNull
    @Override
    public TripsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_trips, parent, false);
        return new ViewHolder(view,this.onNoteListener);
    }

    @Override
    public void onBindViewHolder( TripsAdapter.ViewHolder holder, int position) {



        holder.setdestinationid(String.valueOf(retrievedResponses.get(position).getTripId()));
        holder.setamount(String.valueOf(retrievedResponses.get(position).getAmount()));
        holder.settripname(String.valueOf(retrievedResponses.get(position).getTripname()));

    }

    @Override
    public int getItemCount() {
        return retrievedResponses.size();
    }

    public interface OnNoteListener{
        void onNoteClick(int position);
    }
}