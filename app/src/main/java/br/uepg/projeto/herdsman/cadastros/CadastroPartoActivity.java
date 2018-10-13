package br.uepg.projeto.herdsman.cadastros;

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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import br.uepg.projeto.herdsman.objetos.Parto;
import br.uepg.projeto.herdsman.R;

public class CadastroPartoActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, NavigationView.OnNavigationItemSelectedListener{
    Parto parto;
    Animal animal;
    Button data;
    int dia;
    int mes;
    int ano;
    Boolean adm;
    public static final String myPref = "preferenceName";
    SharedPreferences pref;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_parto);

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



        Calendar c = Calendar.getInstance();
        dia = c.get(Calendar.DAY_OF_MONTH);
        mes = c.get(Calendar.MONTH);
        ano = c.get(Calendar.YEAR);
        animal = (Animal) getIntent().getSerializableExtra("Animal");
        data = (Button) findViewById(R.id.cadastro_parto_button_setDate);
        data.setText(String.valueOf(dia) + "/" + String.valueOf(mes)+"/"+String.valueOf(ano));
        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.cadastro_parto_radio_group);
        TextView titulo = (TextView) findViewById(R.id.cadastro_parto_titulo);
        FloatingActionButton cancelar = (FloatingActionButton) findViewById(R.id.cadastro_parto_cancelar);
        FloatingActionButton cadastrar = (FloatingActionButton)findViewById(R.id.cadastro_parto_done);
        String s = "Cadastrar parto de " + animal.getNumero();
        titulo.setText(s);

        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment fragment = new DatePickerFragment();
                fragment.show(getFragmentManager(), "Data");
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tipoCria = 1;
                switch (radioGroup.getCheckedRadioButtonId())
                {
                    case R.id.cadastro_parto_radio_femea:
                    {
                        tipoCria = 1;
                        break;
                    }

                    case R.id.cadastro_parto_radio_macho:
                    {
                        tipoCria = 2;
                        break;
                    }

                    case R.id.cadastro_parto_radio_gemeos_femea:
                    {
                        tipoCria = 3;
                        break;
                    }

                    case R.id.cadastro_parto_radio_gemeos_macho:
                    {
                        tipoCria = 4;
                        break;
                    }

                    case R.id.cadastro_parto_radio_macho_femea:
                    {
                        tipoCria = 5;
                        break;
                    }
                }
                String dia_formatado = String.valueOf(dia);
                if (dia_formatado.length()  == 1)
                {
                    dia_formatado = '0' + String.valueOf(dia);
                }
                parto = new Parto(animal.getId(), tipoCria,ano + "-" + mes + "-" + String.valueOf(dia_formatado));
                HerdsmanDbHelper mDbHelper = new HerdsmanDbHelper(CadastroPartoActivity.this);
                long insert = mDbHelper.inserirParto(parto);
                if (insert > 0) {
                    Toast.makeText(CadastroPartoActivity.this, "Parto cadastrado", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        data.setText(String.valueOf(dayOfMonth) + "/" + String.valueOf(month)+"/"+String.valueOf(year));
        dia = dayOfMonth;
        mes = month;
        ano = year;
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
                Toast.makeText(CadastroPartoActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(CadastroPartoActivity.this, ListaAnimaisActivity.class);
                CadastroPartoActivity.this.startActivity(intent);
            }

        } else if (id == R.id.nav_enfermidades) {

            if (!adm)
            {
                Toast.makeText(CadastroPartoActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(CadastroPartoActivity.this, ListaEnfermidadesActivity.class);
                CadastroPartoActivity.this.startActivity(intent);
            }

        } else if (id == R.id.nav_remedios) {

            if (!adm)
            {
                Toast.makeText(CadastroPartoActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(CadastroPartoActivity.this, ListaRemediosActivity.class);
                CadastroPartoActivity.this.startActivity(intent);
            }


        } else if (id == R.id.nav_funcionarios) {

            if (!adm)
            {
                Toast.makeText(CadastroPartoActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(CadastroPartoActivity.this, ListaFuncionariosActivity.class);
                CadastroPartoActivity.this.startActivity(intent);
            }

        } else if (id == R.id.nav_cio) {

            Intent intent = new Intent(CadastroPartoActivity.this, NotificarCioActivity.class);
            CadastroPartoActivity.this.startActivity(intent);

        } else if (id == R.id.nav_sinistro) {

            Intent intent = new Intent(CadastroPartoActivity.this, NotificarAnimalEnfermidadeActivity.class);
            CadastroPartoActivity.this.startActivity(intent);

        } else if (id == R.id.nav_outro) {

            if (!adm)
            {
                Toast.makeText(CadastroPartoActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(CadastroPartoActivity.this, NotificarOutroActivity.class);
                CadastroPartoActivity.this.startActivity(intent);
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
