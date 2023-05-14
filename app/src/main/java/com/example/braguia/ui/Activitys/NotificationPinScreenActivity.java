package com.example.braguia.ui.Activitys;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;

import com.example.braguia.R;
import com.example.braguia.model.trails.EdgeTip;
import com.example.braguia.ui.Fragments.PinFragment;

public class NotificationPinScreenActivity extends AppCompatActivity {
    public NotificationPinScreenActivity(){
    }

    public static NotificationPinScreenActivity newInstance() {
        return new NotificationPinScreenActivity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificationpin);

        if(getIntent()!=null) {
            if (getIntent().hasExtra("EdgeTip")) {
                EdgeTip edgeTip = (EdgeTip) getIntent().getSerializableExtra("EdgeTip");
                Bundle bundle = new Bundle();
                bundle.putInt("id", edgeTip.getId());

                PinFragment fragment = PinFragment.newInstance(edgeTip.getId());
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.conteudo_pin,fragment);
                fragmentTransaction.commit();
            }
        }
    }
}
