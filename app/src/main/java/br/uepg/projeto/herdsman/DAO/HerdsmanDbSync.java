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
import br.uepg.projeto.herdsman.Objetos.Cio;
import br.uepg.projeto.herdsman.Objetos.Enfermidade;
import br.uepg.projeto.herdsman.Objetos.Parto;
import br.uepg.projeto.herdsman.Objetos.Pessoa;
import br.uepg.projeto.herdsman.Objetos.Remedio;
import br.uepg.projeto.herdsman.Objetos.Sinistro;
import br.uepg.projeto.herdsman.Objetos.Usuario;

public class HerdsmanDbSync {
    HerdsmanDbHelper mDbHelper;
    Context mContext;
    private DatabaseReference FirebaseSync;

    public HerdsmanDbSync(Context context)
    {
        this.mContext = context;
        this.mDbHelper = new HerdsmanDbHelper(mContext);
        this.FirebaseSync = FirebaseDatabase.getInstance().getReference("Hoekstra");
        this.mDbHelper.setSync(true);
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
                    syncEnfermidade();
                    syncParto();
                    syncFuncionario();
                    syncRemedio();
                    syncCio();
                    syncSinistro();
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

    private void syncSinistro() {
        DatabaseReference database = FirebaseSync.child("Sinistro");

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Sinistro S =  postSnapshot.getValue(Sinistro.class);
                    mDbHelper.inserirSinistro(S);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void syncRemedio() {
        DatabaseReference database = FirebaseSync.child("Remedio");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Remedio R =  postSnapshot.getValue(Remedio.class);
                    mDbHelper.inserirRemedio(R);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    private void syncFuncionario() {
        DatabaseReference database = FirebaseSync.child("Pessoa");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Pessoa Pe =  postSnapshot.getValue(Pessoa.class);
                    mDbHelper.inserirFuncionario(Pe);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    private void syncParto() {
        DatabaseReference database = FirebaseSync.child("Parto");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Parto P =  postSnapshot.getValue(Parto.class);
                    mDbHelper.inserirParto(P);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    private void syncEnfermidade() {
        DatabaseReference database = FirebaseSync.child("Enfermidade");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Enfermidade E =  postSnapshot.getValue(Enfermidade.class);
                    mDbHelper.inserirEnfermidade(E);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    public void syncAnimal() {
        DatabaseReference database = FirebaseSync.child("Animal");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Animal M =  postSnapshot.getValue(Animal.class);
                    mDbHelper.inserirAnimal(M);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    public void syncCio() {
        DatabaseReference database = FirebaseSync.child("Cio");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Cio C =  postSnapshot.getValue(Cio.class);
                    mDbHelper.inserirCio(C);
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
