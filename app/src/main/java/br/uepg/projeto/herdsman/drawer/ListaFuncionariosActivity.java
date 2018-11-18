package br.uepg.projeto.herdsman.drawer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

import br.uepg.projeto.herdsman.cadastros.CadastroFuncionarioActivity;
import br.uepg.projeto.herdsman.dao.HerdsmanDbHelper;
import br.uepg.projeto.herdsman.objetos.Pessoa;
import br.uepg.projeto.herdsman.R;
import br.uepg.projeto.herdsman.drawer.funcionario.TelaFuncionarioActivity;

public class ListaFuncionariosActivity extends TelasActivity implements SearchView.OnQueryTextListener{
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
        setTitle("Funcionários");
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
        mDbHelper.searchDuplicatePessoa();
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
}
