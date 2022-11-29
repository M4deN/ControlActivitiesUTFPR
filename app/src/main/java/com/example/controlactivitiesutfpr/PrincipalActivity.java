package com.example.controlactivitiesutfpr;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.controlactivitiesutfpr.Model.Controle;
import com.example.controlactivitiesutfpr.Persistencia.ControleDatabase;
import com.example.controlactivitiesutfpr.Utils.UtilsGUI;

import java.util.List;

public class PrincipalActivity extends AppCompatActivity {

    private ListView listViewCadastro;
    private ArrayAdapter <Controle> listaAdapter;
    private List<Controle> listaControle;

    private static final String ARQUIVO = "com.example.controlactivitiesutfpr.PREFERENCIAS_MODE";
    private boolean darkMode = false;
    private static final String MODO_NOTURNO = "MODO_NOTURNO";
    private static final int REQUEST_NOVO_CADASTRO = 1;
    private static final int REQUEST_ALTERAR_CADASTRO = 2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        listViewCadastro = findViewById(R.id.listViewCampus);

        lerPreferencia();

        listViewCadastro.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Controle controle = (Controle) parent.getItemAtPosition(position);

                ControleActivity.alterarCadastro(PrincipalActivity.this, REQUEST_ALTERAR_CADASTRO, controle);
            }
        });
        carregaLista();

        registerForContextMenu(listViewCadastro);

    }

    private void carregaLista(){

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                ControleDatabase database = ControleDatabase.getDatabase(PrincipalActivity.this);

                listaControle = database.controleDAO().queryAll();

                PrincipalActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listaAdapter = new ArrayAdapter<>(PrincipalActivity.this, android.R.layout.simple_list_item_1, listaControle);

                        listViewCadastro.setAdapter(listaAdapter);
                    }
                });
            }
        });
    }

    private void lerPreferencia(){

        SharedPreferences shared = getSharedPreferences(ARQUIVO, Context.MODE_PRIVATE);
         darkMode = shared.getBoolean(MODO_NOTURNO,darkMode);
        ativaModoNoturno();
    }

    private void salvarPreferencia(boolean dark){

        SharedPreferences shared = getSharedPreferences(ARQUIVO, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = shared.edit();

        editor.putBoolean(MODO_NOTURNO, dark);

        editor.commit();

        darkMode = dark;

        ativaModoNoturno();
    }

    private void ativaModoNoturno(){

        if(darkMode == true){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
    }


    private void excluirCadastro(final Controle controle){

        String mensagem = getString(R.string.deseja_apagar) + "\n" + controle.getDisciplina();

        DialogInterface.OnClickListener listener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch(which){
                            case DialogInterface.BUTTON_POSITIVE:

                                AsyncTask.execute(new Runnable() {
                                    @Override
                                    public void run() {

                                    ControleDatabase database = ControleDatabase.getDatabase(PrincipalActivity.this);

                                    database.controleDAO().delete(controle);

                                    PrincipalActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            listaAdapter.remove(controle);
                                    }
                                });
                                    }
                                });
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:

                                break;
                        }
                    }
                };

        UtilsGUI.confirmaAcao(this, mensagem, listener);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.principal_opcoes,menu);
        MenuItem switch_modo_black = menu.findItem(R.id.switch_no_menu);

        final Switch widget_switch_modo_black = switch_modo_black.getActionView().findViewById(R.id.switch_action_bar_switch);

        widget_switch_modo_black.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    salvarPreferencia(true);
                }else{
                    salvarPreferencia(false);
                }


            }
        });
        return true;
    }

    private void verificaCampus(){

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                ControleDatabase database = ControleDatabase.getDatabase(PrincipalActivity.this);

                int total = database.campusDao().total();

                if (total == 0){

                   PrincipalActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            UtilsGUI.avisoErro(PrincipalActivity.this, R.string.selecione_o_campus);
                        }
                    });

                    return;
                }

                ControleActivity.novoCadastro(PrincipalActivity.this, REQUEST_NOVO_CADASTRO);
            }
        });
    }
    @Override
    public boolean onPrepareOptionsMenu(@NonNull Menu menu) {

        MenuItem switch_modo = menu.findItem(R.id.switch_no_menu);

        Switch widget_switch = switch_modo.getActionView().findViewById(R.id.switch_action_bar_switch);

        widget_switch.setChecked(darkMode);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.item_selecionado, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info;

        info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        Controle controle = (Controle) listViewCadastro.getItemAtPosition(info.position);

        switch(item.getItemId()){

            case R.id.menuItemAbrir:
                ControleActivity.alterarCadastro(this, REQUEST_ALTERAR_CADASTRO, controle);
                return true;

            case R.id.menuItemApagar:
                excluirCadastro(controle);
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuItemAdicionar:
                verificaCampus();
                return true;
            case R.id.menuItemSobre:
                SobreActivity.sobre(this);
                return true;
            case R.id.switch_action_bar_switch:
                salvarPreferencia(darkMode);
                return true;
            case R.id.menuItemCampus:
                CampusActivity.abrir(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == REQUEST_NOVO_CADASTRO || requestCode == REQUEST_ALTERAR_CADASTRO) && resultCode == Activity.RESULT_OK) {

            carregaLista();
        }
    }

}