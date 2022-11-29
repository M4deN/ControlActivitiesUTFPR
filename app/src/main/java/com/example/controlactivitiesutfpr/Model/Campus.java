package com.example.controlactivitiesutfpr.Model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "campus",indices = @Index(value = {"descricao"}, unique = true))

public class Campus {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String descricao;

    public Campus(String descricao) {
        setDescricao(descricao);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(@NonNull String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {return getDescricao();}
}
