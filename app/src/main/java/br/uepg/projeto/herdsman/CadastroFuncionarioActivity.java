package br.uepg.projeto.herdsman;

import android.content.ContentValues;
import android.database.Cursor;
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
import br.uepg.projeto.herdsman.DAO.HerdsmanDbHelper;
import br.uepg.projeto.herdsman.Objetos.Pessoa;
import br.uepg.projeto.herdsman.Objetos.Telefone;
import br.uepg.projeto.herdsman.Objetos.Usuario;

public class CadastroFuncionarioActivity extends AppCompatActivity {
    Pessoa pessoa;
    Pessoa intent_pessoa;
    Usuario usuario;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_funcionario);

        FloatingActionButton add = (FloatingActionButton) findViewById(R.id.cadastro_funcionario_add);
        FloatingActionButton cancelar = (FloatingActionButton) findViewById(R.id.cadastro_funcionario_cancelar);
        TextView titulo = (TextView) findViewById(R.id.cadastro_funcionario_titulo);
        final EditText campoNome = (EditText) findViewById(R.id.cadastro_funcionario_nome);
        final EditText campoCpf = (EditText) findViewById(R.id.cadastro_funcionario_cpf);
        final EditText campoRg = (EditText) findViewById(R.id.cadastro_funcionario_rg);
        RadioGroup radioGroup = findViewById(R.id.cadastro_funcionario_radio_group);
        final RadioButton radioButtonAtivo = findViewById(R.id.cadastro_funcionario_radio_ativo);
        intent_pessoa = (Pessoa) getIntent().getSerializableExtra("Pessoa");

        if (intent_pessoa != null)
        {
            titulo.setText("Alterar " + intent_pessoa.getNome());
            campoNome.setText(intent_pessoa.getNome());
            campoCpf.setText(intent_pessoa.getCpf());
            campoRg.setText(intent_pessoa.getRg());
            radioGroup.setVisibility(View.VISIBLE);
        }
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intent_pessoa == null) {
                    if (campoNome.getText().length() == 0) {
                        return;
                    }

                    if (campoCpf.getText().length() == 0) {
                        return;
                    }
                    if (campoRg.getText().length() == 0) {
                        return;
                    }

                    pessoa = new Pessoa(campoNome.getText().toString(), campoCpf.getText().toString(), campoRg.getText().toString());

                    SQLiteOpenHelper mDbHelper = new HerdsmanDbHelper(CadastroFuncionarioActivity.this);
                    SQLiteDatabase mDb = mDbHelper.getWritableDatabase();

                    ContentValues valuesP = new ContentValues();
                    valuesP.put(HerdsmanContract.PessoaEntry.COLUMN_NAME_NOME, pessoa.getNome());
                    valuesP.put(HerdsmanContract.PessoaEntry.COLUMN_NAME_CPF, pessoa.getCpf());
                    valuesP.put(HerdsmanContract.PessoaEntry.COLUMN_NAME_RG, pessoa.getRg());
                    valuesP.put(HerdsmanContract.PessoaEntry.COLUMN_NAME_ATIVO, "1");
                    long newRowId = mDb.insert(HerdsmanContract.PessoaEntry.TABLE_NAME, null, valuesP);

                    usuario = new Usuario(campoCpf.getText().toString(), campoRg.getText().toString(), ((int) newRowId));
                    ContentValues valuesU = new ContentValues();
                    valuesU.put(HerdsmanContract.UsuarioEntry.COLUMN_NAME_LOGIN, usuario.getLogin());
                    valuesU.put(HerdsmanContract.UsuarioEntry.COLUMN_NAME_SENHA, usuario.getSenha());
                    valuesU.put(HerdsmanContract.UsuarioEntry.COLUMN_NAME_PESSOA_IDPESSOA, usuario.getIdPessoa());
                    long newRowUsuario = mDb.insert(HerdsmanContract.UsuarioEntry.TABLE_NAME, null, valuesU);


                    Toast.makeText(CadastroFuncionarioActivity.this, pessoa.getNome() + " cadastrado", Toast.LENGTH_SHORT).show();
                    mDb.close();
                    finish();
                }

                else
                {
                    if (campoNome.getText().length() == 0) {
                        return;
                    }
                    if (campoCpf.getText().length() == 0) {
                        return;
                    }
                    if (campoRg.getText().length() == 0) {
                        return;
                    }

                    pessoa = new Pessoa(campoNome.getText().toString(), campoCpf.getText().toString(), campoRg.getText().toString());

                    SQLiteOpenHelper mDbHelper = new HerdsmanDbHelper(CadastroFuncionarioActivity.this);
                    SQLiteDatabase mDb = mDbHelper.getWritableDatabase();

                    ContentValues valuesP = new ContentValues();
                    valuesP.put(HerdsmanContract.PessoaEntry.COLUMN_NAME_IDPESSOA, intent_pessoa.getIdPessoa());
                    valuesP.put(HerdsmanContract.PessoaEntry.COLUMN_NAME_NOME, pessoa.getNome());
                    valuesP.put(HerdsmanContract.PessoaEntry.COLUMN_NAME_CPF, pessoa.getCpf());
                    valuesP.put(HerdsmanContract.PessoaEntry.COLUMN_NAME_RG, pessoa.getRg());
                    if(radioButtonAtivo.isActivated())
                    {
                        valuesP.put(HerdsmanContract.PessoaEntry.COLUMN_NAME_ATIVO, "1");
                    }
                    else {
                        valuesP.put(HerdsmanContract.PessoaEntry.COLUMN_NAME_ATIVO, "0");
                    }
                    long newRowId = mDb.replace(HerdsmanContract.PessoaEntry.TABLE_NAME, null, valuesP);
                    Toast.makeText(CadastroFuncionarioActivity.this, intent_pessoa.getNome() + " alterado", Toast.LENGTH_SHORT).show();
                    mDb.close();
                    finish();
                }
            }
        });

    }
}
