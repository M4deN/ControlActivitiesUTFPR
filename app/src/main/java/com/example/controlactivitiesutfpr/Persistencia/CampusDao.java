package com.example.controlactivitiesutfpr.Persistencia;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.controlactivitiesutfpr.Model.Campus;

import java.util.List;

@Dao
public interface CampusDao {

    @Insert
    long insert(Campus campus);

    @Delete
    void delete(Campus campus);

    @Update
    void update(Campus campus);

    @Query("SELECT * FROM Campus WHERE id = :id")
    Campus queryForId(long id);

    @Query("SELECT * FROM Campus ORDER BY descricao ASC")
    List<Campus> queryAll();

    @Query("SELECT * FROM Campus WHERE descricao = :descricao ORDER BY descricao ASC")
    List<Campus> queryForDescricao(String descricao);

    @Query("SELECT count(*) FROM Campus")
    int total();
}
