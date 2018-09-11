package br.uepg.projeto.herdsman;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import br.uepg.projeto.herdsman.DAO.HerdsmanContract;
import br.uepg.projeto.herdsman.DAO.HerdsmanDbHelper;
import br.uepg.projeto.herdsman.Objetos.Animal;
import br.uepg.projeto.herdsman.Objetos.Inseminacao;

public class TelaAnimalInseminacoesActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    Animal animal;
    TextView titulo;
    ListView listView;
    FloatingActionButton add;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_partos);

        animal = (Animal) getIntent().getSerializableExtra("Animal");
        titulo = findViewById(R.id.lista_partos_titulo);
        listView = findViewById(R.id.lista_partos_listview);
        add = findViewById(R.id.lista_partos_add);

        titulo.setText("Inseminações de " + animal.getNumero());

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment fragment = new DatePickerFragment();
                fragment.show(getFragmentManager(), "Data");
            }
        });

        listarInseminacoes();
    }

    private void listarInseminacoes()
    {
        HerdsmanDbHelper mDbHelper = new HerdsmanDbHelper(TelaAnimalInseminacoesActivity.this);
        ArrayList list = mDbHelper.carregarInseminacoesAnimal(animal);
        ArrayAdapter adapter = new ArrayAdapter(TelaAnimalInseminacoesActivity.this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        String dia;
        if(String.valueOf(i2).length() == 1)
        {
            dia = '0' + String.valueOf(i2);
        }
        else {
            dia = String.valueOf(i2);
        }
        String data = String.valueOf(i) + '-' + String.valueOf(i1) + '-' + dia;
        Inseminacao inseminacao = new Inseminacao(animal.getId(), data);
        HerdsmanDbHelper mDbHelper = new HerdsmanDbHelper(TelaAnimalInseminacoesActivity.this);
        long ins = mDbHelper.inserirInseminacao(inseminacao);
        if(ins > 0)
        {
            Toast.makeText(TelaAnimalInseminacoesActivity.this, "Inseminação inserida", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(TelaAnimalInseminacoesActivity.this, "Falha ao inserir", Toast.LENGTH_SHORT).show();
        }

        listarInseminacoes();

    }
}
