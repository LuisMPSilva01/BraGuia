package com.example.braguia.model;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Trail.class, User.class}, version = 961)
public abstract class GuideDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "BraGuide";

    public abstract TrailDAO trailDAO();

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

        private TrailDAO traildao;

        public PopulateDbAsyn(GuideDatabase catDatabase) {
            traildao = catDatabase.trailDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            traildao.deleteAll();
            return null;
        }
    }
}
