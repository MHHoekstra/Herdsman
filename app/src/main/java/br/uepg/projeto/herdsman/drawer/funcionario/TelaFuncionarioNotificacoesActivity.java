package br.uepg.projeto.herdsman.drawer.funcionario;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import br.uepg.projeto.herdsman.dao.HerdsmanDbHelper;
import br.uepg.projeto.herdsman.drawer.ListaAnimaisActivity;
import br.uepg.projeto.herdsman.drawer.ListaEnfermidadesActivity;
import br.uepg.projeto.herdsman.drawer.ListaFuncionariosActivity;
import br.uepg.projeto.herdsman.drawer.ListaRemediosActivity;
import br.uepg.projeto.herdsman.drawer.notificacao.NotificarCioActivity;
import br.uepg.projeto.herdsman.drawer.notificacao.NotificarOutroActivity;
import br.uepg.projeto.herdsman.drawer.notificacao.NotificarAnimalEnfermidadeActivity;
import br.uepg.projeto.herdsman.objetos.Pessoa;
import br.uepg.projeto.herdsman.R;

public class TelaFuncionarioNotificacoesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ListView listView;
    Pessoa pessoa;
    Boolean adm;
    public static final String myPref = "preferenceName";
    SharedPreferences pref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_funcionario_notificacoes);

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

        pessoa = (Pessoa) getIntent().getSerializableExtra("Funcionario");

        listView = findViewById(R.id.tela_funcionario_notificacoes_listview);
        Button sinistros = findViewById(R.id.tela_funcionario_notificacoes_sinistros);
        Button cios = findViewById(R.id.tela_funcionario_notificacoes_cios);

        sinistros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                carregarSinistros();

            }
        });

        cios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                carregarCios();
            }
        });



    }

    private void carregarCios() {
        HerdsmanDbHelper herdsmanDbHelper = new HerdsmanDbHelper(TelaFuncionarioNotificacoesActivity.this);
        ArrayList ciosList = herdsmanDbHelper.carregarCiosFuncionario(pessoa);
        ArrayAdapter adapter = new ArrayAdapter(TelaFuncionarioNotificacoesActivity.this, android.R.layout.simple_list_item_1, ciosList);
        listView.setAdapter(adapter);
    }

    private void carregarSinistros() {
        HerdsmanDbHelper herdsmanDbHelper = new HerdsmanDbHelper(TelaFuncionarioNotificacoesActivity.this);
        ArrayList sinistrosList = herdsmanDbHelper.carregarSinistrosFuncionario(pessoa);
        ArrayAdapter adapter = new ArrayAdapter(TelaFuncionarioNotificacoesActivity.this, android.R.layout.simple_list_item_1, sinistrosList);
        listView.setAdapter(adapter);
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
            if (false)
            {
                Toast.makeText(TelaFuncionarioNotificacoesActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(TelaFuncionarioNotificacoesActivity.this, ListaAnimaisActivity.class);
                TelaFuncionarioNotificacoesActivity.this.startActivity(intent);
            }

        } else if (id == R.id.nav_enfermidades) {

            if (!adm)
            {
                Toast.makeText(TelaFuncionarioNotificacoesActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(TelaFuncionarioNotificacoesActivity.this, ListaEnfermidadesActivity.class);
                TelaFuncionarioNotificacoesActivity.this.startActivity(intent);
            }

        } else if (id == R.id.nav_remedios) {

            if (!adm)
            {
                Toast.makeText(TelaFuncionarioNotificacoesActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(TelaFuncionarioNotificacoesActivity.this, ListaRemediosActivity.class);
                TelaFuncionarioNotificacoesActivity.this.startActivity(intent);
            }


        } else if (id == R.id.nav_funcionarios) {

            if (!adm)
            {
                Toast.makeText(TelaFuncionarioNotificacoesActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(TelaFuncionarioNotificacoesActivity.this, ListaFuncionariosActivity.class);
                TelaFuncionarioNotificacoesActivity.this.startActivity(intent);
            }

        } else if (id == R.id.nav_cio) {

            Intent intent = new Intent(TelaFuncionarioNotificacoesActivity.this, NotificarCioActivity.class);
            TelaFuncionarioNotificacoesActivity.this.startActivity(intent);

        } else if (id == R.id.nav_sinistro) {

            Intent intent = new Intent(TelaFuncionarioNotificacoesActivity.this, NotificarAnimalEnfermidadeActivity.class);
            TelaFuncionarioNotificacoesActivity.this.startActivity(intent);

        } else if (id == R.id.nav_outro) {

            if (!adm)
            {
                Toast.makeText(TelaFuncionarioNotificacoesActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(TelaFuncionarioNotificacoesActivity.this, NotificarOutroActivity.class);
                TelaFuncionarioNotificacoesActivity.this.startActivity(intent);
            }

        }
        //TODO HELPER
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }







}
