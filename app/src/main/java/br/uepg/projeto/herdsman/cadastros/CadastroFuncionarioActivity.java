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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import br.uepg.projeto.herdsman.dao.HerdsmanDbHelper;
import br.uepg.projeto.herdsman.drawer.ListaAnimaisActivity;
import br.uepg.projeto.herdsman.drawer.ListaEnfermidadesActivity;
import br.uepg.projeto.herdsman.drawer.ListaFuncionariosActivity;
import br.uepg.projeto.herdsman.drawer.ListaRemediosActivity;
import br.uepg.projeto.herdsman.drawer.notificacao.NotificarCioActivity;
import br.uepg.projeto.herdsman.drawer.notificacao.NotificarOutroActivity;
import br.uepg.projeto.herdsman.drawer.notificacao.NotificarSinistroActivity;
import br.uepg.projeto.herdsman.objetos.Pessoa;
import br.uepg.projeto.herdsman.objetos.Administrador;
import br.uepg.projeto.herdsman.R;

public class CadastroFuncionarioActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    Pessoa pessoa;
    Pessoa intent_pessoa;
    Administrador administrador;
    Boolean adm;
    public static final String myPref = "preferenceName";
    SharedPreferences pref;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_funcionario);

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



        FloatingActionButton add = (FloatingActionButton) findViewById(R.id.cadastro_funcionario_add);
        FloatingActionButton cancelar = (FloatingActionButton) findViewById(R.id.cadastro_funcionario_cancelar);
        TextView titulo = (TextView) findViewById(R.id.cadastro_funcionario_titulo);
        final EditText campoNome = (EditText) findViewById(R.id.cadastro_funcionario_nome);
        final EditText campoCpf = (EditText) findViewById(R.id.cadastro_funcionario_cpf);
        final EditText campoRg = (EditText) findViewById(R.id.cadastro_funcionario_rg);
        RadioGroup radioGroup = findViewById(R.id.cadastro_funcionario_radio_group);
        final RadioButton radioButtonAtivo = findViewById(R.id.cadastro_funcionario_radio_ativo);
        intent_pessoa = (Pessoa) getIntent().getSerializableExtra("Pessoa");

        if (intent_pessoa != null)
        {
            titulo.setText("Alterar " + intent_pessoa.getNome());
            campoNome.setText(intent_pessoa.getNome());
            campoCpf.setText(intent_pessoa.getCpf());
            campoRg.setText(intent_pessoa.getRg());
            radioGroup.setVisibility(View.VISIBLE);
        }
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intent_pessoa == null) {
                    if (campoNome.getText().length() == 0) {
                        return;
                    }

                    if (campoCpf.getText().length() == 0) {
                        return;
                    }
                    if (campoRg.getText().length() == 0) {
                        return;
                    }

                    pessoa = new Pessoa(campoNome.getText().toString(), campoCpf.getText().toString(), campoRg.getText().toString());

                    HerdsmanDbHelper mDbHelper = new HerdsmanDbHelper(CadastroFuncionarioActivity.this);
                    long insert = mDbHelper.inserirFuncionario(pessoa);
                    if (insert > 0)
                    {
                        Toast.makeText(CadastroFuncionarioActivity.this, pessoa.getNome() + " cadastrado", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                }

                else
                {
                    if (campoNome.getText().length() == 0) {
                        return;
                    }
                    if (campoCpf.getText().length() == 0) {
                        return;
                    }
                    if (campoRg.getText().length() == 0) {
                        return;
                    }

                    pessoa = new Pessoa(campoNome.getText().toString(), campoCpf.getText().toString(), campoRg.getText().toString());
                    pessoa.setIdPessoa(intent_pessoa.getIdPessoa());
                    HerdsmanDbHelper mDbHelper = new HerdsmanDbHelper(CadastroFuncionarioActivity.this);

                    if(radioButtonAtivo.isActivated())
                    {
                        pessoa.setAtivo(1);
                        long insert = mDbHelper.replaceFuncionario(pessoa);
                        if (insert > 0)
                        {
                            Toast.makeText(CadastroFuncionarioActivity.this, pessoa.getNome() + " alterado.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        pessoa.setAtivo(0);
                        long insert = mDbHelper.replaceFuncionario(pessoa);
                        if (insert > 0)
                        {
                            Toast.makeText(CadastroFuncionarioActivity.this, pessoa.getNome() + " alterado.", Toast.LENGTH_SHORT).show();
                        }
                    }
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
                Toast.makeText(CadastroFuncionarioActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(CadastroFuncionarioActivity.this, ListaAnimaisActivity.class);
                CadastroFuncionarioActivity.this.startActivity(intent);
            }

        } else if (id == R.id.nav_enfermidades) {

            if (!adm)
            {
                Toast.makeText(CadastroFuncionarioActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(CadastroFuncionarioActivity.this, ListaEnfermidadesActivity.class);
                CadastroFuncionarioActivity.this.startActivity(intent);
            }

        } else if (id == R.id.nav_remedios) {

            if (!adm)
            {
                Toast.makeText(CadastroFuncionarioActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(CadastroFuncionarioActivity.this, ListaRemediosActivity.class);
                CadastroFuncionarioActivity.this.startActivity(intent);
            }


        } else if (id == R.id.nav_funcionarios) {

            if (!adm)
            {
                Toast.makeText(CadastroFuncionarioActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(CadastroFuncionarioActivity.this, ListaFuncionariosActivity.class);
                CadastroFuncionarioActivity.this.startActivity(intent);
            }

        } else if (id == R.id.nav_cio) {

            Intent intent = new Intent(CadastroFuncionarioActivity.this, NotificarCioActivity.class);
            CadastroFuncionarioActivity.this.startActivity(intent);

        } else if (id == R.id.nav_sinistro) {

            Intent intent = new Intent(CadastroFuncionarioActivity.this, NotificarSinistroActivity.class);
            CadastroFuncionarioActivity.this.startActivity(intent);

        } else if (id == R.id.nav_outro) {

            if (!adm)
            {
                Toast.makeText(CadastroFuncionarioActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(CadastroFuncionarioActivity.this, NotificarOutroActivity.class);
                CadastroFuncionarioActivity.this.startActivity(intent);
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
