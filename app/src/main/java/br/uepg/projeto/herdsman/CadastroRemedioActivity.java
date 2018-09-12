package br.uepg.projeto.herdsman;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import br.uepg.projeto.herdsman.DAO.HerdsmanContract;
import br.uepg.projeto.herdsman.DAO.HerdsmanDbHelper;
import br.uepg.projeto.herdsman.Objetos.Remedio;

public class CadastroRemedioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_remedio);
        FloatingActionButton cadastrar = (FloatingActionButton) findViewById(R.id.cadastro_remedio_add);
        FloatingActionButton cancelar = (FloatingActionButton) findViewById(R.id.cadastro_remedio_cancelar);
        final EditText descricao = (EditText) findViewById(R.id.cadastro_remedio_nome);

        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(descricao.getText().length() == 0)
                {
                    Toast.makeText(CadastroRemedioActivity.this,"Preencha o campo", Toast.LENGTH_SHORT).show();
                    return;
                }
                HerdsmanDbHelper mDbHelper = new HerdsmanDbHelper(CadastroRemedioActivity.this);
                Remedio remedio = new Remedio(descricao.getText().toString());
                mDbHelper.inserirRemedio(remedio);
                Toast.makeText(CadastroRemedioActivity.this, "Remédio cadastrado", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
