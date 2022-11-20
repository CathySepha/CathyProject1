package com.example.cathyproject;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class DropoffAdapter extends RecyclerView.Adapter<DropoffAdapter.ViewHolder> {
    private OnNoteListener onNoteListener;
    private List<Dropoffs> retrievedResponses;
    private Context context;

    public DropoffAdapter(List<Dropoffs> retrievedResponses) {
        this.retrievedResponses = retrievedResponses;
        this.onNoteListener = onNoteListener;
    }



    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        OnNoteListener onNoteListener;
        private TextView txtDropoff;
        private TextView txttripid;


        public ViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);



            txttripid = itemView.findViewById(R.id.txt_tripid);
            txtDropoff = itemView.findViewById(R.id.txt_Dropoff);

            itemView.setOnClickListener(this);
            this.onNoteListener = onNoteListener;

        }
        @Override
        public void onClick(View v) {
            this.onNoteListener.onNoteClick(getAdapterPosition());
        }
        public void setTripId(String TripId) {
            txttripid.setText(TripId);
        }
        public void setDropoffstage(String Dropoffstage) { txtDropoff.setText(Dropoffstage);
        }




    }

    @NonNull
    @Override
    public DropoffAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dropoffrow, parent, false);
        return new ViewHolder(view,this.onNoteListener);
    }

    @Override
    public void onBindViewHolder( DropoffAdapter.ViewHolder holder, int position) {
        Dropoffs dp = retrievedResponses.get(position);


        holder.setTripId(String.valueOf(dp.getTripId()));
        holder.setDropoffstage(String.valueOf(dp.getDropoffstage()));


    }

    @Override
    public int getItemCount() {
        return retrievedResponses.size();
    }

    public interface OnNoteListener{
        void onNoteClick(int position);
    }
}