package br.uepg.projeto.herdsman;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import br.uepg.projeto.herdsman.DAO.HerdsmanDbHelper;
import br.uepg.projeto.herdsman.Objetos.Animal;
import br.uepg.projeto.herdsman.Objetos.Enfermidade;
import br.uepg.projeto.herdsman.Objetos.Telefone;

public class NotificarSinistroActivity extends AppCompatActivity {
    Spinner animalSpinner;
    Spinner enfermidadeSpinner;
    FloatingActionButton cancelar;
    FloatingActionButton enviar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notificar_sinistro);
        HerdsmanDbHelper mDbHelper = new HerdsmanDbHelper(this);
        cancelar = findViewById(R.id.notificar_sinistro_cancelar);
        enviar = findViewById(R.id.notificar_sinistro_done);
        animalSpinner = findViewById(R.id.notificar_sinistro_animal_spinner);
        enfermidadeSpinner = findViewById(R.id.notificar_sinistro_enfermidade_spinner);

        ArrayList animais = mDbHelper.carregarAnimaisAtivos();
        final Telefone adminTelefone = mDbHelper.carregarTelefoneAdmin();
        ArrayList enfermidades = mDbHelper.carregarEnfermidades();
        ArrayAdapter<String> animalAdapter = new ArrayAdapter(NotificarSinistroActivity.this, R.layout.support_simple_spinner_dropdown_item, animais);
        ArrayAdapter<String> enfermidadeAdapter = new ArrayAdapter(NotificarSinistroActivity.this, R.layout.support_simple_spinner_dropdown_item, enfermidades);
        animalSpinner.setAdapter(animalAdapter);
        enfermidadeSpinner.setAdapter(enfermidadeAdapter);

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmsManager smsManager = SmsManager.getDefault();
                Enfermidade enfermidade = (Enfermidade) enfermidadeSpinner.getSelectedItem();
                Animal animal = (Animal) animalSpinner.getSelectedItem();
                int SMS_PERMISSION_CODE = 0;
                if (ContextCompat.checkSelfPermission(NotificarSinistroActivity.this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(NotificarSinistroActivity.this, android.Manifest.permission.SEND_SMS)) {

                    } else {
                        ActivityCompat.requestPermissions(NotificarSinistroActivity.this, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_CODE);
                    }

                }
                String text =  "Herdsman's Companion;\n2;" + String.valueOf(enfermidade.getId()) + ";"+String.valueOf(animal.getId());
                smsManager.sendTextMessage(adminTelefone.getNumero(), null, text, null, null);
                Toast.makeText(NotificarSinistroActivity.this, "SMS enviado para " + adminTelefone.getNumero(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });


    }
}
