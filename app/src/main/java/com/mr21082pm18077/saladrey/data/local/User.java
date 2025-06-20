package com.mr21082pm18077.saladrey.data.local;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.jspecify.annotations.NonNull;

@Entity(tableName = "users",
        indices = @Index(value = "email", unique = true))
public class User {
    @PrimaryKey(autoGenerate = true)
    public long id;
    @NonNull
    public String email;
    @NonNull
    public String password;
    public boolean isAdmin;
}
