package com.example.braguia.repositories;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.example.braguia.model.GuideDatabase;
import com.example.braguia.model.TrailMetrics.TrailMetrics;
import com.example.braguia.model.TrailMetrics.TrailMetricsDAO;
import com.example.braguia.model.trails.EdgeTip;
import com.example.braguia.model.trails.Trail;
import com.example.braguia.model.user.User;
import com.example.braguia.model.user.UserAPI;
import com.example.braguia.model.user.UserDAO;
import com.example.braguia.viewmodel.Services.Trip;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserRepository {
    public UserDAO userDAO;
    public TrailMetricsDAO trailMetricsDAO;
    public MediatorLiveData<User> user;
    private final GuideDatabase database;
    private final Retrofit retrofit;
    private final UserAPI api;
    private String lastUser;

    public UserRepository(Application application,Boolean freshDB) {
        if(freshDB){
            database = Room.inMemoryDatabaseBuilder(
                            ApplicationProvider.getApplicationContext(),
                            GuideDatabase.class)
                    .allowMainThreadQueries()
                    .build();
        }else {
            database = GuideDatabase.getInstance(application);
        }

        userDAO = database.userDAO();
        trailMetricsDAO = database.trailMetricsDAO();
        retrofit = new Retrofit.Builder()
                .baseUrl("https://c5a2-193-137-92-29.eu.ngrok.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(UserAPI.class);

        user = new MediatorLiveData<>();

        SharedPreferences sharedPreferences = application.getApplicationContext().getSharedPreferences("BraguiaPreferences", Context.MODE_PRIVATE);
        String cookies = sharedPreferences.getString("cookies", "");
        updateUserAPI(cookies);
        lastUser = sharedPreferences.getString("lastUser", "");

        if(lastUser==null || lastUser.equals("")){
            user.postValue(new User("", "loggedOff"));
        }
        user.addSource(userDAO.getUserByUsername(lastUser),localUser -> {
            if(lastUser==null || lastUser.equals("")){
                user.postValue(new User("", "loggedOff"));
            }
            else if(localUser!=null){
                user.postValue(localUser);
            }
        });
    }

    public LiveData<List<TrailMetrics>> getTrailMetrics(){
        return Transformations.switchMap(user, user -> {
            if (user == null) {
                return new MutableLiveData<>(Collections.emptyList());
            } else {
                return trailMetricsDAO.getMetricsByUsername(user.getUsername());
            }
        });
    }

    public LiveData<TrailMetrics> getTrailMetricsById(int id){
        return trailMetricsDAO.getMetricsById(id);
    }

    public void addTrailMetrics(Trip trip) {
        TrailMetrics trailMetrics = trip.finish();
        LiveData<User> userLiveData = userDAO.getUserByUsername(trip.getUsername());

        Observer<User> userObserver = new Observer<>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    new InsertTrailMetricsAsync(trailMetricsDAO).execute(trailMetrics);
                    userLiveData.removeObserver(this);
                }
            }
        };
        userLiveData.observeForever(userObserver);
    }


    public void updateUserAPI(String cookies) {
        if(cookies!=""){
            Log.e("DEBUG","Cookies:"+cookies);
            Call<User> call = api.getUser(cookies);
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        User user = response.body();
                        String responseBody = response.body().toString();
                        Log.e("Retrofit", "Response Body: " + responseBody);
                        insert(user);
                    } else {
                        Log.e("Retrofit", "Unsuccessful Response: " + response);
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.e("Retrofit", "Response error:" + t.getMessage());
                }
            });
        }
    }
    public LiveData<String> getCookies(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("BraguiaPreferences", Context.MODE_PRIVATE);
        MutableLiveData<String> cookiesLiveData = new MutableLiveData<>();
        cookiesLiveData.postValue(sharedPreferences.getString("cookies", ""));

        // Register a shared preference change listener
        SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener = (sharedPrefs, key) -> {
            if ("cookies".equals(key)) {
                cookiesLiveData.postValue(sharedPreferences.getString("cookies", ""));
            }
        };

        sharedPreferences.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);

        return cookiesLiveData;
    }


    public void insert(User user){
        new UserRepository.InsertAsyncTask(userDAO).execute(user);
    }




    public void makeLoginRequest(String username,String password,Context context,final LoginCallback callback) throws IOException {
        JsonObject body = new JsonObject();
        body.addProperty("username", username);
        body.addProperty("email", "");
        body.addProperty("password", password);
        lastUser=username;
        Call<User> call = api.login(body);

        call.enqueue(new retrofit2.Callback<>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = new User(username, "premium");
                    // Store the cookies
                    Headers headers = response.headers();
                    List<String> cookies = headers.values("Set-Cookie").stream().map(e -> e.split(";")[0]).collect(Collectors.toList());
                    if (!cookies.isEmpty()) { //Insert cookie into SharedPreferences
                        String cookieString = TextUtils.join(";", cookies);
                        SharedPreferences sharedPreferences = context.getSharedPreferences("BraguiaPreferences", Context.MODE_PRIVATE);
                        sharedPreferences.edit().putString("cookies", cookieString).apply();
                        sharedPreferences.edit().putString("lastUser", user.getUsername()).apply();
                    }
                    insert(user); //Insert user into user DataBase
                    callback.onLoginSuccess();
                } else {
                    Log.e("main", "onFailure: " + response.errorBody());
                    callback.onLoginFailure();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("main", "onFailure: " + t.getMessage());
                Log.e("main", "message: " + t.getCause());
                callback.onLoginFailure();
            }
        });
    }

    public interface LoginCallback {
        void onLoginSuccess();
        void onLoginFailure();
    }

    public void makeLogOutRequest(Context context,final LogoutCallback callback) throws IOException {
        SharedPreferences sharedPreferences = context.getSharedPreferences("BraguiaPreferences", Context.MODE_PRIVATE);
        String storedCookieString = sharedPreferences.getString("cookies", "");
        Call<User> call = api.logout(storedCookieString);
        call.enqueue(new retrofit2.Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()) {
                    lastUser="";
                    SharedPreferences sharedPreferences = context.getSharedPreferences("BraguiaPreferences", Context.MODE_PRIVATE);
                    sharedPreferences.edit().putString("cookies", "").apply();
                    sharedPreferences.edit().putString("lastUser", "").apply();
                    Log.e("main", "logged out successfully:");
                    callback.onLogoutSuccess();
                }
                else{
                    Log.e("main", "onFailure: "+response.errorBody());
                    callback.onLogoutFailure();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("main", "onFailure: " + t.getMessage());
                callback.onLogoutFailure();
            }
        });
    }

    public interface LogoutCallback {
        void onLogoutSuccess();
        void onLogoutFailure();
    }


    public LiveData<User> getUser() {
        return user;
    }

    public void setUser(User fixedUser) {
        this.user = new MediatorLiveData<>();
        this.user.postValue(fixedUser);
    }


    private static class InsertAsyncTask extends AsyncTask<User,Void,Void> {
        private final UserDAO userDAO;

        public InsertAsyncTask(UserDAO catDao) {
            this.userDAO=catDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDAO.insertOrUpdate(users[0]);
            return null;
        }
    }

    private static class InsertTrailMetricsAsync extends AsyncTask<TrailMetrics,Void,Void> {
        private final TrailMetricsDAO trailMetricsDAO;

        public InsertTrailMetricsAsync(TrailMetricsDAO trailMetricsDAO) {
            this.trailMetricsDAO=trailMetricsDAO;
        }

        @Override
        protected Void doInBackground(TrailMetrics... trailMetrics) {
            trailMetricsDAO.insert(trailMetrics[0]);
            return null;
        }
    }
}
