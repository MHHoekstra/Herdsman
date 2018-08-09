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
        List listaAnimais = new ArrayList<Animal>();
        SQLiteOpenHelper mDbHelper = new HerdsmanDbHelper(NotificarCioActivity.this);
        SQLiteDatabase mDb = mDbHelper.getReadableDatabase();
        Cursor cursor;
        String[] projection =
        {
            HerdsmanContract.AnimalEntry.COLUMN_NAME_NUMERO,
            HerdsmanContract.AnimalEntry.COLUMN_NAME_IDANIMAL,
            HerdsmanContract.AnimalEntry.COLUMN_NAME_NOME
        };
        String selection = HerdsmanContract.AnimalEntry.COLUMN_NAME_ATIVO +  " == 1";
        cursor = mDb.query(
                HerdsmanContract.AnimalEntry.TABLE_NAME,
                projection,
                selection,
                null,
                null,
                null,
                null
        );
        while (cursor.moveToNext())
        {
            String numero = cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalEntry.COLUMN_NAME_NUMERO));
            int idAnimal = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalEntry.COLUMN_NAME_IDANIMAL));
            String nome = cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalEntry.COLUMN_NAME_NOME));
            Animal animal = new Animal(idAnimal, numero, nome);
            listaAnimais.add(animal);
        }
        ArrayAdapter adapter = new ArrayAdapter(NotificarCioActivity.this, R.layout.support_simple_spinner_dropdown_item, listaAnimais);
        animalPorBaixoSpinner.setAdapter(adapter);
        animalPorCimaSpinner.setAdapter(adapter);
        cursor = mDb.query(
                HerdsmanContract.TelefoneEntry.TABLE_NAME,
                new String[] {HerdsmanContract.TelefoneEntry.COLUMN_NAME_NUMERO},
                HerdsmanContract.TelefoneEntry.COLUMN_NAME_PESSOA_IDPESSOA + " == ?",
                new String[] {"1"},
                null,
                null,
                null
        );
        String numero = null;
        while(cursor.moveToNext())
        {
            numero = cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.TelefoneEntry.COLUMN_NAME_NUMERO));
        }
        final Telefone telefoneAdmin = new Telefone(1, numero);
        cursor.close();
        mDb.close();
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });
    }
}
