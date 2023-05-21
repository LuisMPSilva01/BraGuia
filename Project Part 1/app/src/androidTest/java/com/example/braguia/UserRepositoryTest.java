package com.example.braguia;

import static androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.util.Log;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
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
import java.util.concurrent.TimeoutException;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserRepositoryTest {
    UserRepository userRepository;

    @Before
    public void setup() {
        userRepository = new UserRepository(ApplicationProvider.getApplicationContext(),true);
    }

    @Test
    public void testInsertAndGetUser() throws Throwable {
        CountDownLatch latch = new CountDownLatch(1);
        User user = new User("John", "Premium");
        userRepository.setUser(user);
        userRepository.insert(user);

        // Define an observer to check the result of the getUser method
        Observer<User> userObserver = new Observer<>() {
            @Override
            public void onChanged(User u) {
                assertNotNull(u);
                assertEquals(u, user);
                latch.countDown();
                userRepository.getUser().removeObserver(this);
            }
        };

        runOnUiThread(() -> {
            // Add the observer to the getUser LiveData
            userRepository.getUser().observeForever(userObserver);
        });

        // Wait for the observation to complete
        latch.await(5, TimeUnit.SECONDS);
        if (latch.getCount() > 0) {
            throw new TimeoutException("Latch timed out");
        }
    }

}
