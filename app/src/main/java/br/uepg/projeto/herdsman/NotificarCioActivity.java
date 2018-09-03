package br.uepg.projeto.herdsman;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.uepg.projeto.herdsman.DAO.HerdsmanContract;
import br.uepg.projeto.herdsman.DAO.HerdsmanDbHelper;
import br.uepg.projeto.herdsman.Objetos.Animal;
import br.uepg.projeto.herdsman.Objetos.Telefone;

import static android.Manifest.permission.SEND_SMS;

public class NotificarCioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notificar_cio);
        final Spinner animalPorCimaSpinner = findViewById(R.id.notificar_cio_por_cima_spinner);
        final Spinner animalPorBaixoSpinner = findViewById(R.id.notificar_cio_por_baixo_spinner);
        FloatingActionButton cancelar = findViewById(R.id.notifica_cio_cancelar);
        FloatingActionButton done = findViewById(R.id.notificar_cio_done);
        HerdsmanDbHelper mDbHelper = new HerdsmanDbHelper(NotificarCioActivity.this);
        ArrayList listaAnimais = mDbHelper.listarAnimaisAtivos();
        ArrayAdapter adapter = new ArrayAdapter(NotificarCioActivity.this, R.layout.support_simple_spinner_dropdown_item, listaAnimais);
        animalPorBaixoSpinner.setAdapter(adapter);
        animalPorCimaSpinner.setAdapter(adapter);
        final Telefone telefoneAdmin = mDbHelper.carregarTelefoneAdmin();
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // FIXME E se for para o proprio celular?
                SmsManager smsManager = SmsManager.getDefault();
                Animal animalPorBaixo = (Animal) animalPorBaixoSpinner.getSelectedItem();
                Animal animalPorCima = (Animal) animalPorCimaSpinner.getSelectedItem();
                int SMS_PERMISSION_CODE = 0;
                if (ContextCompat.checkSelfPermission(NotificarCioActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(NotificarCioActivity.this, Manifest.permission.SEND_SMS)) {

                    } else {
                        ActivityCompat.requestPermissions(NotificarCioActivity.this, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_CODE);
                    }

                }
                String text =  "Herdsman's Companion;\n1;" + String.valueOf(animalPorBaixo.getId()) + ";"+String.valueOf(animalPorCima.getId());
                   smsManager.sendTextMessage(telefoneAdmin.getNumero(), null, text, null, null);
                Toast.makeText(NotificarCioActivity.this, "SMS enviado para " + telefoneAdmin.getNumero(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
