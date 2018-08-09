package br.uepg.projeto.herdsman;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

import br.uepg.projeto.herdsman.DAO.HerdsmanContract;
import br.uepg.projeto.herdsman.DAO.HerdsmanDbHelper;
import br.uepg.projeto.herdsman.Objetos.Pessoa;
import br.uepg.projeto.herdsman.Objetos.Telefone;

public class ListaFuncionariosActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    SearchView pesquisa;
    Pessoa pessoa;
    ListView listaFuncionariosView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_funcionarios);
        pesquisa = (SearchView) findViewById(R.id.lista_funcionarios_pesquisa);
        listaFuncionariosView = (ListView) findViewById(R.id.funcionariosListView);
        FloatingActionButton addFuncionario = (FloatingActionButton) findViewById(R.id.add_funcionario);
        addFuncionario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListaFuncionariosActivity.this, CadastroFuncionarioActivity.class);
                startActivity(intent);
            }
        });
        listaFuncionariosView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                pessoa  = (Pessoa) listaFuncionariosView.getItemAtPosition(position);
                Intent intent = new Intent(ListaFuncionariosActivity.this, CadastroFuncionarioActivity.class);
                intent.putExtra("Pessoa", pessoa);
                startActivity(intent);
                return true;
            }
        });
        listaFuncionariosView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ListaFuncionariosActivity.this, TelaFuncionarioActivity.class);
                pessoa = (Pessoa) listaFuncionariosView.getItemAtPosition(position);
                intent.putExtra("Pessoa", pessoa);
                startActivity(intent);
            }
        });
        setupSearchView();
        listaFuncionarios();

    }

    private void setupSearchView() {
        pesquisa.setQueryHint("Pesquise aqui");
        pesquisa.setSubmitButtonEnabled(false);
        pesquisa.setOnQueryTextListener(this);
        pesquisa.setIconifiedByDefault(false);
    }

    private void listaFuncionarios() {
        listaFuncionariosView = (ListView) findViewById(R.id.funcionariosListView);
        Cursor cursor;
        String sortOrder = HerdsmanContract.PessoaEntry.COLUMN_NAME_IDPESSOA;
        HerdsmanDbHelper mDbHelper = new HerdsmanDbHelper(this);
        SQLiteDatabase mDb = mDbHelper.getReadableDatabase();
        String[] projection = {
                HerdsmanContract.PessoaEntry.COLUMN_NAME_IDPESSOA,
                HerdsmanContract.PessoaEntry.COLUMN_NAME_NOME,
                HerdsmanContract.PessoaEntry.COLUMN_NAME_CPF,
                HerdsmanContract.PessoaEntry.COLUMN_NAME_RG
        };
        String selection = HerdsmanContract.PessoaEntry.COLUMN_NAME_IDPESSOA + " != 1";
        cursor = mDb.query(
                HerdsmanContract.PessoaEntry.TABLE_NAME,
                projection,
                selection,
                null,
                null,
                null,
                sortOrder
        );

        List listPessoas = new ArrayList<Pessoa>();
        while (cursor.moveToNext())
        {
            pessoa = new Pessoa(
                    cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.PessoaEntry.COLUMN_NAME_IDPESSOA)),
                    cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.PessoaEntry.COLUMN_NAME_NOME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.PessoaEntry.COLUMN_NAME_CPF)),
                    cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.PessoaEntry.COLUMN_NAME_RG))
            );
            listPessoas.add(pessoa);
        }
        cursor.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listPessoas);
        listaFuncionariosView.setAdapter(adapter);
        mDb.close();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        listaFuncionarios();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) listaFuncionariosView.getAdapter();
        Filter filter = adapter.getFilter();
        if (TextUtils.isEmpty(newText))
        {
            filter.filter("");
        }
        else {
            filter.filter(newText.toString());
        }
        return true;
    }
}
