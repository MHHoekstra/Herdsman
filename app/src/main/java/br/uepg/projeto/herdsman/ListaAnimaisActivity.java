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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.SearchView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.uepg.projeto.herdsman.DAO.HerdsmanContract;
import br.uepg.projeto.herdsman.DAO.HerdsmanDbHelper;
import br.uepg.projeto.herdsman.Objetos.Animal;

public class ListaAnimaisActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    ListView animaisListView;
    private SearchView searchView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_animais);
        searchView = (SearchView) findViewById(R.id.animal_pesquisa);
        animaisListView = (ListView) findViewById(R.id.animaisListView);
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.add_animal);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_cadastro = new Intent(ListaAnimaisActivity.this, CadastroAnimalActivity.class);
                startActivity(intent_cadastro);
            }
        });
        animaisListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Animal animal = (Animal) animaisListView.getItemAtPosition(position);
                    Intent intent = new Intent(ListaAnimaisActivity.this, TelaAnimalActivity.class);
                    intent.putExtra("Animal", (Serializable) animal);
                    startActivity(intent);

            }
        });
        animaisListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Animal animal = (Animal) animaisListView.getItemAtPosition(position);
                Intent intent = new Intent(ListaAnimaisActivity.this, CadastroAnimalActivity.class);
                intent.putExtra("Animal", (Serializable) animal);
                startActivity(intent);
                return true;
            }
        });
        setupSearchview();
        listarAnimais();
    }

    private void setupSearchview() {
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(false);
        searchView.setQueryHint("Pesquise aqui");
    }

    private void listarAnimais() {
        animaisListView = (ListView) findViewById(R.id.animaisListView);
        HerdsmanDbHelper mDbHelper = new HerdsmanDbHelper(this);
        ArrayList listaAnimal = (ArrayList<Animal>) mDbHelper.carregarAnimaisDb();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listaAnimal);
        animaisListView.setAdapter(adapter);
        animaisListView.setTextFilterEnabled(false);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        listarAnimais();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) animaisListView.getAdapter();
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
