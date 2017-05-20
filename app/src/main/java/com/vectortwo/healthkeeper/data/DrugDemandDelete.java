package com.vectortwo.healthkeeper.data;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.BaseColumns;
import com.vectortwo.healthkeeper.data.db.DBContract;
import com.vectortwo.healthkeeper.services.DrugNotifyService;

/**
 * Created by ilya on 19/05/2017.
 */
public class DrugDemandDelete extends AsyncTask<Void, Void, Void> {

    private int mDrugId;
    private Context mContext;

    private IResultHandler mResultHandler;

    public DrugDemandDelete(Context context, int drugId, IResultHandler resultHandler) {
        mDrugId = drugId;
        mContext = context;
        mResultHandler = resultHandler;
    }

    public interface IResultHandler {
        void onPreExecute();
        void onPostExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Intent i = new Intent(mContext, DrugNotifyService.class);
        i.putExtra(DrugNotifyService.KEY_DRUG_ID, mDrugId);
        i.setAction(DrugNotifyService.ACTION_CANCEL);
        mContext.startService(i);

        mContext.getContentResolver().delete(
                DBContract.Drug.CONTENT_URI,
                BaseColumns._ID + "=?", new String[] {String.valueOf(mDrugId)});

        mContext.getContentResolver().delete(
                DBContract.Intake.CONTENT_URI,
                DBContract.Intake.DRUG_ID + "=?", new String[] {String.valueOf(mDrugId)});
        return null;
    }

    @Override
    protected void onPreExecute() {
        mResultHandler.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        mResultHandler.onPostExecute();
    }
}