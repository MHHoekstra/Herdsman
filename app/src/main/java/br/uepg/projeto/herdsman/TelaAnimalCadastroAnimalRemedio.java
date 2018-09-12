package br.uepg.projeto.herdsman;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;

import br.uepg.projeto.herdsman.DAO.HerdsmanContract;
import br.uepg.projeto.herdsman.DAO.HerdsmanDbHelper;

public class TelaAnimalCadastroAnimalRemedio extends AppCompatActivity{

    Spinner remedioSpinner;
    Spinner medidasSpinner;
    EditText quantidadeText;
    FloatingActionButton done;
    FloatingActionButton cancel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_animal_remedio);

        done = findViewById(R.id.cadastro_animal_remedio_done);
        cancel = findViewById(R.id.cadastro_animal_remedio_cancelar);
        remedioSpinner = findViewById(R.id.cadastro_animal_remedio_remedio_spinner);
        medidasSpinner = findViewById(R.id.cadastro_animal_remedio_medida_spinner);

        carregarMedidas();
        carregarRemedios();
        //TODO Terminar Inserção

    }


    private void carregarMedidas()
    {
        HerdsmanDbHelper herdsmanDbHelper = new HerdsmanDbHelper(TelaAnimalCadastroAnimalRemedio.this);
        ArrayList listaMedidas = herdsmanDbHelper.carregarMedidas();
        ArrayAdapter adapterMedidas = new ArrayAdapter(TelaAnimalCadastroAnimalRemedio.this, android.R.layout.simple_spinner_dropdown_item, listaMedidas);
        medidasSpinner.setAdapter(adapterMedidas);
    }

    private void carregarRemedios()
    {
        HerdsmanDbHelper herdsmanDbHelper = new HerdsmanDbHelper(TelaAnimalCadastroAnimalRemedio.this);
        ArrayList listaRemedios = herdsmanDbHelper.carregarTodosRemedios();
        ArrayAdapter adapterRemedios = new ArrayAdapter(TelaAnimalCadastroAnimalRemedio.this, android.R.layout.simple_spinner_dropdown_item, listaRemedios);
        remedioSpinner.setAdapter(adapterRemedios);
    }
}
