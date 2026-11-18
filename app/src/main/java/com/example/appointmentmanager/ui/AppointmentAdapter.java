package com.example.appointmentmanager.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appointmentmanager.R;
import com.example.appointmentmanager.model.Appointment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {
    public interface OnItemClickListener { void onClick(Appointment appointment); }

    private final ArrayList<Appointment> items;
    private final OnItemClickListener listener;

    public AppointmentAdapter(ArrayList<Appointment> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_appointment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        Appointment a = items.get(position);
        h.txtTitle.setText(a.getTitle());
        h.txtLocation.setText("Địa điểm: " + a.getLocation());
        h.txtDateTime.setText(formatDate(a.getDate()) + " · " + a.getTime());

        if (a.isReminder()) {
            h.txtReminder.setText("Có nhắc");
            h.txtReminder.setBackgroundResource(R.drawable.bg_reminder_on);
            h.txtReminder.setTextColor(ContextCompat.getColor(h.itemView.getContext(), R.color.success_text));
        } else {
            h.txtReminder.setText("Không nhắc");
            h.txtReminder.setBackgroundResource(R.drawable.bg_reminder_off);
            h.txtReminder.setTextColor(ContextCompat.getColor(h.itemView.getContext(), R.color.text_secondary));
        }

        h.itemView.setOnClickListener(v -> listener.onClick(a));
    }

    private String formatDate(String raw) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(raw);
            return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date);
        } catch (ParseException e) {
            return raw;
        }
    }

    @Override public int getItemCount() { return items.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtLocation, txtDateTime, txtReminder;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtItemTitle);
            txtLocation = itemView.findViewById(R.id.txtItemLocation);
            txtDateTime = itemView.findViewById(R.id.txtItemDateTime);
            txtReminder = itemView.findViewById(R.id.txtItemReminder);
        }
    }
}
