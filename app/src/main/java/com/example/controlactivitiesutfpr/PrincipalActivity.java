package com.example.controlactivitiesutfpr;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class PrincipalActivity extends AppCompatActivity {

    private ListView listViewCadastro;
    private ArrayAdapter <Controle> listaAdapter;
    private ArrayList<Controle> listaControle;

    private ActionMode actionMode;
    private int posicaoSelecionada = -1;
    private View viewSelecionada;

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {

            MenuInflater inflate = mode.getMenuInflater();
            inflate.inflate(R.menu.principal_item_selecionado,menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem menuItem) {

            switch (menuItem.getItemId()){

                case R.id.menuItemAlterar:
                    alterarCadastro();
                    mode.finish();
                    return true;

                case R.id.menuItemExcluir:
                    excluirCadastro();
                    mode.finish();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            if(viewSelecionada != null){
                viewSelecionada.setBackgroundColor(Color.TRANSPARENT);
            }
            actionMode = null;
            viewSelecionada = null;
            listViewCadastro.setEnabled(true);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        listViewCadastro = findViewById(R.id.listViewCadastro);

        listViewCadastro.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        posicaoSelecionada = position;
                        alterarCadastro();
                    }
                });
        listViewCadastro.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listViewCadastro.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                        if(actionMode != null){
                            return false;
                        }
                        posicaoSelecionada = position;
                        view.setBackgroundColor(Color.LTGRAY);
                        viewSelecionada = view;
                        listViewCadastro.setEnabled(false);
                        actionMode = startActionMode(mActionModeCallback);
                        return true;
                    }
                });

        popularLista();
    }

    private void excluirCadastro(){

        listaControle.remove(posicaoSelecionada);
        listaAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.principal_opcoes,menu);
        return true;
    }

    private void alterarCadastro(){

        Controle controle = listaControle.get(posicaoSelecionada);

        ControleActivity.alterarCadastro(this,controle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.menuItemAdicionar:
                ControleActivity.novoCadastro(this);
                return true;
            case R.id.menuItemSobre:
                SobreActivity.sobre(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void popularLista(){

        listaControle = new ArrayList<>();
        listaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listaControle);
        listViewCadastro.setAdapter(listaAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {

            super.onActivityResult(requestCode, resultCode, data);
            Bundle bundle = data.getExtras();

            String nome = bundle.getString(ControleActivity.NOME);
            String periodo = bundle.getString(ControleActivity.PERIODO);
            int tipo = bundle.getInt(ControleActivity.TIPO);
            boolean aluno = bundle.getBoolean(ControleActivity.ALUNO);
            boolean monitor = bundle.getBoolean(ControleActivity.MONITOR);
            String campus = bundle.getString(ControleActivity.CAMPUS);

            if(requestCode == ControleActivity.ALTERAR){

                Controle controle = listaControle.get(posicaoSelecionada);

                controle.setDisciplina(nome);
                controle.setPeriodo(periodo);
                controle.setCampus(campus);
                controle.setTipo(tipo);
                controle.setAluno(aluno);
                controle.setMonitor(monitor);

               posicaoSelecionada = -1;

            }else {

                listaControle.add(new Controle(nome, periodo, tipo, aluno, monitor, campus));

            }

            listaAdapter.notifyDataSetChanged();

        }

    }
        public void abrirSobre(View view){
            SobreActivity.sobre(this);
    }
}