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

import br.uepg.projeto.herdsman.MainActivity;
import br.uepg.projeto.herdsman.cadastros.CadastroRemedioActivity;
import br.uepg.projeto.herdsman.dao.HerdsmanDbHelper;
import br.uepg.projeto.herdsman.drawer.notificacao.NotificarCioActivity;
import br.uepg.projeto.herdsman.drawer.notificacao.NotificarOutroActivity;
import br.uepg.projeto.herdsman.drawer.notificacao.NotificarAnimalEnfermidadeActivity;
import br.uepg.projeto.herdsman.R;
import br.uepg.projeto.herdsman.objetos.Remedio;

public class ListaRemediosActivity extends TelasActivity implements SearchView.OnQueryTextListener{
    SearchView pesquisa;
    ListView listaRemedios;
    Boolean adm;
    public static final String myPref = "preferenceName";
    SharedPreferences pref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_remedios);
        setTitle("Remédios");
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
        listaRemedios.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Remedio remedio = (Remedio) listaRemedios.getItemAtPosition(position);
                Intent intent = new Intent(ListaRemediosActivity.this, CadastroRemedioActivity.class);
                intent.putExtra("Remedio", remedio);
                startActivity(intent);
                return true;
            }
        });
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
        HerdsmanDbHelper herdsmanDbHelper = new HerdsmanDbHelper(this);
        ArrayList lista = herdsmanDbHelper.carregarTodosRemedios();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lista);
        listaRemedios.setAdapter(adapter);
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
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(ListaRemediosActivity.this, MainActivity.class);
            ListaRemediosActivity.this.startActivity(intent);
            //super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        int id = item.getItemId();
        adm = pref.getBoolean("isAdmin", false);
        if (id == R.id.nav_animais) {
            if (!adm)
            {
                Toast.makeText(ListaRemediosActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(ListaRemediosActivity.this, ListaAnimaisActivity.class);
                ListaRemediosActivity.this.startActivity(intent);
            }

        } else if (id == R.id.nav_enfermidades) {

            if (!adm)
            {
                Toast.makeText(ListaRemediosActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(ListaRemediosActivity.this, ListaEnfermidadesActivity.class);
                ListaRemediosActivity.this.startActivity(intent);
            }

        } else if (id == R.id.nav_remedios) {

            if (!adm)
            {
                Toast.makeText(ListaRemediosActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                drawer.closeDrawer(GravityCompat.START);
            }


        } else if (id == R.id.nav_funcionarios) {

            if (!adm)
            {
                Toast.makeText(ListaRemediosActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(ListaRemediosActivity.this, ListaFuncionariosActivity.class);
                ListaRemediosActivity.this.startActivity(intent);
            }

        } else if (id == R.id.nav_cio) {

            Intent intent = new Intent(ListaRemediosActivity.this, NotificarCioActivity.class);
            ListaRemediosActivity.this.startActivity(intent);

        } else if (id == R.id.nav_sinistro) {

            Intent intent = new Intent(ListaRemediosActivity.this, NotificarAnimalEnfermidadeActivity.class);
            ListaRemediosActivity.this.startActivity(intent);

        } else if (id == R.id.nav_outro) {

            if (!adm)
            {
                Toast.makeText(ListaRemediosActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(ListaRemediosActivity.this, NotificarOutroActivity.class);
                ListaRemediosActivity.this.startActivity(intent);
            }

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
