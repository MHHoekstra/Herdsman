package br.uepg.projeto.herdsman.dao;

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
import java.util.Calendar;
import java.util.List;

import br.uepg.projeto.herdsman.drawer.animal.ListaPartosActivity;
import br.uepg.projeto.herdsman.objetos.Animal;
import br.uepg.projeto.herdsman.objetos.AnimalEnfermidade;
import br.uepg.projeto.herdsman.objetos.AnimalRemedio;
import br.uepg.projeto.herdsman.objetos.Cio;
import br.uepg.projeto.herdsman.objetos.Enfermidade;
import br.uepg.projeto.herdsman.objetos.Inseminacao;
import br.uepg.projeto.herdsman.objetos.Medida;
import br.uepg.projeto.herdsman.objetos.AdministradorNotificaPessoa;
import br.uepg.projeto.herdsman.objetos.MensagemPendente;
import br.uepg.projeto.herdsman.objetos.Parto;
import br.uepg.projeto.herdsman.objetos.Pessoa;
import br.uepg.projeto.herdsman.objetos.Remedio;
import br.uepg.projeto.herdsman.objetos.Telefone;
import br.uepg.projeto.herdsman.objetos.Administrador;

import static br.uepg.projeto.herdsman.dao.HerdsmanContract.RemedioEntry.TABLE_NAME;

/**
 * {@link SQLiteOpenHelper} implementation that creates and upgrades the
 * application database.
 */

public class HerdsmanDbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "mydb.db";
    private static final int DB_VERSION = 27;
    private static final String TAG = "DatabaseHelper";
    protected DatabaseReference FirebaseHelper;
    private Context mContext;
    private boolean isSync = false;
    private boolean firstInput = false;
    public HerdsmanDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mContext = context;
        this.FirebaseHelper = FirebaseDatabase.getInstance().getReference("Hoekstra");
        if(firstInput) {
            //firstExec();
        }
    }
    public void firstExec() {
        if (firstInput) {
            List<Animal> firstAnimal = carregarAnimaisDb();
            List<AnimalRemedio> firstAnimalRemedio;
            List<Cio> firstCio;
            List<AnimalEnfermidade> firstAnimalEnfermidade;
            List<Parto> firstParto;
            List<Inseminacao> firstInseminacao;

            List<Remedio> firstRemedio = carregarTodosRemedios();
            for (Remedio insertRemedio : firstRemedio) {
                insereRemedioFB(insertRemedio);
            }

            List<Enfermidade> firstEnfermidade = carregarEnfermidades();
            for (Enfermidade insertEnfermidade : firstEnfermidade) {
                insereEnfermidadeFB(insertEnfermidade);
            }

            List<Pessoa> firstPessoa = carregarFuncionariosDb();
            for (Pessoa insertPessoa : firstPessoa) {
                inserePessoaFB(insertPessoa);
            }

            Administrador adm = carregarAdminDatabase();
            insereAdministradorFB(adm);

            List<AdministradorNotificaPessoa> firstAdministradorNotificaPessoa = carregarTodosAdministradorNotificaPessoa();
            for (AdministradorNotificaPessoa insertANP : firstAdministradorNotificaPessoa) {
                insereAdministradorNotificaPessoaFB(insertANP);
            }


            for (Animal insert : firstAnimal) {
                insereAnimalFB(insert);
                firstAnimalRemedio = carregarRemediosAnimal(insert);
                firstCio = carregarCiosAnimal(insert);
                firstAnimalEnfermidade = carregarSinistrosAnimal(insert);
                firstParto = carregarPartosAnimal(insert);
                firstInseminacao = carregarInseminacoesAnimal(insert);
                for (AnimalRemedio insertRemedio : firstAnimalRemedio) {
                    insereAnimalRemedioFB(insertRemedio);
                }
                for (Cio insertCio : firstCio) {
                    insereCioFB(insertCio);
                }
                for (AnimalEnfermidade insertAnimalEnfermidade : firstAnimalEnfermidade) {
                    insereSinistroFB(insertAnimalEnfermidade);
                }
                for (Parto insertParto : firstParto) {
                    inserePartoFB(insertParto);
                }
                for (Inseminacao insertInseminacao : firstInseminacao) {
                    insereInseminacaoFB(insertInseminacao);
                }

            }
        }
    }
    public void insereAdministradorFB(Administrador administrador)
    {
        DatabaseReference databaseAnimal = FirebaseHelper.child(HerdsmanContract.AdministradorEntry.TABLE_NAME);
        databaseAnimal.child(String.valueOf(administrador.getIdAdministrador())).setValue(administrador);
        databaseAnimal.keepSynced(true);
    }
    public void insereAdministradorNotificaPessoaFB(AdministradorNotificaPessoa administradorNotificaPessoa)
    {
        DatabaseReference databaseAnimal = FirebaseHelper.child(HerdsmanContract.AdministradorNotificaPessoaEntry.TABLE_NAME);
        databaseAnimal.child(String.valueOf(administradorNotificaPessoa.getIdAdministradorNotificaPessoa())).setValue(administradorNotificaPessoa);
        databaseAnimal.keepSynced(true);
    }
    public void inserePessoaFB(Pessoa pessoa)
    {
        DatabaseReference databaseAnimal = FirebaseHelper.child(HerdsmanContract.PessoaEntry.TABLE_NAME);
        databaseAnimal.child(String.valueOf(pessoa.getIdPessoa())).setValue(pessoa);
        databaseAnimal.keepSynced(true);
    }
    public void insereEnfermidadeFB(Enfermidade enfermidade)
    {
        DatabaseReference databaseAnimal = FirebaseHelper.child(HerdsmanContract.EnfermidadeEntry.TABLE_NAME);
        databaseAnimal.child(String.valueOf(enfermidade.getId())).setValue(enfermidade);
        databaseAnimal.keepSynced(true);
    }
    public void insereAnimalFB(Animal animal)
    {
        DatabaseReference databaseAnimal = FirebaseHelper.child(HerdsmanContract.AnimalEntry.TABLE_NAME);
        databaseAnimal.child(String.valueOf(animal.getId())).setValue(animal);
        databaseAnimal.keepSynced(true);
    }
    public void insereMedidaFB(Medida medida)
    {
        DatabaseReference databaseMedida = FirebaseHelper.child(HerdsmanContract.MedidaEntry.TABLE_NAME);
        databaseMedida.child(String.valueOf(medida.getIdMedida())).setValue(medida);
        databaseMedida.keepSynced(true);
    }
    public void insereRemedioFB(Remedio remedio)
    {
        DatabaseReference databaseRemedio = FirebaseHelper.child(HerdsmanContract.RemedioEntry.TABLE_NAME);
        databaseRemedio.child(String.valueOf(remedio.getIdRemedio())).setValue(remedio);
        databaseRemedio.keepSynced(true);
    }
    public void insereAnimalRemedioFB(AnimalRemedio AR)
    {
        DatabaseReference databaseAnimal = FirebaseHelper.child(HerdsmanContract.AnimalRemedioEntry.TABLE_NAME);
        databaseAnimal.child(String.valueOf(AR.getIdAnimalRemedio())).setValue(AR);
        databaseAnimal.keepSynced(true);
    }
    public void insereCioFB(Cio cio)
    {
        DatabaseReference databaseAnimal = FirebaseHelper.child(HerdsmanContract.CioEntry.TABLE_NAME);
        databaseAnimal.child(String.valueOf(cio.getIdCio())).setValue(cio);
        databaseAnimal.keepSynced(true);
    }
    public void insereSinistroFB(AnimalEnfermidade animalEnfermidade)
    {
        DatabaseReference databaseAnimal = FirebaseHelper.child(HerdsmanContract.AnimalEnfermidadeEntry.TABLE_NAME);
        databaseAnimal.child(String.valueOf(animalEnfermidade.getIdAnimalEnfermidade())).setValue(animalEnfermidade);
        databaseAnimal.keepSynced(true);
    }
    public void inserePartoFB(Parto parto)
    {
        DatabaseReference databaseAnimal = FirebaseHelper.child(HerdsmanContract.PartoEntry.TABLE_NAME);
        databaseAnimal.child(String.valueOf(parto.getId())).setValue(parto);
        databaseAnimal.keepSynced(true);
    }
    public void insereInseminacaoFB(Inseminacao inseminacao)
    {
        DatabaseReference databaseAnimal = FirebaseHelper.child(HerdsmanContract.AnimalInseminacaoEntry.TABLE_NAME);
        databaseAnimal.child(String.valueOf(inseminacao.getIdInseminacao())).setValue(inseminacao);
        databaseAnimal.keepSynced(true);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Database creation scripts should be placed in assets/sql
        String triggerTelefone = "CREATE TRIGGER valida_telefone BEFORE INSERT ON Telefone\n" +
                "BEGIN\n" +
                " SELECT CASE WHEN LENGTH(NEW.numero)<9 THEN\n" +
                " RAISE (ABORT,'Número inválido')\n" +
                " END;\n" +
                " SELECT CASE WHEN LENGTH(NEW.numero)>14 THEN\n" +
                " RAISE (ABORT,'Número inválido')\n" +
                " END;\n" +
                "END;";
        String triggerRG = "CREATE TRIGGER validaRG BEFORE INSERT ON PESSOA\n" +
                "BEGIN\n" +
                " SELECT CASE WHEN LENGTH(NEW.rg)>14 THEN\n" +
                " RAISE (ABORT,'RG inválido')\n" +
                " END;\n" +
                "END;";
        String triggerCio1 = "CREATE TRIGGER verificaCio BEFORE INSERT ON Cio WHEN EXISTS(SELECT Animal_idAnimal FROM \"Inseminacao\"  WHERE Animal_idAnimal == NEW.Animal_idAnimalPorCima AND ((NEW.data-data)<1555200000)) BEGIN INSERT INTO Administrador_Notifica_Pessoa(\"Administrador_idNotifica\",\"data\",\"descricao\")  VALUES ((SELECT idAdministrador FROM Administrador WHERE admin == 1) ,date('now'), \"Animal está com provável problema reprodutivo\"); END;";
        String triggerCio2 = "CREATE TRIGGER verificaCio2 BEFORE INSERT ON Cio WHEN (SELECT 1 FROM Inseminacao a WHERE a.Animal_idAnimal == NEW.Animal_idAnimalPorBaixo AND (NEW.data-a.data)<1555200000)\n" +
                "BEGIN\n" +
                " INSERT INTO Administrador_Notifica_Pessoa(\"Administrador_idNotifica\",\"data\",\"descricao\")  VALUES ((SELECT idAdministrador FROM Administrador WHERE admin == 1) ,date('now'), \"Animal está com provável problema reprodutivo\");\n" +
                "END;";
        String triggerCio3 = "CREATE TRIGGER verificaCio3 BEFORE INSERT ON Cio WHEN (SELECT 1 FROM Inseminacao a WHERE a.Animal_idAnimal == NEW.Animal_idAnimalPorCima AND (NEW.data-a.data)>1555200000 AND (NEW.data-a.data)<5184000000)\n" +
                "BEGIN\n" +
                " INSERT INTO Administrador_Notifica_Pessoa(\"Administrador_idNotifica\",\"data\",\"descricao\")  VALUES ((SELECT idAdministrador FROM Administrador WHERE admin == 1) ,date('now'), \"Animal necessita inseminação\");\n" +
                "END;";
        String triggerCio4 = "CREATE TRIGGER verificaCio4 BEFORE INSERT ON Cio WHEN (SELECT 1 FROM Inseminacao a WHERE a.Animal_idAnimal == NEW.Animal_idAnimalPorBaixo AND (NEW.data-a.data)>1555200000 AND (NEW.data-a.data)<5184000000)\n" +
                "BEGIN\n" +
                " INSERT INTO Administrador_Notifica_Pessoa(\"Administrador_idNotifica\",\"data\",\"descricao\")  VALUES ((SELECT idAdministrador FROM Administrador WHERE admin == 1) ,date('now'), \"Animal necessita inseminação\");\n" +
                "END;";
        String triggerCio5 = "CREATE TRIGGER verificaCio5 BEFORE INSERT ON Cio WHEN (SELECT 1 FROM Inseminacao a WHERE a.Animal_idAnimal == NEW.Animal_idAnimalPorCima AND (NEW.data-a.data)>5184000000)\n" +
                "BEGIN\n" +
                " INSERT INTO Administrador_Notifica_Pessoa(\"Administrador_idNotifica\",\"data\",\"descricao\")  VALUES ((SELECT idAdministrador FROM Administrador WHERE admin == 1) ,date('now'), \"Animal necessita inspeção\");\n" +
                "END;";
        String triggerCio6 = "CREATE TRIGGER verificaCio6 BEFORE INSERT ON Cio WHEN (SELECT 1 FROM Inseminacao a WHERE a.Animal_idAnimal == NEW.Animal_idAnimalPorBaixo AND (NEW.data-a.data)>5184000000)\n" +
                "BEGIN\n" +
                " INSERT INTO Administrador_Notifica_Pessoa(\"Administrador_idNotifica\",\"data\",\"descricao\")  VALUES ((SELECT idAdministrador FROM Administrador WHERE admin == 1) ,date('now'), \"Animal necessita inspeção\");\n" +
                "END;";
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
            //Log.d(TAG, "Executing SQL: \r\n" + triggerCio1);
            db.execSQL(triggerRG);
            db.execSQL(triggerTelefone);
            db.execSQL(triggerCio1);
            db.execSQL(triggerCio2);
            db.execSQL(triggerCio3);
            db.execSQL(triggerCio4);
            db.execSQL(triggerCio5);
            db.execSQL(triggerCio6);

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
                (System.currentTimeMillis() - startTime) + "ms.");
    }
    public void setFirstInput(Boolean first)
    {
        firstInput = first;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int from, int to) {
        Log.v(TAG, "Delete database");
        Log.i(TAG, "Database Version: OLD: " + from + " = NEW: " + to);

        if (mContext.deleteDatabase(DB_NAME))
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

    public List<Animal> carregarAnimaisDb() {
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
        while (cursor.moveToNext()) {
            Animal animal = new Animal(cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalEntry.COLUMN_NAME_IDANIMAL)), cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalEntry.COLUMN_NAME_NUMERO)), cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalEntry.COLUMN_NAME_NOME)), cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalEntry.COLUMN_NAME_ATIVO)));
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
        while (cursor.moveToNext()) {
            Pessoa pessoa = new Pessoa(
                    cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.PessoaEntry.COLUMN_NAME_IDPESSOA)),
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
                new String[]{HerdsmanContract.PartoEntry.COLUMN_NAME_DATA, HerdsmanContract.PartoEntry.COLUMN_NAME_CRIA, HerdsmanContract.PartoEntry.COLUMN_NAME_IDPARTO, HerdsmanContract.PartoEntry.COLUMN_NAME_ANIMAL_IDANIMAL},
                selection,
                selectionArgs,
                null,
                null,
                HerdsmanContract.PartoEntry.COLUMN_NAME_DATA + " DESC"

        );

        ArrayList lista = new ArrayList<Parto>();
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.PartoEntry.COLUMN_NAME_IDPARTO));
            long Animal_idAnimal = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.PartoEntry.COLUMN_NAME_ANIMAL_IDANIMAL));
            int cria = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.PartoEntry.COLUMN_NAME_CRIA));
            long data = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.PartoEntry.COLUMN_NAME_DATA));
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
                        HerdsmanContract.CioEntry.COLUMN_NAME_DATA,
                        HerdsmanContract.CioEntry.COLUMN_NAME_PESSOA_IDPESSOA
                },
                null,
                null,
                null,
                null,
                HerdsmanContract.CioEntry.COLUMN_NAME_DATA + " DESC"
        );
        ArrayList listaCios = new ArrayList<Cio>();
        while (cursor.moveToNext()) {
            long idAnimalPorBaixo = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.CioEntry.COLUMN_NAME_ANIMAL_IDANIMALPORBAIXO));
            long idAnimalPorCima = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.CioEntry.COLUMN_NAME_ANIMAL_IDANIMALPORCIMA));
            long idAnimalCio = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.CioEntry.COLUMN_NAME_IDANIMAL_CIO));
            long data = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.CioEntry.COLUMN_NAME_DATA));
            long idPessoa = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.CioEntry.COLUMN_NAME_PESSOA_IDPESSOA));
            Cio cio = new Cio(idAnimalCio, idAnimalPorCima, idAnimalPorBaixo, data, idPessoa);
            Cursor cursor2 = mDb.query(
                    HerdsmanContract.AnimalEntry.TABLE_NAME,
                    new String[]{HerdsmanContract.AnimalEntry.COLUMN_NAME_IDANIMAL,
                            HerdsmanContract.AnimalEntry.COLUMN_NAME_NUMERO,
                            HerdsmanContract.AnimalEntry.COLUMN_NAME_NOME,
                            HerdsmanContract.AnimalEntry.COLUMN_NAME_ATIVO},
                    HerdsmanContract.AnimalEntry.COLUMN_NAME_IDANIMAL + " == ?",
                    new String[]{String.valueOf(idAnimalPorBaixo)},
                    null,
                    null,
                    null
            );
            if (cursor2.moveToNext()) {
                long idAnimal = cursor2.getLong(cursor2.getColumnIndexOrThrow(HerdsmanContract.AnimalEntry.COLUMN_NAME_IDANIMAL));
                String numero = cursor2.getString(cursor2.getColumnIndexOrThrow(HerdsmanContract.AnimalEntry.COLUMN_NAME_NUMERO));
                String nome = cursor2.getString(cursor2.getColumnIndexOrThrow(HerdsmanContract.AnimalEntry.COLUMN_NAME_NOME));
                int ativo = cursor2.getInt(cursor2.getColumnIndexOrThrow(HerdsmanContract.AnimalEntry.COLUMN_NAME_ATIVO));
                Animal animal = new Animal(idAnimal, numero, nome, ativo);
                cio.setAnimalPorBaixo(animal);
            }
            cursor2.close();

            cursor2 = mDb.query(
                    HerdsmanContract.AnimalEntry.TABLE_NAME,
                    new String[]{HerdsmanContract.AnimalEntry.COLUMN_NAME_IDANIMAL,
                            HerdsmanContract.AnimalEntry.COLUMN_NAME_NUMERO,
                            HerdsmanContract.AnimalEntry.COLUMN_NAME_NOME,
                            HerdsmanContract.AnimalEntry.COLUMN_NAME_ATIVO},
                    HerdsmanContract.AnimalEntry.COLUMN_NAME_IDANIMAL + " == ?",
                    new String[]{String.valueOf(idAnimalPorCima)},
                    null,
                    null,
                    null
            );
            if (cursor2.moveToNext()) {
                long idAnimal = cursor2.getLong(cursor2.getColumnIndexOrThrow(HerdsmanContract.AnimalEntry.COLUMN_NAME_IDANIMAL));
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

    public ArrayList carregarCiosAnimal(Animal animal) {
        SQLiteDatabase mDb = this.getReadableDatabase();
        Cursor cursor;
        String selection = HerdsmanContract.CioEntry.COLUMN_NAME_ANIMAL_IDANIMALPORBAIXO + "== ? OR " + HerdsmanContract.CioEntry.COLUMN_NAME_ANIMAL_IDANIMALPORCIMA + " == ?";
        String[] selectionArgs = {String.valueOf(animal.getId()), String.valueOf(animal.getId())};
        cursor = mDb.query(
                HerdsmanContract.CioEntry.TABLE_NAME,
                new String[]{HerdsmanContract.CioEntry.COLUMN_NAME_DATA, HerdsmanContract.CioEntry.COLUMN_NAME_ANIMAL_IDANIMALPORBAIXO, HerdsmanContract.CioEntry.COLUMN_NAME_ANIMAL_IDANIMALPORCIMA, HerdsmanContract.CioEntry.COLUMN_NAME_IDANIMAL_CIO, HerdsmanContract.CioEntry.COLUMN_NAME_PESSOA_IDPESSOA},
                selection,
                selectionArgs,
                null,
                null,
                HerdsmanContract.CioEntry.COLUMN_NAME_DATA + " DESC"
        );
        ArrayList lista = new ArrayList<Cio>();
        while (cursor.moveToNext()) {
            long idCio = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.CioEntry.COLUMN_NAME_IDANIMAL_CIO));
            long idAnimalPorCima = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.CioEntry.COLUMN_NAME_ANIMAL_IDANIMALPORCIMA));
            long idAnimalPorBaixo = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.CioEntry.COLUMN_NAME_ANIMAL_IDANIMALPORBAIXO));
            long usuario = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.CioEntry.COLUMN_NAME_PESSOA_IDPESSOA));
            long data = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.CioEntry.COLUMN_NAME_DATA));
            Cio cio = new Cio(idCio, idAnimalPorCima, idAnimalPorBaixo, data, usuario);
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

    public Administrador carregarAdminDatabase() {
        SQLiteDatabase mDb = this.getWritableDatabase();
        Cursor cursor;
        Administrador adminAdministrador = null;
        String[] projection =
                {
                        HerdsmanContract.AdministradorEntry.COLUMN_NAME_IDADMINISTRADOR,
                        HerdsmanContract.AdministradorEntry.COLUMN_NAME_LOGIN,
                        HerdsmanContract.AdministradorEntry.COLUMN_NAME_SENHA,
                        HerdsmanContract.AdministradorEntry.COLUMN_NAME_PESSOA_IDPESSOA
                };

        cursor = mDb.query(
                HerdsmanContract.AdministradorEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
        while (cursor.moveToNext()) {
            String login = cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.AdministradorEntry.COLUMN_NAME_LOGIN));
            String senha = cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.AdministradorEntry.COLUMN_NAME_SENHA));
            long idPessoa = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.AdministradorEntry.COLUMN_NAME_PESSOA_IDPESSOA));
            long idUsuario = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.AdministradorEntry.COLUMN_NAME_IDADMINISTRADOR));
            adminAdministrador = new Administrador(1, login, senha, idPessoa, idUsuario);
        }
        cursor.close();
        mDb.close();
        return adminAdministrador;
    }

    public long inserirAnimal(Animal animal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HerdsmanContract.AnimalEntry.COLUMN_NAME_NUMERO, animal.getNumero());
        values.put(HerdsmanContract.AnimalEntry.COLUMN_NAME_NOME, animal.getNome());
        values.put(HerdsmanContract.AnimalEntry.COLUMN_NAME_ATIVO, animal.getAtivo());
        if (this.isSync()) {
            values.put(HerdsmanContract.AnimalEntry.COLUMN_NAME_IDANIMAL, animal.getId());
        }
        else
        {
            Calendar calendar = Calendar.getInstance();
            animal.setId(calendar.getTimeInMillis());
            values.put(HerdsmanContract.AnimalEntry.COLUMN_NAME_IDANIMAL, animal.getId());
        }
        long id = db.insert(HerdsmanContract.AnimalEntry.TABLE_NAME, null, values);
        if (id > 0 && !this.isSync()) {
            DatabaseReference databaseAnimal = FirebaseHelper.child(HerdsmanContract.AnimalEntry.TABLE_NAME);
            databaseAnimal.child(String.valueOf(animal.getId())).setValue(animal);
            databaseAnimal.keepSynced(true);
        }
        db.close();
        return id;
    }

    public long replaceAnimal(Animal animal) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HerdsmanContract.AnimalEntry.COLUMN_NAME_IDANIMAL, animal.getId());
        values.put(HerdsmanContract.AnimalEntry.COLUMN_NAME_NUMERO, animal.getNumero());
        values.put(HerdsmanContract.AnimalEntry.COLUMN_NAME_NOME, animal.getNome());
        values.put(HerdsmanContract.AnimalEntry.COLUMN_NAME_ATIVO, animal.getAtivo());
        long id = db.replace(HerdsmanContract.AnimalEntry.TABLE_NAME, null, values);
        if (id > 0 && !this.isSync()) {
            DatabaseReference databaseAnimal = FirebaseHelper.child(HerdsmanContract.AnimalEntry.TABLE_NAME);
            databaseAnimal.child(String.valueOf(animal.getId())).setValue(animal);
            databaseAnimal.keepSynced(true);
        }
        db.close();
        return id;
    }

    public ArrayList carregarTodosSinistros() {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] projection =
                {
                        HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_ANIMAL_IDANIMAL,
                        HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_ENFERMIDADE_IDENFERMIDADE,
                        HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_DATA,
                        HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_PESSOA_IDOPESSOA,
                        HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_IDANIMAL_ENFERMIDADE
                };
        Cursor cursor = db.query(
                HerdsmanContract.AnimalEnfermidadeEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_DATA + " DESC"
        );
        Cursor cursor_animal;
        Cursor cursor_enfermidade;
        ArrayList list = new ArrayList<AnimalEnfermidade>();
        while (cursor.moveToNext()) {
            long idAnimalEnfermidade = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_IDANIMAL_ENFERMIDADE));
            long Animal_idAnimal = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_ANIMAL_IDANIMAL));
            long Enfermidade_idEnfermidade = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_ENFERMIDADE_IDENFERMIDADE));
            long Usuario_idUsuario = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_PESSOA_IDOPESSOA));
            long data = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_DATA));
            AnimalEnfermidade animalEnfermidade = new AnimalEnfermidade(idAnimalEnfermidade, Animal_idAnimal, Enfermidade_idEnfermidade, Usuario_idUsuario, data);
            Log.d("Carregar Sinistros: ", String.valueOf(Animal_idAnimal));
            cursor_animal = db.query(
                    HerdsmanContract.AnimalEntry.TABLE_NAME,
                    new String[]{HerdsmanContract.AnimalEntry.COLUMN_NAME_NOME,
                            HerdsmanContract.AnimalEntry.COLUMN_NAME_IDANIMAL,
                            HerdsmanContract.AnimalEntry.COLUMN_NAME_NUMERO,
                            HerdsmanContract.AnimalEntry.COLUMN_NAME_ATIVO},
                    HerdsmanContract.AnimalEntry.COLUMN_NAME_IDANIMAL + " == ?",
                    new String[]{String.valueOf(Animal_idAnimal)},
                    null,
                    null,
                    null
            );
            if (cursor_animal.moveToNext()) {
                String numero = cursor_animal.getString(cursor_animal.getColumnIndexOrThrow(HerdsmanContract.AnimalEntry.COLUMN_NAME_NUMERO));
                long idAnimal = cursor_animal.getLong(cursor_animal.getColumnIndexOrThrow(HerdsmanContract.AnimalEntry.COLUMN_NAME_IDANIMAL));
                String nome = cursor_animal.getString(cursor_animal.getColumnIndexOrThrow(HerdsmanContract.AnimalEntry.COLUMN_NAME_NOME));
                Animal animal = new Animal(idAnimal, numero, nome);
                Log.d("Carregar Sinistros: ", animal.getNome());
                Log.d("Carregar Sinistros: ", animal.getNumero());
                animalEnfermidade.setAnimal(animal);
            }
            cursor_animal.close();
            cursor_enfermidade = db.query(
                    HerdsmanContract.EnfermidadeEntry.TABLE_NAME,
                    new String[]{HerdsmanContract.EnfermidadeEntry.COLUMN_NAME_DESCRICAO, HerdsmanContract.EnfermidadeEntry.COLUMN_NAME_IDENFERMIDADE},
                    HerdsmanContract.EnfermidadeEntry.COLUMN_NAME_IDENFERMIDADE + " == ?",
                    new String[]{String.valueOf(Enfermidade_idEnfermidade)},
                    null,
                    null,
                    null
            );
            if (cursor_enfermidade.moveToNext()) {
                long idEnfermidade = cursor_enfermidade.getLong(cursor_enfermidade.getColumnIndexOrThrow(HerdsmanContract.EnfermidadeEntry.COLUMN_NAME_IDENFERMIDADE));
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

    public long inserirEnfermidade(Enfermidade enfermidade) {

        SQLiteDatabase mDb = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HerdsmanContract.EnfermidadeEntry.COLUMN_NAME_DESCRICAO, enfermidade.getDescricao());
        if (this.isSync()) {
            values.put(HerdsmanContract.EnfermidadeEntry.COLUMN_NAME_IDENFERMIDADE, enfermidade.getId());
        }
        else
        {
            Calendar calendar = Calendar.getInstance();
            enfermidade.setId(calendar.getTimeInMillis());
            values.put(HerdsmanContract.EnfermidadeEntry.COLUMN_NAME_IDENFERMIDADE, enfermidade.getId());

        }
        long id = mDb.insert(HerdsmanContract.EnfermidadeEntry.TABLE_NAME, null, values);
        if (id > 0 && !this.isSync()) {
            DatabaseReference databaseEnfermidade = FirebaseHelper.child(HerdsmanContract.EnfermidadeEntry.TABLE_NAME);
            databaseEnfermidade.child(String.valueOf(enfermidade.getId())).setValue(enfermidade);
            databaseEnfermidade.keepSynced(true);
        }
        mDb.close();
        return id;
    }

    public long replaceFuncionario(Pessoa pessoa) {
        SQLiteDatabase mDb = this.getWritableDatabase();
        ContentValues valuesP = new ContentValues();
        valuesP.put(HerdsmanContract.PessoaEntry.COLUMN_NAME_IDPESSOA, pessoa.getIdPessoa());
        valuesP.put(HerdsmanContract.PessoaEntry.COLUMN_NAME_NOME, pessoa.getNome());
        valuesP.put(HerdsmanContract.PessoaEntry.COLUMN_NAME_CPF, pessoa.getCpf());
        valuesP.put(HerdsmanContract.PessoaEntry.COLUMN_NAME_RG, pessoa.getRg());
        valuesP.put(HerdsmanContract.PessoaEntry.COLUMN_NAME_ATIVO, pessoa.getAtivo());
        long id = mDb.replace(HerdsmanContract.PessoaEntry.TABLE_NAME, null, valuesP);
        if (id > 0 && !this.isSync()) {
            DatabaseReference databaseFuncionario = FirebaseHelper.child(HerdsmanContract.PessoaEntry.TABLE_NAME);
            databaseFuncionario.child(String.valueOf(pessoa.getIdPessoa())).setValue(pessoa);
            databaseFuncionario.keepSynced(true);
        }
        mDb.close();
        return id;

    }

    public long inserirFuncionario(Pessoa pessoa) {

        SQLiteDatabase mDb = this.getWritableDatabase();
        ContentValues valuesP = new ContentValues();
        valuesP.put(HerdsmanContract.PessoaEntry.COLUMN_NAME_NOME, pessoa.getNome());
        valuesP.put(HerdsmanContract.PessoaEntry.COLUMN_NAME_CPF, pessoa.getCpf());
        valuesP.put(HerdsmanContract.PessoaEntry.COLUMN_NAME_RG, pessoa.getRg());
        valuesP.put(HerdsmanContract.PessoaEntry.COLUMN_NAME_ATIVO, "1");
        if (this.isSync()) {
            valuesP.put(HerdsmanContract.PessoaEntry.COLUMN_NAME_IDPESSOA, pessoa.getIdPessoa());
        }
        else
        {
            Calendar calendar = Calendar.getInstance();
            pessoa.setIdPessoa(calendar.getTimeInMillis());
            valuesP.put(HerdsmanContract.PessoaEntry.COLUMN_NAME_IDPESSOA, pessoa.getIdPessoa());
        }
        long id = mDb.insert(HerdsmanContract.PessoaEntry.TABLE_NAME, null, valuesP);
        if (id > 0 && !this.isSync()) {
            DatabaseReference databaseFuncionario = FirebaseHelper.child(HerdsmanContract.PessoaEntry.TABLE_NAME);
            databaseFuncionario.child(String.valueOf(pessoa.getIdPessoa())).setValue(pessoa);
            databaseFuncionario.keepSynced(true);
        }
        mDb.close();
        return id;

    }

    public long inserirParto(Parto parto) {

        SQLiteDatabase mDb = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HerdsmanContract.PartoEntry.COLUMN_NAME_ANIMAL_IDANIMAL, parto.getAnimal_idAnimal());
        values.put(HerdsmanContract.PartoEntry.COLUMN_NAME_DATA, String.valueOf(parto.getData()));
        values.put(HerdsmanContract.PartoEntry.COLUMN_NAME_CRIA, parto.getCria());
        if (this.isSync()) {
            values.put(HerdsmanContract.PartoEntry.COLUMN_NAME_IDPARTO, parto.getId());
        }
        else
        {
            Calendar calendar = Calendar.getInstance();
            parto.setId(calendar.getTimeInMillis());
            values.put(HerdsmanContract.PartoEntry.COLUMN_NAME_IDPARTO, parto.getId());
        }
        long id = mDb.insert(HerdsmanContract.PartoEntry.TABLE_NAME, null, values);
        if (id > 0 && !this.isSync()) {
            DatabaseReference databaseParto = FirebaseHelper.child(HerdsmanContract.PartoEntry.TABLE_NAME);
            databaseParto.child(String.valueOf(parto.getId())).setValue(parto);
            databaseParto.keepSynced(true);
        }
        mDb.close();
        return id;

    }

    public long inserirRemedio(Remedio remedio) {

        SQLiteDatabase mDb = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HerdsmanContract.RemedioEntry.COLUMN_NAME_NOME, remedio.getNome());
        if (this.isSync()) {
            values.put(HerdsmanContract.RemedioEntry.COLUMN_NAME_IDREMEDIO, remedio.getIdRemedio());
        }
        else
        {
            Calendar calendar = Calendar.getInstance();
            remedio.setIdRemedio(calendar.getTimeInMillis());
            values.put(HerdsmanContract.RemedioEntry.COLUMN_NAME_IDREMEDIO, remedio.getIdRemedio());
        }
        long id = mDb.insert(HerdsmanContract.RemedioEntry.TABLE_NAME, null, values);
        if (id > 0 && !this.isSync()) {
            DatabaseReference databaseRemedio = FirebaseHelper.child(HerdsmanContract.RemedioEntry.TABLE_NAME);
            databaseRemedio.child(String.valueOf(remedio.getIdRemedio())).setValue(remedio);
            databaseRemedio.keepSynced(true);
        }
        mDb.close();
        return id;

}

    public ArrayList carregarTelefonesPessoa(Pessoa pessoa) {
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
        while (cursor.moveToNext()) {
            long idTelefone = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.TelefoneEntry.COLUMN_NAME_IDTELEFONE));
            long Pessoa_idPessoa = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.TelefoneEntry.COLUMN_NAME_PESSOA_IDPESSOA));
            String numero = cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.TelefoneEntry.COLUMN_NAME_NUMERO));
            Telefone tel = new Telefone(idTelefone, Pessoa_idPessoa, numero);
            telefoneItemList.add(tel);
        }
        cursor.close();
        mDb.close();
        return telefoneItemList;

    }

    public boolean existeAnimal(long idAnimal) {
        SQLiteDatabase mDb = this.getReadableDatabase();
        Cursor cursor = mDb.query(
                HerdsmanContract.AnimalEntry.TABLE_NAME,
                new String[]{HerdsmanContract.AnimalEntry.COLUMN_NAME_IDANIMAL},
                HerdsmanContract.AnimalEntry.COLUMN_NAME_IDANIMAL + "== ? and " + HerdsmanContract.AnimalEntry.COLUMN_NAME_ATIVO + " == 1",
                new String[]{String.valueOf(idAnimal)},
                null,
                null,
                null
        );
        boolean ret;
        if (cursor.getCount() > 0) {
            ret = true;
        } else {
            ret = false;
        }
        cursor.close();
        mDb.close();
        return ret;
    }

    public long inserirCio(Cio cio) {

        ContentValues values = new ContentValues();
        values.put(HerdsmanContract.CioEntry.COLUMN_NAME_IDANIMAL_CIO, cio.getIdCio());
        values.put(HerdsmanContract.CioEntry.COLUMN_NAME_ANIMAL_IDANIMALPORCIMA, cio.getIdAnimalPorCima());
        values.put(HerdsmanContract.CioEntry.COLUMN_NAME_ANIMAL_IDANIMALPORBAIXO, cio.getIdAnimalPorBaixo());
        values.put(HerdsmanContract.CioEntry.COLUMN_NAME_DATA, cio.getData());
        values.put(HerdsmanContract.CioEntry.COLUMN_NAME_PESSOA_IDPESSOA, cio.getIdFuncionario());
        SQLiteDatabase mDb = this.getWritableDatabase();
        if (this.isSync()) {
            values.put(HerdsmanContract.CioEntry.COLUMN_NAME_IDANIMAL_CIO, cio.getIdCio());
        }
        long id = mDb.insertWithOnConflict(
                HerdsmanContract.CioEntry.TABLE_NAME,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE
        );
        if (id > 0 && !this.isSync()) {
            DatabaseReference databaseCio = FirebaseHelper.child(HerdsmanContract.CioEntry.TABLE_NAME);
            databaseCio.child(String.valueOf(cio.getIdCio())).setValue(cio);
            databaseCio.keepSynced(true);
        }
        mDb.close();
        return id;
    }

    public long inserirSinistro(AnimalEnfermidade animalEnfermidade) {

        ContentValues values = new ContentValues();
        values.put(HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_IDANIMAL_ENFERMIDADE, animalEnfermidade.getIdAnimalEnfermidade());
        values.put(HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_ANIMAL_IDANIMAL, animalEnfermidade.getIdAnimal());
        values.put(HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_ENFERMIDADE_IDENFERMIDADE, animalEnfermidade.getIdEnfermidade());
        values.put(HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_DATA, animalEnfermidade.getData());
        values.put(HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_PESSOA_IDOPESSOA, animalEnfermidade.getIdFuncionario());
        SQLiteDatabase mDb = this.getWritableDatabase();
        if (this.isSync()) {
            values.put(HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_IDANIMAL_ENFERMIDADE, animalEnfermidade.getIdAnimalEnfermidade());
        }
        long id = mDb.insertWithOnConflict(HerdsmanContract.AnimalEnfermidadeEntry.TABLE_NAME,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE
        );
        if (id > 0 && !this.isSync()) {
            DatabaseReference databaseSinistro = FirebaseHelper.child(HerdsmanContract.AnimalEnfermidadeEntry.TABLE_NAME);
            databaseSinistro.child(String.valueOf(animalEnfermidade.getIdAnimalEnfermidade())).setValue(animalEnfermidade);
            databaseSinistro.keepSynced(true);
        }
        mDb.close();
        return id;
    }

    public boolean existeEnfermidade(long idEnfermidade) {
        SQLiteDatabase mDb = this.getReadableDatabase();
        Cursor cursor = mDb.query(
                HerdsmanContract.EnfermidadeEntry.TABLE_NAME,
                new String[]{HerdsmanContract.EnfermidadeEntry.COLUMN_NAME_IDENFERMIDADE},
                HerdsmanContract.EnfermidadeEntry.COLUMN_NAME_IDENFERMIDADE + "== ?",
                new String[]{String.valueOf(idEnfermidade)},
                null,
                null,
                null
        );
        boolean ret;
        if (cursor.getCount() > 0) {
            ret = true;
        } else {
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
        String selection = HerdsmanContract.AnimalEntry.COLUMN_NAME_ATIVO + " == 1";
        cursor = mDb.query(
                HerdsmanContract.AnimalEntry.TABLE_NAME,
                projection,
                selection,
                null,
                null,
                null,
                HerdsmanContract.AnimalEntry.COLUMN_NAME_NUMERO
        );
        while (cursor.moveToNext()) {
            String numero = cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalEntry.COLUMN_NAME_NUMERO));
            long idAnimal = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalEntry.COLUMN_NAME_IDANIMAL));
            String nome = cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalEntry.COLUMN_NAME_NOME));
            Animal animal = new Animal(idAnimal, numero, nome);
            listaAnimais.add(animal);
        }
        cursor.close();
        mDb.close();
        return listaAnimais;
    }

    public Telefone carregarTelefoneAdmin() {
        SQLiteDatabase mDb = this.getReadableDatabase();
        Cursor cursor = mDb.query(
                HerdsmanContract.TelefoneEntry.TABLE_NAME,
                new String[]{HerdsmanContract.TelefoneEntry.COLUMN_NAME_NUMERO},
                HerdsmanContract.TelefoneEntry.COLUMN_NAME_PESSOA_IDPESSOA + " == ?",
                new String[]{"1"},
                null,
                null,
                null
        );
        String numero = null;
        while (cursor.moveToNext()) {
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
                new String[]{HerdsmanContract.EnfermidadeEntry.COLUMN_NAME_IDENFERMIDADE, HerdsmanContract.EnfermidadeEntry.COLUMN_NAME_DESCRICAO},
                null,
                null,
                null,
                null,
                null
        );
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.EnfermidadeEntry.COLUMN_NAME_IDENFERMIDADE));
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
        Telefone telefone = new Telefone(1,1,s);
        ContentValues values = new ContentValues();
        values.put(HerdsmanContract.TelefoneEntry.COLUMN_NAME_IDTELEFONE, "1");
        values.put(HerdsmanContract.TelefoneEntry.COLUMN_NAME_PESSOA_IDPESSOA, "1");
        values.put(HerdsmanContract.TelefoneEntry.COLUMN_NAME_NUMERO, s);
        long id = mDb.insert(HerdsmanContract.TelefoneEntry.TABLE_NAME, null, values);
        if (id > 0 && !this.isSync()) {
            DatabaseReference databaseTelefone = FirebaseHelper.child(HerdsmanContract.TelefoneEntry.TABLE_NAME);
            databaseTelefone.child(String.valueOf(telefone.getIdTelefone())).setValue(telefone);
            databaseTelefone.keepSynced(true);
        }
        mDb.close();
    }

    public Animal carregarAnimal(long idAnimal) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                HerdsmanContract.AnimalEntry.TABLE_NAME,
                new String[]{HerdsmanContract.AnimalEntry.COLUMN_NAME_IDANIMAL, HerdsmanContract.AnimalEntry.COLUMN_NAME_ATIVO, HerdsmanContract.AnimalEntry.COLUMN_NAME_NUMERO, HerdsmanContract.AnimalEntry.COLUMN_NAME_NOME},
                HerdsmanContract.AnimalEntry.COLUMN_NAME_IDANIMAL + " == ?",
                new String[]{String.valueOf(idAnimal)},
                null,
                null,
                null);
        Animal animal = null;
        if (cursor.moveToNext()) {
            String nome = cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalEntry.COLUMN_NAME_NOME));
            String numero = cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalEntry.COLUMN_NAME_NUMERO));
            int ativo = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalEntry.COLUMN_NAME_ATIVO));
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalEntry.COLUMN_NAME_IDANIMAL));
            animal = new Animal(id, numero, nome, ativo);
        }
        cursor.close();
        db.close();
        return animal;
    }

    public Enfermidade carregarEnfermidade(long idEnfermidade) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                HerdsmanContract.EnfermidadeEntry.TABLE_NAME,
                new String[]{HerdsmanContract.EnfermidadeEntry.COLUMN_NAME_IDENFERMIDADE, HerdsmanContract.EnfermidadeEntry.COLUMN_NAME_DESCRICAO},
                HerdsmanContract.EnfermidadeEntry.COLUMN_NAME_IDENFERMIDADE + " == ?",
                new String[]{String.valueOf(idEnfermidade)},
                null,
                null,
                null
        );
        Enfermidade enfermidade = null;
        if (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.EnfermidadeEntry.COLUMN_NAME_IDENFERMIDADE));
            String desc = cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.EnfermidadeEntry.COLUMN_NAME_DESCRICAO));
            enfermidade = new Enfermidade(id, desc);
        }
        cursor.close();
        db.close();
        return enfermidade;
    }

    public ArrayList carregarSinistrosAnimal(Animal animal) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList list = new ArrayList();
        Cursor cursor = db.query(HerdsmanContract.AnimalEnfermidadeEntry.TABLE_NAME,
                new String[]{HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_ANIMAL_IDANIMAL, HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_DATA, HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_ENFERMIDADE_IDENFERMIDADE, HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_ANIMAL_IDANIMAL, HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_IDANIMAL_ENFERMIDADE, HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_PESSOA_IDOPESSOA},
                HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_ANIMAL_IDANIMAL + " == ?",
                new String[]{String.valueOf(animal.getId())},
                null,
                null,
                HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_DATA + " DESC");
        while (cursor.moveToNext()) {
            long idAnimalEnfermidade = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_IDANIMAL_ENFERMIDADE));
            long Animal_idAnimal = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_ANIMAL_IDANIMAL));
            long Enfermidade_idEnfermidade = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_ENFERMIDADE_IDENFERMIDADE));
            long Usuario_idUsuario = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_PESSOA_IDOPESSOA));
            long data = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_DATA));
            AnimalEnfermidade animalEnfermidade = new AnimalEnfermidade(idAnimalEnfermidade, Animal_idAnimal, Enfermidade_idEnfermidade, Usuario_idUsuario, data);
            animalEnfermidade.setAnimal(this.carregarAnimal(Animal_idAnimal));
            animalEnfermidade.setEnfermidade(this.carregarEnfermidade(Enfermidade_idEnfermidade));
            list.add(animalEnfermidade);
        }
        cursor.close();
        db.close();
        return list;
    }

    public ArrayList carregarInseminacoesAnimal(Animal animal) {
        SQLiteDatabase mDb = this.getReadableDatabase();
        Cursor cursor = mDb.query(
                HerdsmanContract.AnimalInseminacaoEntry.TABLE_NAME,
                new String[]{HerdsmanContract.AnimalInseminacaoEntry.COLUMN_NAME_ANIMAL_IDANIMAL, HerdsmanContract.AnimalInseminacaoEntry.COLUMN_NAME_DATA, HerdsmanContract.AnimalInseminacaoEntry.COLUMN_NAME_IDANIMAL_INSEMINACAO},
                HerdsmanContract.AnimalInseminacaoEntry.COLUMN_NAME_ANIMAL_IDANIMAL + " == ?",
                new String[]{String.valueOf(animal.getId())},
                null,
                null,
                HerdsmanContract.AnimalInseminacaoEntry.COLUMN_NAME_DATA + " DESC"
        );
        ArrayList lista = new ArrayList();
        while (cursor.moveToNext()) {
            long idInseminacao = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalInseminacaoEntry.COLUMN_NAME_IDANIMAL_INSEMINACAO));
            long idAnimal = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalInseminacaoEntry.COLUMN_NAME_ANIMAL_IDANIMAL));
            long data = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalInseminacaoEntry.COLUMN_NAME_DATA));
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
        if (this.isSync()) {
            values.put(HerdsmanContract.AnimalInseminacaoEntry.COLUMN_NAME_IDANIMAL_INSEMINACAO, inseminacao.getIdInseminacao());
        }
        else
        {
            Calendar calendar = Calendar.getInstance();
            inseminacao.setIdInseminacao(calendar.getTimeInMillis());
            values.put(HerdsmanContract.AnimalInseminacaoEntry.COLUMN_NAME_IDANIMAL_INSEMINACAO, inseminacao.getIdInseminacao());
        }
        long id = mDb.insert(HerdsmanContract.AnimalInseminacaoEntry.TABLE_NAME, null, values);
        if (id > 0 && !this.isSync()) {
            DatabaseReference databaseInseminacao = FirebaseHelper.child(HerdsmanContract.AnimalInseminacaoEntry.TABLE_NAME);
            databaseInseminacao.child(String.valueOf(inseminacao.getIdInseminacao())).setValue(inseminacao);
            databaseInseminacao.keepSynced(true);
        }
        mDb.close();
        return id;
    }

    public ArrayList carregarRemediosAnimal(Animal animal) {
        SQLiteDatabase mDb = this.getReadableDatabase();
        Cursor cursor = mDb.query(
                HerdsmanContract.AnimalRemedioEntry.TABLE_NAME,
                new String[]{
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
        while (cursor.moveToNext()) {
            long Animal_idAnimal = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalRemedioEntry.COLUMN_NAME_ANIMAL_IDANIMAL));
            long data = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalRemedioEntry.COLUMN_NAME_DATA));
            long Remedio_idRemedio = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalRemedioEntry.COLUMN_NAME_REMEDIO_IDREMEDIO));
            int quantidade = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalRemedioEntry.COLUMN_NAME_QUANTIDADE));
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalRemedioEntry.COLUMN_NAME_IDANIMAL_REMEDIO));
            long Medida_idMedida = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalRemedioEntry.COLUMN_NAME_MEDIDA_IDMEDIDA));
            AnimalRemedio animalRemedio = new AnimalRemedio(id, Remedio_idRemedio, Animal_idAnimal, Medida_idMedida, data, quantidade);
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

    public ArrayList carregarRemediosAnimal(Remedio remedio) {
        SQLiteDatabase mDb = this.getReadableDatabase();
        Cursor cursor = mDb.query(
                HerdsmanContract.AnimalRemedioEntry.TABLE_NAME,
                new String[]{
                        HerdsmanContract.AnimalRemedioEntry.COLUMN_NAME_ANIMAL_IDANIMAL,
                        HerdsmanContract.AnimalRemedioEntry.COLUMN_NAME_DATA,
                        HerdsmanContract.AnimalRemedioEntry.COLUMN_NAME_IDANIMAL_REMEDIO,
                        HerdsmanContract.AnimalRemedioEntry.COLUMN_NAME_QUANTIDADE,
                        HerdsmanContract.AnimalRemedioEntry.COLUMN_NAME_REMEDIO_IDREMEDIO,
                        HerdsmanContract.AnimalRemedioEntry.COLUMN_NAME_MEDIDA_IDMEDIDA
                },
                HerdsmanContract.AnimalRemedioEntry.COLUMN_NAME_REMEDIO_IDREMEDIO + " == ?",
                new String[]{String.valueOf(remedio.getIdRemedio())},
                null,
                null,
                HerdsmanContract.AnimalRemedioEntry.COLUMN_NAME_DATA + " DESC"
        );
        ArrayList list = new ArrayList();
        while (cursor.moveToNext()) {
            long Animal_idAnimal = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalRemedioEntry.COLUMN_NAME_ANIMAL_IDANIMAL));
            long data = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalRemedioEntry.COLUMN_NAME_DATA));
            long Remedio_idRemedio = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalRemedioEntry.COLUMN_NAME_REMEDIO_IDREMEDIO));
            int quantidade = cursor.getInt(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalRemedioEntry.COLUMN_NAME_QUANTIDADE));
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalRemedioEntry.COLUMN_NAME_IDANIMAL_REMEDIO));
            long Medida_idMedida = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalRemedioEntry.COLUMN_NAME_MEDIDA_IDMEDIDA));
            AnimalRemedio animalRemedio = new AnimalRemedio(id, Remedio_idRemedio, Animal_idAnimal, Medida_idMedida, data, quantidade);
            list.add(animalRemedio);
        }
        cursor.close();
        mDb.close();
        return list;
    }

    private Medida carregarMedida(long medida_idMedida) {
        Log.d("Carregar medida com id:", String.valueOf(medida_idMedida));
        SQLiteDatabase mDb = this.getReadableDatabase();
        Cursor cursor = mDb.query(
                HerdsmanContract.MedidaEntry.TABLE_NAME,
                new String[]{HerdsmanContract.MedidaEntry.COLUMN_NAME_IDMEDIDA, HerdsmanContract.MedidaEntry.COLUMN_NAME_NOME},
                HerdsmanContract.MedidaEntry.COLUMN_NAME_IDMEDIDA + " == ?",
                new String[]{String.valueOf(medida_idMedida)},
                null,
                null,
                null
        );
        Medida medida = null;
        if (cursor.moveToNext()) {
            long idMedida = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.MedidaEntry.COLUMN_NAME_IDMEDIDA));
            String nome = cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.MedidaEntry.COLUMN_NAME_NOME));
            medida = new Medida(idMedida, nome);
        }
        cursor.close();
        mDb.close();
        return medida;
    }

    private Remedio carregarRemedio(long remedio_idRemedio) {
        SQLiteDatabase mDb = this.getReadableDatabase();
        Cursor cursor = mDb.query(
                HerdsmanContract.RemedioEntry.TABLE_NAME,
                new String[]{HerdsmanContract.RemedioEntry.COLUMN_NAME_IDREMEDIO, HerdsmanContract.RemedioEntry.COLUMN_NAME_NOME},
                HerdsmanContract.RemedioEntry.COLUMN_NAME_IDREMEDIO + " ==  ?",
                new String[]{String.valueOf(remedio_idRemedio)},
                null,
                null,
                null
        );
        Remedio remedio = null;
        if (cursor.moveToNext()) {
            long idRemedio = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.RemedioEntry.COLUMN_NAME_IDREMEDIO));
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
                new String[]{HerdsmanContract.MedidaEntry.COLUMN_NAME_IDMEDIDA, HerdsmanContract.MedidaEntry.COLUMN_NAME_NOME},
                null,
                null,
                null,
                null,
                null
        );
        ArrayList arrayList = new ArrayList();
        while (cursor.moveToNext()) {
            long idMedida = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.MedidaEntry.COLUMN_NAME_IDMEDIDA));
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
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.RemedioEntry.COLUMN_NAME_IDREMEDIO));
            String nome = cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.RemedioEntry.COLUMN_NAME_NOME));
            Remedio remedio = new Remedio(id, nome);
            arrayList.add(remedio);
        }
        cursor.close();
        mDb.close();
        return arrayList;

    }

    public long inserirAnimalRemedio(AnimalRemedio animalRemedio) {

        SQLiteDatabase mDb = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(HerdsmanContract.AnimalRemedioEntry.COLUMN_NAME_DATA, animalRemedio.getData());
        values.put(HerdsmanContract.AnimalRemedioEntry.COLUMN_NAME_ANIMAL_IDANIMAL, animalRemedio.getAnimal_idAnimal());
        values.put(HerdsmanContract.AnimalRemedioEntry.COLUMN_NAME_MEDIDA_IDMEDIDA, animalRemedio.getMedida_idMedida());
        values.put(HerdsmanContract.AnimalRemedioEntry.COLUMN_NAME_REMEDIO_IDREMEDIO, animalRemedio.getRemedio_idRemedio());
        values.put(HerdsmanContract.AnimalRemedioEntry.COLUMN_NAME_QUANTIDADE, animalRemedio.getQuantidade());

        if (this.isSync()) {
            values.put(HerdsmanContract.AnimalRemedioEntry.COLUMN_NAME_IDANIMAL_REMEDIO, animalRemedio.getIdAnimalRemedio());
        }
        else
        {
            Calendar calendar = Calendar.getInstance();
            animalRemedio.setIdAnimalRemedio(calendar.getTimeInMillis());
            values.put(HerdsmanContract.AnimalRemedioEntry.COLUMN_NAME_IDANIMAL_REMEDIO, animalRemedio.getIdAnimalRemedio());
        }
        long id = mDb.insert(HerdsmanContract.AnimalRemedioEntry.TABLE_NAME, null, values);
        if (id > 0 && !this.isSync()) {
            DatabaseReference databaseAnimalRemedio = FirebaseHelper.child(HerdsmanContract.AnimalRemedioEntry.TABLE_NAME);
            databaseAnimalRemedio.child(String.valueOf(animalRemedio.getIdAnimalRemedio())).setValue(animalRemedio);
            databaseAnimalRemedio.keepSynced(true);
        }
        mDb.close();
        return id;
    }

    public ArrayList carregarSinistrosFuncionario(Pessoa pessoa) {
        SQLiteDatabase mDb = this.getReadableDatabase();
        Cursor cursor = mDb.query(
                HerdsmanContract.AnimalEnfermidadeEntry.TABLE_NAME,
                new String[]{
                        HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_PESSOA_IDOPESSOA,
                        HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_IDANIMAL_ENFERMIDADE,
                        HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_ENFERMIDADE_IDENFERMIDADE,
                        HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_DATA,
                        HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_ANIMAL_IDANIMAL},
                HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_PESSOA_IDOPESSOA + " == ?",
                new String[]{String.valueOf(pessoa.getIdPessoa())},
                null,
                null,
                HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_DATA + " DESC"
        );
        ArrayList arrayList = new ArrayList();
        while (cursor.moveToNext()) {
            long usuario_idUsuario = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_PESSOA_IDOPESSOA));
            long idAnimalEnfermidade = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_IDANIMAL_ENFERMIDADE));
            long enfermidade_idEnfermidade = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_ENFERMIDADE_IDENFERMIDADE));
            long animal_idAnimal = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_ANIMAL_IDANIMAL));
            long data = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_DATA));
            AnimalEnfermidade animalEnfermidade = new AnimalEnfermidade(idAnimalEnfermidade, animal_idAnimal, enfermidade_idEnfermidade, usuario_idUsuario, data);
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
                new String[]{
                        HerdsmanContract.CioEntry.COLUMN_NAME_ANIMAL_IDANIMALPORCIMA,
                        HerdsmanContract.CioEntry.COLUMN_NAME_ANIMAL_IDANIMALPORBAIXO,
                        HerdsmanContract.CioEntry.COLUMN_NAME_DATA,
                        HerdsmanContract.CioEntry.COLUMN_NAME_IDANIMAL_CIO,
                        HerdsmanContract.CioEntry.COLUMN_NAME_PESSOA_IDPESSOA
                },
                HerdsmanContract.CioEntry.COLUMN_NAME_PESSOA_IDPESSOA + " == ?",
                new String[]{String.valueOf(pessoa.getIdPessoa())},
                null,
                null,
                HerdsmanContract.CioEntry.COLUMN_NAME_DATA + " DESC"
        );
        ArrayList arrayList = new ArrayList();
        while (cursor.moveToNext()) {
            long idAnimal_Cio = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.CioEntry.COLUMN_NAME_IDANIMAL_CIO));
            long animal_idAnimalPorCima = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.CioEntry.COLUMN_NAME_ANIMAL_IDANIMALPORCIMA));
            long animal_idAnimalPorBaixo = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.CioEntry.COLUMN_NAME_ANIMAL_IDANIMALPORBAIXO));
            long usuario_idUsuario = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.CioEntry.COLUMN_NAME_PESSOA_IDPESSOA));
            long data = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.CioEntry.COLUMN_NAME_DATA));
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
        SQLiteDatabase mDb = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(HerdsmanContract.TelefoneEntry.COLUMN_NAME_PESSOA_IDPESSOA, telefone.getPessoa_idPessoa());
        values.put(HerdsmanContract.TelefoneEntry.COLUMN_NAME_NUMERO, telefone.getNumero());

        if (this.isSync()) {
            values.put(HerdsmanContract.TelefoneEntry.COLUMN_NAME_IDTELEFONE, telefone.getIdTelefone());
        }
        else
        {
            Calendar calendar = Calendar.getInstance();
            telefone.setIdTelefone(calendar.getTimeInMillis());
            values.put(HerdsmanContract.TelefoneEntry.COLUMN_NAME_IDTELEFONE, telefone.getIdTelefone());
        }
        long id = mDb.insert(HerdsmanContract.TelefoneEntry.TABLE_NAME, null, values);
        if (id > 0 && !this.isSync()) {
            DatabaseReference databaseTelefone = FirebaseHelper.child(HerdsmanContract.TelefoneEntry.TABLE_NAME);
            databaseTelefone.child(String.valueOf(telefone.getIdTelefone())).setValue(telefone);
            databaseTelefone.keepSynced(true);
        }
        mDb.close();
        return id;
    }

    public int deletaTelefone(Telefone telefone) {
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
        if(delete>0)
        {
            DatabaseReference databaseTelefone = FirebaseHelper.child(HerdsmanContract.TelefoneEntry.TABLE_NAME).child(String.valueOf(telefone.getIdTelefone()));
            databaseTelefone.removeValue();
            databaseTelefone.keepSynced(true);
        }
        mDb.close();
        return delete;
    }

    public int deletaParto(Parto parto)
    {
        SQLiteDatabase mDb = this.getWritableDatabase();
        String where = HerdsmanContract.PartoEntry.COLUMN_NAME_IDPARTO + "== ?";
        String[] whereArgs =
                {
                        String.valueOf(parto.getId())
                };
        int delete = mDb.delete(
                HerdsmanContract.PartoEntry.TABLE_NAME,
                where,
                whereArgs);
        if(delete>0)
        {
            DatabaseReference databaseTelefone = FirebaseHelper.child(HerdsmanContract.PartoEntry.TABLE_NAME).child(String.valueOf(parto.getId()));
            databaseTelefone.removeValue();
            databaseTelefone.keepSynced(true);
        }
        mDb.close();
        return delete;
    }
    public int deletaCio(Cio cio)
    {
        SQLiteDatabase mDb = this.getWritableDatabase();
        String where = HerdsmanContract.CioEntry.COLUMN_NAME_IDANIMAL_CIO + "== ?";
        String[] whereArgs =
                {
                        String.valueOf(cio.getIdCio())
                };
        int delete = mDb.delete(
                HerdsmanContract.CioEntry.TABLE_NAME,
                where,
                whereArgs);
        if(delete>0)
        {
            DatabaseReference databaseTelefone = FirebaseHelper.child(HerdsmanContract.CioEntry.TABLE_NAME).child(String.valueOf(cio.getIdCio()));
            databaseTelefone.removeValue();
            databaseTelefone.keepSynced(true);
        }
        mDb.close();
        return delete;
    }
    public int deletaInseminacao(Inseminacao inseminacao)
    {
        SQLiteDatabase mDb = this.getWritableDatabase();
        String where = HerdsmanContract.AnimalInseminacaoEntry.COLUMN_NAME_IDANIMAL_INSEMINACAO+ "== ?";
        String[] whereArgs =
                {
                        String.valueOf(inseminacao.getIdInseminacao())
                };
        int delete = mDb.delete(
                HerdsmanContract.AnimalInseminacaoEntry.TABLE_NAME,
                where,
                whereArgs);
        if(delete>0)
        {
            DatabaseReference databaseTelefone = FirebaseHelper.child(HerdsmanContract.AnimalInseminacaoEntry.TABLE_NAME).child(String.valueOf(inseminacao.getIdInseminacao()));
            databaseTelefone.removeValue();
            databaseTelefone.keepSynced(true);
        }
        mDb.close();
        return delete;
    }
    public int deletaAnimalRemedio(AnimalRemedio animalRemedio)
    {
        SQLiteDatabase mDb = this.getWritableDatabase();
        String where = HerdsmanContract.AnimalRemedioEntry.COLUMN_NAME_IDANIMAL_REMEDIO+ "== ?";
        String[] whereArgs =
                {
                        String.valueOf(animalRemedio.getIdAnimalRemedio())
                };
        int delete = mDb.delete(
                HerdsmanContract.AnimalRemedioEntry.TABLE_NAME,
                where,
                whereArgs);
        if(delete>0)
        {
            DatabaseReference databaseTelefone = FirebaseHelper.child(HerdsmanContract.AnimalRemedioEntry.TABLE_NAME).child(String.valueOf(animalRemedio.getIdAnimalRemedio()));
            databaseTelefone.removeValue();
            databaseTelefone.keepSynced(true);
        }
        mDb.close();
        return delete;
    }
    public int deletaAnimalEnfermidade(AnimalEnfermidade animalEnfermidade)
    {
        SQLiteDatabase mDb = this.getWritableDatabase();
        String where = HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_IDANIMAL_ENFERMIDADE+ "== ?";
        String[] whereArgs =
                {
                        String.valueOf(animalEnfermidade.getIdAnimalEnfermidade())
                };
        int delete = mDb.delete(
                HerdsmanContract.AnimalEnfermidadeEntry.TABLE_NAME,
                where,
                whereArgs);
        if(delete>0)
        {
            DatabaseReference databaseTelefone = FirebaseHelper.child(HerdsmanContract.AnimalEnfermidadeEntry.TABLE_NAME).child(String.valueOf(animalEnfermidade.getIdAnimalEnfermidade()));
            databaseTelefone.removeValue();
            databaseTelefone.keepSynced(true);
        }
        mDb.close();
        return delete;
    }

    public int deletaPessoa(Pessoa pessoa)
    {
        SQLiteDatabase mDb = this.getWritableDatabase();
        String where = HerdsmanContract.PessoaEntry.COLUMN_NAME_IDPESSOA+ "== ?";
        String[] whereArgs =
                {
                        String.valueOf(pessoa.getIdPessoa())
                };
        int delete = mDb.delete(
                HerdsmanContract.PessoaEntry.TABLE_NAME,
                where,
                whereArgs);
        if(delete>0)
        {
            DatabaseReference database = FirebaseHelper.child(HerdsmanContract.PessoaEntry.TABLE_NAME).child(String.valueOf(pessoa.getIdPessoa()));
            database.removeValue();
            database.keepSynced(true);
        }
        mDb.close();
        return delete;
    }

    public long inserirAdministradorNotificaPessoa(AdministradorNotificaPessoa outro) {

        SQLiteDatabase mDb = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(HerdsmanContract.AdministradorNotificaPessoaEntry.COLUMN_NAME_DESCRICAO, outro.getMensagem());
        values.put(HerdsmanContract.AdministradorNotificaPessoaEntry.COLUMN_NAME_DATA, outro.getData());
        values.put(HerdsmanContract.AdministradorNotificaPessoaEntry.COLUMN_NAME_ADMINISTRADOR_IDNOTIFICA, outro.getIdAdministrador());
        if (this.isSync()) {
            values.put(HerdsmanContract.AdministradorNotificaPessoaEntry.COLUMN_NAME_IDADMINISTRADOR_NOTIFICA_PESSOA, outro.getIdAdministradorNotificaPessoa());
        }
        else
        {
            values.put(HerdsmanContract.AdministradorNotificaPessoaEntry.COLUMN_NAME_IDADMINISTRADOR_NOTIFICA_PESSOA, outro.getIdAdministradorNotificaPessoa());

        }
        long id = mDb.insertWithOnConflict(HerdsmanContract.AdministradorNotificaPessoaEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        if (id > 0 && !this.isSync()) {
            DatabaseReference databaseOutro = FirebaseHelper.child(HerdsmanContract.AdministradorNotificaPessoaEntry.TABLE_NAME);
            databaseOutro.child(String.valueOf(outro.getIdAdministradorNotificaPessoa())).setValue(outro);
            databaseOutro.keepSynced(true);
        }
        mDb.close();
        return id;
    }

    public ArrayList carregarTodosAdministradorNotificaPessoa() {
        SQLiteDatabase mDb = this.getReadableDatabase();
        Cursor cursor = mDb.query(
                HerdsmanContract.AdministradorNotificaPessoaEntry.TABLE_NAME,
                new String[]{
                        HerdsmanContract.AdministradorNotificaPessoaEntry.COLUMN_NAME_ADMINISTRADOR_IDNOTIFICA,
                        HerdsmanContract.AdministradorNotificaPessoaEntry.COLUMN_NAME_IDADMINISTRADOR_NOTIFICA_PESSOA,
                        HerdsmanContract.AdministradorNotificaPessoaEntry.COLUMN_NAME_DATA,
                        HerdsmanContract.AdministradorNotificaPessoaEntry.COLUMN_NAME_DESCRICAO
                },
                null,
                null,
                null,
                null,
                HerdsmanContract.AdministradorNotificaPessoaEntry.COLUMN_NAME_DATA + " DESC"

        );
        ArrayList lista = new ArrayList();
        while (cursor.moveToNext()) {
            long idNotifica = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.AdministradorNotificaPessoaEntry.COLUMN_NAME_IDADMINISTRADOR_NOTIFICA_PESSOA));
            long idAdmin = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.AdministradorNotificaPessoaEntry.COLUMN_NAME_ADMINISTRADOR_IDNOTIFICA));
            long data = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.AdministradorNotificaPessoaEntry.COLUMN_NAME_DATA));
            String mensagem = cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.AdministradorNotificaPessoaEntry.COLUMN_NAME_DESCRICAO));
            AdministradorNotificaPessoa outro = new AdministradorNotificaPessoa(idNotifica, mensagem, data, idAdmin);
            lista.add(outro);
        }
        cursor.close();
        mDb.close();
        return lista;
    }

    public  void deleteDatabase(Context mContext) {

        mContext.deleteDatabase(HerdsmanDbHelper.DB_NAME);
    }

    public void limparDatabase(Context mContext)
    {
        SQLiteDatabase md = this.getWritableDatabase();
        md.delete(HerdsmanContract.CioEntry.TABLE_NAME,null,null);
        md.delete(HerdsmanContract.PartoEntry.TABLE_NAME, null, null);
        md.delete(HerdsmanContract.AnimalEnfermidadeEntry.TABLE_NAME, null, null);
        md.delete(HerdsmanContract.AnimalRemedioEntry.TABLE_NAME, null , null);
        md.delete(HerdsmanContract.AnimalInseminacaoEntry.TABLE_NAME, null , null);
        md.delete(HerdsmanContract.AnimalEntry.TABLE_NAME, null, null);
        md.delete(HerdsmanContract.AdministradorNotificaPessoaEntry.TABLE_NAME, null, null);
        md.delete(HerdsmanContract.TelefoneEntry.TABLE_NAME, null,null);
        md.delete(HerdsmanContract.RemedioEntry.TABLE_NAME, null, null);
        md.delete(HerdsmanContract.EnfermidadeEntry.TABLE_NAME, null,null);
        md.delete(HerdsmanContract.PessoaEntry.TABLE_NAME, null,null);


        md.close();
    }

    public void replaceEnfermidade(Enfermidade enfermidade)     {
        //TODO Atualizar no firebase FEITO
        SQLiteDatabase mDb = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HerdsmanContract.EnfermidadeEntry.COLUMN_NAME_IDENFERMIDADE, enfermidade.getId());
        values.put(HerdsmanContract.EnfermidadeEntry.COLUMN_NAME_DESCRICAO, enfermidade.getDescricao());
        long id = mDb.replace(
                HerdsmanContract.EnfermidadeEntry.TABLE_NAME,
                null,
                values
        );
        if (id > 0) {
            DatabaseReference database = FirebaseHelper.child(HerdsmanContract.EnfermidadeEntry.TABLE_NAME);
            database.child(String.valueOf(enfermidade.getId())).setValue(enfermidade);
            database.keepSynced(true);
        }
        mDb.close();
    }

    public void replaceRemedio(Remedio remedio) {
        //TODO Atualizar no firebase FEITO
        SQLiteDatabase mDb = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HerdsmanContract.RemedioEntry.COLUMN_NAME_IDREMEDIO, remedio.getIdRemedio());
        values.put(HerdsmanContract.RemedioEntry.COLUMN_NAME_NOME, remedio.getNome());
        long id = mDb.replace(
                HerdsmanContract.RemedioEntry.TABLE_NAME,
                null,
                values
        );
        if (id > 0) {
            DatabaseReference database = FirebaseHelper.child(HerdsmanContract.RemedioEntry.TABLE_NAME);
            database.child(String.valueOf(remedio.getIdRemedio())).setValue(remedio);
            database.keepSynced(true);
        }
        mDb.close();
    }

    public boolean searchDuplicateFuncionario(String cpf)
    {
        SQLiteDatabase mDb = this.getReadableDatabase();
        Cursor cursor = mDb.query(
                HerdsmanContract.PessoaEntry.TABLE_NAME,
                new String[]{HerdsmanContract.PessoaEntry.COLUMN_NAME_CPF},
                HerdsmanContract.PessoaEntry.COLUMN_NAME_CPF + "== ?",
                new String[]{String.valueOf(cpf)},
                null,
                null,
                null
        );
        boolean ret;
        if (cursor.getCount() > 0) {
            ret = true;
        } else {
            ret = false;
        }
        cursor.close();
        mDb.close();
        return ret;
    }

    public boolean searchDuplicateRemedio(String nome)
    {
        SQLiteDatabase mDb = this.getReadableDatabase();
        Cursor cursor = mDb.query(
                HerdsmanContract.RemedioEntry.TABLE_NAME,
                new String[]{HerdsmanContract.RemedioEntry.COLUMN_NAME_NOME},
                HerdsmanContract.RemedioEntry.COLUMN_NAME_NOME + "== ? COLLATE NOCASE",
                new String[]{String.valueOf(nome)},
                null,
                null,
                null
        );
        boolean ret;
        if (cursor.getCount() > 0) {
            ret = true;
        } else {
            ret = false;
        }
        cursor.close();
        mDb.close();
        return ret;
    }

    public boolean searchDuplicateEnfermidade(String nome)
    {
        SQLiteDatabase mDb = this.getReadableDatabase();
        Cursor cursor = mDb.query(
                HerdsmanContract.EnfermidadeEntry.TABLE_NAME,
                new String[]{HerdsmanContract.EnfermidadeEntry.COLUMN_NAME_DESCRICAO},
                HerdsmanContract.EnfermidadeEntry.COLUMN_NAME_DESCRICAO + "== ? COLLATE NOCASE",
                new String[]{String.valueOf(nome)},
                null,
                null,
                null
        );
        boolean ret;
        if (cursor.getCount() > 0) {
            ret = true;
        } else {
            ret = false;
        }
        cursor.close();
        mDb.close();
        return ret;
    }

    public void searchDuplicateAnimals()
    {
        ArrayList<Animal> animals = (ArrayList) this.carregarAnimaisDb();
        ArrayList<Animal> merged_animals = new ArrayList<>();
        for (Animal animal:animals) {
            for (Animal animal2:animals) {
                if((animal.getNome().equalsIgnoreCase(animal2.getNome())) && (animal.getNumero().equalsIgnoreCase(animal2.getNumero())) && animal.getId() != animal2.getId() && !merged_animals.contains(animal))
                {
                    merged_animals.add(animal2);
                    mergeAnimal(animal, animal2);
                }
            }
        }
    }

    public void searchDuplicateRemedios()
    {
        Log.d(TAG, "SearchRemedios");
        ArrayList<Remedio> listaremedios = (ArrayList) this.carregarTodosRemedios();
        ArrayList<Remedio> merged_remedios = new ArrayList<>();
        for (Remedio remedio:listaremedios) {
            for (Remedio remedio2 :listaremedios) {
                if((remedio.getNome().equalsIgnoreCase(remedio2.getNome())) && remedio.getIdRemedio() != remedio2.getIdRemedio() && !merged_remedios.contains(remedio))
                {
                    Log.d(TAG, "FoundRemedios");
                    merged_remedios.add(remedio2);
                    mergeRemedio(remedio, remedio2);
                }
            }
        }
    }

    public void deletaRemedio(Remedio remedio)
    {
        Log.d(TAG, "deleteRemedio: " + remedio.getIdRemedio());
        SQLiteDatabase mDb = this.getWritableDatabase();
        mDb.delete(HerdsmanContract.RemedioEntry.TABLE_NAME,
                HerdsmanContract.RemedioEntry.COLUMN_NAME_IDREMEDIO + " ==  ?",
                new String[]{String.valueOf(remedio.getIdRemedio())}
        );
        DatabaseReference databaseAnimal = FirebaseHelper.child(HerdsmanContract.RemedioEntry.TABLE_NAME).child(String.valueOf(remedio.getIdRemedio()));
        databaseAnimal.removeValue();
        databaseAnimal.keepSynced(true);
        mDb.close();
    }

    public void mergeRemedio(Remedio old_remedio, Remedio new_remedio)
    {
        Log.d(TAG, "mergeRemedio: " + old_remedio.getIdRemedio() + " e " +new_remedio.getIdRemedio());
        ArrayList<AnimalRemedio> listaAnimalRemedio = this.carregarRemediosAnimal(new_remedio);
        for (AnimalRemedio animalRemedio:listaAnimalRemedio) {
            animalRemedio.setRemedio_idRemedio(old_remedio.getIdRemedio());
            this.replaceAnimalRemedio(animalRemedio);
        }
        this.deletaRemedio(new_remedio);
    }

    public void searchDuplicateSinistros()
    {
        ArrayList<Enfermidade> enfermidades = (ArrayList) this.carregarEnfermidades();
        ArrayList<Enfermidade> merged_enfermidades = new ArrayList<>();
        for (Enfermidade enf1:enfermidades) {
            for (Enfermidade enf2:enfermidades) {
                if((enf1.getDescricao().equalsIgnoreCase(enf2.getDescricao()))  && enf1.getId() != enf2.getId() && !merged_enfermidades.contains(enf1))
                {
                    merged_enfermidades.add(enf2);
                    mergeEnfermidade(enf1, enf2);
                }
            }
        }

    }

    public void searchDuplicatePessoa()
    {
        ArrayList<Pessoa> pessoas = (ArrayList) this.carregarFuncionariosDb();
        ArrayList<Pessoa> merged_pessoas = new ArrayList<>();
        for (Pessoa pessoa1 : pessoas) {
            for (Pessoa pessoa2 : pessoas) {
                if((pessoa1.getNome().equalsIgnoreCase(pessoa2.getNome()))  && pessoa1.getIdPessoa() != pessoa2.getIdPessoa() && !merged_pessoas.contains(pessoa1))
                {
                    merged_pessoas.add(pessoa2);
                    mergePessoas(pessoa1, pessoa2);
                }
            }
        }
    }

    public void mergePessoas(Pessoa old_pessoa, Pessoa new_pessoa)
    {
        Log.d(TAG, "mergePessoas: " + old_pessoa.getIdPessoa() + " e " +new_pessoa.getIdPessoa());
        ArrayList<Cio> listaCios = this.carregarCiosFuncionario(new_pessoa);
        for (Cio cio : listaCios) {
            cio.setIdFuncionario(old_pessoa.getIdPessoa());
            this.replaceCio(cio);
        }
        ArrayList<AnimalEnfermidade> listaAnimalEnf = this.carregarSinistrosFuncionario(new_pessoa);
        for (AnimalEnfermidade animalEnf : listaAnimalEnf) {
            animalEnf.setIdFuncionario(old_pessoa.getIdPessoa());
            this.replaceAnimalEnfermidade(animalEnf);
        }
        ArrayList<Telefone> listaTelefones = this.carregarTelefonesPessoa(new_pessoa);
        for (Telefone telef : listaTelefones) {
            telef.setPessoa_idPessoa(old_pessoa.getIdPessoa());
            this.replaceTelefone(telef);
        }
        this.deletaPessoa(new_pessoa);
    }

    public ArrayList carregarSinistrosAnimal(Enfermidade enfermidade)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList list = new ArrayList();
        Cursor cursor = db.query(HerdsmanContract.AnimalEnfermidadeEntry.TABLE_NAME,
                new String[]{HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_ANIMAL_IDANIMAL, HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_DATA, HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_ENFERMIDADE_IDENFERMIDADE, HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_ANIMAL_IDANIMAL, HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_IDANIMAL_ENFERMIDADE, HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_PESSOA_IDOPESSOA},
                HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_ENFERMIDADE_IDENFERMIDADE + " == ?",
                new String[]{String.valueOf(enfermidade.getId())},
                null,
                null,
                HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_DATA + " DESC");
        while (cursor.moveToNext()) {
            long idAnimalEnfermidade = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_IDANIMAL_ENFERMIDADE));
            long Animal_idAnimal = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_ANIMAL_IDANIMAL));
            long Enfermidade_idEnfermidade = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_ENFERMIDADE_IDENFERMIDADE));
            long Usuario_idUsuario = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_PESSOA_IDOPESSOA));
            long data = cursor.getLong(cursor.getColumnIndexOrThrow(HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_DATA));
            AnimalEnfermidade animalEnfermidade = new AnimalEnfermidade(idAnimalEnfermidade, Animal_idAnimal, Enfermidade_idEnfermidade, Usuario_idUsuario, data);
            animalEnfermidade.setAnimal(this.carregarAnimal(Animal_idAnimal));
            animalEnfermidade.setEnfermidade(this.carregarEnfermidade(Enfermidade_idEnfermidade));
            list.add(animalEnfermidade);
        }
        cursor.close();
        db.close();
        return list;
    }

    public void mergeEnfermidade(Enfermidade old_enfermidade, Enfermidade new_enfermidade)
    {
        Log.d(TAG, "mergeEnfermidade: " + old_enfermidade.getId() + " e " +new_enfermidade.getId());
        ArrayList<Enfermidade> listaEnfermidade = this.carregarSinistrosAnimal(new_enfermidade);
        for (Enfermidade enfermidade :listaEnfermidade) {
            enfermidade.setId(old_enfermidade.getId());
            this.replaceEnfermidade(enfermidade);
        }
        this.deletaEnfermidade(new_enfermidade);
    }
    public void deletaEnfermidade(Enfermidade enfermidade)
    {
        Log.d(TAG, "deletaEnfermidade: " + enfermidade.getId());
        SQLiteDatabase mDb = this.getWritableDatabase();
        mDb.delete(HerdsmanContract.EnfermidadeEntry.TABLE_NAME,
                HerdsmanContract.EnfermidadeEntry.COLUMN_NAME_IDENFERMIDADE+ " ==  ?",
                new String[]{String.valueOf(enfermidade.getId())}
        );
        DatabaseReference databaseAnimal = FirebaseHelper.child(HerdsmanContract.EnfermidadeEntry.TABLE_NAME).child(String.valueOf(enfermidade.getId()));
        databaseAnimal.removeValue();
        databaseAnimal.keepSynced(true);
        mDb.close();
    }

    public void mergeAnimal(Animal old_animal, Animal new_animal)
    {
        Log.d(TAG, "mergeAnimal: " + old_animal.getId() + " e " +new_animal.getId());
        ArrayList<AnimalEnfermidade> listaAnimalEnfermidades = this.carregarSinistrosAnimal(new_animal);
        for (AnimalEnfermidade animalEnfermidade : listaAnimalEnfermidades) {
            animalEnfermidade.setIdAnimal(old_animal.getId());
            this.replaceAnimalEnfermidade(animalEnfermidade);
        }
        ArrayList<Parto> listaPartos = this.carregarPartosAnimal(new_animal);
        for (Parto parto:
             listaPartos) {
            parto.setAnimal_idAnimal(old_animal.getId());
            this.replaceParto(parto);
        }
        ArrayList<Cio> listaCios = this.carregarCiosAnimal(new_animal);
        for (Cio cio:
             listaCios) {
            if(cio.getIdAnimalPorBaixo() == new_animal.getId())
            {
                cio.setIdAnimalPorBaixo(old_animal.getId());
            }
            else
            {
                cio.setIdAnimalPorCima(old_animal.getId());
            }
            this.replaceCio(cio);

        }
        ArrayList<Inseminacao> listaInseminacao = this.carregarInseminacoesAnimal(new_animal);
        for (Inseminacao inseminacao: listaInseminacao
             ) {
            inseminacao.setIdAnimal(old_animal.getId());
            this.replaceInseminacao(inseminacao);

        }
        ArrayList<AnimalRemedio> listaAnimalRemedio = this.carregarRemediosAnimal(new_animal);
        for (AnimalRemedio animalRemedio:listaAnimalRemedio
             ) {
            animalRemedio.setAnimal_idAnimal(old_animal.getId());
            this.replaceAnimalRemedio(animalRemedio);

        }
        this.deleteAnimal(new_animal);
    }

    private void deleteAnimal(Animal new_animal) {
        Log.d(TAG, "deleteAnimal: " + new_animal.getNumero());
        SQLiteDatabase mDb = this.getWritableDatabase();
        mDb.delete(HerdsmanContract.AnimalEntry.TABLE_NAME,
                HerdsmanContract.AnimalEntry.COLUMN_NAME_IDANIMAL + " ==  ?",
                new String[]{String.valueOf(new_animal.getId())}
        );
        DatabaseReference databaseAnimal = FirebaseHelper.child(HerdsmanContract.AnimalEntry.TABLE_NAME).child(String.valueOf(new_animal.getId()));
        databaseAnimal.removeValue();
        databaseAnimal.keepSynced(true);
        mDb.close();
    }

    private long replaceAnimalRemedio(AnimalRemedio animalRemedio) {
        SQLiteDatabase mDb = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HerdsmanContract.AnimalRemedioEntry.COLUMN_NAME_DATA, animalRemedio.getData());
        values.put(HerdsmanContract.AnimalRemedioEntry.COLUMN_NAME_ANIMAL_IDANIMAL, animalRemedio.getAnimal_idAnimal());
        values.put(HerdsmanContract.AnimalRemedioEntry.COLUMN_NAME_MEDIDA_IDMEDIDA, animalRemedio.getMedida_idMedida());
        values.put(HerdsmanContract.AnimalRemedioEntry.COLUMN_NAME_REMEDIO_IDREMEDIO, animalRemedio.getRemedio_idRemedio());
        values.put(HerdsmanContract.AnimalRemedioEntry.COLUMN_NAME_QUANTIDADE, animalRemedio.getQuantidade());
        values.put(HerdsmanContract.AnimalRemedioEntry.COLUMN_NAME_IDANIMAL_REMEDIO, animalRemedio.getIdAnimalRemedio());

        long id = mDb.replace(HerdsmanContract.AnimalRemedioEntry.TABLE_NAME, null, values);
        if (id > 0 && !this.isSync()) {
            DatabaseReference databaseAnimalRemedio = FirebaseHelper.child(HerdsmanContract.AnimalRemedioEntry.TABLE_NAME);
            databaseAnimalRemedio.child(String.valueOf(animalRemedio.getIdAnimalRemedio())).setValue(animalRemedio);
            databaseAnimalRemedio.keepSynced(true);
        }
        mDb.close();
        return id;
    }

    private long replaceInseminacao(Inseminacao inseminacao) {
        SQLiteDatabase mDb = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HerdsmanContract.AnimalInseminacaoEntry.COLUMN_NAME_IDANIMAL_INSEMINACAO, inseminacao.getIdInseminacao());
        values.put(HerdsmanContract.AnimalInseminacaoEntry.COLUMN_NAME_ANIMAL_IDANIMAL, inseminacao.getIdAnimal());
        values.put(HerdsmanContract.AnimalInseminacaoEntry.COLUMN_NAME_DATA, inseminacao.getData());
        long id = mDb.replace(HerdsmanContract.AnimalInseminacaoEntry.TABLE_NAME, null, values);
        if (id > 0 && !this.isSync()) {
            DatabaseReference database = FirebaseHelper.child(HerdsmanContract.AnimalInseminacaoEntry.TABLE_NAME);
            database.child(String.valueOf(inseminacao.getIdInseminacao())).setValue(inseminacao);
            database.keepSynced(true);
        }
        mDb.close();
        return id;
    }

    private long replaceCio(Cio cio) {
        SQLiteDatabase mDb = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HerdsmanContract.CioEntry.COLUMN_NAME_IDANIMAL_CIO, cio.getIdCio());
        values.put(HerdsmanContract.CioEntry.COLUMN_NAME_ANIMAL_IDANIMALPORBAIXO, cio.getIdAnimalPorBaixo());
        values.put(HerdsmanContract.CioEntry.COLUMN_NAME_ANIMAL_IDANIMALPORCIMA, cio.getIdAnimalPorCima());
        values.put(HerdsmanContract.CioEntry.COLUMN_NAME_PESSOA_IDPESSOA, cio.getIdFuncionario());
        values.put(HerdsmanContract.CioEntry.COLUMN_NAME_DATA, cio.getData());
        long id = mDb.replace(HerdsmanContract.CioEntry.TABLE_NAME, null, values);
        if (id > 0 && !this.isSync()) {
            DatabaseReference database = FirebaseHelper.child(HerdsmanContract.CioEntry.TABLE_NAME);
            database.child(String.valueOf(cio.getIdCio())).setValue(cio);
            database.keepSynced(true);
        }
        mDb.close();
        return id;
    }

    private long replaceParto(Parto parto)
    {
        SQLiteDatabase mDb = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HerdsmanContract.PartoEntry.COLUMN_NAME_ANIMAL_IDANIMAL, parto.getAnimal_idAnimal());
        values.put(HerdsmanContract.PartoEntry.COLUMN_NAME_IDPARTO, parto.getId());
        values.put(HerdsmanContract.PartoEntry.COLUMN_NAME_CRIA, parto.getCria());
        values.put(HerdsmanContract.PartoEntry.COLUMN_NAME_DATA, parto.getData());
        long id = mDb.replace(HerdsmanContract.PartoEntry.TABLE_NAME, null, values);
        if (id > 0 && !this.isSync()) {
            DatabaseReference database = FirebaseHelper.child(HerdsmanContract.PartoEntry.TABLE_NAME);
            database.child(String.valueOf(parto.getId())).setValue(parto);
            database.keepSynced(true);
        }
        mDb.close();
        return id;
    }
    private long replaceTelefone(Telefone telefone) {
        SQLiteDatabase mDb = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HerdsmanContract.TelefoneEntry.COLUMN_NAME_PESSOA_IDPESSOA, telefone.getPessoa_idPessoa());
        values.put(HerdsmanContract.TelefoneEntry.COLUMN_NAME_IDTELEFONE, telefone.getIdTelefone());
        values.put(HerdsmanContract.TelefoneEntry.COLUMN_NAME_NUMERO, telefone.getNumero());
        long id = mDb.replace(HerdsmanContract.TelefoneEntry.TABLE_NAME, null, values);
        if (id > 0 && !this.isSync()) {
            DatabaseReference database = FirebaseHelper.child(HerdsmanContract.TelefoneEntry.TABLE_NAME);
            database.child(String.valueOf(telefone.getIdTelefone())).setValue(telefone);
            database.keepSynced(true);
        }
        mDb.close();
        return id;
    }

    public long replaceAnimalEnfermidade(AnimalEnfermidade animalEnfermidade) {
        SQLiteDatabase mDb = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_ANIMAL_IDANIMAL, animalEnfermidade.getIdAnimal());
        values.put(HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_ENFERMIDADE_IDENFERMIDADE, animalEnfermidade.getIdEnfermidade());
        values.put(HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_PESSOA_IDOPESSOA, animalEnfermidade.getIdFuncionario());
        values.put(HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_DATA, animalEnfermidade.getData());
        values.put(HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_IDANIMAL_ENFERMIDADE, animalEnfermidade.getIdAnimalEnfermidade());
        long id = mDb.replace(HerdsmanContract.AnimalEnfermidadeEntry.TABLE_NAME, null, values);
        if (id > 0 && !this.isSync()) {
            DatabaseReference database = FirebaseHelper.child(HerdsmanContract.AnimalEnfermidadeEntry.TABLE_NAME);
            database.child(String.valueOf(animalEnfermidade.getIdAnimalEnfermidade())).setValue(animalEnfermidade);
            database.keepSynced(true);
        }
        mDb.close();
        return id;

    }

    public long removerMensagemPendente(long id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        long del = db.delete(
                HerdsmanContract.MensagemPendenteEntry.TABLE_NAME,
                HerdsmanContract.MensagemPendenteEntry.COLUMN_NAME_IDMENSAGEM_PENDENTE + " == ? ",
                new String[]{String.valueOf(id)}
        );
        return del;
    }

    public long inserirMensagemPendente(MensagemPendente mensagem) {
        ContentValues values = new ContentValues();
        values.put(HerdsmanContract.MensagemPendenteEntry.COLUMN_NAME_IDMENSAGEM_PENDENTE, mensagem.getId());
        values.put(HerdsmanContract.MensagemPendenteEntry.COLUMN_NAME_NUMERO, mensagem.getNumero());
        values.put(HerdsmanContract.MensagemPendenteEntry.COLUMN_NAME_TEXTO, mensagem.getText());
        SQLiteDatabase db = this.getWritableDatabase();
        long insert = db.insert(
                HerdsmanContract.MensagemPendenteEntry.TABLE_NAME,
                null,
                values
        );
        db.close();
        return insert;

    }

    public ArrayList<MensagemPendente> carregarMensagensPendentes() {
        ArrayList<MensagemPendente> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                HerdsmanContract.MensagemPendenteEntry.TABLE_NAME,
                new String[] {
                        HerdsmanContract.MensagemPendenteEntry.COLUMN_NAME_IDMENSAGEM_PENDENTE,
                        HerdsmanContract.MensagemPendenteEntry.COLUMN_NAME_TEXTO,
                        HerdsmanContract.MensagemPendenteEntry.COLUMN_NAME_NUMERO
                },
                null,
                null,
                null,
                null,
                HerdsmanContract.MensagemPendenteEntry.COLUMN_NAME_IDMENSAGEM_PENDENTE
                );
        while (cursor.moveToNext())
        {
            long id = cursor.getLong(cursor.getColumnIndex(HerdsmanContract.MensagemPendenteEntry.COLUMN_NAME_IDMENSAGEM_PENDENTE));
            String text = cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.MensagemPendenteEntry.COLUMN_NAME_TEXTO));
            String numero = cursor.getString(cursor.getColumnIndexOrThrow(HerdsmanContract.MensagemPendenteEntry.COLUMN_NAME_NUMERO));
            MensagemPendente mp = new MensagemPendente(id, text,numero);
            lista.add(mp);
        }
        cursor.close();
        db.close();
        return lista;

    }

    public void inserirAdministrador(String senha) {
        Administrador admin = new Administrador(1,"admin", senha,1,1);
        SQLiteDatabase mDb = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HerdsmanContract.AdministradorEntry.COLUMN_NAME_ADMIN, "1");;
        values.put(HerdsmanContract.AdministradorEntry.COLUMN_NAME_IDADMINISTRADOR, "1");;
        values.put(HerdsmanContract.AdministradorEntry.COLUMN_NAME_PESSOA_IDPESSOA, "1");;
        values.put(HerdsmanContract.AdministradorEntry.COLUMN_NAME_LOGIN, "admin");
        values.put(HerdsmanContract.AdministradorEntry.COLUMN_NAME_SENHA, senha);
        long id = mDb.replace(HerdsmanContract.AdministradorEntry.TABLE_NAME, null, values);
        if (id > 0 && !this.isSync()) {
            DatabaseReference database = FirebaseHelper.child(HerdsmanContract.AdministradorEntry.TABLE_NAME);
            database.child(String.valueOf(admin.getIdAdministrador())).setValue(admin);
            database.keepSynced(true);
        }
        mDb.close();
    }

    public void inserirAdministrador(Administrador s) {
        SQLiteDatabase mDb = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HerdsmanContract.AdministradorEntry.COLUMN_NAME_ADMIN, "1");;
        values.put(HerdsmanContract.AdministradorEntry.COLUMN_NAME_IDADMINISTRADOR, "1");;
        values.put(HerdsmanContract.AdministradorEntry.COLUMN_NAME_PESSOA_IDPESSOA, "1");;
        values.put(HerdsmanContract.AdministradorEntry.COLUMN_NAME_LOGIN, "admin");
        values.put(HerdsmanContract.AdministradorEntry.COLUMN_NAME_SENHA, s.getSenha());
        long id = mDb.insertWithOnConflict(HerdsmanContract.AdministradorEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        if (id > 0 && !this.isSync()) {
            DatabaseReference database = FirebaseHelper.child(HerdsmanContract.AdministradorEntry.TABLE_NAME);
            database.child(String.valueOf(s.getIdAdministrador())).setValue(s);
            database.keepSynced(true);
        }
        mDb.close();
    }
}
