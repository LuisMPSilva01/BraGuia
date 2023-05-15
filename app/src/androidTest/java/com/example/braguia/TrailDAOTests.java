package com.example.braguia;

import static androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.braguia.model.GuideDatabase;
import com.example.braguia.model.trails.EdgeTip;
import com.example.braguia.model.trails.Trail;
import com.example.braguia.model.trails.TrailDAO;
import com.example.braguia.model.user.User;
import com.example.braguia.model.user.UserDAO;
import com.example.braguia.model.user.UserUpdater;
import com.example.braguia.repositories.TrailRepository;
import com.example.braguia.repositories.UserRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;


@RunWith(AndroidJUnit4.class)
public class TrailDAOTests {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    private TrailRepository trailRepository;
    private TrailDAO trailDAO;

    @Before
    public void setup() throws Throwable {
        runOnUiThread(() -> {
            trailRepository = new TrailRepository(ApplicationProvider.getApplicationContext(), true);
            trailDAO = trailRepository.trailDAO;
        });
    }


    @Test
    public void testGetTrailById() throws Throwable {
        CountDownLatch latch = new CountDownLatch(1);

        trailRepository.getAllTrails().observeForever(allTrails -> {
            if (allTrails != null && allTrails.size() > 0) {
                LiveData<Trail> trailLiveData = trailDAO.getTrailById(allTrails.get(0).getId());
                trailLiveData.observeForever(trail -> {
                    if (trail != null) {
                        assertEquals(allTrails.get(0), trail);
                        latch.countDown();
                    }
                });
            }
        });

        if (!latch.await(10, TimeUnit.SECONDS)) {
            throw new TimeoutException("Timed out waiting for all trails to be loaded");
        }
    }

    @Test
    public void testGetPinsById() throws Throwable {
        CountDownLatch latch = new CountDownLatch(1);
        LiveData<List<Trail>> trailsLiveData = trailRepository.getAllTrails();

        Observer<List<Trail>> allTrailsObserver = new Observer<List<Trail>>() {
                    @Override
                    public void onChanged(List<Trail> allTrails) {
                        if (allTrails != null && allTrails.size() > 0) {
                            LiveData<List<EdgeTip>> pinLiveData = trailRepository.getPinsById(allTrails.get(0).getEdges().stream().map(e->e.getEdge_start().getId()).collect(Collectors.toList()));
                            pinLiveData.observeForever(pins -> {
                                if (pins != null && pins.size() > 0) {
                                    latch.countDown();
                                }
                            });
                            trailsLiveData.removeObserver(this);
                        }
                    }
        };
        trailsLiveData.observeForever(allTrailsObserver);


        if (!latch.await(10, TimeUnit.SECONDS)) {
            throw new TimeoutException("Timed out waiting for all trails to be loaded");
        }
    }

    @Test
    public void testGetTrailsById() throws Throwable {
        CountDownLatch latch = new CountDownLatch(1);

        trailRepository.getAllTrails().observeForever(allTrails -> {
            if (allTrails != null && allTrails.size() > 0) {
                List<Trail> trailSubList = allTrails.subList(0,4);

                LiveData<List<Trail>> trailLiveData = trailDAO.getTrailsById(trailSubList.stream().map(Trail::getId).collect(Collectors.toList()));
                trailLiveData.observeForever(trails -> {
                    if (trails != null & trails.size()>0) {
                        assertTrue(trails.containsAll(trailSubList));
                        latch.countDown();
                    }
                });
            }
        });

        if (!latch.await(10, TimeUnit.SECONDS)) {
            throw new TimeoutException("Timed out waiting for all trails to be loaded");
        }
    }
}
