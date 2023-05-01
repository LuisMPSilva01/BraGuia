package com.example.braguia.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.braguia.R;
import com.example.braguia.viewmodel.UserViewModel;

import java.io.IOException;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private UserViewModel userViewModel;
    private EditText Name;
    private EditText Password;
    private TextView Info;
    private Button Login;

    public LoginActivity(){
    }

    public static LoginActivity newInstance() {
        return new LoginActivity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        try {
            userViewModel.getUser().observe(this, user -> {
                if (user != null && Objects.equals(user.getUser_type(), "premium")) {
                    changeToMainActivity();
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Name = findViewById(R.id.etName);
        Password = findViewById(R.id.etPassword);
        Info = findViewById(R.id.tvInfo);
        Login = findViewById(R.id.btnLogin);

        Info.setText("REMOVE ME");

        Login.setOnClickListener(view -> {
            try {
                validate(Name.getText().toString(), Password.getText().toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void changeToMainActivity(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void validate(String userName, String userPassword) throws IOException {
        getApplicationContext(); //Context is required to right cookies in SharedPreferences
        userViewModel.login(userName,userPassword, getApplicationContext(),new UserViewModel.LoginCallback() {
            @Override
            public void onLoginSuccess() {
                changeToMainActivity();
            }

            @Override
            public void onLoginFailure() {
            }
        });
    }
}
