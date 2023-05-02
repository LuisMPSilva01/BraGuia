package com.example.braguia.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.braguia.model.trails.Trail;
import com.example.braguia.model.user.User;
import com.example.braguia.repositories.TrailRepository;
import com.example.braguia.repositories.UserRepository;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.List;

import retrofit2.http.Body;

public class UserViewModel extends AndroidViewModel {

    private UserRepository repository;
    public LiveData<String> username;

    public UserViewModel(@NonNull Application application) {
        super(application);
        repository= new UserRepository(application);
        username = repository.getUsername();
    }

    public void login(String username, String password, Context context, final LoginCallback callback) throws IOException {
        repository.makeLoginRequest(username,password,context, new UserRepository.LoginCallback() {
            @Override
            public void onLoginSuccess() {
                callback.onLoginSuccess();
            }

            @Override
            public void onLoginFailure() {
                callback.onLoginFailure();
            }
        });
    }

    public interface LoginCallback {
        void onLoginSuccess();
        void onLoginFailure();
    }

    public void logOut(Context context , LogOutCallback callback) throws IOException {
        repository.makeLogOutRequest(context, new UserRepository.LogoutCallback() {
            @Override
            public void onLogoutSuccess() {
                callback.onLogOutSuccess();
            }

            @Override
            public void onLogoutFailure() {
                callback.onLogOutFailure();
            }
        });
    }

    public interface LogOutCallback {
        void onLogOutSuccess();
        void onLogOutFailure();
    }


    public LiveData<String> getUserName(){
        return username;
    }
    public LiveData<User> getUser(String username) throws IOException {
        return repository.getUser(username);
    }
    public void updateTrailHistory(String userName,Integer trailId){
        repository.updateTrailHistory(userName,trailId);
    }

    public void updatePinHistory(String userName,Integer pinId){
        repository.updatePinHistory(userName,pinId);
    }

}
