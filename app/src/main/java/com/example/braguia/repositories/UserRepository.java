package com.example.braguia.repositories;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.braguia.model.GuideDatabase;
import com.example.braguia.model.user.User;
import com.example.braguia.model.user.UserAPI;
import com.example.braguia.model.user.UserDAO;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;

public class UserRepository {
    public UserDAO userDAO;
    public MediatorLiveData<User> user;
    private GuideDatabase database;

    public UserRepository(Application application){
        database = GuideDatabase.getInstance(application);
        userDAO = database.userDAO();
        user = new MediatorLiveData<>();
        user.addSource(
                userDAO.getUser(), localUser -> {
                    // TODO: ADD cache validation logic
                    if (localUser != null) {
                        user.setValue(localUser);
                    }
                }
        );
    }

    public void insert(User user){
        new UserRepository.InsertAsyncTask(userDAO).execute(user);
    }

    public void makeLoginRequest(@Body JsonObject login,Context context,final LoginCallback callback) throws IOException {
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("https://c5a2-193-137-92-29.eu.ngrok.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserAPI api = retrofit.create(UserAPI.class);
        Call<User> call = api.login(login);

        call.enqueue(new retrofit2.Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()) {
                    User user =  new User(login.get("username").toString(),"premium");
                    // Store the cookies
                    Headers headers = response.headers();
                    List<String> cookies = headers.values("Set-Cookie");

                    if (!cookies.isEmpty()) { //Insert cookie into SharedPreferences
                        String cookieString = TextUtils.join(";", cookies);
                        SharedPreferences sharedPreferences = context.getSharedPreferences("BraguiaPreferences", Context.MODE_PRIVATE);
                        sharedPreferences.edit().putString("cookies", cookieString).apply();
                    }
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

    public LiveData<User> getUser(){
        return user;
    }

    private static class InsertAsyncTask extends AsyncTask<User,Void,Void> {
        private UserDAO userDAO;

        public InsertAsyncTask(UserDAO catDao) {
            this.userDAO=catDao;
        }

        @Override
        protected Void doInBackground(User... users) { //TODO isto não está certo acho
            userDAO.insert(users[0]);
            return null;
        }
    }

}
