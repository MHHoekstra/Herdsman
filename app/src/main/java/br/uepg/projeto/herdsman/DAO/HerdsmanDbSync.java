package br.uepg.projeto.herdsman.DAO;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import br.uepg.projeto.herdsman.MainActivity;
import br.uepg.projeto.herdsman.Objetos.Animal;

public class HerdsmanDbSync {
    HerdsmanDbHelper mDbHelper;
    Context mContext;
    DatabaseReference database;

    public HerdsmanDbSync(Context context)
    {
        this.mContext = context;
        this.mDbHelper = new HerdsmanDbHelper(mContext);
    }

    public boolean startSync()
    {
        if(isOnline(mContext)) {
            AlertDialog.Builder confirm = new AlertDialog.Builder(this.mContext);
            confirm.setTitle("Alert");
            confirm.setMessage("Você precisa de uma conexão estável para realizar a sincronização \n Deseja continuar?");
            confirm.setNegativeButton("Cancelar", null);
            confirm.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    syncAnimal();
                }
            });
            AlertDialog alert = confirm.create();
            alert.show();
            return true;
        }
        else
        {
            Toast.makeText(this.mContext, "Você não possui acesso a internet...", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    public void syncAnimal()
    {
        database = FirebaseDatabase.getInstance().getReference().child("Animal");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Animal M =  postSnapshot.getValue(Animal.class);
                    mDbHelper.inserirAnimal(M,1);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }
    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected())
            return true;
        else
            return false;
    }

}
