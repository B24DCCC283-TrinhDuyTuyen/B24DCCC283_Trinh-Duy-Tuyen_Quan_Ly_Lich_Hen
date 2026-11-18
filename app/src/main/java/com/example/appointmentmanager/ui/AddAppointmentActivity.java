package com.example.appointmentmanager.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appointmentmanager.R;
import com.example.appointmentmanager.data.DatabaseHelper;
import com.example.appointmentmanager.model.Appointment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddAppointmentActivity extends AppCompatActivity {
    private EditText edtTitle, edtLocation;
    private TextView txtDate, txtTime;
    private Switch switchReminder;
    private final Calendar selected = Calendar.getInstance();
    private DatabaseHelper database;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private final SimpleDateFormat displayDate = new SimpleDateFormat("dd/MM/yyyy", new Locale("vi", "VN"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment);
        database = new DatabaseHelper(this);

        edtTitle = findViewById(R.id.edtTitle);
        edtLocation = findViewById(R.id.edtLocation);
        txtDate = findViewById(R.id.txtSelectedDate);
        txtTime = findViewById(R.id.txtSelectedTime);
        switchReminder = findViewById(R.id.switchReminder);

        selected.set(Calendar.SECOND, 0);
        selected.set(Calendar.MILLISECOND, 0);
        updateDateText();
        updateTimeText();

        findViewById(R.id.btnChooseDate).setOnClickListener(v -> chooseDate());
        findViewById(R.id.btnChooseTime).setOnClickListener(v -> chooseTime());

        View layoutChooseDate = findViewById(R.id.layoutChooseDate);
        if (layoutChooseDate != null) layoutChooseDate.setOnClickListener(v -> chooseDate());
        View layoutChooseTime = findViewById(R.id.layoutChooseTime);
        if (layoutChooseTime != null) layoutChooseTime.setOnClickListener(v -> chooseTime());

        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(v -> saveAppointment());
        findViewById(R.id.btnCancel).setOnClickListener(v -> finish());
    }

    private void chooseDate() {
        DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, day) -> {
            selected.set(year, month, day);
            updateDateText();
        }, selected.get(Calendar.YEAR), selected.get(Calendar.MONTH), selected.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dialog.show();
    }

    private void chooseTime() {
        new TimePickerDialog(this, (view, hour, minute) -> {
            selected.set(Calendar.HOUR_OF_DAY, hour);
            selected.set(Calendar.MINUTE, minute);
            updateTimeText();
        }, selected.get(Calendar.HOUR_OF_DAY), selected.get(Calendar.MINUTE), true).show();
    }

    private void updateDateText() {
        txtDate.setText(displayDate.format(selected.getTime()));
    }

    private void updateTimeText() {
        txtTime.setText(String.format(Locale.getDefault(), "%02d:%02d",
                selected.get(Calendar.HOUR_OF_DAY), selected.get(Calendar.MINUTE)));
    }

    private void saveAppointment() {
        String title = edtTitle.getText().toString().trim();
        String location = edtLocation.getText().toString().trim();

        if (title.isEmpty()) {
            edtTitle.setError("Không được để trống tên lịch hẹn");
            edtTitle.requestFocus();
            return;
        }
        if (location.isEmpty()) {
            edtLocation.setError("Không được để trống địa điểm");
            edtLocation.requestFocus();
            return;
        }

        Calendar current = Calendar.getInstance();
        if (!selected.after(current)) {
            Toast.makeText(this, "Thời điểm hẹn phải sau thời điểm hiện tại", Toast.LENGTH_LONG).show();
            return;
        }

        String date = dateFormat.format(selected.getTime());
        String time = String.format(Locale.US, "%02d:%02d",
                selected.get(Calendar.HOUR_OF_DAY), selected.get(Calendar.MINUTE));
        long id = database.insertAppointment(new Appointment(title, location, date, time,
                switchReminder.isChecked()));
        if (id != -1) {
            Toast.makeText(this, "Đã lưu lịch hẹn thành công!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Không thể lưu lịch hẹn", Toast.LENGTH_SHORT).show();
        }
    }
}
