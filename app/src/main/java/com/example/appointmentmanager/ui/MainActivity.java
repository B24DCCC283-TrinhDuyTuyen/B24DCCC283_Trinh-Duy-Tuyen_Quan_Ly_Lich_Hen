package com.example.appointmentmanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appointmentmanager.R;
import com.example.appointmentmanager.data.DatabaseHelper;
import com.example.appointmentmanager.model.Appointment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper database;
    private TextView txtTotal, txtReminder;
    private View txtEmpty;
    private RecyclerView recyclerAppointments;
    private AppointmentAdapter adapter;
    private ArrayList<Appointment> appointments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = new DatabaseHelper(this);

        txtTotal = findViewById(R.id.txtTotal);
        txtReminder = findViewById(R.id.txtReminder);
        txtEmpty = findViewById(R.id.txtEmpty);
        recyclerAppointments = findViewById(R.id.recyclerAppointments);
        recyclerAppointments.setLayoutManager(new LinearLayoutManager(this));

        Button btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(v ->
                startActivity(new Intent(this, AddAppointmentActivity.class)));

        Button btnLogout = findViewById(R.id.btnLogout);
        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> confirmLogout());
        }
    }

    private void confirmLogout() {
        new AlertDialog.Builder(this)
                .setTitle("Đăng xuất")
                .setMessage("Bạn có chắc chắn muốn đăng xuất khỏi ứng dụng?")
                .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("Đăng xuất", (dialog, which) -> {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        appointments = database.getAllAppointments();
        txtTotal.setText(String.valueOf(database.countAll()));
        txtReminder.setText(String.valueOf(database.countReminders()));
        txtEmpty.setVisibility(appointments.isEmpty() ? View.VISIBLE : View.GONE);
        adapter = new AppointmentAdapter(appointments, appointment -> {
            Intent intent = new Intent(this, AppointmentDetailActivity.class);
            intent.putExtra(AppointmentDetailActivity.EXTRA_APPOINTMENT_ID, appointment.getId());
            startActivity(intent);
        });
        recyclerAppointments.setAdapter(adapter);
    }
}
