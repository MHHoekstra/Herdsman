package br.uepg.projeto.herdsman;

import android.support.annotation.NonNull;
import android.telephony.SmsManager;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import br.uepg.projeto.herdsman.dao.HerdsmanDbHelper;
import br.uepg.projeto.herdsman.objetos.MensagemPendente;

public class NoteSyncJob extends Job {

    public static final String TAG = "job_note_sync";

    @Override
    @NonNull
    protected Result onRunJob(@NonNull Params params) {
        HerdsmanDbHelper mDbHelper = new HerdsmanDbHelper(getContext());
        ArrayList<MensagemPendente> mensagemPendentes = mDbHelper.carregarMensagensPendentes();
        SmsManager smsManager = SmsManager.getDefault();
        for (MensagemPendente m:
             mensagemPendentes)
        {
            smsManager.sendTextMessage(m.getNumero(),null, m.getText(), null, null);
        }
        return Result.SUCCESS;
    }

    public static void scheduleJob() {
        Set<JobRequest> jobRequests = JobManager.instance().getAllJobRequestsForTag(NoteSyncJob.TAG);
        if (!jobRequests.isEmpty()) {
            return;
        }
        new JobRequest.Builder(NoteSyncJob.TAG)
                .setPeriodic(TimeUnit.MINUTES.toMillis(15), TimeUnit.MINUTES.toMillis(7))
                .setUpdateCurrent(true) // calls cancelAllForTag(NoteSyncJob.TAG) for you
                .build()
                .schedule();
    }
}