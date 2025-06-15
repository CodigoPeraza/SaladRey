package com.mr21082pm18077.saladrey.data.local;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jspecify.annotations.NonNull;

@Entity(tableName = "salads")
public class Salad {
    @PrimaryKey(autoGenerate = true) public long id;
    @NonNull
    public String name;
    public double price;
    /** URI string: content://… */
    @NonNull public String imageUri;
}
