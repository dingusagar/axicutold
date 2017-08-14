package com.example.dingu.axicut.Utils.General;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by dingu on 22/7/17.
 */

public class NetworkLostDetector {

    private DatabaseReference connectedRef;
    private Snackbar snackbar;
    private View parentView;
    private Boolean connected = true;
    private int datasnapshotCounter = 0; // to avoid "connected" message for the first time

    public NetworkLostDetector(final int parentLayoutId, Activity activity) {

        parentView = activity.findViewById(parentLayoutId);
        connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot snapshot) {
                 connected = snapshot.getValue(Boolean.class);
                datasnapshotCounter++;

                if (!connected) {
                    snackbar = Snackbar.make(parentView, "No Internet Connection", Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction("Okay", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();
                } else if(connected && datasnapshotCounter > 1)
                {

                    if(snackbar != null){
                        snackbar.dismiss();
                        snackbar = Snackbar.make(parentView, "Internet Connection Regained", Snackbar.LENGTH_LONG);
                        snackbar.setAction("Okay", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                snackbar.dismiss();
                            }
                        });
                        snackbar.show();
                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled");
            }
        });
    }
}
