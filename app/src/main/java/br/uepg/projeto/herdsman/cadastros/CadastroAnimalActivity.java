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
import br.uepg.projeto.herdsman.drawer.notificacao.NotificarAnimalEnfermidadeActivity;
import br.uepg.projeto.herdsman.helper.HelperTelaCadastroAnimal;
import br.uepg.projeto.herdsman.objetos.Animal;
import br.uepg.projeto.herdsman.R;

public class CadastroAnimalActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Animal animal;
    TextView titulo;
    EditText nomeAnimal;
    EditText numeroAnimal;
    RadioGroup radioGroup;
    RadioButton ativoRadio;
    RadioButton inativoRadio;
    Boolean adm;
    public static final String myPref = "preferenceName";
    SharedPreferences pref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_animal);

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

        FloatingActionButton cancela = (FloatingActionButton) findViewById(R.id.cadastro_animal_cancelar);
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.cadastro_animal_add);
        final Animal intent_animal = (Animal) getIntent().getSerializableExtra("Animal");
        titulo = (TextView) findViewById(R.id.cadastro_animal_titulo);
        numeroAnimal = (EditText) findViewById(R.id.cadastro_animal_numero);
        nomeAnimal = (EditText) findViewById(R.id.cadastro_animal_nome);
        radioGroup = (RadioGroup) findViewById(R.id.cadastro_animal_radio_group);
        ativoRadio = (RadioButton) findViewById(R.id.cadastro_animal_radio_ativo);
        inativoRadio = (RadioButton) findViewById(R.id.cadastro_animal_radio_inativo);
        radioGroup.setVisibility(View.INVISIBLE);
        if (intent_animal != null) {
            titulo.setText("Alterar " + intent_animal.getNumero());
            numeroAnimal.setText(intent_animal.getNumero());
            nomeAnimal.setText(intent_animal.getNome());
            radioGroup.setVisibility(View.VISIBLE);
            if (intent_animal.getAtivo() == 1) {
                ativoRadio.toggle();
            } else {
                inativoRadio.toggle();
            }
        }
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intent_animal == null) {
                    if (numeroAnimal.getText().length() == 0) {
                        Toast.makeText(CadastroAnimalActivity.this, "Preencha o número", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (nomeAnimal.getText().length() == 0) {
                        Toast.makeText(CadastroAnimalActivity.this, "Preencha o nome", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    HerdsmanDbHelper mDbHelper = new HerdsmanDbHelper(CadastroAnimalActivity.this);
                    animal = new Animal(nomeAnimal.getText().toString(), numeroAnimal.getText().toString(), 1);
                    long id = mDbHelper.inserirAnimal(animal);
                    Toast.makeText(CadastroAnimalActivity.this, "Animal " + animal.getNumero() + " cadastrado", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    if (numeroAnimal.getText().length() == 0) {
                        Toast.makeText(CadastroAnimalActivity.this, "Preencha o número", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (nomeAnimal.getText().length() == 0) {
                        Toast.makeText(CadastroAnimalActivity.this, "Preencha o nome", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    HerdsmanDbHelper mDbHelper = new HerdsmanDbHelper(CadastroAnimalActivity.this);

                    if (ativoRadio.isChecked()) {
                        animal = new Animal(intent_animal.getId(), numeroAnimal.getText().toString(), nomeAnimal.getText().toString(), 1);
                    } else {
                        animal = new Animal(intent_animal.getId(), numeroAnimal.getText().toString(), nomeAnimal.getText().toString(), 0);
                    }
                    long newRowId = mDbHelper.replaceAnimal(animal);
                    Toast.makeText(CadastroAnimalActivity.this, "Animal " + intent_animal.getNumero() + " alterado", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        cancela.setOnClickListener(new View.OnClickListener() {
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
            if (!adm) {
                Toast.makeText(CadastroAnimalActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(CadastroAnimalActivity.this, ListaAnimaisActivity.class);
                CadastroAnimalActivity.this.startActivity(intent);
            }
        } else if (id == R.id.nav_enfermidades) {
            if (!adm) {
                Toast.makeText(CadastroAnimalActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            } else {
                Intent intent = new Intent(CadastroAnimalActivity.this, ListaEnfermidadesActivity.class);
                CadastroAnimalActivity.this.startActivity(intent);
            }
        } else if (id == R.id.nav_remedios) {
            if (!adm) {
                Toast.makeText(CadastroAnimalActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(CadastroAnimalActivity.this, ListaRemediosActivity.class);
                CadastroAnimalActivity.this.startActivity(intent);
            }
        } else if (id == R.id.nav_funcionarios) {
            if (!adm) {
                Toast.makeText(CadastroAnimalActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(CadastroAnimalActivity.this, ListaFuncionariosActivity.class);
                CadastroAnimalActivity.this.startActivity(intent);
            }
        } else if (id == R.id.nav_cio) {

            Intent intent = new Intent(CadastroAnimalActivity.this, NotificarCioActivity.class);
            CadastroAnimalActivity.this.startActivity(intent);

        } else if (id == R.id.nav_sinistro) {

            Intent intent = new Intent(CadastroAnimalActivity.this, NotificarAnimalEnfermidadeActivity.class);
            CadastroAnimalActivity.this.startActivity(intent);

        } else if (id == R.id.nav_outro) {
            if (!adm) {
                Toast.makeText(CadastroAnimalActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(CadastroAnimalActivity.this, NotificarOutroActivity.class);
                CadastroAnimalActivity.this.startActivity(intent);
            }
        }
        else if (id == R.id.nav_help)
        {
            Intent intent = new Intent(CadastroAnimalActivity.this, HelperTelaCadastroAnimal.class);
            CadastroAnimalActivity.this.startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
