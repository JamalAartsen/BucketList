package com.example.bucketlist;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ItemDao {

    @Insert
    void insert(Item item);

    @Delete
    void delete(Item item);

    @Delete
    void delete(List<Item> items);

    @Query("SELECT * from item_table")
    List<Item> getAllItems();
}
