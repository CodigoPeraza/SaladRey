package com.mr21082pm18077.saladrey.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Salad.class, User.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    public static AppDatabase get(Context ctx) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    ctx.getApplicationContext(),
                                    AppDatabase.class,
                                    "saladrey.db")
                            .fallbackToDestructiveMigration() //para volver a construir la BD
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract SaladDao saladDao();

    public abstract UserDao userDao();
}
