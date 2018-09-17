package br.uepg.projeto.herdsman.drawer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

import br.uepg.projeto.herdsman.MainActivity;
import br.uepg.projeto.herdsman.cadastros.CadastroAnimalActivity;
import br.uepg.projeto.herdsman.dao.HerdsmanDbHelper;
import br.uepg.projeto.herdsman.drawer.notificacao.NotificarCioActivity;
import br.uepg.projeto.herdsman.drawer.notificacao.NotificarOutroActivity;
import br.uepg.projeto.herdsman.drawer.notificacao.NotificarSinistroActivity;
import br.uepg.projeto.herdsman.objetos.Animal;
import br.uepg.projeto.herdsman.R;
import br.uepg.projeto.herdsman.drawer.animal.TelaAnimalActivity;

public class ListaAnimaisActivity extends TelasActivity implements SearchView.OnQueryTextListener{
    ListView animaisListView;
    private SearchView searchView;
    Boolean adm;
    public static final String myPref = "preferenceName";
    SharedPreferences pref;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_animais);
        setTitle("Animais");
        pref = getApplicationContext().getSharedPreferences("isAdmin", MODE_PRIVATE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




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
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(ListaAnimaisActivity.this, MainActivity.class);
            ListaAnimaisActivity.this.startActivity(intent);
            super.onBackPressed();
        }
    }
}
