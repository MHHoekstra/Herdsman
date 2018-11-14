package br.uepg.projeto.herdsman.drawer.animal;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import br.uepg.projeto.herdsman.dao.HerdsmanDbHelper;
import br.uepg.projeto.herdsman.utils.DatePickerFragment;
import br.uepg.projeto.herdsman.drawer.ListaAnimaisActivity;
import br.uepg.projeto.herdsman.drawer.ListaEnfermidadesActivity;
import br.uepg.projeto.herdsman.drawer.ListaFuncionariosActivity;
import br.uepg.projeto.herdsman.drawer.ListaRemediosActivity;
import br.uepg.projeto.herdsman.drawer.notificacao.NotificarCioActivity;
import br.uepg.projeto.herdsman.drawer.notificacao.NotificarOutroActivity;
import br.uepg.projeto.herdsman.drawer.notificacao.NotificarAnimalEnfermidadeActivity;
import br.uepg.projeto.herdsman.objetos.Animal;
import br.uepg.projeto.herdsman.objetos.Inseminacao;
import br.uepg.projeto.herdsman.R;

public class TelaAnimalInseminacoesActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, NavigationView.OnNavigationItemSelectedListener {
    Animal animal;
    TextView titulo;
    ListView listView;
    FloatingActionButton add;
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

        setTitle(animal.getNumero() + " - " + animal.getNome());
        titulo = findViewById(R.id.lista_partos_titulo);
        listView = findViewById(R.id.lista_partos_listview);
        add = findViewById(R.id.lista_partos_add);

        titulo.setText("Inseminações");

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment fragment = new DatePickerFragment();
                fragment.show(getFragmentManager(), "Data");
            }
        });

        listarInseminacoes();
    }

    private void listarInseminacoes()
    {
        HerdsmanDbHelper mDbHelper = new HerdsmanDbHelper(TelaAnimalInseminacoesActivity.this);
        ArrayList list = mDbHelper.carregarInseminacoesAnimal(animal);
        ArrayAdapter adapter = new ArrayAdapter(TelaAnimalInseminacoesActivity.this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        String dia;
        if(String.valueOf(i2).length() == 1)
        {
            dia = '0' + String.valueOf(i2);
        }
        else {
            dia = String.valueOf(i2);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(i,i1,i2);
        long data = calendar.getTimeInMillis();
        Inseminacao inseminacao = new Inseminacao(animal.getId(), data);
        HerdsmanDbHelper mDbHelper = new HerdsmanDbHelper(TelaAnimalInseminacoesActivity.this);
        //TODO Inserir no firebase
        long ins = mDbHelper.inserirInseminacao(inseminacao);
        if(ins > 0)
        {
            Toast.makeText(TelaAnimalInseminacoesActivity.this, "Inseminação inserida", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(TelaAnimalInseminacoesActivity.this, "Falha ao inserir", Toast.LENGTH_SHORT).show();
        }

        listarInseminacoes();

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
                Toast.makeText(TelaAnimalInseminacoesActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(TelaAnimalInseminacoesActivity.this, ListaAnimaisActivity.class);
                TelaAnimalInseminacoesActivity.this.startActivity(intent);
            }

        } else if (id == R.id.nav_enfermidades) {

            if (!adm)
            {
                Toast.makeText(TelaAnimalInseminacoesActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(TelaAnimalInseminacoesActivity.this, ListaEnfermidadesActivity.class);
                TelaAnimalInseminacoesActivity.this.startActivity(intent);
            }

        } else if (id == R.id.nav_remedios) {

            if (!adm)
            {
                Toast.makeText(TelaAnimalInseminacoesActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(TelaAnimalInseminacoesActivity.this, ListaRemediosActivity.class);
                TelaAnimalInseminacoesActivity.this.startActivity(intent);
            }


        } else if (id == R.id.nav_funcionarios) {

            if (!adm)
            {
                Toast.makeText(TelaAnimalInseminacoesActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(TelaAnimalInseminacoesActivity.this, ListaFuncionariosActivity.class);
                TelaAnimalInseminacoesActivity.this.startActivity(intent);
            }

        } else if (id == R.id.nav_cio) {

            Intent intent = new Intent(TelaAnimalInseminacoesActivity.this, NotificarCioActivity.class);
            TelaAnimalInseminacoesActivity.this.startActivity(intent);

        } else if (id == R.id.nav_sinistro) {

            Intent intent = new Intent(TelaAnimalInseminacoesActivity.this, NotificarAnimalEnfermidadeActivity.class);
            TelaAnimalInseminacoesActivity.this.startActivity(intent);

        } else if (id == R.id.nav_outro) {

            if (!adm)
            {
                Toast.makeText(TelaAnimalInseminacoesActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(TelaAnimalInseminacoesActivity.this, NotificarOutroActivity.class);
                TelaAnimalInseminacoesActivity.this.startActivity(intent);
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
