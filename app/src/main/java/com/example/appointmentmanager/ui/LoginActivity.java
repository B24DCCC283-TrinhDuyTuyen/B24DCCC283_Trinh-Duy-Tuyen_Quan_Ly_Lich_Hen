package com.example.appointmentmanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appointmentmanager.R;
import com.example.appointmentmanager.data.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {
    private EditText edtUsername, edtPassword;
    private TextView txtError;
    private DatabaseHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        database = new DatabaseHelper(this);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        txtError = findViewById(R.id.txtError);

        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(v -> login());
    }

    private void login() {
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString();

        if (username.isEmpty()) {
            edtUsername.setError("Vui lòng nhập tên đăng nhập");
            return;
        }
        if (password.isEmpty()) {
            edtPassword.setError("Vui lòng nhập mật khẩu");
            return;
        }

        if (database.checkLogin(username, password)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            txtError.setText("Sai tên đăng nhập hoặc mật khẩu");
        }
    }
}
