package com.example.braguia;


import static androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.braguia.model.TrailMetrics.TrailMetrics;
import com.example.braguia.model.TrailMetrics.TrailMetricsDAO;
import com.example.braguia.model.trails.Trail;
import com.example.braguia.model.trails.TrailDAO;
import com.example.braguia.model.user.User;
import com.example.braguia.model.user.UserDAO;
import com.example.braguia.repositories.TrailRepository;
import com.example.braguia.repositories.UserRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@RunWith(AndroidJUnit4.class)
public class TrailMetricsTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    private TrailRepository trailRepository;
    private UserRepository userRepository;
    private TrailDAO trailDAO;
    private UserDAO userDAO;
    private TrailMetricsDAO trailMetricsDAO;

    @Before
    public void setup() throws Throwable {
        runOnUiThread(() -> {
            trailRepository = new TrailRepository(ApplicationProvider.getApplicationContext(), true);
            /*
            try {
                trailRepository.makeRequest();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }*/
            trailDAO = trailRepository.trailDAO;
            userRepository = new UserRepository(ApplicationProvider.getApplicationContext(), true);
            userDAO = userRepository.userDAO;
            trailMetricsDAO = userRepository.trailMetricsDAO;
        });
    }


    @Test
    public void insertMetrics() throws Throwable {
        CountDownLatch latch = new CountDownLatch(1);


        User user = new User("John", "Premium");
        userDAO.insert(user);
        trailDAO.insert(new Trail(1));
        trailDAO.getTrailById(1).observeForever(trail -> {
            if (trail != null) {
                userDAO.getUserByUsername("John").observeForever(dbUser -> {
                    if (dbUser != null) {
                        trailMetricsDAO.insert(new TrailMetrics(dbUser.getUsername(),trail.getId(),34,100, Arrays.asList(1,2,3)));
                        trailMetricsDAO.insert(new TrailMetrics(dbUser.getUsername(),trail.getId(),34,100, Arrays.asList(1,2,3)));
                        trailMetricsDAO.getMetricsByUsername(dbUser.getUsername()).observeForever(trailMetrics -> {
                            if(trailMetrics.size()==2){
                                latch.countDown();
                            }
                        });
                    }
                });
            }
        });

        if (!latch.await(60, TimeUnit.SECONDS)) {
            throw new TimeoutException("Timed out");
        }
    }
}

