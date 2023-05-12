package com.example.braguia.model;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.braguia.model.TrailMetrics.TrailMetricsDAO;
import com.example.braguia.model.app.AppInfo;
import com.example.braguia.model.app.AppInfoDAO;
import com.example.braguia.model.app.Contact;
import com.example.braguia.model.trails.EdgeTip;
import com.example.braguia.model.trails.Trail;
import com.example.braguia.model.trails.TrailDAO;
import com.example.braguia.model.TrailMetrics.TrailMetrics;
import com.example.braguia.model.user.User;
import com.example.braguia.model.user.UserDAO;
import com.example.braguia.model.user.UserUpdater;

@Database(entities = {Trail.class, AppInfo.class, User.class, Contact.class, UserUpdater.class, TrailMetrics.class, EdgeTip.class}, version = 1004)
public abstract class GuideDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "BraGuide";

    public abstract TrailDAO trailDAO();
    public abstract UserDAO userDAO();
    public abstract AppInfoDAO appInfoDAO();
    public abstract TrailMetricsDAO trailMetricsDAO();
    public static volatile GuideDatabase INSTANCE = null;

    public static GuideDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (GuideDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context, GuideDatabase.class, DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .addCallback(callback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public static Callback callback = new Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDbAsyn(INSTANCE);
        }
    };

    static  class  PopulateDbAsyn extends AsyncTask<Void,Void,Void> {

        private final TrailDAO traildao;
        private final UserDAO userDAO;
        private final AppInfoDAO appInfoDAO;
        private final TrailMetricsDAO trailMetricsDAO;


        public PopulateDbAsyn(GuideDatabase catDatabase) {
            traildao = catDatabase.trailDAO();
            userDAO = catDatabase.userDAO();
            appInfoDAO = catDatabase.appInfoDAO();
            trailMetricsDAO = catDatabase.trailMetricsDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            traildao.deleteAll();
            appInfoDAO.deleteAll();
            userDAO.deleteAll();
            trailMetricsDAO.deleteAll();
            return null;
        }
    }
}
