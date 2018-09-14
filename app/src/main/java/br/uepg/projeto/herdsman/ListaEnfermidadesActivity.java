package br.uepg.projeto.herdsman;

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
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

import br.uepg.projeto.herdsman.DAO.HerdsmanDbHelper;

public class ListaEnfermidadesActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, NavigationView.OnNavigationItemSelectedListener {
    ListView listaEnfermidades;
    SearchView pesquisa;
    Boolean adm;
    public static final String myPref = "preferenceName";
    SharedPreferences pref;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_enfermidades);

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
                Toast.makeText(ListaEnfermidadesActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(ListaEnfermidadesActivity.this, ListaAnimaisActivity.class);
                ListaEnfermidadesActivity.this.startActivity(intent);
            }

        } else if (id == R.id.nav_enfermidades) {

            if (!adm)
            {
                Toast.makeText(ListaEnfermidadesActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(ListaEnfermidadesActivity.this, ListaEnfermidadesActivity.class);
                ListaEnfermidadesActivity.this.startActivity(intent);
            }

        } else if (id == R.id.nav_remedios) {

            if (!adm)
            {
                Toast.makeText(ListaEnfermidadesActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(ListaEnfermidadesActivity.this, ListaRemediosActivity.class);
                ListaEnfermidadesActivity.this.startActivity(intent);
            }


        } else if (id == R.id.nav_funcionarios) {

            if (!adm)
            {
                Toast.makeText(ListaEnfermidadesActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(ListaEnfermidadesActivity.this, ListaFuncionariosActivity.class);
                ListaEnfermidadesActivity.this.startActivity(intent);
            }

        } else if (id == R.id.nav_cio) {

            Intent intent = new Intent(ListaEnfermidadesActivity.this, NotificarCioActivity.class);
            ListaEnfermidadesActivity.this.startActivity(intent);

        } else if (id == R.id.nav_sinistro) {

            Intent intent = new Intent(ListaEnfermidadesActivity.this, NotificarSinistroActivity.class);
            ListaEnfermidadesActivity.this.startActivity(intent);

        } else if (id == R.id.nav_outro) {

            if (!adm)
            {
                Toast.makeText(ListaEnfermidadesActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(ListaEnfermidadesActivity.this, NotificarOutroActivity.class);
                ListaEnfermidadesActivity.this.startActivity(intent);
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
