package br.uepg.projeto.herdsman.drawer.notificacao;

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

import br.uepg.projeto.herdsman.dao.HerdsmanDbHelper;
import br.uepg.projeto.herdsman.drawer.ListaAnimaisActivity;
import br.uepg.projeto.herdsman.drawer.ListaEnfermidadesActivity;
import br.uepg.projeto.herdsman.drawer.ListaFuncionariosActivity;
import br.uepg.projeto.herdsman.drawer.ListaRemediosActivity;
import br.uepg.projeto.herdsman.helper.HelperTelaNotificaEnfermidade;
import br.uepg.projeto.herdsman.objetos.Animal;
import br.uepg.projeto.herdsman.objetos.AnimalEnfermidade;
import br.uepg.projeto.herdsman.objetos.Enfermidade;
import br.uepg.projeto.herdsman.objetos.Telefone;
import br.uepg.projeto.herdsman.R;

public class NotificarAnimalEnfermidadeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
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
        ArrayAdapter<String> animalAdapter = new ArrayAdapter(NotificarAnimalEnfermidadeActivity.this, R.layout.support_simple_spinner_dropdown_item, animais);
        ArrayAdapter<String> enfermidadeAdapter = new ArrayAdapter(NotificarAnimalEnfermidadeActivity.this, R.layout.support_simple_spinner_dropdown_item, enfermidades);
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
                adm = pref.getBoolean("isAdmin", false);
                if(adm)
                {
                    Enfermidade enfermidade = (Enfermidade) enfermidadeSpinner.getSelectedItem();
                    Animal animal = (Animal) animalSpinner.getSelectedItem();
                    Calendar c = Calendar.getInstance();
                    int dia = c.get(Calendar.DAY_OF_MONTH);
                    int mes = c.get(Calendar.MONTH);
                    int ano = c.get(Calendar.YEAR);
                    String diaFormatado = String.valueOf(dia);
                    if(diaFormatado.length() == 1)
                    {
                        diaFormatado = '0' + diaFormatado;
                    }

                    long data = c.getTimeInMillis();
                    AnimalEnfermidade animalEnfermidade = new AnimalEnfermidade(animal.getId(), enfermidade.getId(), 1, data);
                    HerdsmanDbHelper herdsmanDbHelper = new HerdsmanDbHelper(NotificarAnimalEnfermidadeActivity.this);
                    long ins = herdsmanDbHelper.inserirSinistro(animalEnfermidade);
                    if(ins > 0 )
                    {
                        Toast.makeText(NotificarAnimalEnfermidadeActivity.this, "Ocorrencia registrada", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                    {
                        Toast.makeText(NotificarAnimalEnfermidadeActivity.this, "Erro ao registrar", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                else {
                    SmsManager smsManager = SmsManager.getDefault();
                    Enfermidade enfermidade = (Enfermidade) enfermidadeSpinner.getSelectedItem();
                    Animal animal = (Animal) animalSpinner.getSelectedItem();
                    int SMS_PERMISSION_CODE = 0;
                    if (ContextCompat.checkSelfPermission(NotificarAnimalEnfermidadeActivity.this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(NotificarAnimalEnfermidadeActivity.this, android.Manifest.permission.SEND_SMS)) {

                        } else {
                            ActivityCompat.requestPermissions(NotificarAnimalEnfermidadeActivity.this, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_CODE);
                        }

                    }
                    String text = "Herdsman's Companion;\n2;" + String.valueOf(enfermidade.getId()) + ";" + String.valueOf(animal.getId());
                    try {
                        smsManager.sendTextMessage(adminTelefone.getNumero(), null, text, null, null);
                        Toast.makeText(NotificarAnimalEnfermidadeActivity.this, "SMS enviado para " + adminTelefone.getNumero(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(NotificarAnimalEnfermidadeActivity.this, "Erro ao enviar, telefone inválido", Toast.LENGTH_SHORT).show();
                        finish();
                    }
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
                Toast.makeText(NotificarAnimalEnfermidadeActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(NotificarAnimalEnfermidadeActivity.this, ListaAnimaisActivity.class);
                NotificarAnimalEnfermidadeActivity.this.startActivity(intent);
            }

        } else if (id == R.id.nav_enfermidades) {

            if (!adm)
            {
                Toast.makeText(NotificarAnimalEnfermidadeActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(NotificarAnimalEnfermidadeActivity.this, ListaEnfermidadesActivity.class);
                NotificarAnimalEnfermidadeActivity.this.startActivity(intent);
            }

        } else if (id == R.id.nav_remedios) {

            if (!adm)
            {
                Toast.makeText(NotificarAnimalEnfermidadeActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(NotificarAnimalEnfermidadeActivity.this, ListaRemediosActivity.class);
                NotificarAnimalEnfermidadeActivity.this.startActivity(intent);
            }


        } else if (id == R.id.nav_funcionarios) {

            if (!adm)
            {
                Toast.makeText(NotificarAnimalEnfermidadeActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(NotificarAnimalEnfermidadeActivity.this, ListaFuncionariosActivity.class);
                NotificarAnimalEnfermidadeActivity.this.startActivity(intent);
            }

        } else if (id == R.id.nav_cio) {

            Intent intent = new Intent(NotificarAnimalEnfermidadeActivity.this, NotificarCioActivity.class);
            NotificarAnimalEnfermidadeActivity.this.startActivity(intent);

        } else if (id == R.id.nav_sinistro) {

            Intent intent = new Intent(NotificarAnimalEnfermidadeActivity.this, NotificarAnimalEnfermidadeActivity.class);
            NotificarAnimalEnfermidadeActivity.this.startActivity(intent);

        } else if (id == R.id.nav_outro) {

            if (!adm)
            {
                Toast.makeText(NotificarAnimalEnfermidadeActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(NotificarAnimalEnfermidadeActivity.this, NotificarOutroActivity.class);
                NotificarAnimalEnfermidadeActivity.this.startActivity(intent);
            }

        }        else {
            Intent intent = new Intent(NotificarAnimalEnfermidadeActivity.this, HelperTelaNotificaEnfermidade.class);
            NotificarAnimalEnfermidadeActivity.this.startActivity(intent);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
