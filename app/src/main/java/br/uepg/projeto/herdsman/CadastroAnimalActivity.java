package br.uepg.projeto.herdsman;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import br.uepg.projeto.herdsman.DAO.HerdsmanContract;
import br.uepg.projeto.herdsman.DAO.HerdsmanContract.AnimalEntry;
import br.uepg.projeto.herdsman.DAO.HerdsmanDbHelper;
import br.uepg.projeto.herdsman.Objetos.Animal;

public class CadastroAnimalActivity extends AppCompatActivity {
    Animal animal;
    TextView titulo;
    EditText nomeAnimal;
    EditText numeroAnimal;
    RadioGroup radioGroup;
    RadioButton ativoRadio;
    RadioButton inativoRadio;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_animal);
        FloatingActionButton cancela = (FloatingActionButton) findViewById(R.id.cadastro_animal_cancelar);
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.cadastro_animal_add);
        final Animal intent_animal = (Animal) getIntent().getSerializableExtra("Animal");
        titulo = (TextView) findViewById(R.id.cadastro_animal_titulo);
        numeroAnimal = (EditText) findViewById(R.id.cadastro_animal_numero);
        nomeAnimal = (EditText) findViewById(R.id.cadastro_animal_nome);
        radioGroup = (RadioGroup) findViewById(R.id.cadastro_animal_radio_group);
        ativoRadio = (RadioButton) findViewById(R.id.cadastro_animal_radio_ativo);
        inativoRadio = (RadioButton) findViewById(R.id.cadastro_animal_radio_inativo);
        radioGroup.setVisibility(View.INVISIBLE);
        if (intent_animal != null) {
            titulo.setText("Alterar " + intent_animal.getNumero());
            numeroAnimal.setText(intent_animal.getNumero());
            nomeAnimal.setText(intent_animal.getNome());
            radioGroup.setVisibility(View.VISIBLE);
            if(intent_animal.getAtivo() == 1)
            {
                ativoRadio.toggle();
            }
            else
            {
                inativoRadio.toggle();
            }
        }

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intent_animal == null) {
                    if (numeroAnimal.getText().length() == 0) {
                        Toast.makeText(CadastroAnimalActivity.this, "Preencha o número", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (nomeAnimal.getText().length() == 0) {
                        Toast.makeText(CadastroAnimalActivity.this, "Preencha o nome", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    HerdsmanDbHelper mDbHelper = new HerdsmanDbHelper(CadastroAnimalActivity.this);
                    animal = new Animal(nomeAnimal.getText().toString(), numeroAnimal.getText().toString(), 1);
                    long id = mDbHelper.inserirAnimal(animal);
                    Toast.makeText(CadastroAnimalActivity.this, "Animal " + animal.getNumero() + " cadastrado", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                {
                    if (numeroAnimal.getText().length() == 0) {
                        Toast.makeText(CadastroAnimalActivity.this, "Preencha o número", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (nomeAnimal.getText().length() == 0) {
                        Toast.makeText(CadastroAnimalActivity.this, "Preencha o nome", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    HerdsmanDbHelper mDbHelper = new HerdsmanDbHelper(CadastroAnimalActivity.this);

                    if(ativoRadio.isChecked())
                    {
                        animal = new Animal(intent_animal.getId(), numeroAnimal.getText().toString(), nomeAnimal.getText().toString(), 1);
                    }
                    else
                    {
                        animal = new Animal(intent_animal.getId(),numeroAnimal.getText().toString(), nomeAnimal.getText().toString(), 0);
                    }
                    long newRowId = mDbHelper.replaceAnimal(animal);
                    Toast.makeText(CadastroAnimalActivity.this, "Animal " + intent_animal.getNumero() + " alterado", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        cancela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });





    }
}
