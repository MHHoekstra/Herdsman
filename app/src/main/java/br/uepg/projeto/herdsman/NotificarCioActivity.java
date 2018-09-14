package br.uepg.projeto.herdsman;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import br.uepg.projeto.herdsman.DAO.HerdsmanDbHelper;
import br.uepg.projeto.herdsman.Objetos.Animal;
import br.uepg.projeto.herdsman.Objetos.Cio;
import br.uepg.projeto.herdsman.Objetos.Enfermidade;
import br.uepg.projeto.herdsman.Objetos.Sinistro;
import br.uepg.projeto.herdsman.Objetos.Telefone;

public class NotificarCioActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    Boolean adm;
    public static final String myPref = "preferenceName";
    SharedPreferences pref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notificar_cio);

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




        final Spinner animalPorCimaSpinner = findViewById(R.id.notificar_cio_por_cima_spinner);
        final Spinner animalPorBaixoSpinner = findViewById(R.id.notificar_cio_por_baixo_spinner);
        FloatingActionButton cancelar = findViewById(R.id.notifica_cio_cancelar);
        FloatingActionButton done = findViewById(R.id.notificar_cio_done);
        HerdsmanDbHelper mDbHelper = new HerdsmanDbHelper(NotificarCioActivity.this);
        ArrayList listaAnimais = mDbHelper.carregarAnimaisAtivos();
        ArrayAdapter adapter = new ArrayAdapter(NotificarCioActivity.this, R.layout.support_simple_spinner_dropdown_item, listaAnimais);
        animalPorBaixoSpinner.setAdapter(adapter);
        animalPorCimaSpinner.setAdapter(adapter);
        final Telefone telefoneAdmin = mDbHelper.carregarTelefoneAdmin();
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // FIXME E se for para o proprio celular?
                adm = pref.getBoolean("isAdmin", false);
                if(adm) {
                    Animal animalPorCima = (Animal) animalPorCimaSpinner.getSelectedItem();
                    Animal animalPorBaixo = (Animal) animalPorBaixoSpinner.getSelectedItem();
                    Calendar c = Calendar.getInstance();
                    int dia = c.get(Calendar.DAY_OF_MONTH);
                    int mes = c.get(Calendar.MONTH);
                    int ano = c.get(Calendar.YEAR);
                    String diaFormatado = String.valueOf(dia);
                    if (diaFormatado.length() == 1) {
                        diaFormatado = '0' + diaFormatado;
                    }

                    String data = String.valueOf(ano) + '-' + String.valueOf(mes) + '-' + diaFormatado;
                    Cio cio = new Cio(animalPorCima.getId(), animalPorBaixo.getId(), data, 1);
                    HerdsmanDbHelper herdsmanDbHelper = new HerdsmanDbHelper(NotificarCioActivity.this);
                    long ins = herdsmanDbHelper.inserirCio(cio);
                    if (ins > 0) {
                        Toast.makeText(NotificarCioActivity.this, "Cio registrado", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(NotificarCioActivity.this, "Erro ao registrar", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                    else{
                    SmsManager smsManager = SmsManager.getDefault();
                    Animal animalPorBaixo = (Animal) animalPorBaixoSpinner.getSelectedItem();
                    Animal animalPorCima = (Animal) animalPorCimaSpinner.getSelectedItem();
                    int SMS_PERMISSION_CODE = 0;
                    if (ContextCompat.checkSelfPermission(NotificarCioActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(NotificarCioActivity.this, Manifest.permission.SEND_SMS)) {

                        } else {
                            ActivityCompat.requestPermissions(NotificarCioActivity.this, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_CODE);
                        }

                    }
                    String text = "Herdsman's Companion;\n1;" + String.valueOf(animalPorBaixo.getId()) + ";" + String.valueOf(animalPorCima.getId());
                    smsManager.sendTextMessage(telefoneAdmin.getNumero(), null, text, null, null);
                    Toast.makeText(NotificarCioActivity.this, "SMS enviado para " + telefoneAdmin.getNumero(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
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
                Toast.makeText(NotificarCioActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(NotificarCioActivity.this, ListaAnimaisActivity.class);
                NotificarCioActivity.this.startActivity(intent);
            }

        } else if (id == R.id.nav_enfermidades) {

            if (!adm)
            {
                Toast.makeText(NotificarCioActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(NotificarCioActivity.this, ListaEnfermidadesActivity.class);
                NotificarCioActivity.this.startActivity(intent);
            }

        } else if (id == R.id.nav_remedios) {

            if (!adm)
            {
                Toast.makeText(NotificarCioActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(NotificarCioActivity.this, ListaRemediosActivity.class);
                NotificarCioActivity.this.startActivity(intent);
            }


        } else if (id == R.id.nav_funcionarios) {

            if (!adm)
            {
                Toast.makeText(NotificarCioActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(NotificarCioActivity.this, ListaFuncionariosActivity.class);
                NotificarCioActivity.this.startActivity(intent);
            }

        } else if (id == R.id.nav_cio) {

            Intent intent = new Intent(NotificarCioActivity.this, NotificarCioActivity.class);
            NotificarCioActivity.this.startActivity(intent);

        } else if (id == R.id.nav_sinistro) {

            Intent intent = new Intent(NotificarCioActivity.this, NotificarSinistroActivity.class);
            NotificarCioActivity.this.startActivity(intent);

        } else if (id == R.id.nav_outro) {

            if (!adm)
            {
                Toast.makeText(NotificarCioActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(NotificarCioActivity.this, NotificarOutroActivity.class);
                NotificarCioActivity.this.startActivity(intent);
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
