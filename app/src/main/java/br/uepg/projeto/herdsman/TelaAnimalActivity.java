package br.uepg.projeto.herdsman;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import br.uepg.projeto.herdsman.DAO.HerdsmanContract;
import br.uepg.projeto.herdsman.DAO.HerdsmanDbHelper;
import br.uepg.projeto.herdsman.Objetos.Animal;

public class TelaAnimalActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_animal);

        animal = (Animal) getIntent().getSerializableExtra("Animal");

        campoNomeAnimal = (TextView) findViewById(R.id.tela_animal_nome_animal);
        campoNumeroAnimal = (TextView) findViewById(R.id.tela_animal_numero_animal);
        buttonCios = (Button) findViewById(R.id.tela_animal_button_cios);
        buttonPartos = (Button) findViewById(R.id.tela_animal_button_partos);
        buttonInseminacoes = (Button) findViewById(R.id.tela_animal_button_inseminacoes);
        buttonRemedios = (Button) findViewById(R.id.tela_animal_button_remedios);
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
}
