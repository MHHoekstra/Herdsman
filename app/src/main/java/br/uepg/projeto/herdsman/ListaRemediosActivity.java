package br.uepg.projeto.herdsman;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import br.uepg.projeto.herdsman.DAO.HerdsmanContract;
import br.uepg.projeto.herdsman.DAO.HerdsmanDbHelper;

import static br.uepg.projeto.herdsman.DAO.HerdsmanContract.RemedioEntry.TABLE_NAME;

public class ListaRemediosActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    SearchView pesquisa;
    ListView listaRemedios;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_remedios);
        listaRemedios = (ListView) findViewById(R.id.remediosListView);
        FloatingActionButton addRemedio = (FloatingActionButton) findViewById(R.id.add_remedio);
        pesquisa = (SearchView) findViewById(R.id.lista_remedios_pesquisa);
        addRemedio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListaRemediosActivity.this, CadastroRemedioActivity.class);
                startActivity(intent);
            }
        });
        setupSearchView();
        listarRemedios();
    }

    private void setupSearchView() {
        pesquisa.setIconifiedByDefault(false);
        pesquisa.setSubmitButtonEnabled(false);
        pesquisa.setQueryHint("Pesquise aqui");
        pesquisa.setOnQueryTextListener(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        listarRemedios();
    }

    private void listarRemedios()
    {
        HerdsmanDbHelper mDbHelper = new HerdsmanDbHelper(this);
        SQLiteDatabase mDb = mDbHelper.getReadableDatabase();
        Cursor cursor;
        cursor = mDb.query(
                TABLE_NAME,
                new String[]{HerdsmanContract.RemedioEntry.COLUMN_NAME_NOME},
                null,
                null,
                null,
                null,
                HerdsmanContract.RemedioEntry.COLUMN_NAME_NOME
        );
        ArrayList<String> lista = new ArrayList<>();
        while (cursor.moveToNext())
        {
            lista.add(cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.RemedioEntry.COLUMN_NAME_NOME)));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lista);
        listaRemedios.setAdapter(adapter);
        mDb.close();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) listaRemedios.getAdapter();
        Filter filter = adapter.getFilter();
        if (TextUtils.isEmpty(newText))
        {
            filter.filter("");
            return true;
        }
        else
        {
            filter.filter(newText.toString());
            return true;
        }
    }
}
