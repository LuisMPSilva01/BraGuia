package com.example.braguia.repositories;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.example.braguia.model.GuideDatabase;
import com.example.braguia.model.user.User;
import com.example.braguia.model.user.UserAPI;
import com.example.braguia.model.user.UserDAO;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import okhttp3.Cookie;
import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;

public class UserRepository {
    public UserDAO userDAO;
    public MediatorLiveData<String> username;
    private GuideDatabase database;
    private Retrofit retrofit;
    private UserAPI api;

    public UserRepository(Application application) {
        database = GuideDatabase.getInstance(application);
        userDAO = database.userDAO();
        username = new MediatorLiveData<>();
        username.addSource(getUsername(application.getApplicationContext()) , localUsername ->{
            Log.e("CHANGE","new localUsername:"+localUsername);
            username.setValue(localUsername);
        });

        retrofit = new Retrofit.Builder()
                .baseUrl("https://c5a2-193-137-92-29.eu.ngrok.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(UserAPI.class);
    }


    public LiveData<String> getUsername(){
        return username;
    }
    public void insert(User user){
        new UserRepository.InsertAsyncTask(userDAO).execute(user);
    }

    public void makeLoginRequest(String username,String password,Context context,final LoginCallback callback) throws IOException {
        JsonObject body = new JsonObject();
        body.addProperty("username", username);
        body.addProperty("email", "");
        body.addProperty("password", password);

        Call<User> call = api.login(body);

        call.enqueue(new retrofit2.Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()) {
                    User user =  new User(username,"premium");
                    // Store the cookies
                    Headers headers = response.headers();
                    List<String> cookies = headers.values("Set-Cookie").stream().map(e->e.split(";")[0]).collect(Collectors.toList());
                    if (!cookies.isEmpty()) { //Insert cookie into SharedPreferences
                        String cookieString = TextUtils.join(";", cookies);
                        SharedPreferences sharedPreferences = context.getSharedPreferences("BraguiaPreferences", Context.MODE_PRIVATE);
                        sharedPreferences.edit().putString("cookies", cookieString).apply();
                    }
                    SharedPreferences sharedPreferences = context.getSharedPreferences("BraguiaPreferences", Context.MODE_PRIVATE);
                    sharedPreferences.edit().putString("isLogged", username).apply();
                    insert(user); //Insert user into user DataBase
                    callback.onLoginSuccess();
                }
                else{
                    Log.e("main", "onFailure: "+response.errorBody());
                    callback.onLoginFailure();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("main", "onFailure: " + t.getMessage());
                Log.e("main", "message: "+ t.getCause());
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
        List<String> cookieList = Arrays.stream(storedCookieString.split(";")).collect(Collectors.toList());
        Call<User> call = api.logout(storedCookieString);
        call.enqueue(new retrofit2.Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()) {
                    insert(new User("","loggedOut"));
                    SharedPreferences sharedPreferences = context.getSharedPreferences("BraguiaPreferences", Context.MODE_PRIVATE);
                    sharedPreferences.edit().putString("isLogged", "").apply();
                    Log.e("main", "logged out successfully:"+sharedPreferences.getString("isLogged", ""));
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

    public LiveData<User> getUser(String username){
        return userDAO.getUserByUsername(username);
    }

    private LiveData<String> getUsername(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("BraguiaPreferences", Context.MODE_PRIVATE);
        MutableLiveData<String> userNameLiveData = new MutableLiveData<>();
        userNameLiveData.setValue(sharedPreferences.getString("isLogged", ""));

        // Register a shared preference change listener
        SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener = (sharedPrefs, key) -> {
            if ("isLogged".equals(key)) {
                userNameLiveData.setValue(sharedPreferences.getString("isLogged", ""));
            }
        };

        sharedPreferences.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);

        //TODO comentar isto somehow fixes the code
        //userNameLiveData.observeForever(ignored -> sharedPreferences.unregisterOnSharedPreferenceChangeListener(sharedPreferenceChangeListener));

        return userNameLiveData;
    }



    public void updateTrailHistory(String userName,Integer trailId){
        User u = getUser(userName).getValue();
        if(u!=null){
            List<Integer> ids = u.getTrailHistoryList();
            ids.add(trailId);
            userDAO.updateTrailHistory(userName,User.convertListToString(ids));
        }
    }

    public void updatePinHistory(String userName,Integer pinId){
        User u = getUser(userName).getValue();
        if(u!=null){
            List<Integer> ids = u.getPinHistoryList();
            ids.add(pinId);
            userDAO.updatePinHistory(userName,User.convertListToString(ids));
        }
    }

    private static class InsertAsyncTask extends AsyncTask<User,Void,Void> {
        private UserDAO userDAO;

        public InsertAsyncTask(UserDAO catDao) {
            this.userDAO=catDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDAO.insert(users[0]);
            return null;
        }
    }

}
