package com.example.controlactivitiesutfpr;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.controlactivitiesutfpr.Model.Campus;
import com.example.controlactivitiesutfpr.Model.Controle;
import com.example.controlactivitiesutfpr.Persistencia.ControleDatabase;
import com.example.controlactivitiesutfpr.Utils.UtilsDate;
import com.example.controlactivitiesutfpr.Utils.UtilsGUI;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ControleActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    public  static  final  String MODO = "MODO";
    public static final String ID      = "ID";

    public static final int NOVO = 1;
    public static final int ALTERAR = 2;
    private int modo;

    private EditText editTextNome, editTextPeriodo;
    private RadioGroup radioGroupPeriodos;
    private CheckBox cbAluno, cbMonitor;
    private Spinner spinnerCampus1;
    private Controle controle;
    private List<Campus> listaCampus;
    private Calendar calendarDataAtividade;
    private TextView Data;
    private EditText editTextData;
    private TextView textViewDataCadastro;
    private EditText editTextDataCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controle);

        cbMonitor = findViewById(R.id.checkBoxMonitor);
        cbAluno = findViewById(R.id.checkBoxAluno);
        editTextNome = findViewById(R.id.editTextNome);
        editTextPeriodo = findViewById(R.id.editTextPeriodo);
        radioGroupPeriodos = findViewById(R.id.radioGroupPeriodos);
        spinnerCampus1 = findViewById(R.id.spinnerCampus);
        Data = findViewById(R.id.Data);
        editTextData = findViewById(R.id.editTextData);
        textViewDataCadastro   = findViewById(R.id.textViewDataCadastro);
        editTextDataCadastro   = findViewById(R.id.editTextDataCadastro);

        editTextDataCadastro.setEnabled(false);

        Intent intent = getIntent();

        final Bundle bundle = intent.getExtras();
        modo = bundle.getInt(MODO, NOVO);

        calendarDataAtividade = Calendar.getInstance();
        calendarDataAtividade.add(Calendar.YEAR,- getResources().getInteger(R.integer.anos_para_tras));

        editTextData.setFocusable(false);
        editTextData.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                        DatePickerDialog picker = new DatePickerDialog(ControleActivity.this, R.style.CustomDatePickerDialogTheme, ControleActivity.this,
                        calendarDataAtividade.get(Calendar.YEAR),
                        calendarDataAtividade.get(Calendar.MONTH),
                        calendarDataAtividade.get(Calendar.DAY_OF_MONTH));

                picker.show();
            }
        });
        carregaCampus();

        if (modo == ALTERAR){

            editTextNome.requestFocus();
            setTitle(getString(R.string.cadastro_alterar));

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    int id = bundle.getInt(ID);

                    ControleDatabase database = ControleDatabase.getDatabase(ControleActivity.this);

                    controle = database.controleDAO().queryForId(id);

                    ControleActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            editTextNome.setText(controle.getDisciplina());
                            editTextPeriodo.setText(String.valueOf(controle.getPeriodo()));

                            calendarDataAtividade.setTime(controle.getDataAtividade());
                            String textoData = UtilsDate.formatDate(ControleActivity.this, controle.getDataAtividade());

                            editTextData.setText(textoData);

                            textoData = UtilsDate.formatDate(ControleActivity.this, controle.getDataCadastro());


                            editTextDataCadastro.setText(textoData);
                            int tipo = controle.getTipo();

                            RadioButton button;
                            switch (tipo) {
                                case Controle.BACHARELADO:
                                    button = findViewById(R.id.radioButtonPrimeiro);
                                    button.setChecked(true);
                                    break;

                                case Controle.LICENCIATURA:
                                    button = findViewById(R.id.radioButtonSegundo);
                                    button.setChecked(true);
                                    break;
                                case Controle.TECNOLOGO:
                                    button = findViewById(R.id.radioButtonTerceiro);
                                    button.setChecked(true);
                                    break;
                            }
                            cbAluno.setChecked(controle.isAluno());
                            cbMonitor.setChecked(controle.isMonitor());

                            int posicao = posicaoCampus(controle.getCampusId());
                            spinnerCampus1.setSelection(posicao);
                       }
                   });
                }
           });

        }else{

            setTitle(getString(R.string.cadastro_novo));

            controle = new Controle("");

            textViewDataCadastro.setVisibility(View.INVISIBLE);
            editTextDataCadastro.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edicao_detalhes,menu);
        return true;
    }

    private void cancelar(){
        setResult(Activity.RESULT_CANCELED);
        finish();
    }
    public void salvar() {

        String nome = UtilsGUI.validaCampoTexto(this, editTextNome, R.string.disc_vazia);

        if (nome == null) {
            return;
        }
        String periodotext = UtilsGUI.validaCampoTexto(this, editTextPeriodo, R.string.periodo_vazio);

        if (periodotext == null || periodotext.trim().isEmpty()) {
            return;
        }
        String txtData = UtilsGUI.validaCampoTexto(this, editTextData, R.string.erro);
        if (txtData == null){
            return;
        }

        int tipo;

        switch (radioGroupPeriodos.getCheckedRadioButtonId()) {
            case R.id.radioButtonPrimeiro:
                tipo = Controle.BACHARELADO;
                break;
            case R.id.radioButtonSegundo:
                tipo = Controle.LICENCIATURA;
                break;
            case R.id.radioButtonTerceiro:
                tipo = Controle.TECNOLOGO;
                break;
            default:
                tipo = -1;
        }
        if(tipo ==-1){
            return;

        }
        boolean aluno = cbAluno.isChecked();
        boolean monitor = cbMonitor.isChecked();

        Campus campus = (Campus) spinnerCampus1.getSelectedItem();

        if (campus != null){

            controle.setCampusId(campus.getId());
        }

        controle.setDisciplina(nome);
        controle.setPeriodo(periodotext);
        controle.setTipo(tipo);
        controle.setMonitor(monitor);
        controle.setAluno(aluno);
        controle.setDataAtividade(calendarDataAtividade.getTime());

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                ControleDatabase database = ControleDatabase.getDatabase(ControleActivity.this);

                if (modo == NOVO) {

                    controle.setDataCadastro(new Date());

                    database.controleDAO().insert(controle);

                } else {

                    database.controleDAO().update(controle);
                }

                setResult(Activity.RESULT_OK);
                finish();
            }
        });
    }

    private int posicaoCampus(int campusId){

        for (int pos = 0; pos < listaCampus.size(); pos++){

            Campus camp = listaCampus.get(pos);

            if (camp.getId() == campusId){
                return pos;
            }
        }

        return -1;
    }

    private void carregaCampus(){

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                ControleDatabase database = ControleDatabase.getDatabase(ControleActivity.this);

                listaCampus = database.campusDao().queryAll();

                ControleActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayAdapter<Campus> spinnerAdapter = new ArrayAdapter<>(ControleActivity.this, android.R.layout.simple_list_item_1, listaCampus);

                        spinnerCampus1.setAdapter(spinnerAdapter);
                    }
                });
            }
        });
    }

    public static void novoCadastro(Activity activity, int requestCode){

        Intent intent = new Intent(activity, ControleActivity.class);

        intent.putExtra(MODO, NOVO);

        activity.startActivityForResult(intent,requestCode);
    }

    public static void alterarCadastro(Activity activity, int requestCode, Controle controle){

        Intent intent = new Intent(activity, ControleActivity.class);

        intent.putExtra(MODO, ALTERAR);
        intent.putExtra(ID, controle.getId());

        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        calendarDataAtividade.set(year, month, dayOfMonth);

        String textoData = UtilsDate.formatDate(this, calendarDataAtividade.getTime());

        editTextData.setText(textoData);
    }

    @Override
    public void onBackPressed(){
        cancelar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.menuItemSalvar:
                salvar();
                return true;
            case R.id.menuItemCancelar:
                cancelar();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

}