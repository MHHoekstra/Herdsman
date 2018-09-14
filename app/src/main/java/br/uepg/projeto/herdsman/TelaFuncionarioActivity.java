package br.uepg.projeto.herdsman;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import br.uepg.projeto.herdsman.DAO.HerdsmanContract;
import br.uepg.projeto.herdsman.DAO.HerdsmanDbHelper;
import br.uepg.projeto.herdsman.Objetos.Pessoa;
import br.uepg.projeto.herdsman.Objetos.Telefone;

public class TelaFuncionarioActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ListView listaTelefones;
    Pessoa pessoa;
    Boolean adm;
    public static final String myPref = "preferenceName";
    SharedPreferences pref;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_funcionario);

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


        pessoa = (Pessoa) getIntent().getSerializableExtra("Pessoa");
        TextView titulo = findViewById(R.id.tela_funcionario_titulo);
        TextView cpfText = findViewById(R.id.tela_funcionario_cpf);
        TextView rgText = findViewById(R.id.tela_funcionario_rg);
        listaTelefones = findViewById(R.id.tela_funcionario_lista_telefone);
        titulo.setText(pessoa.getNome());
        cpfText.setText(pessoa.getCpf());
        rgText.setText(pessoa.getRg());
        Button addTelefone = findViewById(R.id.tela_funcionario_add_telefone);
        Button notificacoesGeradas = findViewById(R.id.tela_funcionario_notificacoes);
        notificacoesGeradas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TelaFuncionarioActivity.this, TelaFuncionarioNotificacoesActivity.class);
                intent.putExtra("Funcionario", pessoa);
                startActivity(intent);
            }
        });
        addTelefone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuild = new AlertDialog.Builder(TelaFuncionarioActivity.this);
                final EditText input = new EditText(TelaFuncionarioActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                alertDialogBuild.setView(input);
                alertDialogBuild.setTitle("Telefone");
                alertDialogBuild.setNegativeButton("Cancelar", null);
                alertDialogBuild.setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (input.getText().length() == 0)
                        {
                            return;
                        }
                        else
                        {
                            HerdsmanDbHelper mDbHelper = new HerdsmanDbHelper(TelaFuncionarioActivity.this);
                            Telefone telefone = new Telefone(pessoa.getIdPessoa(), input.getText().toString());
                            long insert = mDbHelper.inserirTelefone(telefone);
                            if(insert > 0)
                            {
                                Toast.makeText(TelaFuncionarioActivity.this, "Telefone cadastrado", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(TelaFuncionarioActivity.this, "Erro ao cadastrar", Toast.LENGTH_SHORT).show();
                            }
                            listarTelefones();
                        }
                    }
                });
                AlertDialog alert = alertDialogBuild.create();
                alert.show();
            }
        });
        listaTelefones.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Telefone telefone = (Telefone) listaTelefones.getItemAtPosition(position);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TelaFuncionarioActivity.this);
                alertDialogBuilder.setTitle("Deletar número?");
                alertDialogBuilder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        HerdsmanDbHelper mDbHelper = new HerdsmanDbHelper(TelaFuncionarioActivity.this);
                        int delete = mDbHelper.removerTelefone(telefone);
                        if(delete == 0)
                        {
                            Toast.makeText(TelaFuncionarioActivity.this, "Erro ao deletar", Toast.LENGTH_SHORT).show();
                        }
                        listarTelefones();
                    }

                });
                alertDialogBuilder.setNegativeButton("Não", null);
                AlertDialog alert = alertDialogBuilder.create();
                alert.show();
                return true;
            }
        });
        listarTelefones();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        listarTelefones();
    }

    private void listarTelefones()
    {

        HerdsmanDbHelper mDbHelper = new HerdsmanDbHelper(TelaFuncionarioActivity.this);
        ArrayList telefoneItemList = mDbHelper.carregarTelefonesPessoa(pessoa);
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, telefoneItemList);
        listaTelefones.setAdapter(adapter);
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
                Toast.makeText(TelaFuncionarioActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(TelaFuncionarioActivity.this, ListaAnimaisActivity.class);
                TelaFuncionarioActivity.this.startActivity(intent);
            }

        } else if (id == R.id.nav_enfermidades) {

            if (!adm)
            {
                Toast.makeText(TelaFuncionarioActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(TelaFuncionarioActivity.this, ListaEnfermidadesActivity.class);
                TelaFuncionarioActivity.this.startActivity(intent);
            }

        } else if (id == R.id.nav_remedios) {

            if (!adm)
            {
                Toast.makeText(TelaFuncionarioActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(TelaFuncionarioActivity.this, ListaRemediosActivity.class);
                TelaFuncionarioActivity.this.startActivity(intent);
            }


        } else if (id == R.id.nav_funcionarios) {

            if (!adm)
            {
                Toast.makeText(TelaFuncionarioActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(TelaFuncionarioActivity.this, ListaFuncionariosActivity.class);
                TelaFuncionarioActivity.this.startActivity(intent);
            }

        } else if (id == R.id.nav_cio) {

            Intent intent = new Intent(TelaFuncionarioActivity.this, NotificarCioActivity.class);
            TelaFuncionarioActivity.this.startActivity(intent);

        } else if (id == R.id.nav_sinistro) {

            Intent intent = new Intent(TelaFuncionarioActivity.this, NotificarSinistroActivity.class);
            TelaFuncionarioActivity.this.startActivity(intent);

        } else if (id == R.id.nav_outro) {

            if (!adm)
            {
                Toast.makeText(TelaFuncionarioActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(TelaFuncionarioActivity.this, NotificarOutroActivity.class);
                TelaFuncionarioActivity.this.startActivity(intent);
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
