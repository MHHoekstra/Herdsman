package br.uepg.projeto.herdsman.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.uepg.projeto.herdsman.dao.HerdsmanContract;
import br.uepg.projeto.herdsman.dao.HerdsmanDbHelper;
import br.uepg.projeto.herdsman.objetos.Animal;
import br.uepg.projeto.herdsman.objetos.AnimalEnfermidade;
import br.uepg.projeto.herdsman.objetos.Cio;
import br.uepg.projeto.herdsman.objetos.AdministradorNotificaPessoa;
import br.uepg.projeto.herdsman.objetos.Telefone;

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
            long idPessoa = cursor.getLong(id);
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
                    cursor.getLong(indexID),
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
            if(senderTel.contains(tel.getNumero()))
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
                    long animalPorBaixo = Long.parseLong(tokens[2]);
                    long animalPorCima = Long.parseLong(tokens[3]);
                    long data = c.getTimeInMillis();
                    long idCio = Long.parseLong(tokens[4]);
                    if (!mDbHelper.existeAnimal(animalPorCima))
                    {
                        Log.d("SMSReceiver", "Animal por cima inválido");
                        return;
                    }
                    if(!mDbHelper.existeAnimal(animalPorBaixo))
                    {
                        Log.d("SMSReceiver", "Animal por baixo inválido");
                        return;
                    }
                    mDbHelper = new HerdsmanDbHelper(context);
                    Cio cio = new Cio(idCio, animalPorCima,animalPorBaixo, data, senderTelefone.getPessoa_idPessoa());
                    //TODO Inserir no Firebase
                    long ins = mDbHelper.inserirCio(cio);
                    if(ins > 0) {
                        Log.d("SMSReceiver", "Cio de " + String.valueOf(animalPorBaixo) + " inserido");
                    }
                    else
                    {
                        Log.d("SMSReceiver", "Erro ao inserir cio");
                    }
                    SmsManager.getDefault().sendTextMessage(senderTelefone.getNumero(),null,"Herdsman's Companion;\n4;"+String.valueOf(idCio),null,null);
                    break;
                }
                case 2: {
                    Log.d("Tipo de msg:", "SINISTRO");
                    Calendar c = Calendar.getInstance();
                    long idEnfermidade = Long.parseLong(tokens[2]);
                    long idAnimal = Long.parseLong(tokens[3]);
                    long idAnimalEnfermidade = Long.parseLong(tokens[4]);
                    if (!mDbHelper.existeAnimal(idAnimal))
                    {
                        Log.d("SMSReceiver", "Animal inválido");
                        return;
                    }
                    if (!mDbHelper.existeEnfermidade(idEnfermidade))
                    {
                        Log.d("SMSReceiver", "Enfermidade inválida");
                        return;
                    }
                    long data = c.getTimeInMillis();
                    AnimalEnfermidade animalEnfermidade = new AnimalEnfermidade(idAnimalEnfermidade, idAnimal, idEnfermidade, senderTelefone.getPessoa_idPessoa(), data);
                    //TODO Inserir no Firebase
                    long insert = mDbHelper.inserirSinistro(animalEnfermidade);
                    if(insert > 0) {
                        Log.d("SMSReceiver", "AnimalEnfermidade inserido");
                    }
                    else
                    {
                        Log.d("SMSReceiver", "Falha ao inserir animalEnfermidade");
                    }
                    SmsManager.getDefault().sendTextMessage(senderTelefone.getNumero(),null,"Herdsman's Companion;\n4;"+String.valueOf(idAnimalEnfermidade),null,null);
                    break;
                }
                case 3: {
                    Log.d("Tipo de msg:", "OUTRO");
                    String mensagem = tokens[2];
                    long idMensagem = Long.parseLong(tokens[3]);
                    long idRetorno = Long.parseLong(tokens[4]);
                    Log.d("OUTRO: ", mensagem);
                    AdministradorNotificaPessoa outro = new AdministradorNotificaPessoa(idMensagem, mensagem, idMensagem);
                    Long insert = mDbHelper.inserirAdministradorNotificaPessoa(outro);
                    if(insert > 0) {
                        Log.d("SMSReceiver", "Outro inserido");
                    }
                    else
                    {
                        Log.d("SMSReceiver", "Falha ao inserir outro");
                    }
                    SmsManager.getDefault().sendTextMessage(senderTelefone.getNumero(), null, "Herdsman's Companion;\n4;"+String.valueOf(idRetorno),null,null);
                    break;
                }
                case 4:
                {
                    Log.d("Tipo de msg:", "ACK");
                    long idAck = Long.parseLong(tokens[2]);
                    mDbHelper.removerMensagemPendente(idAck);
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
