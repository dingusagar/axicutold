package com.example.dingu.axicut;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.dingu.axicut.Admin.AdminActivity;
import com.example.dingu.axicut.Admin.user.User;
import com.example.dingu.axicut.Design.DesignMainActivity;
import com.example.dingu.axicut.Inward.InwardMainActivity;
import com.example.dingu.axicut.Production.ProductionActivity;
import com.example.dingu.axicut.Utils.General.MyDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import static com.example.dingu.axicut.LoginActivity.getUserID;
import static com.example.dingu.axicut.LoginActivity.getUsername;
import static com.example.dingu.axicut.LoginActivity.mAuth;
import static com.example.dingu.axicut.LoginActivity.mdatabaseUsers;
import static com.example.dingu.axicut.UserMode.DESIGN;

public class MainActivity extends Activity {
    private VideoView videoHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try{
            mAuth = FirebaseAuth.getInstance();
            mdatabaseUsers = MyDatabase.getDatabase().getInstance().getReference().child("Users");
            mdatabaseUsers.keepSynced(true);
            videoHolder = (VideoView)findViewById(R.id.SplashVideo);
            Uri video = Uri.parse("android.resource://" + getPackageName() + "/"
                    + R.raw.axicut_splash_vid);
           videoHolder.setVideoURI(video);
           videoHolder.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    jump();
                }
            });
        }catch (Exception e){
            jump();
        }
    }
    private void jump(){
        if(isFinishing())
            return;
        else if(mAuth.getCurrentUser()!=null){
            autoSignIn();
            finish();
            return;
        }
        startActivity(new Intent(this,LoginActivity.class));
        finish();

    }

    @Override
    protected void onStart() {
        super.onStart();
        videoHolder.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        jump();
        return true;

    }

    private void autoSignIn(){
        final String userID = mAuth.getCurrentUser().getUid();
        mdatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild(userID)) { // only if the user is present in the db
                    // getting the string userMode in DB to enum userMode
                   UserMode userMode = UserMode.valueOf(dataSnapshot.child(userID).child("userMode").getValue().toString());
                    Intent intent;
                    switch (userMode) {
                        case INWARD:
                            // the user is inward type
                            Log.e("app", "inward identified");
                            intent = new Intent(MainActivity.this, InwardMainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            break;

                        case ADMIN:
                            // the user is inward type
                            Log.e("app", "admin identified");
                            intent = new Intent(MainActivity.this, AdminActivity.class);
                            intent.putExtra("name", getUsername());
                            intent.putExtra("id", getUserID());
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            break;

                        case DESIGN:
                            Log.e("app", "Design identified");
                            intent = new Intent(MainActivity.this, DesignMainActivity.class);
                            startActivity(intent);
                            break;

                        case PRODUCTION:
                            Log.e("app", "Production identified");
                            intent = new Intent(MainActivity.this, ProductionActivity.class);
                            startActivity(intent);
                            break;



                    }
                    finish();
                }else
                    Toast.makeText(getApplicationContext(),"User was not found in database..Contact Admin",Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"DB Error : "+databaseError,Toast.LENGTH_SHORT).show();
            }
        });


    }

}
