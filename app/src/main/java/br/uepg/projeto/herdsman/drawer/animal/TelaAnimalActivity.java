package br.uepg.projeto.herdsman.drawer.animal;

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
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import br.uepg.projeto.herdsman.dao.HerdsmanContract;
import br.uepg.projeto.herdsman.dao.HerdsmanDbHelper;
import br.uepg.projeto.herdsman.drawer.ListaAnimaisActivity;
import br.uepg.projeto.herdsman.drawer.ListaEnfermidadesActivity;
import br.uepg.projeto.herdsman.drawer.ListaFuncionariosActivity;
import br.uepg.projeto.herdsman.drawer.ListaRemediosActivity;
import br.uepg.projeto.herdsman.drawer.notificacao.NotificarCioActivity;
import br.uepg.projeto.herdsman.drawer.notificacao.NotificarOutroActivity;
import br.uepg.projeto.herdsman.drawer.notificacao.NotificarAnimalEnfermidadeActivity;
import br.uepg.projeto.herdsman.helper.HelperTelaAnimal;
import br.uepg.projeto.herdsman.objetos.Animal;
import br.uepg.projeto.herdsman.R;

public class TelaAnimalActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Animal animal;
    private TextView campoNomeAnimal;
    private TextView campoNumeroAnimal;
    private Button buttonCios;
    private Button buttonPartos;
    private Button buttonInseminacoes;
    private Button buttonRemedios;
    private Button buttonSinistros;
    private Switch campoAtivoAnimal;
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
        setTitle(animal.getNumero() +" - " + animal.getNome());
        campoNomeAnimal = (TextView) findViewById(R.id.tela_animal_nome_animal);
        campoNumeroAnimal = (TextView) findViewById(R.id.tela_animal_numero_animal);
        campoAtivoAnimal = (Switch) findViewById(R.id.tela_animal_switch_ativo);

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

        buttonSinistros = (Button) findViewById(R.id.tela_animal_button_sinistros);


        campoUltimoCio = (TextView) findViewById(R.id.tela_animal_cio_data);
        campoUltimaInseminacao = (TextView) findViewById(R.id.tela_animal_inseminacao_data);
        campoUltimoParto = (TextView) findViewById(R.id.tela_animal_parto_data);
        campoNomeAnimal.setText(animal.getNome());
        campoNumeroAnimal.setText(animal.getNumero());

        // FIXME Carregar corretamente "Ativo e inativo"
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

        campoAtivoAnimal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                HerdsmanDbHelper update = new HerdsmanDbHelper(TelaAnimalActivity.this);
                animal.setAtivo(b?1 :0);
                update.replaceAnimal(animal);
            }
        });


        buttonSinistros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TelaAnimalActivity.this, TelaAnimalSinistrosActivity.class);
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
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(Long.valueOf(dataUltimoCio));
            String data = "" + c.get(Calendar.DAY_OF_MONTH) + '/' + (c.get(Calendar.MONTH)+1)+'/'+c.get(Calendar.YEAR);
            campoUltimoCio.setText(data);
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
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(Long.valueOf(dataUltimoParto));
            String data = "" + c.get(Calendar.DAY_OF_MONTH) + '/' + (c.get(Calendar.MONTH)+1)+'/'+c.get(Calendar.YEAR);
            campoUltimoParto.setText(data);
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
            dataUltimaInseminacao = cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalInseminacaoEntry.COLUMN_NAME_DATA));
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(Long.valueOf(dataUltimaInseminacao));
            String data = "" + c.get(Calendar.DAY_OF_MONTH) + '/' + (c.get(Calendar.MONTH)+1)+'/'+c.get(Calendar.YEAR);
            campoUltimaInseminacao.setText(data);
        }
        cursor.close();
        cursor = mDb.query(
                HerdsmanContract.AnimalEntry.TABLE_NAME,
                new String[] {HerdsmanContract.AnimalEntry.COLUMN_NAME_ATIVO},
                HerdsmanContract.AnimalEntry.COLUMN_NAME_IDANIMAL + "== ?",
                new String[] {String.valueOf(animal.getId())},
                null,
                null,
                null
        );

        if (cursor.moveToNext())
        {
            //String ati = cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalEntry.COLUMN_NAME_ATIVO));
            int ati = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalEntry.COLUMN_NAME_ATIVO));
            if(ati==1)
            {
                campoAtivoAnimal.setChecked(true);
            }
            else
            {
                campoAtivoAnimal.setChecked(false);
            }
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (id == R.id.nav_animais) {
            if (false)
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

            Intent intent = new Intent(TelaAnimalActivity.this, NotificarAnimalEnfermidadeActivity.class);
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
        else if (id == R.id.nav_help)
        {
            Intent intent = new Intent(TelaAnimalActivity.this, HelperTelaAnimal.class);
            TelaAnimalActivity.this.startActivity(intent);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
