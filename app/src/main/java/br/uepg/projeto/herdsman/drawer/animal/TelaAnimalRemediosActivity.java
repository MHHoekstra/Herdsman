package br.uepg.projeto.herdsman.drawer.animal;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
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
import br.uepg.projeto.herdsman.objetos.Animal;
import br.uepg.projeto.herdsman.R;

public class TelaAnimalRemediosActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    ListView listView;
    FloatingActionButton add;
    TextView titulo;
    Animal animal;
    Boolean adm;
    public static final String myPref = "preferenceName";
    SharedPreferences pref;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_partos);
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



        animal = (Animal) getIntent().getSerializableExtra("Animal");
        listView = findViewById(R.id.lista_partos_listview);
        add = findViewById(R.id.lista_partos_add);
        titulo = findViewById(R.id.lista_partos_titulo);
        titulo.setText("Remédios de " + animal.getNumero());
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TelaAnimalRemediosActivity.this, TelaAnimalCadastroAnimalRemedio.class);
                intent.putExtra("Animal", animal);
                startActivity(intent);
            }
        });
        listarRemedios();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        listarRemedios();
    }

    private void listarRemedios()
    {
        HerdsmanDbHelper mDbHelper = new HerdsmanDbHelper(TelaAnimalRemediosActivity.this);
        ArrayList list = mDbHelper.carregarRemediosAnimal(animal);
        ArrayAdapter adapter = new ArrayAdapter(TelaAnimalRemediosActivity.this, android.R.layout.simple_list_item_1, list);
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
            if (!adm)
            {
                Toast.makeText(TelaAnimalRemediosActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(TelaAnimalRemediosActivity.this, ListaAnimaisActivity.class);
                TelaAnimalRemediosActivity.this.startActivity(intent);
            }

        } else if (id == R.id.nav_enfermidades) {

            if (!adm)
            {
                Toast.makeText(TelaAnimalRemediosActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(TelaAnimalRemediosActivity.this, ListaEnfermidadesActivity.class);
                TelaAnimalRemediosActivity.this.startActivity(intent);
            }

        } else if (id == R.id.nav_remedios) {

            if (!adm)
            {
                Toast.makeText(TelaAnimalRemediosActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(TelaAnimalRemediosActivity.this, ListaRemediosActivity.class);
                TelaAnimalRemediosActivity.this.startActivity(intent);
            }


        } else if (id == R.id.nav_funcionarios) {

            if (!adm)
            {
                Toast.makeText(TelaAnimalRemediosActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(TelaAnimalRemediosActivity.this, ListaFuncionariosActivity.class);
                TelaAnimalRemediosActivity.this.startActivity(intent);
            }

        } else if (id == R.id.nav_cio) {

            Intent intent = new Intent(TelaAnimalRemediosActivity.this, NotificarCioActivity.class);
            TelaAnimalRemediosActivity.this.startActivity(intent);

        } else if (id == R.id.nav_sinistro) {

            Intent intent = new Intent(TelaAnimalRemediosActivity.this, NotificarAnimalEnfermidadeActivity.class);
            TelaAnimalRemediosActivity.this.startActivity(intent);

        } else if (id == R.id.nav_outro) {

            if (!adm)
            {
                Toast.makeText(TelaAnimalRemediosActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(TelaAnimalRemediosActivity.this, NotificarOutroActivity.class);
                TelaAnimalRemediosActivity.this.startActivity(intent);
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
