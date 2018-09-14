package br.uepg.projeto.herdsman;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.EditText;
import android.widget.Toast;

import br.uepg.projeto.herdsman.DAO.HerdsmanContract;
import br.uepg.projeto.herdsman.DAO.HerdsmanDbHelper;
import br.uepg.projeto.herdsman.Objetos.Enfermidade;

public class CadastroEnfermidadeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    Enfermidade enfermidade;
    Boolean adm;
    public static final String myPref = "preferenceName";
    SharedPreferences pref;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_enfermidade);

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



        FloatingActionButton cadastrar = (FloatingActionButton) findViewById(R.id.cadastro_enfermidade_add);
        FloatingActionButton cancelar = (FloatingActionButton) findViewById(R.id.cadastro_enfermidade_cancelar);
        final EditText descricao = (EditText) findViewById(R.id.cadastro_enfermidade_descricão);

        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(descricao.getText().length() == 0)
                {
                    Toast.makeText(CadastroEnfermidadeActivity.this, "Preencha o campo", Toast.LENGTH_SHORT).show();
                    return;
                }
                enfermidade = new Enfermidade(descricao.getText().toString());
                HerdsmanDbHelper mDbHelper = new HerdsmanDbHelper(CadastroEnfermidadeActivity.this);
                long insert = mDbHelper.inserirEnfermidade(enfermidade);
                if (insert > 0)
                {
                    Toast.makeText(CadastroEnfermidadeActivity.this, "Enfermidade cadastrada", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                Toast.makeText(CadastroEnfermidadeActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(CadastroEnfermidadeActivity.this, ListaAnimaisActivity.class);
                CadastroEnfermidadeActivity.this.startActivity(intent);
            }

        } else if (id == R.id.nav_enfermidades) {

            if (!adm)
            {
                Toast.makeText(CadastroEnfermidadeActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(CadastroEnfermidadeActivity.this, ListaEnfermidadesActivity.class);
                CadastroEnfermidadeActivity.this.startActivity(intent);
            }

        } else if (id == R.id.nav_remedios) {

            if (!adm)
            {
                Toast.makeText(CadastroEnfermidadeActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(CadastroEnfermidadeActivity.this, ListaRemediosActivity.class);
                CadastroEnfermidadeActivity.this.startActivity(intent);
            }


        } else if (id == R.id.nav_funcionarios) {

            if (!adm)
            {
                Toast.makeText(CadastroEnfermidadeActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(CadastroEnfermidadeActivity.this, ListaFuncionariosActivity.class);
                CadastroEnfermidadeActivity.this.startActivity(intent);
            }

        } else if (id == R.id.nav_cio) {

            Intent intent = new Intent(CadastroEnfermidadeActivity.this, NotificarCioActivity.class);
            CadastroEnfermidadeActivity.this.startActivity(intent);

        } else if (id == R.id.nav_sinistro) {

            Intent intent = new Intent(CadastroEnfermidadeActivity.this, NotificarSinistroActivity.class);
            CadastroEnfermidadeActivity.this.startActivity(intent);

        } else if (id == R.id.nav_outro) {

            if (!adm)
            {
                Toast.makeText(CadastroEnfermidadeActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(CadastroEnfermidadeActivity.this, NotificarOutroActivity.class);
                CadastroEnfermidadeActivity.this.startActivity(intent);
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
