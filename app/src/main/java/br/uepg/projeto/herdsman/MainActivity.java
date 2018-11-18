package br.uepg.projeto.herdsman;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import br.uepg.projeto.herdsman.dao.HerdsmanDbHelper;
import br.uepg.projeto.herdsman.dao.HerdsmanDbSync;
import br.uepg.projeto.herdsman.drawer.ListaAnimaisActivity;
import br.uepg.projeto.herdsman.drawer.ListaEnfermidadesActivity;
import br.uepg.projeto.herdsman.drawer.ListaFuncionariosActivity;
import br.uepg.projeto.herdsman.drawer.ListaRemediosActivity;
import br.uepg.projeto.herdsman.drawer.notificacao.NotificarCioActivity;
import br.uepg.projeto.herdsman.drawer.notificacao.NotificarOutroActivity;
import br.uepg.projeto.herdsman.drawer.notificacao.NotificarAnimalEnfermidadeActivity;
import br.uepg.projeto.herdsman.objetos.Administrador;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    HerdsmanDbHelper mDbHelper;
    Administrador administrador = null;
    ListView lista;
    Button buttonCio;
    Button buttonAnimalEnfermidade;
    Button buttonOutros;
    ArrayList listaCios;
    ArrayList listaEnfermidades;
    ArrayList listaOutros;
    MenuItem sairAdmin;
    MenuItem entrarAdmin;
    Menu menuInicio;
    Boolean adm;
    String cinza = "#c7c9cc";
    String azul = "#84b3ff";
    public static final String myPref = "preferenceName";
    SharedPreferences pref;
    //TODO Atualizar automaticamente ao inserir um novo cio
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseDatabase.getInstance().getReference("Hoekstra - Base de Testes");
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
        pref = getApplicationContext().getSharedPreferences("isAdmin", MODE_PRIVATE);

        if (isOnline(this)) {
            setTitle("Herdsman");
        } else {setTitle("Herdsman (OFFLINE)");
        }
        this.mDbHelper = new HerdsmanDbHelper(this);
        this.mDbHelper.searchDuplicateAnimals();
        this.mDbHelper.searchDuplicateRemedios();
        this.mDbHelper.searchDuplicateSinistros();
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
        buttonCio = findViewById(R.id.main_activity_button_cios);
        buttonCio.getBackground().setColorFilter(Color.parseColor(azul), PorterDuff.Mode.MULTIPLY);
        buttonAnimalEnfermidade = findViewById(R.id.main_activity_button_animal_enfermidades);
        buttonOutros = findViewById(R.id.main_activity_button_outros);
        buttonCio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listarCios();
                buttonCio.getBackground().setColorFilter(Color.parseColor(azul), PorterDuff.Mode.MULTIPLY);
                buttonAnimalEnfermidade.getBackground().setColorFilter(Color.parseColor(cinza), PorterDuff.Mode.MULTIPLY);
                buttonOutros.getBackground().setColorFilter(Color.parseColor(cinza), PorterDuff.Mode.MULTIPLY);

            }
        });
        buttonAnimalEnfermidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listarSinistros();
                buttonCio.getBackground().setColorFilter(Color.parseColor(cinza), PorterDuff.Mode.MULTIPLY);
                buttonAnimalEnfermidade.getBackground().setColorFilter(Color.parseColor(azul), PorterDuff.Mode.MULTIPLY);
                buttonOutros.getBackground().setColorFilter(Color.parseColor(cinza), PorterDuff.Mode.MULTIPLY);
            }
        });
        buttonOutros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listarOutros();
                buttonCio.getBackground().setColorFilter(Color.parseColor(cinza), PorterDuff.Mode.MULTIPLY);
                buttonAnimalEnfermidade.getBackground().setColorFilter(Color.parseColor(cinza), PorterDuff.Mode.MULTIPLY);
                buttonOutros.getBackground().setColorFilter(Color.parseColor(azul), PorterDuff.Mode.MULTIPLY);
            }
        });
        listarCios();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //FIXME Arrumar a volta pra sair do APP
            //super.onBackPressed();
            MainActivity.this.moveTaskToBack(true);
        }
    }

    @SuppressLint("ResourceType")
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
        adm = pref.getBoolean("isAdmin", false);
        //noinspection SimplifiableIfStatement
        if (id == R.id.sincronizar_bd) {
            HerdsmanDbSync sync = new HerdsmanDbSync(this);
            return sync.startSync();
            }
        if (id == R.id.alterar_administrador) {
            if (!adm) {
                Toast.makeText(MainActivity.this, R.string.acesso_negado, Toast.LENGTH_SHORT).show();
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
                        HerdsmanDbHelper mDbHelper = new HerdsmanDbHelper(MainActivity.this);
                        mDbHelper.updateAdminTelefone(input.getText().toString());
                        Toast.makeText(MainActivity.this, "Número alterado para " + input.getText().toString(), Toast.LENGTH_SHORT).show();
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
            input.setGravity(Gravity.CENTER);
            //input.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            alertDialogBuild.setView(input);
            alertDialogBuild.setTitle("Insira a senha:");
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
                        HerdsmanDbHelper mDbHelper = new HerdsmanDbHelper(MainActivity.this);
                        Administrador adminAdministrador = mDbHelper.carregarAdminDatabase();
                        if (input.getText().toString().compareTo(adminAdministrador.getSenha()) == 0)
                        {
                            administrador = adminAdministrador;
                            Toast.makeText(MainActivity.this, "Login efetuado!", Toast.LENGTH_SHORT).show();
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putBoolean("isAdmin", true);
                            editor.commit();
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "Senha incorreta", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            });
            AlertDialog alert = alertDialogBuild.create();
            alert.show();
            return true;
        }
        if (id == R.id.sair_admin)
        {
            administrador = null;
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("isAdmin", false);
            editor.commit();
            return true;
        }

        if(id == R.id.sobre_menu)
        {
            AlertDialog.Builder alertDialogBuild = new AlertDialog.Builder(MainActivity.this);
            final TextView input = new TextView(MainActivity.this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);
            input.setGravity(Gravity.CENTER);
            alertDialogBuild.setView(input);
            alertDialogBuild.setTitle("Sobre");
            input.setText("Herdsman's Companion v1.0\nIcones disponíveis em https://icons8.com/\n");
            alertDialogBuild.setPositiveButton("Ok", null);
            AlertDialog alert = alertDialogBuild.create();
            alert.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem sairAdm = menu.findItem(R.id.sair_admin);
        MenuItem entrarAdm = menu.findItem(R.id.entrar_como_administrador);
        adm = pref.getBoolean("isAdmin", false);
        if(adm){
            sairAdm.setVisible(true);
            entrarAdm.setVisible(false);
        }
        else {
            entrarAdm.setVisible(true);
            sairAdm.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
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
                Toast.makeText(MainActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(MainActivity.this, ListaAnimaisActivity.class);
                MainActivity.this.startActivity(intent);
            }

        } else if (id == R.id.nav_enfermidades) {

            if (!adm)
            {
                Toast.makeText(MainActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(MainActivity.this, ListaEnfermidadesActivity.class);
                MainActivity.this.startActivity(intent);
            }

        } else if (id == R.id.nav_remedios) {

            if (!adm)
            {
                Toast.makeText(MainActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(MainActivity.this, ListaRemediosActivity.class);
                MainActivity.this.startActivity(intent);
            }


        } else if (id == R.id.nav_funcionarios) {

            if (!adm)
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

            Intent intent = new Intent(MainActivity.this, NotificarAnimalEnfermidadeActivity.class);
            MainActivity.this.startActivity(intent);

        } else if (id == R.id.nav_outro) {

            if (!adm)
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
        listaCios = this.mDbHelper.carregarTodosCios();
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listaCios);
        lista.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void listarSinistros()
    {
        listaEnfermidades = this.mDbHelper.carregarTodosSinistros();
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listaEnfermidades);
        lista.setAdapter(adapter);
    }

    private void listarOutros()
    {
        listaOutros = this.mDbHelper.carregarTodosAdministradorNotificaPessoa();
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listaOutros);
        lista.setAdapter(adapter);
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected())
            return true;
        else
            return false;
    }
}
