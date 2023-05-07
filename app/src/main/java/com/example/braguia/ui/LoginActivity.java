package com.example.braguia.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    private EditText Name;
    private EditText Password;
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

        Name = findViewById(R.id.name_input);
        Password = findViewById(R.id.password_input);
        Login = findViewById(R.id.btnLogin);

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
        Log.e("DEBUG","INTENT START");
        startActivity(intent);
    }

    private void validate(String userName, String userPassword) throws IOException {
        getApplicationContext(); //Context is required to right cookies in SharedPreferences
        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.login(userName,userPassword, getApplicationContext(),new UserViewModel.LoginCallback() {
            @Override
            public void onLoginSuccess() {
                changeToMainActivity();
            }

            @Override
            public void onLoginFailure() {
                TextView txt_view = findViewById(R.id.login_failed_txt);
                txt_view.setVisibility(View.VISIBLE);
            }
        });
    }
}
