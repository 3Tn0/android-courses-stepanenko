package com.example.lab4.db;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface GroupDao {
    @Query("SELECT * FROM [group]")
    List<Group> getAll();

    @Insert
    void insert(@NonNull Group group);

    @Query(
            "SELECT COUNT(*) FROM [group] WHERE " +
                    "name = :name"
    )
    int count(@NonNull String name);
}
