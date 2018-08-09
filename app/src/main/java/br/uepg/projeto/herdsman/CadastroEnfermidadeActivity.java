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
import br.uepg.projeto.herdsman.Objetos.Enfermidade;

public class CadastroEnfermidadeActivity extends AppCompatActivity {
    Enfermidade enfermidade;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_enfermidade);
        FloatingActionButton cadastrar = (FloatingActionButton) findViewById(R.id.cadastro_enfermidade_add);
        FloatingActionButton cancelar = (FloatingActionButton) findViewById(R.id.cadastro_enfermidade_cancelar);
        final EditText descricao = (EditText) findViewById(R.id.cadastro_enfermidade_descricão);

        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(descricao.getText().length() == 0)
                {
                    Toast.makeText(CadastroEnfermidadeActivity.this, "Preencha o campo", Toast.LENGTH_SHORT).show();
                    return;
                }
                enfermidade = new Enfermidade(descricao.getText().toString());
                HerdsmanDbHelper mDbHelper = new HerdsmanDbHelper(CadastroEnfermidadeActivity.this);
                SQLiteDatabase mDb = mDbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(HerdsmanContract.EnfermidadeEntry.COLUMN_NAME_DESCRICAO, enfermidade.getDescricao());
                long insert = mDb.insert(HerdsmanContract.EnfermidadeEntry.TABLE_NAME, null, values);
                Toast.makeText(CadastroEnfermidadeActivity.this, "Enfermidade cadastrada", Toast.LENGTH_SHORT).show();
                mDb.close();
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
