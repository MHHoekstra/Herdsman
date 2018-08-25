package br.uepg.projeto.herdsman.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.uepg.projeto.herdsman.CadastroEnfermidadeActivity;
import br.uepg.projeto.herdsman.CadastroFuncionarioActivity;
import br.uepg.projeto.herdsman.Objetos.Animal;
import br.uepg.projeto.herdsman.Objetos.AnimalEnfermidade;
import br.uepg.projeto.herdsman.Objetos.Cio;
import br.uepg.projeto.herdsman.Objetos.Enfermidade;
import br.uepg.projeto.herdsman.Objetos.Parto;
import br.uepg.projeto.herdsman.Objetos.Pessoa;
import br.uepg.projeto.herdsman.Objetos.Remedio;
import br.uepg.projeto.herdsman.Objetos.Telefone;
import br.uepg.projeto.herdsman.Objetos.Usuario;


/**
 * {@link SQLiteOpenHelper} implementation that creates and upgrades the
 * application database.
 */
public class HerdsmanDbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "mydb.db";
    private static final int DB_VERSION = 17;
    private static final String TAG = "DatabaseHelper";
    private DatabaseReference FirebaseHelper;
    private Context mContext;
    private boolean isSync = false;

    public HerdsmanDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mContext = context;
        this.FirebaseHelper = FirebaseDatabase.getInstance().getReference("Hoekstra");
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

    public boolean isSync() {
        return isSync;
    }

    public void setSync(boolean sync) {
        isSync = sync;
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

    public ArrayList carregarPartosAnimal(Animal animal) {
        SQLiteDatabase mDb = this.getReadableDatabase();
        Cursor cursor;
        String selection = HerdsmanContract.PartoEntry.COLUMN_NAME_ANIMAL_IDANIMAL + "== ?";
        String[] selectionArgs = {
                String.valueOf(animal.getId())
        };
        cursor = mDb.query(
                HerdsmanContract.PartoEntry.TABLE_NAME,
                new String[]{HerdsmanContract.PartoEntry.COLUMN_NAME_DATA, HerdsmanContract.PartoEntry.COLUMN_NAME_CRIA, HerdsmanContract.PartoEntry.COLUMN_NAME_IDPARTO,HerdsmanContract.PartoEntry.COLUMN_NAME_ANIMAL_IDANIMAL},
                selection,
                selectionArgs,
                null,
                null,
                HerdsmanContract.PartoEntry.COLUMN_NAME_DATA + " DESC"

        );

        ArrayList lista = new ArrayList<Parto>();
        while(cursor.moveToNext())
        {   int id = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.PartoEntry.COLUMN_NAME_IDPARTO));
            int Animal_idAnimal = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.PartoEntry.COLUMN_NAME_ANIMAL_IDANIMAL));
            int cria = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.PartoEntry.COLUMN_NAME_CRIA));
            String data = cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.PartoEntry.COLUMN_NAME_DATA));
            Parto parto = new Parto(id, Animal_idAnimal, cria, data);
            lista.add(parto);
        }
        cursor.close();
        mDb.close();
        return lista;
    }

    public ArrayList carregarTodosCiosDatabase() {
        SQLiteDatabase mDb = this.getReadableDatabase();
        Cursor cursor = mDb.query(
                HerdsmanContract.CioEntry.TABLE_NAME,
                new String[]{
                        HerdsmanContract.CioEntry.COLUMN_NAME_ANIMAL_IDANIMALPORBAIXO,
                        HerdsmanContract.CioEntry.COLUMN_NAME_ANIMAL_IDANIMALPORCIMA,
                        HerdsmanContract.CioEntry.COLUMN_NAME_IDANIMAL_CIO,
                        HerdsmanContract.CioEntry.COLUMN_NAME_DATA
                },
                null,
                null,
                null,
                null,
                HerdsmanContract.CioEntry.COLUMN_NAME_DATA + " DESC"
        );
        ArrayList listaCios = new ArrayList<Cio>();
        while(cursor.moveToNext())
        {
            int idAnimalPorBaixo = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.CioEntry.COLUMN_NAME_ANIMAL_IDANIMALPORBAIXO));
            int idAnimalPorCima = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.CioEntry.COLUMN_NAME_ANIMAL_IDANIMALPORCIMA));
            int idAnimalCio = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.CioEntry.COLUMN_NAME_IDANIMAL_CIO));
            String data = cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.CioEntry.COLUMN_NAME_DATA));
            Cio cio = new Cio(idAnimalCio,idAnimalPorCima,idAnimalPorBaixo,data);
            Cursor cursor2 = mDb.query(
                    HerdsmanContract.AnimalEntry.TABLE_NAME,
                    new String[] {HerdsmanContract.AnimalEntry.COLUMN_NAME_IDANIMAL,
                            HerdsmanContract.AnimalEntry.COLUMN_NAME_NUMERO,
                            HerdsmanContract.AnimalEntry.COLUMN_NAME_NOME,
                            HerdsmanContract.AnimalEntry.COLUMN_NAME_ATIVO},
                    HerdsmanContract.AnimalEntry.COLUMN_NAME_IDANIMAL + " == ?",
                    new String[] {String.valueOf(idAnimalPorBaixo)},
                    null,
                    null,
                    HerdsmanContract.CioEntry.COLUMN_NAME_DATA + " DESC"
            );
            if (cursor2.moveToNext()) {
                int idAnimal = cursor2.getInt(cursor2.getColumnIndexOrThrow(HerdsmanContract.AnimalEntry.COLUMN_NAME_IDANIMAL));
                String numero = cursor2.getString(cursor2.getColumnIndexOrThrow(HerdsmanContract.AnimalEntry.COLUMN_NAME_NUMERO));
                String nome = cursor2.getString(cursor2.getColumnIndexOrThrow(HerdsmanContract.AnimalEntry.COLUMN_NAME_NOME));
                int ativo = cursor2.getInt(cursor2.getColumnIndexOrThrow(HerdsmanContract.AnimalEntry.COLUMN_NAME_ATIVO));
                Animal animal = new Animal(idAnimal, numero, nome, ativo);
                cio.setAnimalPorBaixo(animal);
            }
            cursor2.close();

            cursor2 = mDb.query(
                    HerdsmanContract.AnimalEntry.TABLE_NAME,
                    new String[] {HerdsmanContract.AnimalEntry.COLUMN_NAME_IDANIMAL,
                            HerdsmanContract.AnimalEntry.COLUMN_NAME_NUMERO,
                            HerdsmanContract.AnimalEntry.COLUMN_NAME_NOME,
                            HerdsmanContract.AnimalEntry.COLUMN_NAME_ATIVO},
                    HerdsmanContract.AnimalEntry.COLUMN_NAME_IDANIMAL + " == ?",
                    new String[] {String.valueOf(idAnimalPorCima)},
                    null,
                    null,
                    null
            );
            if (cursor2.moveToNext()) {
                int idAnimal = cursor2.getInt(cursor2.getColumnIndexOrThrow(HerdsmanContract.AnimalEntry.COLUMN_NAME_IDANIMAL));
                String numero = cursor2.getString(cursor2.getColumnIndexOrThrow(HerdsmanContract.AnimalEntry.COLUMN_NAME_NUMERO));
                String nome = cursor2.getString(cursor2.getColumnIndexOrThrow(HerdsmanContract.AnimalEntry.COLUMN_NAME_NOME));
                int ativo = cursor2.getInt(cursor2.getColumnIndexOrThrow(HerdsmanContract.AnimalEntry.COLUMN_NAME_ATIVO));
                Animal animal = new Animal(idAnimal, numero, nome, ativo);
                cio.setAnimalPorCima(animal);
            }
            cursor2.close();
            listaCios.add(cio);
        }
        cursor.close();
        mDb.close();
        return listaCios;
    }

    public Usuario carregarAdminDatabase()
    {
        SQLiteDatabase mDb = this.getWritableDatabase();
        Cursor cursor;
        Usuario adminUsuario = null;
        String[] projection =
                {
                        HerdsmanContract.UsuarioEntry.COLUMN_NAME_IDUSUARIO,
                        HerdsmanContract.UsuarioEntry.COLUMN_NAME_LOGIN,
                        HerdsmanContract.UsuarioEntry.COLUMN_NAME_SENHA,
                        HerdsmanContract.UsuarioEntry.COLUMN_NAME_PESSOA_IDPESSOA,
                        HerdsmanContract.UsuarioEntry.COLUMN_NAME_ADMIN
                };
        String selection = HerdsmanContract.UsuarioEntry.COLUMN_NAME_ADMIN + "== 1";
        cursor = mDb.query(
                HerdsmanContract.UsuarioEntry.TABLE_NAME,
                projection,
                selection,
                null,
                null,
                null,
                null
        );
        while(cursor.moveToNext())
        {
            String login = cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.UsuarioEntry.COLUMN_NAME_LOGIN));
            String senha = cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.UsuarioEntry.COLUMN_NAME_SENHA));
            int idPessoa = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.UsuarioEntry.COLUMN_NAME_PESSOA_IDPESSOA));
            int admin = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.UsuarioEntry.COLUMN_NAME_ADMIN));
            int idUsuario = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.UsuarioEntry.COLUMN_NAME_IDUSUARIO));
            adminUsuario = new Usuario(admin, login, senha, idPessoa, idUsuario);
        }
        cursor.close();
        mDb.close();
        return adminUsuario;
    }
    public long inserirAnimal(Animal animal)
    {

        DatabaseReference databaseAnimal = FirebaseHelper.child("Animal");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HerdsmanContract.AnimalEntry.COLUMN_NAME_NUMERO, animal.getNumero());
        values.put(HerdsmanContract.AnimalEntry.COLUMN_NAME_NOME, animal.getNome());
        values.put(HerdsmanContract.AnimalEntry.COLUMN_NAME_ATIVO, animal.getAtivo());
        if (this.isSync()) {
            values.put(HerdsmanContract.AnimalEntry.COLUMN_NAME_IDANIMAL, animal.getId());
        }
        long id = db.insert(HerdsmanContract.AnimalEntry.TABLE_NAME, null, values);
        if(id != -1)
        {
            animal.setId((int)id);
            databaseAnimal.child(String.valueOf(id)).setValue(animal);
            databaseAnimal.keepSynced(true);
        }
        db.close();
        return id;
    }
    public long replaceAnimal(Animal animal)
    {
        DatabaseReference databaseAnimal = FirebaseHelper.child("Animal");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HerdsmanContract.AnimalEntry.COLUMN_NAME_IDANIMAL, animal.getId());
        values.put(HerdsmanContract.AnimalEntry.COLUMN_NAME_NUMERO, animal.getNumero());
        values.put(HerdsmanContract.AnimalEntry.COLUMN_NAME_NOME, animal.getNome());
        values.put(HerdsmanContract.AnimalEntry.COLUMN_NAME_ATIVO, animal.getAtivo());
        long id = db.replace(HerdsmanContract.AnimalEntry.TABLE_NAME, null, values);
        if(id != -1)
        {
            databaseAnimal.child(String.valueOf(id)).setValue(animal);
            databaseAnimal.keepSynced(true);
        }
        db.close();
        return id;
    }
    public ArrayList carregarTodasEnfermidadesDatabase()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] projection =
                {
                        HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_ANIMAL_IDANIMAL,
                        HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_ENFERMIDADE_IDENFERMIDADE,
                        HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_DATA,
                        HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_USUARIO_IDUSUARIO,
                        HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_IDANIMAL_ENFERMIDADE
                };
        Cursor cursor = db.query(
                HerdsmanContract.AnimalEnfermidadeEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_DATA+ " DESC"
        );
        ArrayList list = new ArrayList<AnimalEnfermidade>();
        while(cursor.moveToNext())
        {
            int idAnimalEnfermidade = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_IDANIMAL_ENFERMIDADE));
            int Animal_idAnimal = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_ANIMAL_IDANIMAL));
            int Enfermidade_idEnfermidade = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_ENFERMIDADE_IDENFERMIDADE));
            int Usuario_idUsuario = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_USUARIO_IDUSUARIO));
            String data = cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_DATA));
            AnimalEnfermidade animalEnfermidade = new AnimalEnfermidade(idAnimalEnfermidade, Animal_idAnimal, Enfermidade_idEnfermidade, data, Usuario_idUsuario);
            list.add(animalEnfermidade);
        }
        cursor.close();
        db.close();
        return list;
    }

    public long inserirEnfermidade(Enfermidade enfermidade)
    {
        DatabaseReference databaseEnfermidade = FirebaseHelper.child("Enfermidade");
        SQLiteDatabase mDb = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HerdsmanContract.EnfermidadeEntry.COLUMN_NAME_DESCRICAO, enfermidade.getDescricao());
        if (this.isSync()) {
            values.put(HerdsmanContract.EnfermidadeEntry.COLUMN_NAME_IDENFERMIDADE, enfermidade.getId());
        }
        long id = mDb.insert(HerdsmanContract.EnfermidadeEntry.TABLE_NAME, null, values);
        if(id != -1)
        {
            enfermidade.setId((int)id);
            databaseEnfermidade.child(String.valueOf(id)).setValue(enfermidade);
            databaseEnfermidade.keepSynced(true);
        }
        mDb.close();
        return id;
    }

    public long replaceFuncionario(Pessoa pessoa)
    {
        SQLiteDatabase mDb = this.getWritableDatabase();
        ContentValues valuesP = new ContentValues();
        valuesP.put(HerdsmanContract.PessoaEntry.COLUMN_NAME_IDPESSOA, pessoa.getIdPessoa());
        valuesP.put(HerdsmanContract.PessoaEntry.COLUMN_NAME_NOME, pessoa.getNome());
        valuesP.put(HerdsmanContract.PessoaEntry.COLUMN_NAME_CPF, pessoa.getCpf());
        valuesP.put(HerdsmanContract.PessoaEntry.COLUMN_NAME_RG, pessoa.getRg());
        valuesP.put(HerdsmanContract.PessoaEntry.COLUMN_NAME_ATIVO, pessoa.getAtivo());
        long newRowId = mDb.replace(HerdsmanContract.PessoaEntry.TABLE_NAME, null, valuesP);
        mDb.close();
        return  newRowId;

    }

    public long inserirFuncionario(Pessoa pessoa)
    {
        DatabaseReference databaseFuncionario = FirebaseHelper.child("Funcionario");
        SQLiteDatabase mDb = this.getWritableDatabase();

        ContentValues valuesP = new ContentValues();
        valuesP.put(HerdsmanContract.PessoaEntry.COLUMN_NAME_NOME, pessoa.getNome());
        valuesP.put(HerdsmanContract.PessoaEntry.COLUMN_NAME_CPF, pessoa.getCpf());
        valuesP.put(HerdsmanContract.PessoaEntry.COLUMN_NAME_RG, pessoa.getRg());
        valuesP.put(HerdsmanContract.PessoaEntry.COLUMN_NAME_ATIVO, "1");
        if (this.isSync()) {
            valuesP.put(HerdsmanContract.PessoaEntry.COLUMN_NAME_IDPESSOA, pessoa.getIdPessoa());
        }
        long id = mDb.insert(HerdsmanContract.PessoaEntry.TABLE_NAME, null, valuesP);
        if(id != -1)
        {
            pessoa.setIdPessoa((int)id);
            databaseFuncionario.child(String.valueOf(id)).setValue(pessoa);
            databaseFuncionario.keepSynced(true);
        }
        mDb.close();
        return id;

    }

    public long inserirParto(Parto parto)
    {
        DatabaseReference databaseParto = FirebaseHelper.child("Parto");
        SQLiteDatabase mDb = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HerdsmanContract.PartoEntry.COLUMN_NAME_ANIMAL_IDANIMAL, parto.getAnimal_idAnimal());
        values.put(HerdsmanContract.PartoEntry.COLUMN_NAME_DATA, String.valueOf(parto.getData()));
        values.put(HerdsmanContract.PartoEntry.COLUMN_NAME_CRIA, parto.getCria());
        if (this.isSync()) {
            values.put(HerdsmanContract.PartoEntry.COLUMN_NAME_IDPARTO, parto.getId());
        }
        long id = mDb.insert(HerdsmanContract.PartoEntry.TABLE_NAME,null, values);
        if(id != -1)
        {
            parto.setId((int)id);
            databaseParto.child(String.valueOf(id)).setValue(parto);
            databaseParto.keepSynced(true);
        }
        mDb.close();
        return id;

    }

    public long inserirRemedio(Remedio remedio) {
        DatabaseReference databaseRemedio = FirebaseHelper.child("Remedio");
        SQLiteDatabase mDb = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HerdsmanContract.RemedioEntry.COLUMN_NAME_NOME, remedio.getDescricao());
        if (this.isSync()) {
            values.put(HerdsmanContract.RemedioEntry.COLUMN_NAME_IDREMEDIO, remedio.getIdRemedio());
        }
        long id = mDb.insert(HerdsmanContract.RemedioEntry.TABLE_NAME,null, values);
        if(id != -1)
        {
            remedio.setIdRemedio((int)id);
            databaseRemedio.child(String.valueOf(id)).setValue(remedio);
            databaseRemedio.keepSynced(true);
        }
        mDb.close();
        return id;

    }

    public ArrayList listarTelefonesPessoa(Pessoa pessoa)
    {
        SQLiteDatabase mDb = this.getReadableDatabase();
        Cursor cursor;
        String[] projection =
                {
                        HerdsmanContract.TelefoneEntry.COLUMN_NAME_IDTELEFONE,
                        HerdsmanContract.TelefoneEntry.COLUMN_NAME_PESSOA_IDPESSOA,
                        HerdsmanContract.TelefoneEntry.COLUMN_NAME_NUMERO
                };
        String selection = HerdsmanContract.TelefoneEntry.COLUMN_NAME_PESSOA_IDPESSOA + " == ?";
        String[] selectionArgs =
                {
                        String.valueOf(pessoa.getIdPessoa())
                };
        cursor = mDb.query(
                HerdsmanContract.TelefoneEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        ArrayList telefoneItemList = new ArrayList<Telefone>();
        while(cursor.moveToNext())
        {
            int idTelefone = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.TelefoneEntry.COLUMN_NAME_IDTELEFONE));
            int Pessoa_idPessoa = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.TelefoneEntry.COLUMN_NAME_PESSOA_IDPESSOA));
            String numero = cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.TelefoneEntry.COLUMN_NAME_NUMERO));
            Telefone tel = new Telefone(idTelefone,Pessoa_idPessoa,numero);
            telefoneItemList.add(tel);
        }
        cursor.close();
        mDb.close();
        return telefoneItemList;

    }

}