package com.sc2guide.sc2_guides_android.repo;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import com.sc2guide.sc2_guides_android.data.model.Guide;
import com.sc2guide.sc2_guides_android.room.database.AppDatabase;
import com.sc2guide.sc2_guides_android.utils.CallBackReceiver;

import java.util.List;

//
// use Dagger2 -> @Singleton
public class SavedGuidesRepository {


    private String DB_NAME = "db_guides";
    private static AppDatabase appDatabase;

    public SavedGuidesRepository(Context context) {
        appDatabase = Room.databaseBuilder(context, AppDatabase.class,
                DB_NAME).build();
    }


    public LiveData<List<Guide>> getAllGuides() {
        return appDatabase.guideDao().getAll();
    }

    public void insertGuide(Guide guide) {
        handleInsertGuide(guide);
    }

    private static void handleInsertGuide(final Guide guide) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                appDatabase.guideDao().insert(guide);
                return null;
            }
        }.execute();
    }

    public void nukeTable() {
        handleNukeTable();
    }

    public void deleteGuide(Guide guide) {
        handleDeleteGuide(guide);
    }

    private static void handleDeleteGuide(Guide guide){
        new AsyncTask<Void, Void, Void >() {
            @Override
            protected Void doInBackground(Void... voids) {
                appDatabase.guideDao().delete(guide);
                return null;
            }
        };
    }

    private static void handleNukeTable() {
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                appDatabase.guideDao().nukeTable();
                return null;
            }
        }.execute();
    }

    public abstract static class CheckGuideExistAsyncTask extends AsyncTask<String, Void, String> implements CallBackReceiver {
        private String id;

        public CheckGuideExistAsyncTask(String id) {
            super();
            this.id = id;
        }

        public abstract void receiveData(Object object);

        @Override
        protected String doInBackground(String... string) {
            return appDatabase.guideDao().getGuideTitle(id);

        }

        @Override
        protected void onPostExecute(String s) {
            if (s!=null) {
                receiveData(s);
            } else {
                receiveData(" ");
            }
        }

         @Override
         protected void onCancelled() {
             super.onCancelled();
         }
     }
}
