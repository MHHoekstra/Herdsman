package br.uepg.projeto.herdsman;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.uepg.projeto.herdsman.DAO.HerdsmanContract;
import br.uepg.projeto.herdsman.DAO.HerdsmanDbHelper;
import br.uepg.projeto.herdsman.Objetos.Animal;
import br.uepg.projeto.herdsman.Objetos.Parto;
import br.uepg.projeto.herdsman.Objetos.Telefone;

public class ListaPartosActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ListView listaPartos;
    Animal animal;
    Boolean adm;
    public static final String myPref = "preferenceName";
    SharedPreferences pref;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_partos);

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
        TextView titulo = (TextView) findViewById(R.id.lista_partos_titulo);
        titulo.setText("Partos " + animal.getNumero());
        listaPartos = (ListView) findViewById(R.id.lista_partos_listview);

        FloatingActionButton add = (FloatingActionButton) findViewById(R.id.lista_partos_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListaPartosActivity.this, CadastroPartoActivity.class);
                intent.putExtra("Animal", animal);
                startActivity(intent);
            }
        });
        listarPartos();
        listaPartos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Parto parto = (Parto) listaPartos.getItemAtPosition(position);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ListaPartosActivity.this);
                alertDialogBuilder.setTitle("Deletar parto?");
                alertDialogBuilder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteOpenHelper mDbHelper = new HerdsmanDbHelper(ListaPartosActivity.this);
                        SQLiteDatabase mDb = mDbHelper.getWritableDatabase();
                        String where = HerdsmanContract.PartoEntry.COLUMN_NAME_IDPARTO + "== ?";
                        String[] whereArgs =
                                {
                                        String.valueOf(parto.getId())
                                };
                        mDb.delete(
                                HerdsmanContract.PartoEntry.TABLE_NAME,
                                where,
                                whereArgs);
                        mDb.close();
                        listarPartos();
                    }

                });
                alertDialogBuilder.setNegativeButton("Não", null);
                AlertDialog alert = alertDialogBuilder.create();
                alert.show();
                return true;
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        listarPartos();
    }

    private void listarPartos()
    {
        HerdsmanDbHelper mDbHelper = new HerdsmanDbHelper(ListaPartosActivity.this);
        ArrayList listaPartosmDbHelper = mDbHelper.carregarPartosAnimal(animal);
        // FIXME: Listar está crashando o APP
        ArrayAdapter<String> adapter = new ArrayAdapter(ListaPartosActivity.this, android.R.layout.simple_list_item_1, (List) listaPartosmDbHelper);
        listaPartos.setAdapter(adapter);
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
                Toast.makeText(ListaPartosActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(ListaPartosActivity.this, ListaAnimaisActivity.class);
                ListaPartosActivity.this.startActivity(intent);
            }

        } else if (id == R.id.nav_enfermidades) {

            if (!adm)
            {
                Toast.makeText(ListaPartosActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(ListaPartosActivity.this, ListaEnfermidadesActivity.class);
                ListaPartosActivity.this.startActivity(intent);
            }

        } else if (id == R.id.nav_remedios) {

            if (!adm)
            {
                Toast.makeText(ListaPartosActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(ListaPartosActivity.this, ListaRemediosActivity.class);
                ListaPartosActivity.this.startActivity(intent);
            }


        } else if (id == R.id.nav_funcionarios) {

            if (!adm)
            {
                Toast.makeText(ListaPartosActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(ListaPartosActivity.this, ListaFuncionariosActivity.class);
                ListaPartosActivity.this.startActivity(intent);
            }

        } else if (id == R.id.nav_cio) {

            Intent intent = new Intent(ListaPartosActivity.this, NotificarCioActivity.class);
            ListaPartosActivity.this.startActivity(intent);

        } else if (id == R.id.nav_sinistro) {

            Intent intent = new Intent(ListaPartosActivity.this, NotificarSinistroActivity.class);
            ListaPartosActivity.this.startActivity(intent);

        } else if (id == R.id.nav_outro) {

            if (!adm)
            {
                Toast.makeText(ListaPartosActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(ListaPartosActivity.this, NotificarOutroActivity.class);
                ListaPartosActivity.this.startActivity(intent);
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
