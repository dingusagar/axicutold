package com.example.dingu.axicut.Utils.General;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by dingu on 16/5/17.
 */

public class MyDatabase {
    private static FirebaseDatabase mDatabase;

    public static FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase;
    }
}
