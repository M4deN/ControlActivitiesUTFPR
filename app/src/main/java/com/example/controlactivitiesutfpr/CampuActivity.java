package com.example.controlactivitiesutfpr;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.controlactivitiesutfpr.Model.Campus;
import com.example.controlactivitiesutfpr.Persistencia.ControleDatabase;
import com.example.controlactivitiesutfpr.Utils.UtilsGUI;

import java.util.List;

public class CampuActivity extends AppCompatActivity {

    public static final String MODO    = "MODO";
    public static final String ID      = "ID";
    public static final int    NOVO    = 1;
    public static final int    ALTERAR = 2;

    private EditText editTexDescricao;

    private int  modo;
    private Campus campus;

    public static void novo(Activity activity, int requestCode) {

        Intent intent = new Intent(activity, CampuActivity.class);

        intent.putExtra(MODO, NOVO);

        activity.startActivityForResult(intent, requestCode);
    }

    public static void alterar(Activity activity, int requestCode, Campus campus){

        Intent intent = new Intent(activity, CampuActivity.class);

        intent.putExtra(MODO, ALTERAR);
        intent.putExtra(ID, campus.getId());

        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campu);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        editTexDescricao = findViewById(R.id.editTextDescricao);

        Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();

        if (bundle != null){
            modo = bundle.getInt(MODO, NOVO);
        }else{
            modo = NOVO;
        }

        if (modo == ALTERAR){

            setTitle(getString(R.string.alterar_campus));

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {

                    int id = bundle.getInt(ID);

                    ControleDatabase database = ControleDatabase.getDatabase(CampuActivity.this);

                    campus = database.campusDao().queryForId(id);

                    CampuActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            editTexDescricao.setText(campus.getDescricao());
                        }
                    });
                }
            });

        }else{

            setTitle(getString(R.string.novo_campus));

            campus = new Campus("");
        }
    }

    private void salvar(){

        final String descricao  = UtilsGUI.validaCampoTexto(this, editTexDescricao, R.string.campus_vazio);
        if (descricao == null){
            return;
        }

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                ControleDatabase database =ControleDatabase.getDatabase(CampuActivity.this);

                List<Campus> lista = database.campusDao().queryForDescricao(descricao);

                if (modo == NOVO) {

                    if (lista.size() > 0){

                        CampuActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                UtilsGUI.avisoErro(CampuActivity.this, R.string.campus_ja_cadastrado);
                            }
                        });

                        return;
                    }

                    campus.setDescricao(descricao);

                    database.campusDao().insert(campus);

                } else {

                    if (!descricao.equals(campus.getDescricao())){

                        if (lista.size() >= 1){

                            CampuActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    UtilsGUI.avisoErro(CampuActivity.this, R.string.campus_ja_cadastrado);
                                }
                            });

                            return;
                        }

                        campus.setDescricao(descricao);

                        database.campusDao().update(campus);
                    }
                }

                setResult(Activity.RESULT_OK);
                finish();
            }
        });
    }

    private void cancelar(){
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lista_campu, menu);
        return true;
    }

    public void limparCampos(){
        editTexDescricao.setText(null);
        Toast.makeText(this, R.string.limpacampos, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case R.id.menuItemSalvar:
                salvar();
                return true;
            case R.id.menuItemCancelar:
                cancelar();
                return true;
            case R.id.menuItemLimpar:
                limparCampos();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}