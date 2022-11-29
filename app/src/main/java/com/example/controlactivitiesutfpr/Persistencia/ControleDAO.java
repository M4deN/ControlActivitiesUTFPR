package com.example.controlactivitiesutfpr.Persistencia;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.example.controlactivitiesutfpr.Model.Controle;

import java.util.List;

@Dao
public interface ControleDAO {

    @Insert
    long insert(Controle controle);

    @Delete
    void delete(Controle controle);

    @Update
    void update(Controle controle);

    @Query("SELECT * FROM controle WHERE id = :id")
    Controle queryForId(long id);

    @Query("SELECT * FROM controle ORDER BY disciplina ASC")
    List<Controle> queryAll();

    @Query("SELECT count(*) FROM controle WHERE campusId = :id LIMIT 1")
    int queryForCampusId(long id);
}
