package com.example.appointmentmanager.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.appointmentmanager.model.Appointment;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "appointments.db";
    private static final int DB_VERSION = 1;

    private static final String TABLE_USERS = "users";
    private static final String TABLE_APPOINTMENTS = "appointments";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT NOT NULL UNIQUE, " +
                "password TEXT NOT NULL)");

        db.execSQL("CREATE TABLE appointments (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT NOT NULL, " +
                "location TEXT NOT NULL, " +
                "date TEXT NOT NULL, " +
                "time TEXT NOT NULL, " +
                "reminder INTEGER NOT NULL)");

        ContentValues user = new ContentValues();
        user.put("username", "admin");
        user.put("password", "123456");
        db.insert(TABLE_USERS, null, user);

        insertSample(db, "Họp nhóm đồ án", "Phòng B203", "2026-10-25", "09:00", 1);
        insertSample(db, "Khám răng", "Nha khoa Minh Tâm", "2026-10-26", "14:30", 1);
        insertSample(db, "Đi cà phê với bạn", "Highland Coffee", "2026-10-27", "19:00", 0);
    }

    private void insertSample(SQLiteDatabase db, String title, String location,
                              String date, String time, int reminder) {
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("location", location);
        values.put("date", date);
        values.put("time", time);
        values.put("reminder", reminder);
        db.insert(TABLE_APPOINTMENTS, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Version 1: chưa có thay đổi cấu trúc.
    }

    public boolean checkLogin(String username, String password) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{"id"},
                "username = ? AND password = ?", new String[]{username, password},
                null, null, null);
        boolean result = cursor.moveToFirst();
        cursor.close();
        return result;
    }

    public long insertUser(String username, String password) {
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        return getWritableDatabase().insert(TABLE_USERS, null, values);
    }

    public long insertAppointment(Appointment a) {
        ContentValues values = new ContentValues();
        values.put("title", a.getTitle());
        values.put("location", a.getLocation());
        values.put("date", a.getDate());
        values.put("time", a.getTime());
        values.put("reminder", a.isReminder() ? 1 : 0);
        return getWritableDatabase().insert(TABLE_APPOINTMENTS, null, values);
    }

    public ArrayList<Appointment> getAllAppointments() {
        ArrayList<Appointment> list = new ArrayList<>();
        Cursor cursor = getReadableDatabase().query(TABLE_APPOINTMENTS, null,
                null, null, null, null, "date ASC, time ASC");
        try {
            while (cursor.moveToNext()) list.add(readAppointment(cursor));
        } finally {
            cursor.close();
        }
        return list;
    }

    public Appointment getAppointmentById(int id) {
        Cursor cursor = getReadableDatabase().query(TABLE_APPOINTMENTS, null,
                "id = ?", new String[]{String.valueOf(id)}, null, null, null);
        try {
            return cursor.moveToFirst() ? readAppointment(cursor) : null;
        } finally {
            cursor.close();
        }
    }

    private Appointment readAppointment(Cursor c) {
        return new Appointment(c.getInt(c.getColumnIndexOrThrow("id")),
                c.getString(c.getColumnIndexOrThrow("title")),
                c.getString(c.getColumnIndexOrThrow("location")),
                c.getString(c.getColumnIndexOrThrow("date")),
                c.getString(c.getColumnIndexOrThrow("time")),
                c.getInt(c.getColumnIndexOrThrow("reminder")) == 1);
    }

    public int updateReminder(int id, boolean on) {
        ContentValues values = new ContentValues();
        values.put("reminder", on ? 1 : 0);
        return getWritableDatabase().update(TABLE_APPOINTMENTS, values,
                "id = ?", new String[]{String.valueOf(id)});
    }

    public int deleteAppointment(int id) {
        return getWritableDatabase().delete(TABLE_APPOINTMENTS,
                "id = ?", new String[]{String.valueOf(id)});
    }

    public int countAll() {
        return countQuery("SELECT COUNT(*) FROM appointments");
    }

    public int countReminders() {
        return countQuery("SELECT COUNT(*) FROM appointments WHERE reminder = 1");
    }

    private int countQuery(String sql) {
        Cursor cursor = getReadableDatabase().rawQuery(sql, null);
        try {
            return cursor.moveToFirst() ? cursor.getInt(0) : 0;
        } finally {
            cursor.close();
        }
    }
}
