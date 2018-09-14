package br.uepg.projeto.herdsman;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import java.util.ArrayList;
import java.util.List;

import br.uepg.projeto.herdsman.DAO.HerdsmanContract;
import br.uepg.projeto.herdsman.DAO.HerdsmanDbHelper;
import br.uepg.projeto.herdsman.Objetos.Pessoa;
import br.uepg.projeto.herdsman.Objetos.Telefone;

public class ListaFuncionariosActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, NavigationView.OnNavigationItemSelectedListener {
    SearchView pesquisa;
    Pessoa pessoa;
    ListView listaFuncionariosView;
    Boolean adm;
    public static final String myPref = "preferenceName";
    SharedPreferences pref;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_funcionarios);

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


        pesquisa = (SearchView) findViewById(R.id.lista_funcionarios_pesquisa);
        listaFuncionariosView = (ListView) findViewById(R.id.funcionariosListView);
        FloatingActionButton addFuncionario = (FloatingActionButton) findViewById(R.id.add_funcionario);
        addFuncionario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListaFuncionariosActivity.this, CadastroFuncionarioActivity.class);
                startActivity(intent);
            }
        });
        listaFuncionariosView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                pessoa  = (Pessoa) listaFuncionariosView.getItemAtPosition(position);
                Intent intent = new Intent(ListaFuncionariosActivity.this, CadastroFuncionarioActivity.class);
                intent.putExtra("Pessoa", pessoa);
                startActivity(intent);
                return true;
            }
        });
        listaFuncionariosView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ListaFuncionariosActivity.this, TelaFuncionarioActivity.class);
                pessoa = (Pessoa) listaFuncionariosView.getItemAtPosition(position);
                intent.putExtra("Pessoa", pessoa);
                startActivity(intent);
            }
        });
        setupSearchView();
        listaFuncionarios();

    }

    private void setupSearchView() {
        pesquisa.setQueryHint("Pesquise aqui");
        pesquisa.setSubmitButtonEnabled(false);
        pesquisa.setOnQueryTextListener(this);
        pesquisa.setIconifiedByDefault(false);
    }

    private void listaFuncionarios() {
        listaFuncionariosView = (ListView) findViewById(R.id.funcionariosListView);
        HerdsmanDbHelper mDbHelper = new HerdsmanDbHelper(this);
        ArrayList listPessoas = mDbHelper.carregarFuncionariosDb();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listPessoas);
        listaFuncionariosView.setAdapter(adapter);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        listaFuncionarios();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) listaFuncionariosView.getAdapter();
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
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        adm = pref.getBoolean("isAdmin", false);
        if (id == R.id.nav_animais) {
            if (!adm)
            {
                Toast.makeText(ListaFuncionariosActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(ListaFuncionariosActivity.this, ListaAnimaisActivity.class);
                ListaFuncionariosActivity.this.startActivity(intent);
            }

        } else if (id == R.id.nav_enfermidades) {

            if (!adm)
            {
                Toast.makeText(ListaFuncionariosActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(ListaFuncionariosActivity.this, ListaEnfermidadesActivity.class);
                ListaFuncionariosActivity.this.startActivity(intent);
            }

        } else if (id == R.id.nav_remedios) {

            if (!adm)
            {
                Toast.makeText(ListaFuncionariosActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(ListaFuncionariosActivity.this, ListaRemediosActivity.class);
                ListaFuncionariosActivity.this.startActivity(intent);
            }


        } else if (id == R.id.nav_funcionarios) {

            if (!adm)
            {
                Toast.makeText(ListaFuncionariosActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(ListaFuncionariosActivity.this, ListaFuncionariosActivity.class);
                ListaFuncionariosActivity.this.startActivity(intent);
            }

        } else if (id == R.id.nav_cio) {

            Intent intent = new Intent(ListaFuncionariosActivity.this, NotificarCioActivity.class);
            ListaFuncionariosActivity.this.startActivity(intent);

        } else if (id == R.id.nav_sinistro) {

            Intent intent = new Intent(ListaFuncionariosActivity.this, NotificarSinistroActivity.class);
            ListaFuncionariosActivity.this.startActivity(intent);

        } else if (id == R.id.nav_outro) {

            if (!adm)
            {
                Toast.makeText(ListaFuncionariosActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(ListaFuncionariosActivity.this, NotificarOutroActivity.class);
                ListaFuncionariosActivity.this.startActivity(intent);
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
