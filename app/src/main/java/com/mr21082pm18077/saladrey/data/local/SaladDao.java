package com.mr21082pm18077.saladrey.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SaladDao {
    @Query("SELECT * FROM salads ORDER BY name")
    LiveData<List<Salad>> getAll();
    @Insert
    long insert(Salad s);
    @Update
    void update(Salad s);
    @Delete
    void delete(Salad s);
}
