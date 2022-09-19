package com.example.controlactivitiesutfpr;

public class Controle {
    public static final  int BACHARELADO = 1;
    public static final  int LICENCIATURA = 2;
    public static final int TECNOLOGO = 3;

    private String disciplina;
    private String periodo;
    private int tipo;
    private boolean aluno;
    private boolean monitor;
    private String campus;


    public Controle ( String disciplina, String periodo, int tipo, boolean aluno, boolean monitor, String campus){
        setDisciplina(disciplina);
        setPeriodo(periodo);
        setTipo(tipo);
        setCampus(campus);
        setAluno(aluno);
        setMonitor(monitor);

    }

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

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    @Override
    public String toString(){
        return getDisciplina() + "  " + getPeriodo() + "  " + getTipo() + "  " + getCampus() + " "+ isAluno() + " " + isMonitor();
    }
}