package br.uepg.projeto.herdsman;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import br.uepg.projeto.herdsman.DAO.HerdsmanDbHelper;
import br.uepg.projeto.herdsman.Objetos.Animal;

public class TelaAnimalRemediosActivity extends AppCompatActivity{
    ListView listView;
    FloatingActionButton add;
    TextView titulo;
    Animal animal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_partos);
        animal = (Animal) getIntent().getSerializableExtra("Animal");
        listView = findViewById(R.id.lista_partos_listview);
        add = findViewById(R.id.lista_partos_add);
        titulo = findViewById(R.id.lista_partos_titulo);
        titulo.setText("Remédios de " + animal.getNumero());
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TelaAnimalRemediosActivity.this, TelaAnimalCadastroAnimalRemedio.class);
                intent.putExtra("Animal", animal);
                startActivity(intent);
            }
        });

    }

    private void listarRemedios()
    {
        HerdsmanDbHelper mDbHelper = new HerdsmanDbHelper(TelaAnimalRemediosActivity.this);
        ArrayList list = mDbHelper.carregarRemediosAnimal(animal);
        ArrayAdapter adapter = new ArrayAdapter(TelaAnimalRemediosActivity.this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
    }

}
