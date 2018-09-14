package br.uepg.projeto.herdsman;

import android.app.DatePickerDialog;
import android.app.FragmentManager;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import br.uepg.projeto.herdsman.DAO.HerdsmanContract;
import br.uepg.projeto.herdsman.DAO.HerdsmanDbHelper;

public class TelaAnimalCadastroAnimalRemedio extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, NavigationView.OnNavigationItemSelectedListener{

    Spinner remedioSpinner;
    Spinner medidasSpinner;
    EditText quantidadeText;
    Button data;
    FloatingActionButton done;
    FloatingActionButton cancel;
    String dataString;
    Boolean adm;
    public static final String myPref = "preferenceName";
    SharedPreferences pref;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_animal_remedio);
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


        done = findViewById(R.id.cadastro_animal_remedio_done);
        cancel = findViewById(R.id.cadastro_animal_remedio_cancelar);
        remedioSpinner = findViewById(R.id.cadastro_animal_remedio_remedio_spinner);
        medidasSpinner = findViewById(R.id.cadastro_animal_remedio_medida_spinner);
        quantidadeText = findViewById(R.id.cadastro_animal_remedio_quantidade);
        data = findViewById(R.id.cadastro_animal_remedio_data);
        Calendar c = Calendar.getInstance();
        int diaMes =  c.get(Calendar.DAY_OF_MONTH);
        int mes = c.get(Calendar.MONTH);
        int ano = c.get(Calendar.YEAR);
        String dia;
        if (String.valueOf(diaMes).length() == 1)
        {
            dia = '0' + String.valueOf(diaMes);
        }
        else
        {
            dia = String.valueOf(diaMes);
        }
        dataString = String.valueOf(ano) + '-' + String.valueOf(mes) + '-' + dia;
        data.setText("Data: " + dia+'/'+String.valueOf(mes)+'/'+String.valueOf(ano));
        carregarMedidas();
        carregarRemedios();
        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment fragment = new DatePickerFragment();
                fragment.show(getFragmentManager(), "Data");
            }
        });



        //TODO Terminar Inserção

    }


    private void carregarMedidas()
    {
        HerdsmanDbHelper herdsmanDbHelper = new HerdsmanDbHelper(TelaAnimalCadastroAnimalRemedio.this);
        ArrayList listaMedidas = herdsmanDbHelper.carregarMedidas();
        ArrayAdapter adapterMedidas = new ArrayAdapter(TelaAnimalCadastroAnimalRemedio.this, android.R.layout.simple_spinner_dropdown_item, listaMedidas);
        medidasSpinner.setAdapter(adapterMedidas);
    }

    private void carregarRemedios()
    {
        HerdsmanDbHelper herdsmanDbHelper = new HerdsmanDbHelper(TelaAnimalCadastroAnimalRemedio.this);
        ArrayList listaRemedios = herdsmanDbHelper.carregarTodosRemedios();
        ArrayAdapter adapterRemedios = new ArrayAdapter(TelaAnimalCadastroAnimalRemedio.this, android.R.layout.simple_spinner_dropdown_item, listaRemedios);
        remedioSpinner.setAdapter(adapterRemedios);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        String dia;
        if(String.valueOf(i2).length() == 1)
        {
            dia = '0' + String.valueOf(i2);
        }
        else
        {
            dia = String.valueOf(i2);
        }
        dataString = String.valueOf(i) + "-" + String.valueOf(i1)+'-'+ dia;
        data.setText("Data: "+ dia + '/' + String.valueOf(i1) + '/' + String.valueOf(i));
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
                Toast.makeText(TelaAnimalCadastroAnimalRemedio.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(TelaAnimalCadastroAnimalRemedio.this, ListaAnimaisActivity.class);
                TelaAnimalCadastroAnimalRemedio.this.startActivity(intent);
            }

        } else if (id == R.id.nav_enfermidades) {

            if (!adm)
            {
                Toast.makeText(TelaAnimalCadastroAnimalRemedio.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(TelaAnimalCadastroAnimalRemedio.this, ListaEnfermidadesActivity.class);
                TelaAnimalCadastroAnimalRemedio.this.startActivity(intent);
            }

        } else if (id == R.id.nav_remedios) {

            if (!adm)
            {
                Toast.makeText(TelaAnimalCadastroAnimalRemedio.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(TelaAnimalCadastroAnimalRemedio.this, ListaRemediosActivity.class);
                TelaAnimalCadastroAnimalRemedio.this.startActivity(intent);
            }


        } else if (id == R.id.nav_funcionarios) {

            if (!adm)
            {
                Toast.makeText(TelaAnimalCadastroAnimalRemedio.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(TelaAnimalCadastroAnimalRemedio.this, ListaFuncionariosActivity.class);
                TelaAnimalCadastroAnimalRemedio.this.startActivity(intent);
            }

        } else if (id == R.id.nav_cio) {

            Intent intent = new Intent(TelaAnimalCadastroAnimalRemedio.this, NotificarCioActivity.class);
            TelaAnimalCadastroAnimalRemedio.this.startActivity(intent);

        } else if (id == R.id.nav_sinistro) {

            Intent intent = new Intent(TelaAnimalCadastroAnimalRemedio.this, NotificarSinistroActivity.class);
            TelaAnimalCadastroAnimalRemedio.this.startActivity(intent);

        } else if (id == R.id.nav_outro) {

            if (!adm)
            {
                Toast.makeText(TelaAnimalCadastroAnimalRemedio.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(TelaAnimalCadastroAnimalRemedio.this, NotificarOutroActivity.class);
                TelaAnimalCadastroAnimalRemedio.this.startActivity(intent);
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
