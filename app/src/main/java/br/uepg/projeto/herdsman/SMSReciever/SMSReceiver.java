package br.uepg.projeto.herdsman.SMSReciever;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

import br.uepg.projeto.herdsman.DAO.HerdsmanContract;
import br.uepg.projeto.herdsman.DAO.HerdsmanDbHelper;
import br.uepg.projeto.herdsman.Objetos.Animal;
import br.uepg.projeto.herdsman.Objetos.Telefone;

public class SMSReceiver extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.M)
    Telefone senderTelefone;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        Object[] pdus = (Object[]) intent.getSerializableExtra("pdus");
        byte[] pdu = (byte[]) pdus[0];
        String format = (String) intent.getSerializableExtra("format");
        SmsMessage message = SmsMessage.createFromPdu(pdu, format);
        String senderTel = message.getDisplayOriginatingAddress();
        String messageBody = message.getDisplayMessageBody();

        if (!messageBody.startsWith("Herdsman's Companion;\n"))
        {
            Toast.makeText(context, "Mensagem nao é do herdsman", Toast.LENGTH_SHORT).show();
            return;
        }


        List<Telefone> listaTelefones = new <Telefone> ArrayList();
        HerdsmanDbHelper mDbHelper = new HerdsmanDbHelper(context);
        SQLiteDatabase mDb = mDbHelper.getReadableDatabase();
        Cursor cursor = mDb.query(
                HerdsmanContract.TelefoneEntry.TABLE_NAME,
                new String[]{HerdsmanContract.TelefoneEntry.COLUMN_NAME_NUMERO,HerdsmanContract.TelefoneEntry.COLUMN_NAME_PESSOA_IDPESSOA},
                null,
                null,
                null,
                null,
                null,
                null
        );
        int num = cursor.getColumnIndexOrThrow(HerdsmanContract.TelefoneEntry.COLUMN_NAME_NUMERO);
        int id = cursor.getColumnIndexOrThrow(HerdsmanContract.TelefoneEntry.COLUMN_NAME_PESSOA_IDPESSOA);
        while(cursor.moveToNext())
        {
            String numero = cursor.getString(num);
            int idPessoa = cursor.getInt(id);
            Telefone tel = new Telefone(idPessoa, numero);
            listaTelefones.add(tel);
        }
        cursor.close();
        cursor =  mDb.query(
                HerdsmanContract.AnimalEntry.TABLE_NAME,
                new String[] {
                        HerdsmanContract.AnimalEntry.COLUMN_NAME_IDANIMAL,
                        HerdsmanContract.AnimalEntry.COLUMN_NAME_NOME,
                        HerdsmanContract.AnimalEntry.COLUMN_NAME_NUMERO,
                        HerdsmanContract.AnimalEntry.COLUMN_NAME_ATIVO
                },
                null,
                null,
                null,
                null,
        null
        );
        List listaAnimais = new <Animal> ArrayList();
        int indexID = cursor.getColumnIndex(HerdsmanContract.AnimalEntry.COLUMN_NAME_IDANIMAL);
        int indexNome = cursor.getColumnIndex(HerdsmanContract.AnimalEntry.COLUMN_NAME_NOME);
        int indexNumero = cursor.getColumnIndex(HerdsmanContract.AnimalEntry.COLUMN_NAME_NUMERO);
        int indexAtivo = cursor.getColumnIndex(HerdsmanContract.AnimalEntry.COLUMN_NAME_ATIVO);
        while (cursor.moveToNext())
        {
            Animal animal = new Animal(
                    cursor.getInt(indexID),
                    cursor.getString(indexNumero),
                    cursor.getString(indexNome),
                    cursor.getInt(indexAtivo)
            );
            listaAnimais.add(animal);
        }
        cursor.close();
        mDb.close();
        boolean telefoneValido = false;

        for(Telefone tel : listaTelefones)
        {
            if(senderTel.compareTo(tel.getNumero()) == 0)
            {
                telefoneValido = true;
                senderTelefone = tel;
            }
        }
        if (!telefoneValido)
        {
            Toast.makeText(context, "Telefone invalido", Toast.LENGTH_SHORT).show();
            return;
        }
        String[] tokens = messageBody.split(";");
        for (String s : tokens)
        {
            Log.d("Oi:", s);
        }
        try {
            switch (Integer.parseInt(tokens[1].replace("\n", ""))) {
                case 1: {
                    Log.d("Tipo de msg:", "CIO");
                    Calendar c = Calendar.getInstance();
                    int animalPorBaixo = Integer.parseInt(tokens[2]);
                    int animalPorCima = Integer.parseInt(tokens[3]);
                    String data = String.valueOf(c.get(Calendar.YEAR))+"-"+String.valueOf(c.get(Calendar.MONTH))+"-"+String.valueOf(c.get(Calendar.DAY_OF_MONTH));
                    ContentValues values = new ContentValues();
                    values.put(HerdsmanContract.CioEntry.COLUMN_NAME_ANIMAL_IDANIMALPORCIMA, animalPorCima);
                    values.put(HerdsmanContract.CioEntry.COLUMN_NAME_ANIMAL_IDANIMALPORBAIXO, animalPorBaixo);
                    values.put(HerdsmanContract.CioEntry.COLUMN_NAME_DATA, data);
                    values.put(HerdsmanContract.CioEntry.COLUMN_NAME_USUARIO_IDUSUARIO, senderTelefone.getPessoa_idPessoa());
                    mDbHelper = new HerdsmanDbHelper(context);
                    mDb = mDbHelper.getWritableDatabase();
                    mDb.insert(
                            HerdsmanContract.CioEntry.TABLE_NAME,
                            null,
                            values

                    );
                    Log.d("SMSReceiver" , "Cio " + String.valueOf(animalPorBaixo) + "inserido");
                    mDb.close();
                    break;
                }
                case 2: {
                    Log.d("Tipo de msg:", "SINISTRO");
                    Calendar c = Calendar.getInstance();
                    int idEnfermidade = Integer.parseInt(tokens[2]);
                    int idAnimal = Integer.parseInt(tokens[3]);
                    String data = String.valueOf(c.get(Calendar.YEAR))+"-"+String.valueOf(c.get(Calendar.MONTH))+"-"+String.valueOf(c.get(Calendar.DAY_OF_MONTH));
                    ContentValues values = new ContentValues();
                    values.put(HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_ANIMAL_IDANIMAL, idAnimal);
                    values.put(HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_ENFERMIDADE_IDENFERMIDADE, idEnfermidade);
                    values.put(HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_DATA, data);
                    values.put(HerdsmanContract.AnimalEnfermidadeEntry.COLUMN_NAME_USUARIO_IDUSUARIO, senderTelefone.getPessoa_idPessoa());
                    mDb = mDbHelper.getWritableDatabase();
                    mDb.insert(
                            HerdsmanContract.AnimalEnfermidadeEntry.TABLE_NAME,
                            null,
                            values
                    );
                    mDb.close();
                    Log.d("SMSReceiver", "Sinistro inserido");
                    break;
                }
                case 3: {
                    Log.d("Tipo de msg:", "OUTRO");
                    break;
                }
            }
        }catch (Exception e)
        {
            Log.d("SMSReceiver", " Deu ruim");
            this.abortBroadcast();
        }
    }
}
