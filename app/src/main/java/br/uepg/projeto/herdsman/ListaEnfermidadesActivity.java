package br.uepg.projeto.herdsman;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

import br.uepg.projeto.herdsman.DAO.HerdsmanDbHelper;

public class ListaEnfermidadesActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    ListView listaEnfermidades;
    SearchView pesquisa;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_enfermidades);
        listaEnfermidades = (ListView) findViewById(R.id.enfermidadesListView);
        pesquisa = (SearchView) findViewById(R.id.lista_enfermidades_pesquisa);
        FloatingActionButton addEnfermidade = (FloatingActionButton) findViewById(R.id.add_enfermidade);

        addEnfermidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListaEnfermidadesActivity.this, CadastroEnfermidadeActivity.class);
                startActivity(intent);
            }
        });
        setupSearchView();
        listaEnfermidades();
    }

    private void setupSearchView() {
        pesquisa.setIconifiedByDefault(false);
        pesquisa.setOnQueryTextListener((SearchView.OnQueryTextListener) this);
        pesquisa.setSubmitButtonEnabled(false);
        pesquisa.setQueryHint("Pesquise aqui");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        listaEnfermidades();
    }

    private void listaEnfermidades()
    {
        HerdsmanDbHelper mDbHelper = new HerdsmanDbHelper(ListaEnfermidadesActivity.this);
        ArrayList enfermidades = mDbHelper.carregarEnfermidades();
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, enfermidades);
        listaEnfermidades.setAdapter(adapter);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) listaEnfermidades.getAdapter();
        Filter filter = adapter.getFilter();
        if (TextUtils.isEmpty(newText))
        {
            filter.filter("");
        }
        else {
            filter.filter(newText.toString());
        }
        return true;
    }
}
