package com.example.dingu.axicut;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.VideoView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try{
            VideoView videoHolder = (VideoView)findViewById(R.id.SplashVideo);
//            Uri video = Uri.parse("android.resource://" + getPackageName() + "/"
//                    + R.raw.someVideo);
//            videoHolder.setVideoURI(video);
            videoHolder.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    jump();
                }
            });
            videoHolder.start();
        }catch (Exception e){
            jump();
        }
    }
    private void jump(){
        if(isFinishing())
            return;
        startActivity(new Intent(this,LoginActivity.class));
        finish();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        jump();
        return true;

    }
}
