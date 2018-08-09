package br.uepg.projeto.herdsman;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.uepg.projeto.herdsman.DAO.HerdsmanContract;
import br.uepg.projeto.herdsman.DAO.HerdsmanDbHelper;
import br.uepg.projeto.herdsman.Objetos.Animal;
import br.uepg.projeto.herdsman.Objetos.Parto;
import br.uepg.projeto.herdsman.Objetos.Telefone;

public class ListaPartosActivity extends AppCompatActivity {
    ListView listaPartos;
    Animal animal;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_partos);
        animal = (Animal) getIntent().getSerializableExtra("Animal");
        TextView titulo = (TextView) findViewById(R.id.lista_partos_titulo);
        titulo.setText("Partos " + animal.getNumero());
        listaPartos = (ListView) findViewById(R.id.lista_partos_listview);

        FloatingActionButton add = (FloatingActionButton) findViewById(R.id.lista_partos_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListaPartosActivity.this, CadastroPartoActivity.class);
                intent.putExtra("Animal", animal);
                startActivity(intent);
            }
        });
        listarPartos();
        listaPartos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Parto parto = (Parto) listaPartos.getItemAtPosition(position);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ListaPartosActivity.this);
                alertDialogBuilder.setTitle("Deletar número?");
                alertDialogBuilder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteOpenHelper mDbHelper = new HerdsmanDbHelper(ListaPartosActivity.this);
                        SQLiteDatabase mDb = mDbHelper.getWritableDatabase();
                        String where = HerdsmanContract.PartoEntry.COLUMN_NAME_IDPARTO + "== ?";
                        String[] whereArgs =
                                {
                                        String.valueOf(parto.getId())
                                };
                        mDb.delete(
                                HerdsmanContract.PartoEntry.TABLE_NAME,
                                where,
                                whereArgs);
                        mDb.close();
                        listarPartos();
                    }

                });
                alertDialogBuilder.setNegativeButton("Não", null);
                AlertDialog alert = alertDialogBuilder.create();
                alert.show();
                return true;
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        listarPartos();
    }

    private void listarPartos()
    {
        HerdsmanDbHelper mDbHelper = new HerdsmanDbHelper(ListaPartosActivity.this);
        ArrayList listaPartosmDbHelper = mDbHelper.carregarPartosAnimal(animal);
        ArrayAdapter<String> adapter = new ArrayAdapter(ListaPartosActivity.this, android.R.layout.simple_list_item_1, (List) listaPartos);
        listaPartos.setAdapter(adapter);
    }

}
