package br.uepg.projeto.herdsman;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import br.uepg.projeto.herdsman.DAO.HerdsmanDbHelper;
import br.uepg.projeto.herdsman.Objetos.Animal;

public class TelaAnimalSinistrosActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ListView listView;
    Animal animal;

    Boolean adm;
    public static final String myPref = "preferenceName";
    SharedPreferences pref;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_cios);
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
        TextView titulo = findViewById(R.id.lista_cios_titulo);
        listView = findViewById(R.id.lista_cios_listview);
        animal = (Animal) getIntent().getSerializableExtra("Animal");
        titulo.setText("Sinistros de " + animal.getNumero());

        carregarSinistros();

    }

    private void carregarSinistros() {
        HerdsmanDbHelper herdsmanDbHelper = new HerdsmanDbHelper(TelaAnimalSinistrosActivity.this);
        ArrayList list = herdsmanDbHelper.carregarSinistrosAnimal(animal);
        ArrayAdapter adapter = new ArrayAdapter(TelaAnimalSinistrosActivity.this, android.R.layout.simple_list_item_1, list);
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
                Toast.makeText(TelaAnimalSinistrosActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(TelaAnimalSinistrosActivity.this, ListaAnimaisActivity.class);
                TelaAnimalSinistrosActivity.this.startActivity(intent);
            }

        } else if (id == R.id.nav_enfermidades) {

            if (!adm)
            {
                Toast.makeText(TelaAnimalSinistrosActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(TelaAnimalSinistrosActivity.this, ListaEnfermidadesActivity.class);
                TelaAnimalSinistrosActivity.this.startActivity(intent);
            }

        } else if (id == R.id.nav_remedios) {

            if (!adm)
            {
                Toast.makeText(TelaAnimalSinistrosActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(TelaAnimalSinistrosActivity.this, ListaRemediosActivity.class);
                TelaAnimalSinistrosActivity.this.startActivity(intent);
            }


        } else if (id == R.id.nav_funcionarios) {

            if (!adm)
            {
                Toast.makeText(TelaAnimalSinistrosActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(TelaAnimalSinistrosActivity.this, ListaFuncionariosActivity.class);
                TelaAnimalSinistrosActivity.this.startActivity(intent);
            }

        } else if (id == R.id.nav_cio) {

            Intent intent = new Intent(TelaAnimalSinistrosActivity.this, NotificarCioActivity.class);
            TelaAnimalSinistrosActivity.this.startActivity(intent);

        } else if (id == R.id.nav_sinistro) {

            Intent intent = new Intent(TelaAnimalSinistrosActivity.this, NotificarSinistroActivity.class);
            TelaAnimalSinistrosActivity.this.startActivity(intent);

        } else if (id == R.id.nav_outro) {

            if (!adm)
            {
                Toast.makeText(TelaAnimalSinistrosActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(TelaAnimalSinistrosActivity.this, NotificarOutroActivity.class);
                TelaAnimalSinistrosActivity.this.startActivity(intent);
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }









}
