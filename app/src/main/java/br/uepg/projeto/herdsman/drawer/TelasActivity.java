package br.uepg.projeto.herdsman.drawer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import br.uepg.projeto.herdsman.MainActivity;
import br.uepg.projeto.herdsman.R;
import br.uepg.projeto.herdsman.drawer.notificacao.NotificarCioActivity;
import br.uepg.projeto.herdsman.drawer.notificacao.NotificarOutroActivity;
import br.uepg.projeto.herdsman.drawer.notificacao.NotificarAnimalEnfermidadeActivity;

public class TelasActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Boolean adm;
    public static final String myPref = "preferenceName";
    SharedPreferences pref;
    private static final String SELECTED_ITEM_ID = "selected_id";
    private int mSelectedId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        pref = getApplicationContext().getSharedPreferences("isAdmin", MODE_PRIVATE);
        if (savedInstanceState != null) {
            mSelectedId = savedInstanceState.getInt(SELECTED_ITEM_ID);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        InputMethodManager inputMethodManager = (InputMethodManager)  this.getSystemService(this.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        int id = item.getItemId();
        if (this.mSelectedId == id) {
            drawer.closeDrawer(GravityCompat.START);
            return false;
        }
        if (id == R.id.nav_cio) {
            Intent intent = new Intent(this, NotificarCioActivity.class);
            this.startActivity(intent);
        }
        if (id == R.id.nav_sinistro) {

            Intent intent = new Intent(this, NotificarAnimalEnfermidadeActivity.class);
            this.startActivity(intent);
        }
        adm = pref.getBoolean("isAdmin", false);
        if (!adm) {
            Toast.makeText(this, "Faça login para ter acesso", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.nav_animais) {
            Intent intent = new Intent(this, ListaAnimaisActivity.class);
            this.startActivity(intent);
        } else if (id == R.id.nav_enfermidades) {
            Intent intent = new Intent(this, ListaEnfermidadesActivity.class);
            this.startActivity(intent);
        } else if (id == R.id.nav_remedios) {
            Intent intent = new Intent(this, ListaRemediosActivity.class);
            this.startActivity(intent);
        } else if (id == R.id.nav_funcionarios) {
            Intent intent = new Intent(this, ListaFuncionariosActivity.class);
            this.startActivity(intent);
        } else if (id == R.id.nav_outro) {
            Intent intent = new Intent(this, NotificarOutroActivity.class);
            this.startActivity(intent);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);
            //super.onBackPressed();
        }
    }
}
