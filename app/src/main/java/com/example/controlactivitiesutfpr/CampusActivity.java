package com.example.controlactivitiesutfpr;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.controlactivitiesutfpr.Model.Campus;
import com.example.controlactivitiesutfpr.Persistencia.ControleDatabase;
import com.example.controlactivitiesutfpr.Utils.UtilsGUI;

import java.util.List;

public class CampusActivity extends AppCompatActivity {

    private static final int REQUEST_NOVO_CAMPUS = 1;
    private static final int REQUEST_ALTERAR_CAMPUS = 2;

    private ListView listViewCampus;
    private ArrayAdapter<Campus> listaAdapter;
    private List<Campus> lista;
    public static void abrir(Activity activity){

        Intent intent = new Intent(activity, CampusActivity.class);

        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        listViewCampus = findViewById(R.id.listViewCampus);

        listViewCampus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Campus tipo = (Campus) parent.getItemAtPosition(position);

                CampuActivity.alterar(CampusActivity.this, REQUEST_ALTERAR_CAMPUS, tipo);
            }
        });

        carregaCampus();

        registerForContextMenu(listViewCampus);

        setTitle(getString(R.string.campus_title));
    }

    private void carregaCampus(){

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

               ControleDatabase database = ControleDatabase.getDatabase(CampusActivity.this);

                lista = database.campusDao().queryAll();

                CampusActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listaAdapter = new ArrayAdapter<>(CampusActivity.this, android.R.layout.simple_list_item_1, lista);

                        listViewCampus.setAdapter(listaAdapter);
                    }
                });
            }
        });
    }

    private void verificaUsoCampus(final Campus campus){

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                ControleDatabase database = ControleDatabase.getDatabase(CampusActivity.this);

                int lista = database.controleDAO().queryForCampusId(campus.getId());

                if (lista > 0){

                    CampusActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            UtilsGUI.avisoErro(CampusActivity.this, R.string.campus_em_uso);
                        }
                    });

                    return;
                }

                CampusActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        excluirCampus(campus);
                    }
                });
            }
        });
    }

    private void excluirCampus(final Campus campus){

        String mensagem = getString(R.string.deseja_apagar) + "\n" + campus.getDescricao();

        DialogInterface.OnClickListener listener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch(which){
                            case DialogInterface.BUTTON_POSITIVE:

                                AsyncTask.execute(new Runnable() {
                                    @Override
                                    public void run() {

                                        ControleDatabase database = ControleDatabase.getDatabase(CampusActivity.this);

                                        database.campusDao().delete(campus);

                                        CampusActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                listaAdapter.remove(campus);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == REQUEST_NOVO_CAMPUS || requestCode == REQUEST_ALTERAR_CAMPUS) && resultCode == Activity.RESULT_OK) {

            carregaCampus();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lista_campus, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case R.id.menuItemNovo:
                CampuActivity.novo(this, REQUEST_NOVO_CAMPUS);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

        final Campus campus = (Campus) listViewCampus.getItemAtPosition(info.position);

        switch(item.getItemId()){

            case R.id.menuItemAbrir:
                CampuActivity.alterar(this, REQUEST_ALTERAR_CAMPUS, campus);
                return true;

            case R.id.menuItemApagar:
                verificaUsoCampus(campus);
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }
}