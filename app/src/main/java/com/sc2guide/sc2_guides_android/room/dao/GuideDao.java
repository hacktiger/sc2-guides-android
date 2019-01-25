package com.sc2guide.sc2_guides_android.room.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.sc2guide.sc2_guides_android.data.model.Guide;

import java.util.List;

@Dao
public interface GuideDao {
    @Query("SELECT * FROM guide")
    LiveData<List<Guide>> getAll();

    @Query("SELECT title FROM guide WHERE id = :id LIMIT 1")
    String getGuideTitle(String id);

    @Query("DELETE FROM guide")
    void nukeTable();

    @Insert
    void insert(Guide guide);

    @Delete
    void delete(Guide guide);

    // implement other methods in the future
}
