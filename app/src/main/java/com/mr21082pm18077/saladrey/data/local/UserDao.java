package com.mr21082pm18077.saladrey.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserDao {
    @Insert
    long insert(User u);

    @Query("SELECT * FROM users WHERE email = :mail COLLATE NOCASE LIMIT 1")
    User findByEmail(String mail);

    @Query("SELECT * FROM users WHERE email = :mail COLLATE NOCASE AND password = :pass LIMIT 1")
    User login(String mail, String pass);

}

