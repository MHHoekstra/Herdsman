package br.uepg.projeto.herdsman.drawer.notificacao;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import br.uepg.projeto.herdsman.dao.HerdsmanDbHelper;
import br.uepg.projeto.herdsman.drawer.ListaAnimaisActivity;
import br.uepg.projeto.herdsman.drawer.ListaEnfermidadesActivity;
import br.uepg.projeto.herdsman.drawer.ListaFuncionariosActivity;
import br.uepg.projeto.herdsman.drawer.ListaRemediosActivity;
import br.uepg.projeto.herdsman.objetos.Pessoa;
import br.uepg.projeto.herdsman.objetos.Telefone;
import br.uepg.projeto.herdsman.R;

public class NotificarOutroActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    FloatingActionButton send;
    FloatingActionButton cancel;
    Boolean adm;
    public static final String myPref = "preferenceName";
    SharedPreferences pref;
    // TODO Implementar a tela de notificar "outro"
    // FIXME Falta inserir no Banco a Notificação e
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notificar_outro);
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

        cancel = findViewById(R.id.notificar_outro_cancelar);
        send = findViewById(R.id.notificar_outro_done);
        final EditText mensagem = findViewById(R.id.notificar_outro_mensagem);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmsManager smsManager = SmsManager.getDefault();
                String mensagemText = mensagem.getText().toString();
                int SMS_PERMISSION_CODE = 0;
                if (ContextCompat.checkSelfPermission(NotificarOutroActivity.this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(NotificarOutroActivity.this, android.Manifest.permission.SEND_SMS)) {

                    } else {
                        ActivityCompat.requestPermissions(NotificarOutroActivity.this, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_CODE);
                    }

                }
                if(mensagemText.length() == 0)
                {
                    Toast.makeText(NotificarOutroActivity.this, "Preencha uma mensagem", Toast.LENGTH_SHORT).show();
                    return;
                }
                String text = "Herdsman's Companion;\n3;" + mensagemText;
                HerdsmanDbHelper herdsmanDbHelper = new HerdsmanDbHelper(NotificarOutroActivity.this);
                ArrayList<Pessoa> listaFuncionarios = herdsmanDbHelper.carregarFuncionariosDb();
                for (Pessoa pessoa : listaFuncionarios) {
                    ArrayList<Telefone> listaTelefones = herdsmanDbHelper.carregarTelefonesPessoa(pessoa);
                    for(Telefone telefone : listaTelefones)
                    {
                        try{
                            smsManager.sendTextMessage(telefone.getNumero(), null, text, null, null);
                            Toast.makeText(NotificarOutroActivity.this, "SMS enviado para " + telefone.getNumero(), Toast.LENGTH_SHORT).show();
                        }
                        catch (Exception e)
                        {
                            Toast.makeText(NotificarOutroActivity.this, "Erro ao enviar", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }
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
                Toast.makeText(NotificarOutroActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(NotificarOutroActivity.this, ListaAnimaisActivity.class);
                NotificarOutroActivity.this.startActivity(intent);
            }

        } else if (id == R.id.nav_enfermidades) {

            if (!adm)
            {
                Toast.makeText(NotificarOutroActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(NotificarOutroActivity.this, ListaEnfermidadesActivity.class);
                NotificarOutroActivity.this.startActivity(intent);
            }

        } else if (id == R.id.nav_remedios) {

            if (!adm)
            {
                Toast.makeText(NotificarOutroActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(NotificarOutroActivity.this, ListaRemediosActivity.class);
                NotificarOutroActivity.this.startActivity(intent);
            }


        } else if (id == R.id.nav_funcionarios) {

            if (!adm)
            {
                Toast.makeText(NotificarOutroActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(NotificarOutroActivity.this, ListaFuncionariosActivity.class);
                NotificarOutroActivity.this.startActivity(intent);
            }

        } else if (id == R.id.nav_cio) {

            Intent intent = new Intent(NotificarOutroActivity.this, NotificarCioActivity.class);
            NotificarOutroActivity.this.startActivity(intent);

        } else if (id == R.id.nav_sinistro) {

            Intent intent = new Intent(NotificarOutroActivity.this, NotificarSinistroActivity.class);
            NotificarOutroActivity.this.startActivity(intent);

        } else if (id == R.id.nav_outro) {

            if (!adm)
            {
                Toast.makeText(NotificarOutroActivity.this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(NotificarOutroActivity.this, NotificarOutroActivity.class);
                NotificarOutroActivity.this.startActivity(intent);
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
