package br.uepg.projeto.herdsman.helper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import br.uepg.projeto.herdsman.R;

public class HelperTelaCadastroRemedioAnimal extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.helper_tela_cadastro_remedio_animal);
        setTitle("Ajuda");
    }
}
