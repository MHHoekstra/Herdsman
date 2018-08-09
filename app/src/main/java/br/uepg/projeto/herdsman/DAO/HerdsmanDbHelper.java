package br.uepg.projeto.herdsman.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.uepg.projeto.herdsman.Objetos.Animal;
import br.uepg.projeto.herdsman.Objetos.Pessoa;


/**
 * {@link SQLiteOpenHelper} implementation that creates and upgrades the
 * application database.
 */
public class HerdsmanDbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "mydb.db";
    private static final int DB_VERSION = 17;
    private static final String TAG = "DatabaseHelper";

    private Context mContext;

    public HerdsmanDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Database creation scripts should be placed in assets/sql
        String createDbFile = "sql/db_create_v" + DB_VERSION + ".sql";
        Log.v(TAG, "Creating new database from " + createDbFile + ".");
        AssetManager am = mContext.getAssets();
        StringBuilder createStatement = new StringBuilder();
        long startTime = System.currentTimeMillis();
        db.beginTransaction();
        try {
            InputStream is = am.open(createDbFile);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            /* Cache the file line by line, when the line ends with a
             * semi-colon followed by a line break (end of a SQL command),
             * execute it against the database and move on. */
            while ((line = br.readLine()) != null) {
                String lineTrimmed = line.trim();
                if (lineTrimmed.length() == 0)
                    continue;
                createStatement.append(line).append("\r\n");
                if (lineTrimmed.endsWith(";")) {
                    Log.d(TAG, "Executing SQL: \r\n" + createStatement.toString());
                    db.execSQL(createStatement.toString());
                    createStatement.setLength(0);
                }
            }
            br.close();
        } catch (IOException e) {
            Log.e(TAG, "IOException thrown while attempting to "
                    + "create database from " + createDbFile + ".");
            return;
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        Log.i(TAG, "New database created from script "
                + createDbFile + " in " +
                (System.currentTimeMillis() - startTime) +"ms.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int from, int to) {
        Log.v(TAG, "Delete database");
        Log.i(TAG, "Database Version: OLD: "+ from + " = NEW: "+ to);

        if(mContext.deleteDatabase(DB_NAME))
            Log.i(TAG, "Database Deleted....");
        else
            Log.i(TAG, "Database Not Deleted..");
        onCreate(db);
    }

    public List<Animal> carregarAnimaisDb()
    {
        Cursor cursor;
        String sortOrder =
                HerdsmanContract.AnimalEntry.COLUMN_NAME_NUMERO;

        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                HerdsmanContract.AnimalEntry.COLUMN_NAME_IDANIMAL,
                HerdsmanContract.AnimalEntry.COLUMN_NAME_NUMERO,
                HerdsmanContract.AnimalEntry.COLUMN_NAME_NOME,
                HerdsmanContract.AnimalEntry.COLUMN_NAME_ATIVO
        };
        cursor = db.query(
                HerdsmanContract.AnimalEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );
        List itemsNomes = new ArrayList<Animal>();
        while(cursor.moveToNext()){
            Animal animal = new Animal(cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalEntry.COLUMN_NAME_IDANIMAL)),cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalEntry.COLUMN_NAME_NUMERO)), cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalEntry.COLUMN_NAME_NOME)), cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalEntry.COLUMN_NAME_ATIVO)));
            itemsNomes.add(animal);
        }
        cursor.close();
        db.close();
        return itemsNomes;
    }

    public ArrayList carregarFuncionariosDb() {

        Cursor cursor;
        String sortOrder = HerdsmanContract.PessoaEntry.COLUMN_NAME_IDPESSOA;
        SQLiteDatabase mDb = this.getReadableDatabase();
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
            Pessoa pessoa = new Pessoa(
                    cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.PessoaEntry.COLUMN_NAME_IDPESSOA)),
                    cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.PessoaEntry.COLUMN_NAME_NOME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.PessoaEntry.COLUMN_NAME_CPF)),
                    cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.PessoaEntry.COLUMN_NAME_RG))
            );
            listPessoas.add(pessoa);
        }
        cursor.close();
        mDb.close();
        return (ArrayList) listPessoas;
    }
}