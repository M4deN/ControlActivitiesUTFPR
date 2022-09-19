package com.example.controlactivitiesutfpr;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class ControleActivity extends AppCompatActivity {
    public  static  final  String MODO = "MODO";
    public static final String NOME = "NOME";
    public static final String PERIODO = "PERIODO";
    public static final String ALUNO = "ALUNO";
    public static final String MONITOR = "MONITOR";
    public static final String TIPO = "TIPO";
    public static final String CAMPUS = "CAMPUS";

    public static final int NOVO = 1;
    public static final int ALTERAR = 2;
    private int      modo;

    private EditText editTextNome, editTextPeriodo;
    private RadioGroup radioGroupPeriodos;
    private CheckBox cbAluno, cbMonitor;
    private Spinner spinnerCampus1;
    private String nomeOriginal;

    public static void novoCadastro(AppCompatActivity activity){

        Intent intent = new Intent(activity, ControleActivity.class);

        intent.putExtra(MODO, NOVO);
        activity.startActivityForResult(intent,NOVO);
    }
    public static void alterarCadastro(AppCompatActivity activity, Controle controle){

        Intent intent = new Intent(activity, ControleActivity.class);

        intent.putExtra(MODO,ALTERAR);
        intent.putExtra(NOME, controle.getDisciplina());
        intent.putExtra(PERIODO,controle.getPeriodo());
        intent.putExtra(TIPO, controle.getTipo());
        intent.putExtra(ALUNO, controle.isAluno());
        intent.putExtra(MONITOR, controle.isMonitor());
        intent.putExtra(CAMPUS, controle.getCampus());

        activity.startActivityForResult(intent, ALTERAR);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem);


        cbMonitor = findViewById(R.id.checkBoxMonitor);
        cbAluno = findViewById(R.id.checkBoxAluno);
        editTextNome = findViewById(R.id.editTextNome);
        editTextPeriodo = findViewById(R.id.editTextPeriodo);
        radioGroupPeriodos = findViewById(R.id.radioGroupPeriodos);
        spinnerCampus1 = findViewById(R.id.spinnerCampus);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null){
            modo = bundle.getInt(MODO, NOVO);

            if(modo == NOVO){
                setTitle(getString(R.string.tela_novo_cadastro));
            }else {
                nomeOriginal = bundle.getString(NOME);
                editTextNome.setText(nomeOriginal);

                String periodo = bundle.getString(PERIODO);
                editTextPeriodo.setText(periodo);

                int tipo = bundle.getInt(TIPO);

                RadioButton button;

                switch (tipo){
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

                boolean aluno = bundle.getBoolean(ALUNO);
                cbAluno.setChecked(aluno);
                boolean monitor = bundle.getBoolean(MONITOR);
                cbMonitor.setChecked(monitor);

                String campus = bundle.getString(CAMPUS);

                for(int pos = 0; 0 < spinnerCampus1.getAdapter().getCount(); pos++){

                    String valor = (String) spinnerCampus1.getItemAtPosition(pos);

                    if(valor.equals(campus)){
                        spinnerCampus1.setSelection(pos);
                        break;
                    }
                }
                setTitle(getString(R.string.alterar_cadastro));
            }
        }
        editTextNome.requestFocus();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cadastro_opcoes,menu);
        return true;
    }

    public void limparCampos(){
        editTextNome.setText(null);
        editTextPeriodo.setText(null);
        radioGroupPeriodos.clearCheck();
        spinnerCampus1.setSelection(0);
        cbAluno.setChecked(false);
        cbMonitor.setChecked(false);
        Toast.makeText(this, R.string.limpacampos, Toast.LENGTH_SHORT).show();
    }

    private void cancelar(){
        setResult(Activity.RESULT_CANCELED);
        finish();
    }
    public void salvar(){

        String nome = editTextNome.getText().toString();

        if(nome == null || nome.trim().isEmpty()){
            Toast.makeText(this, R.string.discplina_vazio,Toast.LENGTH_SHORT).show();
            editTextNome.requestFocus();
            return;
        }
        String periodotext = editTextPeriodo.getText().toString();

        if(periodotext == null || periodotext.trim().isEmpty()){
            Toast.makeText(this, R.string.periodo_vazio,Toast.LENGTH_SHORT).show();
            editTextPeriodo.requestFocus();
            return;
        }
        int tipo;
        switch (radioGroupPeriodos.getCheckedRadioButtonId()){
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
                Toast.makeText(this, R.string.modalidade_vazia, Toast.LENGTH_SHORT).show();
                return;
        }
        boolean aluno = cbAluno.isChecked();
        boolean monitor = cbMonitor.isChecked();

        String campus = (String) spinnerCampus1.getSelectedItem();

        if(campus == "Selecione"){
            Toast.makeText(this, R.string.campus_vazio,Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(NOME,nome);
        intent.putExtra(PERIODO,periodotext);
        intent.putExtra(TIPO,tipo);
        intent.putExtra(ALUNO,aluno);
        intent.putExtra(MONITOR,monitor);
        intent.putExtra(CAMPUS,campus);

        setResult(Activity.RESULT_OK,intent);
        finish();
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
            case R.id.menuItemLimpar:
                limparCampos();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

}