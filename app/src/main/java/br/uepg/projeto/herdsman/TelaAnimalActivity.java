package br.uepg.projeto.herdsman;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import br.uepg.projeto.herdsman.DAO.HerdsmanContract;
import br.uepg.projeto.herdsman.DAO.HerdsmanDbHelper;
import br.uepg.projeto.herdsman.Objetos.Animal;

public class TelaAnimalActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Animal animal;
    private TextView campoNomeAnimal;
    private TextView campoNumeroAnimal;
    private Button buttonCios;
    private Button buttonPartos;
    private Button buttonInseminacoes;
    private Button buttonRemedios;
    private Button buttonSinistros;
    private TextView campoUltimoCio;
    private TextView campoUltimaInseminacao;
    private TextView campoUltimoParto;
    Boolean adm;
    public static final String myPref = "preferenceName";
    SharedPreferences pref;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_animal);

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



        animal = (Animal) getIntent().getSerializableExtra("Animal");

        campoNomeAnimal = (TextView) findViewById(R.id.tela_animal_nome_animal);
        campoNumeroAnimal = (TextView) findViewById(R.id.tela_animal_numero_animal);

        // TODO Implementar tela de cios FEITO
        buttonCios = (Button) findViewById(R.id.tela_animal_button_cios);
        buttonPartos = (Button) findViewById(R.id.tela_animal_button_partos);

        buttonInseminacoes = (Button) findViewById(R.id.tela_animal_button_inseminacoes);
        buttonInseminacoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TelaAnimalActivity.this, TelaAnimalInseminacoesActivity.class);
                intent.putExtra("Animal", animal);
                startActivity(intent);
            }
        });

        buttonRemedios = (Button) findViewById(R.id.tela_animal_button_remedios);
        buttonRemedios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TelaAnimalActivity.this, TelaAnimalRemediosActivity.class);
                intent.putExtra("Animal", animal);
                startActivity(intent);
            }
        });

        // TODO Implementar tela de sinistros
        buttonSinistros = (Button) findViewById(R.id.tela_animal_button_sinistros);


        campoUltimoCio = (TextView) findViewById(R.id.tela_animal_cio_data);
        campoUltimaInseminacao = (TextView) findViewById(R.id.tela_animal_inseminacao_data);
        campoUltimoParto = (TextView) findViewById(R.id.tela_animal_parto_data);
        campoNomeAnimal.setText(animal.getNome());
        campoNumeroAnimal.setText(animal.getNumero());
        carregarDados(animal, campoUltimoCio, campoUltimoParto, campoUltimaInseminacao);


        buttonPartos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TelaAnimalActivity.this, ListaPartosActivity.class);
                intent.putExtra("Animal", animal);
                startActivity(intent);
            }
        });

        buttonCios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TelaAnimalActivity.this, ListaCiosActivity.class);
                intent.putExtra("Animal", animal);
                startActivity(intent);
            }
        });




    }

    private void carregarDados(Animal animal, TextView campoUltimoCio, TextView campoUltimoParto, TextView campoUltimaInseminacao) {
        HerdsmanDbHelper mDbHelper = new HerdsmanDbHelper(this);
        SQLiteDatabase mDb = mDbHelper.getReadableDatabase();
        Cursor cursor;
        String dataUltimoCio;
        String dataUltimoParto;
        String dataUltimaInseminacao;
        String order = "data DESC";
        String [] selectionArgs = {String.valueOf(animal.getId()), String.valueOf(animal.getId())};
        cursor = mDb.query(
                HerdsmanContract.CioEntry.TABLE_NAME,
                new String[] {HerdsmanContract.CioEntry.COLUMN_NAME_DATA},
                HerdsmanContract.CioEntry.COLUMN_NAME_ANIMAL_IDANIMALPORCIMA + " == ? OR " + HerdsmanContract.CioEntry.COLUMN_NAME_ANIMAL_IDANIMALPORBAIXO + " == ?",
                selectionArgs,
                null,
                null,
                order

        );
        if(cursor.moveToNext())
        {
            dataUltimoCio = cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.CioEntry.COLUMN_NAME_DATA));
            Log.d("DATA CIO: ", dataUltimoCio);
            campoUltimoCio.setText(dataUltimoCio);
        }
        cursor.close();
        order = HerdsmanContract.PartoEntry.COLUMN_NAME_DATA + " DESC";
        selectionArgs = new String[]{String.valueOf(animal.getId())};
        cursor = mDb.query(
                HerdsmanContract.PartoEntry.TABLE_NAME,
                new String[] {HerdsmanContract.PartoEntry.COLUMN_NAME_DATA},
                HerdsmanContract.PartoEntry.COLUMN_NAME_ANIMAL_IDANIMAL + " == ?",
                selectionArgs,
                null,
                null,
                order

        );
        if(cursor.moveToNext())
        {
            dataUltimoParto = (cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.PartoEntry.COLUMN_NAME_DATA)));
            Log.d("DATA PARTO: ", dataUltimoParto);
            campoUltimoParto.setText(dataUltimoParto);
        }

        cursor.close();

        cursor = mDb.query(
                HerdsmanContract.AnimalInseminacaoEntry.TABLE_NAME,
                new String[] {HerdsmanContract.AnimalInseminacaoEntry.COLUMN_NAME_DATA},
                HerdsmanContract.AnimalInseminacaoEntry.COLUMN_NAME_ANIMAL_IDANIMAL + "== ?",
                new String[] {String.valueOf(animal.getId())},
                null,
                null,
                order

        );
        if (cursor.moveToNext())
        {
            String ultimaInseminação = cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalInseminacaoEntry.COLUMN_NAME_DATA));
            campoUltimaInseminacao.setText(ultimaInseminação);
        }
        cursor.close();
        mDb.close();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        carregarDados(animal, campoUltimoCio, campoUltimoParto, campoUltimaInseminacao);
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
                Toast.makeText(TelaAnimalActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(TelaAnimalActivity.this, ListaAnimaisActivity.class);
                TelaAnimalActivity.this.startActivity(intent);
            }

        } else if (id == R.id.nav_enfermidades) {

            if (!adm)
            {
                Toast.makeText(TelaAnimalActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(TelaAnimalActivity.this, ListaEnfermidadesActivity.class);
                TelaAnimalActivity.this.startActivity(intent);
            }

        } else if (id == R.id.nav_remedios) {

            if (!adm)
            {
                Toast.makeText(TelaAnimalActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(TelaAnimalActivity.this, ListaRemediosActivity.class);
                TelaAnimalActivity.this.startActivity(intent);
            }


        } else if (id == R.id.nav_funcionarios) {

            if (!adm)
            {
                Toast.makeText(TelaAnimalActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(TelaAnimalActivity.this, ListaFuncionariosActivity.class);
                TelaAnimalActivity.this.startActivity(intent);
            }

        } else if (id == R.id.nav_cio) {

            Intent intent = new Intent(TelaAnimalActivity.this, NotificarCioActivity.class);
            TelaAnimalActivity.this.startActivity(intent);

        } else if (id == R.id.nav_sinistro) {

            Intent intent = new Intent(TelaAnimalActivity.this, NotificarSinistroActivity.class);
            TelaAnimalActivity.this.startActivity(intent);

        } else if (id == R.id.nav_outro) {

            if (!adm)
            {
                Toast.makeText(TelaAnimalActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(TelaAnimalActivity.this, NotificarOutroActivity.class);
                TelaAnimalActivity.this.startActivity(intent);
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
