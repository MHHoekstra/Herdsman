package br.uepg.projeto.herdsman.cadastros;

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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import br.uepg.projeto.herdsman.dao.HerdsmanDbHelper;
import br.uepg.projeto.herdsman.drawer.ListaAnimaisActivity;
import br.uepg.projeto.herdsman.drawer.ListaEnfermidadesActivity;
import br.uepg.projeto.herdsman.drawer.ListaFuncionariosActivity;
import br.uepg.projeto.herdsman.drawer.ListaRemediosActivity;
import br.uepg.projeto.herdsman.drawer.animal.TelaAnimalActivity;
import br.uepg.projeto.herdsman.drawer.notificacao.NotificarCioActivity;
import br.uepg.projeto.herdsman.drawer.notificacao.NotificarOutroActivity;
import br.uepg.projeto.herdsman.drawer.notificacao.NotificarAnimalEnfermidadeActivity;
import br.uepg.projeto.herdsman.helper.HelperTelaAnimal;
import br.uepg.projeto.herdsman.helper.HelperTelaCadastroEnfermidade;
import br.uepg.projeto.herdsman.objetos.Enfermidade;
import br.uepg.projeto.herdsman.R;

public class CadastroEnfermidadeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    TextView titulo;
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
        titulo = (TextView) findViewById(R.id.cadastro_enfermidade_titulo);
        FloatingActionButton cadastrar = (FloatingActionButton) findViewById(R.id.cadastro_enfermidade_add);
        FloatingActionButton cancelar = (FloatingActionButton) findViewById(R.id.cadastro_enfermidade_cancelar);
        final EditText descricao = (EditText) findViewById(R.id.cadastro_enfermidade_descricão);

        enfermidade = (Enfermidade) getIntent().getSerializableExtra("Enfermidade");
        if(enfermidade != null)
        {
            titulo.setText("Alterar Enfermidade");
            descricao.setText(enfermidade.getDescricao());
        }
            cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(descricao.getText().length() == 0)
                {
                    Toast.makeText(CadastroEnfermidadeActivity.this, "Preencha o campo", Toast.LENGTH_SHORT).show();
                    return;
                }

                HerdsmanDbHelper mDbHelper = new HerdsmanDbHelper(CadastroEnfermidadeActivity.this);
                if(mDbHelper.searchDuplicateEnfermidade(descricao.getText().toString()))
                {
                    Toast.makeText(CadastroEnfermidadeActivity.this, "Enfermidade já cadastrada", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(enfermidade == null) {

                    enfermidade = new Enfermidade(descricao.getText().toString());

                    long insert = mDbHelper.inserirEnfermidade(enfermidade);
                    if (insert > 0) {
                        Toast.makeText(CadastroEnfermidadeActivity.this, "Enfermidade cadastrada", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    enfermidade.setDescricao(descricao.getText().toString());
                    mDbHelper.replaceEnfermidade(enfermidade);
                    Toast.makeText(CadastroEnfermidadeActivity.this, "Enfermidade alterada", Toast.LENGTH_SHORT).show();

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
            if (false)
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

            Intent intent = new Intent(CadastroEnfermidadeActivity.this, NotificarAnimalEnfermidadeActivity.class);
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
        else if (id == R.id.nav_help)
        {
            Intent intent = new Intent(CadastroEnfermidadeActivity.this, HelperTelaCadastroEnfermidade.class);
            CadastroEnfermidadeActivity.this.startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
