package br.uepg.projeto.herdsman;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import br.uepg.projeto.herdsman.DAO.HerdsmanContract;
import br.uepg.projeto.herdsman.DAO.HerdsmanDbHelper;
import br.uepg.projeto.herdsman.Objetos.Pessoa;
import br.uepg.projeto.herdsman.Objetos.Telefone;

public class TelaFuncionarioActivity extends AppCompatActivity {

    ListView listaTelefones;
    Pessoa pessoa;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_funcionario);
        pessoa = (Pessoa) getIntent().getSerializableExtra("Pessoa");
        TextView titulo = findViewById(R.id.tela_funcionario_titulo);
        TextView cpfText = findViewById(R.id.tela_funcionario_cpf);
        TextView rgText = findViewById(R.id.tela_funcionario_rg);
        listaTelefones = findViewById(R.id.tela_funcionario_lista_telefone);
        titulo.setText(pessoa.getNome());
        cpfText.setText(pessoa.getCpf());
        rgText.setText(pessoa.getRg());
        Button addTelefone = findViewById(R.id.tela_funcionario_add_telefone);
        // TODO Implementar tela de notificações geradas pelo funcionario
        Button notificacoesGeradas = findViewById(R.id.tela_funcionario_notificacoes);
        addTelefone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuild = new AlertDialog.Builder(TelaFuncionarioActivity.this);
                final EditText input = new EditText(TelaFuncionarioActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                alertDialogBuild.setView(input);
                alertDialogBuild.setTitle("Telefone");
                alertDialogBuild.setNegativeButton("Cancelar", null);
                alertDialogBuild.setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (input.getText().length() == 0)
                        {
                            return;
                        }
                        else
                        {
                            // FIXME: 14/08/18 Encapsular inserção de telefone
                            SQLiteOpenHelper mDbHelper = new HerdsmanDbHelper(TelaFuncionarioActivity.this);
                            SQLiteDatabase mDb = mDbHelper.getWritableDatabase();
                            ContentValues values = new ContentValues();
                            values.put(HerdsmanContract.TelefoneEntry.COLUMN_NAME_PESSOA_IDPESSOA, pessoa.getIdPessoa());
                            values.put(HerdsmanContract.TelefoneEntry.COLUMN_NAME_NUMERO, input.getText().toString());
                            mDb.insert(HerdsmanContract.TelefoneEntry.TABLE_NAME,null,values);
                            mDb.close();
                            listarTelefones();
                        }
                    }
                });
                AlertDialog alert = alertDialogBuild.create();
                alert.show();
            }
        });
        listaTelefones.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Telefone telefone = (Telefone) listaTelefones.getItemAtPosition(position);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TelaFuncionarioActivity.this);
                alertDialogBuilder.setTitle("Deletar número?");
                alertDialogBuilder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteOpenHelper mDbHelper = new HerdsmanDbHelper(TelaFuncionarioActivity.this);
                        // FIXME: Encapsular remoção de telefone
                        SQLiteDatabase mDb = mDbHelper.getWritableDatabase();
                        String where = HerdsmanContract.TelefoneEntry.COLUMN_NAME_IDTELEFONE + "== ?";
                        String[] whereArgs =
                                {
                                        String.valueOf(telefone.getIdTelefone())
                                };
                        mDb.delete(
                                HerdsmanContract.TelefoneEntry.TABLE_NAME,
                                where,
                                whereArgs);
                        mDb.close();
                        listarTelefones();
                    }

                });
                alertDialogBuilder.setNegativeButton("Não", null);
                AlertDialog alert = alertDialogBuilder.create();
                alert.show();
                return true;
            }
        });
        listarTelefones();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        listarTelefones();
    }

    private void listarTelefones()
    {

        HerdsmanDbHelper mDbHelper = new HerdsmanDbHelper(TelaFuncionarioActivity.this);
        ArrayList telefoneItemList = mDbHelper.listarTelefonesPessoa(pessoa);
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, telefoneItemList);
        listaTelefones.setAdapter(adapter);
    }
}
