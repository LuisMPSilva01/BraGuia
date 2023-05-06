package com.example.braguia;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.util.Log;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.example.braguia.model.GuideDatabase;
import com.example.braguia.model.user.User;
import com.example.braguia.model.user.UserAPI;
import com.example.braguia.model.user.UserDAO;
import com.example.braguia.repositories.UserRepository;
import com.example.braguia.viewmodel.TrailViewModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.manipulation.Ordering;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserRepositoryTest {
    UserRepository userRepository;

    @Before
    public void setup() {
        userRepository = new UserRepository(ApplicationProvider.getApplicationContext());
    }


    @Test
    public void userDetails() throws InterruptedException, IOException {
        CountDownLatch latch = new CountDownLatch(1);
        userRepository.makeLoginRequest("premium_user", "paiduser", ApplicationProvider.getApplicationContext(), new UserRepository.LoginCallback() {
            @Override
            public void onLoginSuccess() {
                LiveData<User> userLiveData = userRepository.getUser();
            }

            @Override
            public void onLoginFailure() {
            }
        });

        latch.await(100, TimeUnit.SECONDS);
    }

}
