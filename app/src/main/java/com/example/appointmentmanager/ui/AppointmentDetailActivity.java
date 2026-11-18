package com.example.appointmentmanager.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.appointmentmanager.R;
import com.example.appointmentmanager.data.DatabaseHelper;
import com.example.appointmentmanager.model.Appointment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AppointmentDetailActivity extends AppCompatActivity {
    public static final String EXTRA_APPOINTMENT_ID = "appointment_id";
    private DatabaseHelper database;
    private int appointmentId;
    private TextView txtTitle, txtLocation, txtDateTime, txtReminderStatus;
    private Switch switchReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_detail);
        database = new DatabaseHelper(this);
        appointmentId = getIntent().getIntExtra(EXTRA_APPOINTMENT_ID, -1);

        txtTitle = findViewById(R.id.txtDetailTitle);
        txtLocation = findViewById(R.id.txtDetailLocation);
        txtDateTime = findViewById(R.id.txtDetailDateTime);
        txtReminderStatus = findViewById(R.id.txtReminderStatus);
        switchReminder = findViewById(R.id.switchDetailReminder);

        switchReminder.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()) {
                database.updateReminder(appointmentId, isChecked);
                updateReminderStatus(isChecked);
                Toast.makeText(this, isChecked ? "Đã bật nhắc nhở" : "Đã tắt nhắc nhở", Toast.LENGTH_SHORT).show();
            }
        });

        Button btnBack = findViewById(R.id.btnDetailBack);
        btnBack.setOnClickListener(v -> finish());
        Button btnDelete = findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(v -> confirmDelete());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAppointment();
    }

    private void loadAppointment() {
        Appointment a = database.getAppointmentById(appointmentId);
        if (a == null) {
            Toast.makeText(this, "Lịch hẹn không còn tồn tại", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        txtTitle.setText(a.getTitle());
        txtLocation.setText(a.getLocation());
        txtDateTime.setText(formatDate(a.getDate()) + " · " + a.getTime());
        switchReminder.setChecked(a.isReminder());
        updateReminderStatus(a.isReminder());
    }

    private String formatDate(String raw) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(raw);
            return new SimpleDateFormat("dd/MM/yyyy", new Locale("vi", "VN")).format(date);
        } catch (ParseException e) {
            return raw;
        }
    }

    private void updateReminderStatus(boolean enabled) {
        txtReminderStatus.setText(enabled ? "Đang bật" : "Đang tắt");
        txtReminderStatus.setTextColor(ContextCompat.getColor(this,
                enabled ? R.color.success_text : R.color.text_secondary));
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Xóa lịch hẹn")
                .setMessage("Bạn có chắc chắn muốn xóa lịch hẹn này không?")
                .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("Xóa", (dialog, which) -> {
                    database.deleteAppointment(appointmentId);
                    Toast.makeText(this, "Đã xóa lịch hẹn", Toast.LENGTH_SHORT).show();
                    finish();
                }).show();
    }
}
