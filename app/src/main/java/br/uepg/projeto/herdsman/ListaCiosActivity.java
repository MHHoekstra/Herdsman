package br.uepg.projeto.herdsman;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import br.uepg.projeto.herdsman.DAO.HerdsmanDbHelper;
import br.uepg.projeto.herdsman.Objetos.Animal;

public class ListaCiosActivity extends AppCompatActivity {
    ListView ListaCios;
    Animal animal;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_cios);
        animal = (Animal) getIntent().getSerializableExtra("Animal");
        TextView titulo = findViewById(R.id.lista_cios_titulo);
        titulo.setText("Cios " + animal.getNumero());
        ListaCios = (ListView) findViewById(R.id.lista_cios_listview);

        listarCios();

    }

    private void listarCios() {
        HerdsmanDbHelper mDbHelper = new HerdsmanDbHelper(ListaCiosActivity.this);
        ArrayList listaCiosmDbHelper = mDbHelper.carregarCiosAnimal(animal);
        ArrayAdapter<String> adapter = new ArrayAdapter(ListaCiosActivity.this, android.R.layout.simple_list_item_1, (List) listaCiosmDbHelper);
        ListaCios.setAdapter(adapter);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        listarCios();
    }



}
