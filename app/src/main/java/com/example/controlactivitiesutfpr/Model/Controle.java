package com.example.controlactivitiesutfpr.Model;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "Controle", foreignKeys = @ForeignKey(entity = Campus.class, parentColumns =  "id", childColumns = "campusId"))
public class Controle {
    public static final  int BACHARELADO = 1;
    public static final  int LICENCIATURA = 2;
    public static final int TECNOLOGO = 3;


    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String disciplina;

    private Date dataAtividade;
    private Date dataCadastro;
    private String periodo;
    private int tipo;
    private boolean aluno;
    private boolean monitor;


    @ColumnInfo(index = true)
    private int campusId;

    public Controle(String disciplina){
        setDisciplina(disciplina);
    }

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    @NonNull
    public String getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public boolean isAluno() {return aluno;}

    public void setAluno(boolean aluno) {this.aluno = aluno;}

    public boolean isMonitor() {return monitor;}

    public void setMonitor(boolean monitor) {this.monitor = monitor;}

    public int getCampusId() {return campusId;}

    public void setCampusId(int campusId) {this.campusId = campusId;}

    public Date getDataAtividade() {return dataAtividade;}

    public void setDataAtividade(Date dataAtividade) {this.dataAtividade = dataAtividade;}

    public Date getDataCadastro() {return dataCadastro;}

    public void setDataCadastro(Date dataCadastro) {this.dataCadastro = dataCadastro;}

    @Override
    public String toString(){
        return getDisciplina() + "  " + getPeriodo() + "  " + getTipo() + "  " + getCampusId() + " "+ isAluno() + " " + isMonitor() +" " + getDataAtividade();
    }
}