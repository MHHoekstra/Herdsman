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

import br.uepg.projeto.herdsman.DAO.HerdsmanDbHelper;
import br.uepg.projeto.herdsman.Objetos.Animal;
import br.uepg.projeto.herdsman.Objetos.Enfermidade;
import br.uepg.projeto.herdsman.Objetos.Telefone;

public class NotificarSinistroActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Spinner animalSpinner;
    Spinner enfermidadeSpinner;
    FloatingActionButton cancelar;
    FloatingActionButton enviar;
    Boolean adm;
    public static final String myPref = "preferenceName";
    SharedPreferences pref;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notificar_sinistro);

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

        HerdsmanDbHelper mDbHelper = new HerdsmanDbHelper(this);
        cancelar = findViewById(R.id.notificar_sinistro_cancelar);
        enviar = findViewById(R.id.notificar_sinistro_done);
        animalSpinner = findViewById(R.id.notificar_sinistro_animal_spinner);
        enfermidadeSpinner = findViewById(R.id.notificar_sinistro_enfermidade_spinner);

        ArrayList animais = mDbHelper.carregarAnimaisAtivos();
        final Telefone adminTelefone = mDbHelper.carregarTelefoneAdmin();
        ArrayList enfermidades = mDbHelper.carregarEnfermidades();
        ArrayAdapter<String> animalAdapter = new ArrayAdapter(NotificarSinistroActivity.this, R.layout.support_simple_spinner_dropdown_item, animais);
        ArrayAdapter<String> enfermidadeAdapter = new ArrayAdapter(NotificarSinistroActivity.this, R.layout.support_simple_spinner_dropdown_item, enfermidades);
        animalSpinner.setAdapter(animalAdapter);
        enfermidadeSpinner.setAdapter(enfermidadeAdapter);

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmsManager smsManager = SmsManager.getDefault();
                Enfermidade enfermidade = (Enfermidade) enfermidadeSpinner.getSelectedItem();
                Animal animal = (Animal) animalSpinner.getSelectedItem();
                int SMS_PERMISSION_CODE = 0;
                if (ContextCompat.checkSelfPermission(NotificarSinistroActivity.this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(NotificarSinistroActivity.this, android.Manifest.permission.SEND_SMS)) {

                    } else {
                        ActivityCompat.requestPermissions(NotificarSinistroActivity.this, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_CODE);
                    }

                }
                String text =  "Herdsman's Companion;\n2;" + String.valueOf(enfermidade.getId()) + ";"+String.valueOf(animal.getId());
                smsManager.sendTextMessage(adminTelefone.getNumero(), null, text, null, null);
                Toast.makeText(NotificarSinistroActivity.this, "SMS enviado para " + adminTelefone.getNumero(), Toast.LENGTH_SHORT).show();
                finish();
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
                Toast.makeText(NotificarSinistroActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(NotificarSinistroActivity.this, ListaAnimaisActivity.class);
                NotificarSinistroActivity.this.startActivity(intent);
            }

        } else if (id == R.id.nav_enfermidades) {

            if (!adm)
            {
                Toast.makeText(NotificarSinistroActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(NotificarSinistroActivity.this, ListaEnfermidadesActivity.class);
                NotificarSinistroActivity.this.startActivity(intent);
            }

        } else if (id == R.id.nav_remedios) {

            if (!adm)
            {
                Toast.makeText(NotificarSinistroActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(NotificarSinistroActivity.this, ListaRemediosActivity.class);
                NotificarSinistroActivity.this.startActivity(intent);
            }


        } else if (id == R.id.nav_funcionarios) {

            if (!adm)
            {
                Toast.makeText(NotificarSinistroActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(NotificarSinistroActivity.this, ListaFuncionariosActivity.class);
                NotificarSinistroActivity.this.startActivity(intent);
            }

        } else if (id == R.id.nav_cio) {

            Intent intent = new Intent(NotificarSinistroActivity.this, NotificarCioActivity.class);
            NotificarSinistroActivity.this.startActivity(intent);

        } else if (id == R.id.nav_sinistro) {

            Intent intent = new Intent(NotificarSinistroActivity.this, NotificarSinistroActivity.class);
            NotificarSinistroActivity.this.startActivity(intent);

        } else if (id == R.id.nav_outro) {

            if (!adm)
            {
                Toast.makeText(NotificarSinistroActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(NotificarSinistroActivity.this, NotificarOutroActivity.class);
                NotificarSinistroActivity.this.startActivity(intent);
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
