package com.mr21082pm18077.saladrey.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.Executors;

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
                    INSTANCE = Room.databaseBuilder(
                                    ctx.getApplicationContext(),
                                    AppDatabase.class,
                                    "saladrey.db")
                            .fallbackToDestructiveMigration()
                            .build();
                    Executors.newSingleThreadExecutor().execute(() -> {
                        if (INSTANCE.userDao().findByEmail("admin@salad.com") == null) {
                            User u = new User();
                            u.email = "admin@salad.com";
                            u.password = "1234";
                            u.isAdmin = true;
                            INSTANCE.userDao().insert(u);
                        }
                    });

                }
            }
        }
        return INSTANCE;
    }

    public abstract SaladDao saladDao();

    public abstract UserDao userDao();
}
