package com.example.dingu.axicut.Utils.General;

import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by dingu on 16/5/17.
 */

public class MyDatabase {
    private static FirebaseDatabase mDatabase;
    private MyDatabase(){};
    public static FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase;
    }
    public static class TempAuthCreator{
        private static FirebaseOptions firebaseOptions;
        private static FirebaseApp myApp;
        private  TempAuthCreator() {}
        public static FirebaseApp getTempAuth(Context context){
            if(myApp==null){
                firebaseOptions = new FirebaseOptions.Builder()
                        .setDatabaseUrl("https://axicut-3fe9b.firebaseio.com/")
                        .setApiKey("AIzaSyDKjXmCJ377LALkI87aIX8fa9Km-_OcF68")
                        .setApplicationId("axicut-3fe9b").build();
                myApp = FirebaseApp.initializeApp(context, firebaseOptions, "axicut");
            }
            return myApp;
        }
    }
}
