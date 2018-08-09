package br.uepg.projeto.herdsman;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import br.uepg.projeto.herdsman.DAO.HerdsmanContract;
import br.uepg.projeto.herdsman.DAO.HerdsmanDbHelper;
import br.uepg.projeto.herdsman.Objetos.Animal;
import br.uepg.projeto.herdsman.Objetos.Cio;
import br.uepg.projeto.herdsman.Objetos.Usuario;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Usuario usuario = null;
    ListView lista;
    Button buttonCio;
    Button buttonSinistro;
    Button buttonOutros;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int reqCod = 0;
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, reqCod);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, reqCod);

        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, reqCod);

        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, reqCod);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        lista = (ListView) findViewById(R.id.main_activity_lista);
        listarCios();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.sincronizar_bd) {

            if (usuario == null || usuario.isAdmin() == 0) {
                Toast.makeText(MainActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
        }
        if (id == R.id.alterar_administrador) {

            if (usuario == null || usuario.isAdmin() == 0) {
                Toast.makeText(MainActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();
                return false;
            }
            AlertDialog.Builder alertDialogBuild = new AlertDialog.Builder(MainActivity.this);
            final EditText input = new EditText(MainActivity.this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            alertDialogBuild.setView(input);
            alertDialogBuild.setTitle("Telefone");
            alertDialogBuild.setNegativeButton("Cancelar", null);
            AlertDialog.Builder adicionar = alertDialogBuild.setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (input.getText().length() == 0) {
                        return;
                    } else {
                        SQLiteOpenHelper mDbHelper = new HerdsmanDbHelper(MainActivity.this);
                        SQLiteDatabase mDb = mDbHelper.getWritableDatabase();
                        String[] where = {"1"};
                        mDb.delete(
                                HerdsmanContract.TelefoneEntry.TABLE_NAME,
                                HerdsmanContract.TelefoneEntry.COLUMN_NAME_PESSOA_IDPESSOA + " == ?",
                                where
                        );
                        ContentValues values = new ContentValues();
                        values.put(HerdsmanContract.TelefoneEntry.COLUMN_NAME_PESSOA_IDPESSOA, "1");
                        values.put(HerdsmanContract.TelefoneEntry.COLUMN_NAME_NUMERO, input.getText().toString());
                        mDb.insert(HerdsmanContract.TelefoneEntry.TABLE_NAME, null, values);
                        mDb.close();
                    }
                }
            });
            AlertDialog alert = alertDialogBuild.create();
            alert.show();
            return true;
        }

        if (id == R.id.entrar_como_administrador)
        {
            AlertDialog.Builder alertDialogBuild = new AlertDialog.Builder(MainActivity.this);
            final EditText input = new EditText(MainActivity.this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);
            input.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            alertDialogBuild.setView(input);
            alertDialogBuild.setTitle("Insira a senha");
            alertDialogBuild.setNegativeButton("Cancelar", null);
            alertDialogBuild.setPositiveButton("Entrar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (input.getText().length() == 0)
                    {
                        return;
                    }
                    else
                    {
                        SQLiteOpenHelper mDbHelper = new HerdsmanDbHelper(MainActivity.this);
                        SQLiteDatabase mDb = mDbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        Cursor cursor;
                        Usuario adminUsuario = null;
                        String[] projection =
                                {
                                        HerdsmanContract.UsuarioEntry.COLUMN_NAME_IDUSUARIO,
                                        HerdsmanContract.UsuarioEntry.COLUMN_NAME_LOGIN,
                                        HerdsmanContract.UsuarioEntry.COLUMN_NAME_SENHA,
                                        HerdsmanContract.UsuarioEntry.COLUMN_NAME_PESSOA_IDPESSOA,
                                        HerdsmanContract.UsuarioEntry.COLUMN_NAME_ADMIN
                                };
                        String selection = HerdsmanContract.UsuarioEntry.COLUMN_NAME_ADMIN + "== 1";
                        cursor = mDb.query(
                                HerdsmanContract.UsuarioEntry.TABLE_NAME,
                                projection,
                                selection,
                                null,
                                null,
                                null,
                                null
                        );
                        while(cursor.moveToNext())
                        {
                            String login = cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.UsuarioEntry.COLUMN_NAME_LOGIN));
                            String senha = cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.UsuarioEntry.COLUMN_NAME_SENHA));
                            int idPessoa = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.UsuarioEntry.COLUMN_NAME_PESSOA_IDPESSOA));
                            int admin = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.UsuarioEntry.COLUMN_NAME_ADMIN));
                            int idUsuario = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.UsuarioEntry.COLUMN_NAME_IDUSUARIO));
                            adminUsuario = new Usuario(admin, login, senha, idPessoa, idUsuario);
                        }
                        if (input.getText().toString().compareTo(adminUsuario.getSenha()) == 0)
                        {
                            usuario = adminUsuario;
                            Toast.makeText(MainActivity.this, "Login efetuado!", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "Senha incorreta", Toast.LENGTH_SHORT).show();
                        }
                        cursor.close();
                        mDb.close();
                    }
                }
            });
            AlertDialog alert = alertDialogBuild.create();
            alert.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_animais) {
            if (usuario == null || usuario.isAdmin() == 0)
            {
                Toast.makeText(MainActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(MainActivity.this, ListaAnimaisActivity.class);
                MainActivity.this.startActivity(intent);
            }

        } else if (id == R.id.nav_enfermidades) {

            if (usuario == null || usuario.isAdmin() == 0)
            {
                Toast.makeText(MainActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(MainActivity.this, ListaEnfermidadesActivity.class);
                MainActivity.this.startActivity(intent);
            }

        } else if (id == R.id.nav_remedios) {

            if (usuario == null || usuario.isAdmin() == 0)
            {
                Toast.makeText(MainActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(MainActivity.this, ListaRemediosActivity.class);
                MainActivity.this.startActivity(intent);
            }


        } else if (id == R.id.nav_funcionarios) {

            if (usuario == null || usuario.isAdmin() == 0)
            {
                Toast.makeText(MainActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(MainActivity.this, ListaFuncionariosActivity.class);
                MainActivity.this.startActivity(intent);
            }

        } else if (id == R.id.nav_cio) {

            Intent intent = new Intent(MainActivity.this, NotificarCioActivity.class);
            MainActivity.this.startActivity(intent);

        } else if (id == R.id.nav_sinistro) {

            Intent intent = new Intent(MainActivity.this, NotificarSinistroActivity.class);
            MainActivity.this.startActivity(intent);

        } else if (id == R.id.nav_outro) {

            if (usuario == null || usuario.isAdmin() == 0)
            {
                Toast.makeText(MainActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(MainActivity.this, NotificarOutroActivity.class);
                MainActivity.this.startActivity(intent);
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void listarCios()
    {
        HerdsmanDbHelper mDbHelper = new HerdsmanDbHelper(this);
        SQLiteDatabase mDb = mDbHelper.getReadableDatabase();
        Cursor cursor = mDb.query(
                HerdsmanContract.CioEntry.TABLE_NAME,
                new String[]{
                        HerdsmanContract.CioEntry.COLUMN_NAME_ANIMAL_IDANIMALPORBAIXO,
                        HerdsmanContract.CioEntry.COLUMN_NAME_ANIMAL_IDANIMALPORCIMA,
                        HerdsmanContract.CioEntry.COLUMN_NAME_IDANIMAL_CIO,
                        HerdsmanContract.CioEntry.COLUMN_NAME_DATA
                },
                null,
                null,
                null,
                null,
                HerdsmanContract.CioEntry.COLUMN_NAME_DATA + " DESC"
        );
        ArrayList listaCios = new ArrayList<Cio>();
        while(cursor.moveToNext())
        {
            int idAnimalPorBaixo = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.CioEntry.COLUMN_NAME_ANIMAL_IDANIMALPORBAIXO));
            int idAnimalPorCima = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.CioEntry.COLUMN_NAME_ANIMAL_IDANIMALPORCIMA));
            int idAnimalCio = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.CioEntry.COLUMN_NAME_IDANIMAL_CIO));
            String data = cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.CioEntry.COLUMN_NAME_DATA));
            Cio cio = new Cio(idAnimalCio,idAnimalPorCima,idAnimalPorBaixo,data);
            Cursor cursor2 = mDb.query(
              HerdsmanContract.AnimalEntry.TABLE_NAME,
              new String[] {HerdsmanContract.AnimalEntry.COLUMN_NAME_IDANIMAL,
                      HerdsmanContract.AnimalEntry.COLUMN_NAME_NUMERO,
                      HerdsmanContract.AnimalEntry.COLUMN_NAME_NOME,
              HerdsmanContract.AnimalEntry.COLUMN_NAME_ATIVO},
              HerdsmanContract.AnimalEntry.COLUMN_NAME_IDANIMAL + " == ?",
              new String[] {String.valueOf(idAnimalPorBaixo)},
              null,
              null,
              null
            );
            if (cursor2.moveToNext()) {
                int idAnimal = cursor2.getInt(cursor2.getColumnIndexOrThrow(HerdsmanContract.AnimalEntry.COLUMN_NAME_IDANIMAL));
                String numero = cursor2.getString(cursor2.getColumnIndexOrThrow(HerdsmanContract.AnimalEntry.COLUMN_NAME_NUMERO));
                String nome = cursor2.getString(cursor2.getColumnIndexOrThrow(HerdsmanContract.AnimalEntry.COLUMN_NAME_NOME));
                int ativo = cursor2.getInt(cursor2.getColumnIndexOrThrow(HerdsmanContract.AnimalEntry.COLUMN_NAME_ATIVO));
                Animal animal = new Animal(idAnimal, numero, nome, ativo);
                cio.setAnimalPorBaixo(animal);
            }
            cursor2.close();

            cursor2 = mDb.query(
                    HerdsmanContract.AnimalEntry.TABLE_NAME,
                    new String[] {HerdsmanContract.AnimalEntry.COLUMN_NAME_IDANIMAL,
                            HerdsmanContract.AnimalEntry.COLUMN_NAME_NUMERO,
                            HerdsmanContract.AnimalEntry.COLUMN_NAME_NOME,
                            HerdsmanContract.AnimalEntry.COLUMN_NAME_ATIVO},
                    HerdsmanContract.AnimalEntry.COLUMN_NAME_IDANIMAL + " == ?",
                    new String[] {String.valueOf(idAnimalPorCima)},
                    null,
                    null,
                    null
            );
            if (cursor2.moveToNext()) {
                int idAnimal = cursor2.getInt(cursor2.getColumnIndexOrThrow(HerdsmanContract.AnimalEntry.COLUMN_NAME_IDANIMAL));
                String numero = cursor2.getString(cursor2.getColumnIndexOrThrow(HerdsmanContract.AnimalEntry.COLUMN_NAME_NUMERO));
                String nome = cursor2.getString(cursor2.getColumnIndexOrThrow(HerdsmanContract.AnimalEntry.COLUMN_NAME_NOME));
                int ativo = cursor2.getInt(cursor2.getColumnIndexOrThrow(HerdsmanContract.AnimalEntry.COLUMN_NAME_ATIVO));
                Animal animal = new Animal(idAnimal, numero, nome, ativo);
                cio.setAnimalPorCima(animal);
            }
            cursor2.close();
            listaCios.add(cio);
        }
        cursor.close();
        mDb.close();
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listaCios);
        lista.setAdapter(adapter);
    }
}
