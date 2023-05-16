package com.example.braguia.ui.Activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.braguia.R;
import com.example.braguia.viewmodel.UserViewModel;

import java.io.IOException;

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

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                //do nothing
            }
        };
        this.getOnBackPressedDispatcher().addCallback(this, callback);

        setContentView(R.layout.activity_login);
        View rootView = findViewById(android.R.id.content);

        Name = findViewById(R.id.name_input);
        Password = findViewById(R.id.password_input);
        Login = findViewById(R.id.btnLogin);
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Hide the keyboard and clear focus from the input form
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);
                Name.clearFocus();
                Password.clearFocus();
                return false; // Return false to allow touch events to be passed to other views
            }
        });

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
