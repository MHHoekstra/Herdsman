package br.uepg.projeto.herdsman.dao;

import android.app.Application;

import com.evernote.android.job.JobManager;
import com.google.firebase.database.FirebaseDatabase;

import br.uepg.projeto.herdsman.NoteJobCreator;

public class HerdsmanFirebase extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        /* Enable disk persistence  */
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        JobManager.create(this).addJobCreator(new NoteJobCreator());

    }
}
