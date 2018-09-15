package br.uepg.projeto.herdsman.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import br.uepg.projeto.herdsman.Objetos.Animal;
import br.uepg.projeto.herdsman.Objetos.AnimalEnfermidade;
import br.uepg.projeto.herdsman.Objetos.AnimalRemedio;
import br.uepg.projeto.herdsman.Objetos.Cio;
import br.uepg.projeto.herdsman.Objetos.Enfermidade;
import br.uepg.projeto.herdsman.Objetos.Inseminacao;
import br.uepg.projeto.herdsman.Objetos.Medida;
import br.uepg.projeto.herdsman.Objetos.AdministradorNotificaPessoa;
import br.uepg.projeto.herdsman.Objetos.Parto;
import br.uepg.projeto.herdsman.Objetos.Pessoa;
import br.uepg.projeto.herdsman.Objetos.Remedio;
import br.uepg.projeto.herdsman.Objetos.Sinistro;
import br.uepg.projeto.herdsman.Objetos.Telefone;
import br.uepg.projeto.herdsman.Objetos.Administrador;

import static br.uepg.projeto.herdsman.DAO.HerdsmanContract.RemedioEntry.TABLE_NAME;

/**
 * {@link SQLiteOpenHelper} implementation that creates and upgrades the
 * application database.
 */
public class HerdsmanDbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "mydb.db";
    private static final int DB_VERSION = 21;
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

    public List<Animal> carregarAnimaisDb(){
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

    public ArrayList carregarFuncionariosDb(){

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

    public ArrayList carregarTodosCios() {
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
                    null
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

    public ArrayList carregarCiosAnimal(Animal animal)
    {
        SQLiteDatabase mDb = this.getReadableDatabase();
        Cursor cursor;
        String selection = HerdsmanContract.CioEntry.COLUMN_NAME_ANIMAL_IDANIMALPORBAIXO + "== ? OR " + HerdsmanContract.CioEntry.COLUMN_NAME_ANIMAL_IDANIMALPORCIMA + " == ?";
        String[] selectionArgs = {String.valueOf(animal.getId()), String.valueOf(animal.getId())};
        cursor = mDb.query(
                HerdsmanContract.CioEntry.TABLE_NAME,
                new String[]{HerdsmanContract.CioEntry.COLUMN_NAME_DATA, HerdsmanContract.CioEntry.COLUMN_NAME_ANIMAL_IDANIMALPORBAIXO, HerdsmanContract.CioEntry.COLUMN_NAME_ANIMAL_IDANIMALPORCIMA,HerdsmanContract.CioEntry.COLUMN_NAME_IDANIMAL_CIO,HerdsmanContract.CioEntry.COLUMN_NAME_PESSOA_IDPESSOA},
                selection,
                selectionArgs,
                null,
                null,
                HerdsmanContract.CioEntry.COLUMN_NAME_DATA + " DESC"
        );
        ArrayList lista = new ArrayList<Cio>();
        while(cursor.moveToNext())
        {   int idCio = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.CioEntry.COLUMN_NAME_IDANIMAL_CIO));
            int idAnimalPorCima = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.CioEntry.COLUMN_NAME_ANIMAL_IDANIMALPORCIMA));
            int idAnimalPorBaixo = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.CioEntry.COLUMN_NAME_ANIMAL_IDANIMALPORBAIXO));
            int usuario = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.CioEntry.COLUMN_NAME_PESSOA_IDPESSOA));
            String data = cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.CioEntry.COLUMN_NAME_DATA));
            Cio cio = new Cio(idCio,idAnimalPorCima,idAnimalPorBaixo, data,usuario) ;
            Log.d("CarregarCiosAnimal: ", String.valueOf(idAnimalPorBaixo));
            Log.d("CarregarCiosAnimal: ", String.valueOf(idAnimalPorCima));
            Animal animalPorBaixo = this.carregarAnimal(idAnimalPorBaixo);
            Animal animalPorCima = this.carregarAnimal(idAnimalPorCima);
            cio.setAnimalPorBaixo(animalPorBaixo);
            cio.setAnimalPorCima(animalPorCima);
            lista.add(cio);
        }
        cursor.close();
        mDb.close();
        return lista;
    }
    public Administrador carregarAdminDatabase()
    {
        SQLiteDatabase mDb = this.getWritableDatabase();
        Cursor cursor;
        Administrador adminAdministrador = null;
        String[] projection =
                {
                        HerdsmanContract.AdministradorEntry.COLUMN_NAME_IDADMINISTRADOR,
                        HerdsmanContract.AdministradorEntry.COLUMN_NAME_LOGIN,
                        HerdsmanContract.AdministradorEntry.COLUMN_NAME_SENHA,
                        HerdsmanContract.AdministradorEntry.COLUMN_NAME_PESSOA_IDPESSOA,
                        HerdsmanContract.AdministradorEntry.COLUMN_NAME_ADMIN
                };
        String selection = HerdsmanContract.AdministradorEntry.COLUMN_NAME_ADMIN + "== 1";
        cursor = mDb.query(
                HerdsmanContract.AdministradorEntry.TABLE_NAME,
                projection,
                selection,
                null,
                null,
                null,
                null
        );
        while(cursor.moveToNext())
        {
            String login = cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.AdministradorEntry.COLUMN_NAME_LOGIN));
            String senha = cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.AdministradorEntry.COLUMN_NAME_SENHA));
            int idPessoa = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.AdministradorEntry.COLUMN_NAME_PESSOA_IDPESSOA));
            int admin = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.AdministradorEntry.COLUMN_NAME_ADMIN));
            int idUsuario = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.AdministradorEntry.COLUMN_NAME_IDADMINISTRADOR));
            adminAdministrador = new Administrador(admin, login, senha, idPessoa, idUsuario);
        }
        cursor.close();
        mDb.close();
        return adminAdministrador;
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
    public ArrayList carregarTodosSinistros()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] projection =
                {
                        HerdsmanContract.SinistroEntry.COLUMN_NAME_ANIMAL_IDANIMAL,
                        HerdsmanContract.SinistroEntry.COLUMN_NAME_ENFERMIDADE_IDENFERMIDADE,
                        HerdsmanContract.SinistroEntry.COLUMN_NAME_DATA,
                        HerdsmanContract.SinistroEntry.COLUMN_NAME_PESSOA_IDOPESSOA,
                        HerdsmanContract.SinistroEntry.COLUMN_NAME_IDANIMAL_ENFERMIDADE
                };
        Cursor cursor = db.query(
                HerdsmanContract.SinistroEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                HerdsmanContract.SinistroEntry.COLUMN_NAME_DATA+ " DESC"
        );
        Cursor cursor_animal;
        Cursor cursor_enfermidade;
        ArrayList list = new ArrayList<AnimalEnfermidade>();
        while(cursor.moveToNext())
        {
            int idAnimalEnfermidade = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.SinistroEntry.COLUMN_NAME_IDANIMAL_ENFERMIDADE));
            int Animal_idAnimal = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.SinistroEntry.COLUMN_NAME_ANIMAL_IDANIMAL));
            int Enfermidade_idEnfermidade = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.SinistroEntry.COLUMN_NAME_ENFERMIDADE_IDENFERMIDADE));
            int Usuario_idUsuario = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.SinistroEntry.COLUMN_NAME_PESSOA_IDOPESSOA));
            String data = cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.SinistroEntry.COLUMN_NAME_DATA));
            Sinistro animalEnfermidade = new Sinistro(idAnimalEnfermidade, Animal_idAnimal, Enfermidade_idEnfermidade, Usuario_idUsuario, data);
            Log.d("Carregar Sinistros: ", String.valueOf(Animal_idAnimal));
            cursor_animal = db.query(
                    HerdsmanContract.AnimalEntry.TABLE_NAME,
                    new String [] {HerdsmanContract.AnimalEntry.COLUMN_NAME_NOME,
                            HerdsmanContract.AnimalEntry.COLUMN_NAME_IDANIMAL,
                            HerdsmanContract.AnimalEntry.COLUMN_NAME_NUMERO,
                            HerdsmanContract.AnimalEntry.COLUMN_NAME_ATIVO},
            HerdsmanContract.AnimalEntry.COLUMN_NAME_IDANIMAL + " == ?",
                    new String [] {String.valueOf(Animal_idAnimal)},
                    null,
                    null,
                    null
            );
            if(cursor_animal.moveToNext()) {
                String numero = cursor_animal.getString(cursor_animal.getColumnIndexOrThrow(HerdsmanContract.AnimalEntry.COLUMN_NAME_NUMERO));
                int idAnimal = cursor_animal.getInt(cursor_animal.getColumnIndexOrThrow(HerdsmanContract.AnimalEntry.COLUMN_NAME_IDANIMAL));
                String nome = cursor_animal.getString(cursor_animal.getColumnIndexOrThrow(HerdsmanContract.AnimalEntry.COLUMN_NAME_NOME));
                Animal animal = new Animal(idAnimal, numero, nome);
                Log.d("Carregar Sinistros: ", animal.getNome());
                Log.d("Carregar Sinistros: ", animal.getNumero());
                animalEnfermidade.setAnimal(animal);
            }
            cursor_animal.close();
            cursor_enfermidade = db.query(
                    HerdsmanContract.EnfermidadeEntry.TABLE_NAME,
                    new String[] {HerdsmanContract.EnfermidadeEntry.COLUMN_NAME_DESCRICAO, HerdsmanContract.EnfermidadeEntry.COLUMN_NAME_IDENFERMIDADE},
                    HerdsmanContract.EnfermidadeEntry.COLUMN_NAME_IDENFERMIDADE + " == ?",
                    new String[] {String.valueOf(Enfermidade_idEnfermidade)},
                    null,
                    null,
                    null
            );
            if(cursor_enfermidade.moveToNext()) {
                int idEnfermidade = cursor_enfermidade.getInt(cursor_enfermidade.getColumnIndexOrThrow(HerdsmanContract.EnfermidadeEntry.COLUMN_NAME_IDENFERMIDADE));
                String desc = cursor_enfermidade.getString(cursor_enfermidade.getColumnIndexOrThrow(HerdsmanContract.EnfermidadeEntry.COLUMN_NAME_DESCRICAO));
                Enfermidade enfermidade = new Enfermidade(idEnfermidade, desc);
                animalEnfermidade.setEnfermidade(enfermidade);
            }
            cursor_enfermidade.close();
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
        values.put(HerdsmanContract.RemedioEntry.COLUMN_NAME_NOME, remedio.getNome());
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

    public ArrayList carregarTelefonesPessoa(Pessoa pessoa)
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
        ArrayList telefoneItemList = new ArrayList();
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

    public boolean existeAnimal(int idAnimal) {
        SQLiteDatabase mDb = this.getReadableDatabase();
        Cursor cursor = mDb.query(
                HerdsmanContract.AnimalEntry.TABLE_NAME,
                new String[]{HerdsmanContract.AnimalEntry.COLUMN_NAME_IDANIMAL},
                HerdsmanContract.AnimalEntry.COLUMN_NAME_IDANIMAL + "== ? and " + HerdsmanContract.AnimalEntry.COLUMN_NAME_ATIVO + " == 1",
                new String[] {String.valueOf(idAnimal)},
                null,
                null,
                null
        );
        boolean ret;
        if (cursor.getCount() > 0)
        {
            ret = true;
        }
        else
        {
            ret = false;
        }
        cursor.close();
        mDb.close();
        return ret;
    }

    public long inserirCio(Cio cio) {
        //TODO Inserir no Firebase CIO FEITO
        DatabaseReference databaseCio = FirebaseHelper.child("Cio");
        ContentValues values = new ContentValues();
        values.put(HerdsmanContract.CioEntry.COLUMN_NAME_ANIMAL_IDANIMALPORCIMA, cio.getIdAnimalPorCima());
        values.put(HerdsmanContract.CioEntry.COLUMN_NAME_ANIMAL_IDANIMALPORBAIXO, cio.getIdAnimalPorBaixo());
        values.put(HerdsmanContract.CioEntry.COLUMN_NAME_DATA, cio.getData());
        values.put(HerdsmanContract.CioEntry.COLUMN_NAME_PESSOA_IDPESSOA, cio.getIdFuncionario());
        SQLiteDatabase mDb = this.getWritableDatabase();
        if (this.isSync()) {
            values.put(HerdsmanContract.CioEntry.COLUMN_NAME_IDANIMAL_CIO, cio.getIdCio());
        }
        long id =  mDb.insert(
                HerdsmanContract.CioEntry.TABLE_NAME,
                null,
                values

        );
        if(id != -1)
        {
            cio.setIdCio((int)id);
            databaseCio.child(String.valueOf(id)).setValue(cio);
            databaseCio.keepSynced(true);
        }
        mDb.close();
        return id;
    }

    public long inserirSinistro(Sinistro sinistro) {
        //TODO Inserir no Firebase SINISTRO FEITO
        DatabaseReference databaseSinistro = FirebaseHelper.child("Sinistro");
        ContentValues values = new ContentValues();
        values.put(HerdsmanContract.SinistroEntry.COLUMN_NAME_ANIMAL_IDANIMAL, sinistro.getIdAnimal());
        values.put(HerdsmanContract.SinistroEntry.COLUMN_NAME_ENFERMIDADE_IDENFERMIDADE, sinistro.getIdEnfermidade());
        values.put(HerdsmanContract.SinistroEntry.COLUMN_NAME_DATA, sinistro.getData());
        values.put(HerdsmanContract.SinistroEntry.COLUMN_NAME_PESSOA_IDOPESSOA, sinistro.getIdFuncionario());
        SQLiteDatabase mDb = this.getWritableDatabase();
        if (this.isSync()) {
            values.put(HerdsmanContract.SinistroEntry.COLUMN_NAME_IDANIMAL_ENFERMIDADE, sinistro.getIdSinistro());
        }
        long id = mDb.insert(HerdsmanContract.SinistroEntry.TABLE_NAME,
                null,
                values
        );
        if(id != -1)
        {
            sinistro.setIdSinistro((int)id);
            databaseSinistro.child(String.valueOf(id)).setValue(sinistro);
            databaseSinistro.keepSynced(true);
        }
        mDb.close();
        return id;
    }

    public boolean existeEnfermidade(int idEnfermidade) {
        SQLiteDatabase mDb = this.getReadableDatabase();
        Cursor cursor = mDb.query(
                HerdsmanContract.EnfermidadeEntry.TABLE_NAME,
                new String[]{HerdsmanContract.EnfermidadeEntry.COLUMN_NAME_IDENFERMIDADE},
                HerdsmanContract.EnfermidadeEntry.COLUMN_NAME_IDENFERMIDADE + "== ?",
                new String[] {String.valueOf(idEnfermidade)},
                null,
                null,
                null
        );
        boolean ret;
        if (cursor.getCount() > 0)
        {
            ret = true;
        }
        else
        {
            ret = false;
        }
        cursor.close();
        mDb.close();
        return ret;
    }

    public ArrayList carregarAnimaisAtivos() {
        SQLiteDatabase mDb = this.getReadableDatabase();
        Cursor cursor;
        ArrayList listaAnimais = new ArrayList();
        String[] projection =
                {
                        HerdsmanContract.AnimalEntry.COLUMN_NAME_NUMERO,
                        HerdsmanContract.AnimalEntry.COLUMN_NAME_IDANIMAL,
                        HerdsmanContract.AnimalEntry.COLUMN_NAME_NOME
                };
        String selection = HerdsmanContract.AnimalEntry.COLUMN_NAME_ATIVO +  " == 1";
        cursor = mDb.query(
                HerdsmanContract.AnimalEntry.TABLE_NAME,
                projection,
                selection,
                null,
                null,
                null,
                HerdsmanContract.AnimalEntry.COLUMN_NAME_NUMERO
        );
        while (cursor.moveToNext())
        {
            String numero = cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalEntry.COLUMN_NAME_NUMERO));
            int idAnimal = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalEntry.COLUMN_NAME_IDANIMAL));
            String nome = cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalEntry.COLUMN_NAME_NOME));
            Animal animal = new Animal(idAnimal, numero, nome);
            listaAnimais.add(animal);
        }
        cursor.close();
        mDb.close();
        return listaAnimais;
    }

    public Telefone carregarTelefoneAdmin()
    {
        SQLiteDatabase mDb = this.getReadableDatabase();
        Cursor cursor = mDb.query(
                HerdsmanContract.TelefoneEntry.TABLE_NAME,
                new String[] {HerdsmanContract.TelefoneEntry.COLUMN_NAME_NUMERO},
                HerdsmanContract.TelefoneEntry.COLUMN_NAME_PESSOA_IDPESSOA + " == ?",
                new String[] {"1"},
                null,
                null,
                null
        );
        String numero = null;
        while(cursor.moveToNext())
        {
            numero = cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.TelefoneEntry.COLUMN_NAME_NUMERO));
        }
        Telefone telefoneAdmin = new Telefone(1, numero);
        cursor.close();
        mDb.close();
        return telefoneAdmin;
    }

    public ArrayList carregarEnfermidades() {
        SQLiteDatabase mDb = this.getReadableDatabase();
        ArrayList enfermidadeList = new ArrayList();
        Cursor cursor = mDb.query(
                HerdsmanContract.EnfermidadeEntry.TABLE_NAME,
                new String[] {HerdsmanContract.EnfermidadeEntry.COLUMN_NAME_IDENFERMIDADE, HerdsmanContract.EnfermidadeEntry.COLUMN_NAME_DESCRICAO},
                null,
                null,
                null,
                null,
                null
                );
        while(cursor.moveToNext())
        {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.EnfermidadeEntry.COLUMN_NAME_IDENFERMIDADE));
            String desc = cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.EnfermidadeEntry.COLUMN_NAME_DESCRICAO));
            Enfermidade enfermidade = new Enfermidade(id, desc);
            enfermidadeList.add(enfermidade);
        }
        cursor.close();
        mDb.close();
        return enfermidadeList;
    }

    public void updateAdminTelefone(String s) {
        SQLiteDatabase mDb = this.getWritableDatabase();
        String[] where = {"1"};
        mDb.delete(
                HerdsmanContract.TelefoneEntry.TABLE_NAME,
                HerdsmanContract.TelefoneEntry.COLUMN_NAME_PESSOA_IDPESSOA + " == ?",
                where
        );
        ContentValues values = new ContentValues();
        values.put(HerdsmanContract.TelefoneEntry.COLUMN_NAME_PESSOA_IDPESSOA, "1");
        values.put(HerdsmanContract.TelefoneEntry.COLUMN_NAME_NUMERO, s);
        mDb.insert(HerdsmanContract.TelefoneEntry.TABLE_NAME, null, values);
        mDb.close();
    }


    public Animal carregarAnimal(int idAnimal)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                HerdsmanContract.AnimalEntry.TABLE_NAME,
                new String[] {HerdsmanContract.AnimalEntry.COLUMN_NAME_IDANIMAL,HerdsmanContract.AnimalEntry.COLUMN_NAME_ATIVO,HerdsmanContract.AnimalEntry.COLUMN_NAME_NUMERO,HerdsmanContract.AnimalEntry.COLUMN_NAME_NOME},
                HerdsmanContract.AnimalEntry.COLUMN_NAME_IDANIMAL + " == ?",
                new String[] {String.valueOf(idAnimal)},
                null,
                null,
                null);
        Animal animal = null;
        if(cursor.moveToNext())
        {
            String nome = cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalEntry.COLUMN_NAME_NOME));
            String numero = cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalEntry.COLUMN_NAME_NUMERO));
            int ativo = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalEntry.COLUMN_NAME_ATIVO));
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalEntry.COLUMN_NAME_IDANIMAL));
            animal = new Animal(id, numero, nome, ativo);
        }
        cursor.close();
        db.close();
        return animal;
    }

    public Enfermidade carregarEnfermidade(int idEnfermidade)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                HerdsmanContract.EnfermidadeEntry.TABLE_NAME,
                new String[] {HerdsmanContract.EnfermidadeEntry.COLUMN_NAME_IDENFERMIDADE, HerdsmanContract.EnfermidadeEntry.COLUMN_NAME_DESCRICAO},
                HerdsmanContract.EnfermidadeEntry.COLUMN_NAME_IDENFERMIDADE + " == ?",
                new String [] {String.valueOf(idEnfermidade)},
                null,
                null,
                null
        );
        Enfermidade enfermidade = null;
        if(cursor.moveToNext())
        {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.EnfermidadeEntry.COLUMN_NAME_IDENFERMIDADE));
            String desc = cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.EnfermidadeEntry.COLUMN_NAME_DESCRICAO));
            enfermidade = new Enfermidade(id, desc);
        }
        cursor.close();
        db.close();
        return enfermidade;
    }

    public ArrayList carregarSinistrosAnimal(Animal animal)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList list = new ArrayList();
        Cursor cursor = db.query(HerdsmanContract.SinistroEntry.TABLE_NAME,
                new String[] {HerdsmanContract.SinistroEntry.COLUMN_NAME_ANIMAL_IDANIMAL, HerdsmanContract.SinistroEntry.COLUMN_NAME_DATA, HerdsmanContract.SinistroEntry.COLUMN_NAME_ENFERMIDADE_IDENFERMIDADE, HerdsmanContract.SinistroEntry.COLUMN_NAME_ANIMAL_IDANIMAL, HerdsmanContract.SinistroEntry.COLUMN_NAME_IDANIMAL_ENFERMIDADE, HerdsmanContract.SinistroEntry.COLUMN_NAME_PESSOA_IDOPESSOA},
                HerdsmanContract.SinistroEntry.COLUMN_NAME_ANIMAL_IDANIMAL + " == ?",
                new String[] { String.valueOf(animal.getId())},
                null,
                null,
                HerdsmanContract.SinistroEntry.COLUMN_NAME_DATA + " DESC");
        while(cursor.moveToNext())
        {
            int idAnimalEnfermidade = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.SinistroEntry.COLUMN_NAME_IDANIMAL_ENFERMIDADE));
            int Animal_idAnimal = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.SinistroEntry.COLUMN_NAME_ANIMAL_IDANIMAL));
            int Enfermidade_idEnfermidade = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.SinistroEntry.COLUMN_NAME_ENFERMIDADE_IDENFERMIDADE));
            int Usuario_idUsuario = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.SinistroEntry.COLUMN_NAME_PESSOA_IDOPESSOA));
            String data = cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.SinistroEntry.COLUMN_NAME_DATA));
            Sinistro sinistro = new Sinistro(idAnimalEnfermidade, Animal_idAnimal, Enfermidade_idEnfermidade, Usuario_idUsuario, data);
            sinistro.setAnimal(this.carregarAnimal(Animal_idAnimal));
            sinistro.setEnfermidade(this.carregarEnfermidade(Enfermidade_idEnfermidade));
            list.add(sinistro);
        }
        cursor.close();
        db.close();
        return list;
    }

    public ArrayList carregarInseminacoesAnimal(Animal animal) {
        SQLiteDatabase mDb = this.getReadableDatabase();
        Cursor cursor = mDb.query(
                HerdsmanContract.AnimalInseminacaoEntry.TABLE_NAME,
                new String[] {HerdsmanContract.AnimalInseminacaoEntry.COLUMN_NAME_ANIMAL_IDANIMAL, HerdsmanContract.AnimalInseminacaoEntry.COLUMN_NAME_DATA, HerdsmanContract.AnimalInseminacaoEntry.COLUMN_NAME_IDANIMAL_INSEMINACAO},
                HerdsmanContract.AnimalInseminacaoEntry.COLUMN_NAME_ANIMAL_IDANIMAL + " == ?",
                new String[] {String.valueOf(animal.getId())},
                null,
                null,
                HerdsmanContract.AnimalInseminacaoEntry.COLUMN_NAME_DATA + " DESC"
        );
        ArrayList lista = new ArrayList();
        while(cursor.moveToNext())
        {
            int idInseminacao = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalInseminacaoEntry.COLUMN_NAME_IDANIMAL_INSEMINACAO));
            int idAnimal = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalInseminacaoEntry.COLUMN_NAME_ANIMAL_IDANIMAL));
            String data = cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalInseminacaoEntry.COLUMN_NAME_DATA));
            Inseminacao inseminacao = new Inseminacao(idInseminacao, idAnimal, data);
            lista.add(inseminacao);
        }
        cursor.close();
        mDb.close();
        return lista;
    }

    public long inserirInseminacao(Inseminacao inseminacao) {
        SQLiteDatabase mDb = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HerdsmanContract.AnimalInseminacaoEntry.COLUMN_NAME_ANIMAL_IDANIMAL, inseminacao.getIdAnimal());
        values.put(HerdsmanContract.AnimalInseminacaoEntry.COLUMN_NAME_DATA, inseminacao.getData());
        long ins = mDb.insert(HerdsmanContract.AnimalInseminacaoEntry.TABLE_NAME, null, values);
        return ins;
    }

    public ArrayList carregarRemediosAnimal(Animal animal) {
        SQLiteDatabase mDb = this.getReadableDatabase();
        Cursor cursor = mDb.query(
                HerdsmanContract.AnimalRemedioEntry.TABLE_NAME,
                new String[] {
                        HerdsmanContract.AnimalRemedioEntry.COLUMN_NAME_ANIMAL_IDANIMAL,
                        HerdsmanContract.AnimalRemedioEntry.COLUMN_NAME_DATA,
                        HerdsmanContract.AnimalRemedioEntry.COLUMN_NAME_IDANIMAL_REMEDIO,
                        HerdsmanContract.AnimalRemedioEntry.COLUMN_NAME_QUANTIDADE,
                        HerdsmanContract.AnimalRemedioEntry.COLUMN_NAME_REMEDIO_IDREMEDIO,
                        HerdsmanContract.AnimalRemedioEntry.COLUMN_NAME_MEDIDA_IDMEDIDA
                },
                HerdsmanContract.AnimalRemedioEntry.COLUMN_NAME_ANIMAL_IDANIMAL + " == ?",
                new String[]{String.valueOf(animal.getId())},
                null,
                null,
                HerdsmanContract.AnimalRemedioEntry.COLUMN_NAME_DATA + " DESC"
        );
        ArrayList list = new ArrayList();
        while(cursor.moveToNext())
        {
            int Animal_idAnimal = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalRemedioEntry.COLUMN_NAME_ANIMAL_IDANIMAL));
            String data = cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalRemedioEntry.COLUMN_NAME_DATA));
            int Remedio_idRemedio = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalRemedioEntry.COLUMN_NAME_REMEDIO_IDREMEDIO));
            int quantidade = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalRemedioEntry.COLUMN_NAME_QUANTIDADE));
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalRemedioEntry.COLUMN_NAME_IDANIMAL_REMEDIO));
            int Medida_idMedida = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalRemedioEntry.COLUMN_NAME_MEDIDA_IDMEDIDA));
            AnimalRemedio animalRemedio = new AnimalRemedio(id,Remedio_idRemedio,Animal_idAnimal,Medida_idMedida,data,quantidade);
            Remedio remedio = this.carregarRemedio(Remedio_idRemedio);
            animalRemedio.setRemedio(remedio);
            animalRemedio.setAnimal(animal);
            Medida medida = this.carregarMedida(Medida_idMedida);
            animalRemedio.setMedida(medida);
            list.add(animalRemedio);
        }
        cursor.close();
        mDb.close();
        return list;
    }

    private Medida carregarMedida(int medida_idMedida) {
        Log.d("Carregar medida com id:" , String.valueOf(medida_idMedida));
        SQLiteDatabase mDb = this.getReadableDatabase();
        Cursor cursor = mDb.query(
                HerdsmanContract.MedidaEntry.TABLE_NAME,
                new String[] {HerdsmanContract.MedidaEntry.COLUMN_NAME_IDMEDIDA, HerdsmanContract.MedidaEntry.COLUMN_NAME_NOME},
                HerdsmanContract.MedidaEntry.COLUMN_NAME_IDMEDIDA + " == ?",
                new String[] {String.valueOf(medida_idMedida)},
                null,
                null,
                null
        );
        Medida medida = null;
        if(cursor.moveToNext())
        {
            int idMedida = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.MedidaEntry.COLUMN_NAME_IDMEDIDA));
            String nome = cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.MedidaEntry.COLUMN_NAME_NOME));
            medida = new Medida(idMedida,nome);
        }
        cursor.close();
        mDb.close();
        return medida;
    }

    private Remedio carregarRemedio(int remedio_idRemedio) {
        SQLiteDatabase mDb = this.getReadableDatabase();
        Cursor cursor = mDb.query(
                HerdsmanContract.RemedioEntry.TABLE_NAME,
                new String[] {HerdsmanContract.RemedioEntry.COLUMN_NAME_IDREMEDIO,HerdsmanContract.RemedioEntry.COLUMN_NAME_NOME},
                HerdsmanContract.RemedioEntry.COLUMN_NAME_IDREMEDIO + " ==  ?",
                new String[] {String.valueOf(remedio_idRemedio)},
                null,
                null,
                null
        );
        Remedio remedio = null;
        if(cursor.moveToNext())
        {
            int idRemedio = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.RemedioEntry.COLUMN_NAME_IDREMEDIO));
            String nome = cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.RemedioEntry.COLUMN_NAME_NOME));
            remedio = new Remedio(idRemedio, nome);
        }
        cursor.close();
        mDb.close();
        return remedio;

    }

    public ArrayList carregarMedidas() {
        SQLiteDatabase mDb = this.getReadableDatabase();
        Cursor cursor = mDb.query(
                HerdsmanContract.MedidaEntry.TABLE_NAME,
                new String[] {HerdsmanContract.MedidaEntry.COLUMN_NAME_IDMEDIDA, HerdsmanContract.MedidaEntry.COLUMN_NAME_NOME},
                null,
                null,
                null,
                null,
                null
        );
        ArrayList arrayList = new ArrayList();
        while(cursor.moveToNext())
        {
            int idMedida = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.MedidaEntry.COLUMN_NAME_IDMEDIDA));
            String nome = cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.MedidaEntry.COLUMN_NAME_NOME));
            Medida medida = new Medida(idMedida, nome);
            arrayList.add(medida);
        }
        cursor.close();
        mDb.close();
        return arrayList;
    }

    public ArrayList carregarTodosRemedios() {
        ArrayList arrayList = new ArrayList();
        SQLiteDatabase mDb = this.getReadableDatabase();
        Cursor cursor;
        cursor = mDb.query(
                TABLE_NAME,
                new String[]{HerdsmanContract.RemedioEntry.COLUMN_NAME_NOME, HerdsmanContract.RemedioEntry.COLUMN_NAME_IDREMEDIO},
                null,
                null,
                null,
                null,
                HerdsmanContract.RemedioEntry.COLUMN_NAME_NOME
        );
        while (cursor.moveToNext())
        {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.RemedioEntry.COLUMN_NAME_IDREMEDIO));
            String nome = cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.RemedioEntry.COLUMN_NAME_NOME));
            Remedio remedio = new Remedio(id, nome);
            arrayList.add(remedio);
        }
        cursor.close();
        mDb.close();
        return  arrayList;

    }

    public long inserirAnimalRemedio(AnimalRemedio animalRemedio) {
        SQLiteDatabase mDb = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(HerdsmanContract.AnimalRemedioEntry.COLUMN_NAME_DATA, animalRemedio.getData());
        values.put(HerdsmanContract.AnimalRemedioEntry.COLUMN_NAME_ANIMAL_IDANIMAL, animalRemedio.getAnimal_idAnimal());
        values.put(HerdsmanContract.AnimalRemedioEntry.COLUMN_NAME_MEDIDA_IDMEDIDA, animalRemedio.getMedida_idMedida());
        values.put(HerdsmanContract.AnimalRemedioEntry.COLUMN_NAME_REMEDIO_IDREMEDIO, animalRemedio.getRemedio_idRemedio());
        values.put(HerdsmanContract.AnimalRemedioEntry.COLUMN_NAME_QUANTIDADE, animalRemedio.getQuantidade());
        long insert = mDb.insert(HerdsmanContract.AnimalRemedioEntry.TABLE_NAME, null, values);
        mDb.close();
        return insert;
    }

    public ArrayList carregarSinistrosFuncionario(Pessoa pessoa) {
        SQLiteDatabase mDb = this.getReadableDatabase();
        Cursor cursor = mDb.query(
                HerdsmanContract.SinistroEntry.TABLE_NAME,
                new String[] {
                        HerdsmanContract.SinistroEntry.COLUMN_NAME_PESSOA_IDOPESSOA,
                        HerdsmanContract.SinistroEntry.COLUMN_NAME_IDANIMAL_ENFERMIDADE,
                HerdsmanContract.SinistroEntry.COLUMN_NAME_ENFERMIDADE_IDENFERMIDADE,
                HerdsmanContract.SinistroEntry.COLUMN_NAME_DATA,
                HerdsmanContract.SinistroEntry.COLUMN_NAME_ANIMAL_IDANIMAL},
                HerdsmanContract.SinistroEntry.COLUMN_NAME_PESSOA_IDOPESSOA +" == ?",
                new String[] {String.valueOf(pessoa.getIdPessoa())},
                null,
                null,
                HerdsmanContract.SinistroEntry.COLUMN_NAME_DATA + " DESC"
        );
        ArrayList arrayList = new ArrayList();
        while (cursor.moveToNext())
        {
            int usuario_idUsuario = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.SinistroEntry.COLUMN_NAME_PESSOA_IDOPESSOA));
            int idAnimalEnfermidade = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.SinistroEntry.COLUMN_NAME_IDANIMAL_ENFERMIDADE));
            int enfermidade_idEnfermidade = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.SinistroEntry.COLUMN_NAME_ENFERMIDADE_IDENFERMIDADE));
            int animal_idAnimal = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.SinistroEntry.COLUMN_NAME_ANIMAL_IDANIMAL));
            String data = cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.SinistroEntry.COLUMN_NAME_DATA));
            Sinistro animalEnfermidade = new Sinistro(idAnimalEnfermidade,animal_idAnimal, enfermidade_idEnfermidade, usuario_idUsuario, data);
            animalEnfermidade.setAnimal(this.carregarAnimal(animal_idAnimal));
            animalEnfermidade.setEnfermidade(this.carregarEnfermidade(enfermidade_idEnfermidade));
            arrayList.add(animalEnfermidade);
        }
        cursor.close();
        mDb.close();
        return arrayList;
    }

    public ArrayList carregarCiosFuncionario(Pessoa pessoa) {
        SQLiteDatabase mDb = this.getReadableDatabase();
        Cursor cursor = mDb.query(
                HerdsmanContract.CioEntry.TABLE_NAME,
                new String[] {
                        HerdsmanContract.CioEntry.COLUMN_NAME_ANIMAL_IDANIMALPORCIMA,
                        HerdsmanContract.CioEntry.COLUMN_NAME_ANIMAL_IDANIMALPORBAIXO,
                        HerdsmanContract.CioEntry.COLUMN_NAME_DATA,
                        HerdsmanContract.CioEntry.COLUMN_NAME_IDANIMAL_CIO,
                        HerdsmanContract.CioEntry.COLUMN_NAME_PESSOA_IDPESSOA
                },
                HerdsmanContract.CioEntry.COLUMN_NAME_PESSOA_IDPESSOA + " == ?",
                new String[] {String.valueOf(pessoa.getIdPessoa())},
                null,
                null,
                HerdsmanContract.CioEntry.COLUMN_NAME_DATA +  " DESC"
        );
        ArrayList arrayList = new ArrayList();
        while(cursor.moveToNext())
        {
            int idAnimal_Cio = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.CioEntry.COLUMN_NAME_IDANIMAL_CIO));
            int animal_idAnimalPorCima = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.CioEntry.COLUMN_NAME_ANIMAL_IDANIMALPORCIMA));
            int animal_idAnimalPorBaixo = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.CioEntry.COLUMN_NAME_ANIMAL_IDANIMALPORBAIXO));
            int usuario_idUsuario = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.CioEntry.COLUMN_NAME_PESSOA_IDPESSOA));
            String data = cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.CioEntry.COLUMN_NAME_DATA));
            Cio cio = new Cio(idAnimal_Cio, animal_idAnimalPorCima, animal_idAnimalPorBaixo, data, usuario_idUsuario);
            cio.setAnimalPorCima(this.carregarAnimal(animal_idAnimalPorCima));
            cio.setAnimalPorBaixo(this.carregarAnimal(animal_idAnimalPorBaixo));
            arrayList.add(cio);
        }
        cursor.close();
        mDb.close();
        return arrayList;
    }

    public long inserirTelefone(Telefone telefone) {
        //TODO Firebase
        SQLiteDatabase mDb = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HerdsmanContract.TelefoneEntry.COLUMN_NAME_PESSOA_IDPESSOA, telefone.getPessoa_idPessoa());
        values.put(HerdsmanContract.TelefoneEntry.COLUMN_NAME_NUMERO, telefone.getNumero());
        long insert = mDb.insert(HerdsmanContract.TelefoneEntry.TABLE_NAME,null,values);
        mDb.close();
        return insert;
    }

    public int removerTelefone(Telefone telefone) {
        SQLiteDatabase mDb = this.getWritableDatabase();
        String where = HerdsmanContract.TelefoneEntry.COLUMN_NAME_IDTELEFONE + "== ?";
        String[] whereArgs =
                {
                        String.valueOf(telefone.getIdTelefone())
                };
        int delete = mDb.delete(
                HerdsmanContract.TelefoneEntry.TABLE_NAME,
                where,
                whereArgs);
        mDb.close();
        return delete;
    }

    public long inserirAdministradorNotificaPessoa(AdministradorNotificaPessoa outro) {
        SQLiteDatabase mDb= this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HerdsmanContract.AdministradorNotificaPessoaEntry.COLUMN_NAME_DESCRICAO, outro.getMensagem());
        values.put(HerdsmanContract.AdministradorNotificaPessoaEntry.COLUMN_NAME_DATA, outro.getData());
        values.put(HerdsmanContract.AdministradorNotificaPessoaEntry.COLUMN_NAME_ADMINISTRADOR_IDNOTIFICA, outro.getIdAdministrador());
        long insert = mDb.insert(HerdsmanContract.AdministradorNotificaPessoaEntry.TABLE_NAME, null,values);
        return insert;
    }

    public ArrayList carregarTodosAdministradorNotificaPessoa() {
        SQLiteDatabase mDb = this.getReadableDatabase();
        Cursor cursor =  mDb.query(
                HerdsmanContract.AdministradorNotificaPessoaEntry.TABLE_NAME,
                new String[] {
                        HerdsmanContract.AdministradorNotificaPessoaEntry.COLUMN_NAME_ADMINISTRADOR_IDNOTIFICA,
                        HerdsmanContract.AdministradorNotificaPessoaEntry.COLUMN_NAME_IDADMINISTRADOR_NOTIFICA_PESSOA,
                        HerdsmanContract.AdministradorNotificaPessoaEntry.COLUMN_NAME_DATA,
                        HerdsmanContract.AdministradorNotificaPessoaEntry.COLUMN_NAME_DESCRICAO
                },
                null,
                null,
                null,
                null,
                HerdsmanContract.AdministradorNotificaPessoaEntry.COLUMN_NAME_DATA +  " DESC"

        );
        ArrayList lista = new ArrayList();
        while(cursor.moveToNext())
        {
            int idNotifica = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.AdministradorNotificaPessoaEntry.COLUMN_NAME_IDADMINISTRADOR_NOTIFICA_PESSOA));
            int idAdmin = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.AdministradorNotificaPessoaEntry.COLUMN_NAME_ADMINISTRADOR_IDNOTIFICA));
            String data = cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.AdministradorNotificaPessoaEntry.COLUMN_NAME_DATA));
            String mensagem = cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.AdministradorNotificaPessoaEntry.COLUMN_NAME_DESCRICAO));
            AdministradorNotificaPessoa outro = new AdministradorNotificaPessoa(idNotifica, mensagem, data, idAdmin);
            lista.add(outro);
        }
        cursor.close();
        mDb.close();
        return lista;
    }
}