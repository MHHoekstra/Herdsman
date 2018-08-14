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

                    HerdsmanDbHelper mDbHelper = new HerdsmanDbHelper(CadastroFuncionarioActivity.this);
                    long insert = mDbHelper.inserirFuncionario(pessoa);
                    if (insert > 0)
                    {
                        Toast.makeText(CadastroFuncionarioActivity.this, pessoa.getNome() + " cadastrado", Toast.LENGTH_SHORT).show();
                    }
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
                    pessoa.setIdPessoa(intent_pessoa.getIdPessoa());
                    HerdsmanDbHelper mDbHelper = new HerdsmanDbHelper(CadastroFuncionarioActivity.this);

                    if(radioButtonAtivo.isActivated())
                    {
                        pessoa.setAtivo(1);
                        long insert = mDbHelper.replaceFuncionario(pessoa);
                        if (insert > 0)
                        {
                            Toast.makeText(CadastroFuncionarioActivity.this, pessoa.getNome() + " alterado.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        pessoa.setAtivo(0);
                        long insert = mDbHelper.replaceFuncionario(pessoa);
                        if (insert > 0)
                        {
                            Toast.makeText(CadastroFuncionarioActivity.this, pessoa.getNome() + " alterado.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    finish();
                }
            }
        });

    }
}
