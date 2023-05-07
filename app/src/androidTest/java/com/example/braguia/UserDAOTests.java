package com.example.braguia;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.braguia.model.GuideDatabase;
import com.example.braguia.model.user.User;
import com.example.braguia.model.user.UserDAO;
import com.example.braguia.model.user.UserUpdater;

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

@RunWith(AndroidJUnit4.class)
public class UserDAOTests {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private GuideDatabase database;
    private UserDAO userDao;

    @Before
    public void setup() {
        // Create an in-memory database for testing
        database = Room.inMemoryDatabaseBuilder(
                        ApplicationProvider.getApplicationContext(),
                        GuideDatabase.class)
                .allowMainThreadQueries()
                .build();

        // Get the UserDAO instance from the database
        userDao = database.userDAO();
    }

    @After
    public void cleanup() {
        // Close the in-memory database after each test
        database.close();
    }

    @Test
    public void testInsertAndGetUser() throws InterruptedException {
        // Create a user
        User user = new User("John", "Premium","","");

        // Insert the user into the database
        userDao.insert(user);

        // Retrieve the user using LiveData and observe changes
        LiveData<User> userLiveData = userDao.getUserByUsername("John");
        CountDownLatch latch = new CountDownLatch(1);
        userLiveData.observeForever(u -> {
            assertNotNull(u);
            assertEquals(user.getUsername(), u.getUsername());
            latch.countDown();
        });

        // Wait for the observation to complete
        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    public void testInsertAndGetUsers() throws InterruptedException {
        // Create a user
        User user1 = new User("John", "Premium","","");
        User user2 = new User("Wick", "Premium","","");
        // Insert the user into the database
        userDao.insert(user1);
        userDao.insert(user2);
        // Retrieve the user using LiveData and observe changes
        LiveData<User> userLiveData1 = userDao.getUserByUsername("John");

        userLiveData1.observeForever(u -> {
            assertNotNull(u);
            assertEquals(user1.getUsername(), u.getUsername());
        });

        LiveData<User> userLiveData2 = userDao.getUserByUsername("Wick");
        CountDownLatch latch = new CountDownLatch(1);
        userLiveData2.observeForever(u -> {
            assertNotNull(u);
            assertEquals(user2.getUsername(), u.getUsername());
            latch.countDown();
        });

        // Wait for the observation to complete
        latch.await(3, TimeUnit.SECONDS);
    }



    @Test
    public void updateTrailHistory() throws InterruptedException {
        // Insert a user into the database
        User user = new User("John", "Premium","","");
        userDao.insert(user);

        // Update the trail history of the user
        List<Integer> ids = new ArrayList<>();
        ids.add(1);
        String newTrailHistory =  User.convertListToString(ids);

        userDao.updateTrailHistory(user.getUsername(), newTrailHistory);

        LiveData<User> userLiveData = userDao.getUserByUsername("John");
        CountDownLatch latch = new CountDownLatch(1);
        userLiveData.observeForever(u -> {
            assertNotNull(u);
            assertEquals(ids, u.getTrailHistoryList());
            latch.countDown();
        });

        // Wait for the observation to complete
        latch.await(2, TimeUnit.SECONDS);
    }


    @Test
    public void testUpdatePinHistory() throws InterruptedException {
        // Insert a user into the database
        User user = new User("John", "Premium","","");
        userDao.insert(user);

        // Update the trail history of the user
        List<Integer> ids = new ArrayList<>();
        ids.add(1);
        String newPinHistory =  User.convertListToString(ids);
        userDao.updatePinHistory(user.getUsername(), newPinHistory);

        LiveData<User> userLiveData = userDao.getUserByUsername("John");
        CountDownLatch latch = new CountDownLatch(1);
        userLiveData.observeForever(u -> {
            assertNotNull(u);
            assertEquals(ids, u.getPinHistoryList());
            latch.countDown();
        });

        // Wait for the observation to complete
        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    public void userAdapterUpdate() throws InterruptedException, TimeoutException {
        // Insert a user into the database
        User user = new User("John", "Premium","","");
        userDao.insert(user);
        // Update the trail history of the user
        List<Integer> ids = new ArrayList<>();
        ids.add(1);
        String newTrailHistory =  User.convertListToString(ids);

        userDao.updateTrailHistory(user.getUsername(), newTrailHistory);


        UserUpdater userUpdater = new UserUpdater("John","a","b","@",true,true,"premium",true);
        LiveData<User> userLiveData = userDao.getUserByUsername("John");
        CountDownLatch latch = new CountDownLatch(1);
        userLiveData.observeForever(u -> {
            assertNotNull(u);
            assertEquals(ids, u.getTrailHistoryList());
            latch.countDown();
        });

        // Wait for the observation to complete
        latch.await(2, TimeUnit.SECONDS);

        CountDownLatch latch2 = new CountDownLatch(1);

        userDao.update(userUpdater);
        userLiveData = userDao.getUserByUsername("John");
        userLiveData.observeForever(u -> {
            if(u.compativel(userUpdater)) {
                assertNotNull(u);
                assertEquals(ids, u.getTrailHistoryList());
                latch2.countDown();
            }
        });
        // Wait for the observation to complete
        latch2.await(10, TimeUnit.SECONDS);
        if (latch.getCount()+latch2.getCount() > 0) {
            throw new TimeoutException("Latch timed out");
        }
    }

    @Test
    public void userInsertOrUpdate() throws InterruptedException, TimeoutException {
        // Insert a user into the database
        User user = new User("John", "Premium","1","1");
        userDao.insertOrUpdate(user);

        LiveData<User> userLiveData = userDao.getUserByUsername("John");
        CountDownLatch latch = new CountDownLatch(1);
        LiveData<User> finalUserLiveData = userLiveData;
        Observer<User> userObserver = new Observer<User>() {
            @Override
            public void onChanged(User u) {
                assertNotNull(u);
                assertEquals(user, u);
                latch.countDown();
                finalUserLiveData.removeObserver(this);
            }
        };

        userLiveData.observeForever(userObserver);

        // Wait for the observation to complete
        latch.await(2, TimeUnit.SECONDS);



        CountDownLatch latch2 = new CountDownLatch(1);
        User user2 = new User("John", "Standard","2","2");
        userDao.insertOrUpdate(user2);
        userLiveData = userDao.getUserByUsername("John");
        userLiveData.observeForever(u -> {
            assertNotNull(u);
            assertEquals(new User("John", "Standard","1","1"), u); //trail history and pin history remain the same on updates
            latch2.countDown();
        });
        // Wait for the observation to complete
        latch2.await(10, TimeUnit.SECONDS);
        if (latch.getCount()+latch2.getCount() > 0) {
            throw new TimeoutException("Latch timed out");
        }
    }
}