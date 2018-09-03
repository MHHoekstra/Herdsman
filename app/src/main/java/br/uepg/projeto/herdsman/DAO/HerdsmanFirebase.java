package br.uepg.projeto.herdsman.DAO;

import android.app.Application;
import com.google.firebase.database.FirebaseDatabase;

public class HerdsmanFirebase extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        /* Enable disk persistence  */
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
