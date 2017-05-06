package com.example.dingu.axicut;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.dingu.axicut.Admin.*;

public class LoginActivity extends AppCompatActivity {

    private EditText emailField;
    private EditText passwordField;
    private Button loginButton;


    private ProgressDialog progress;
    UserMode userMode;


    private FirebaseAuth mAuth;
    private DatabaseReference mdatabaseUsers;  // to reference the users details in the database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mdatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");

        progress = new ProgressDialog(this);

        emailField = (EditText)findViewById(R.id.email);
        passwordField = (EditText)findViewById(R.id.password);
        passwordField.setTransformationMethod(new PasswordTransformationMethod());
        loginButton = (Button)findViewById(R.id.login);

        ButtonAnimator.buttonEffect(loginButton); // onClick animation defined in ButtonAnimator Class

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAndLogin();
            }
        });



    }

    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        if (exit) {

            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }

    private void checkAndLogin() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        progress.setMessage("Logging in ..");

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) //
        {
            progress.show();
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                       // successfully logged in
                        getUserMode();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LoginActivity.this,"Error Login : "+ e.toString(),Toast.LENGTH_LONG).show();
                }
            });

            progress.dismiss();

        }
        else
        {
            Toast.makeText(LoginActivity.this,"Fields cannot be empty..",Toast.LENGTH_SHORT).show();
        }
    }


    private void getUserMode() {
        final String userID = mAuth.getCurrentUser().getUid();
        progress.setMessage("Verifying user..");
        progress.show();
        mdatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // getting the string userMode in DB to enum userMode
                userMode = UserMode.valueOf(dataSnapshot.child(userID).child("userMode").getValue().toString());
                Log.e("app","user mode = "+userMode);
                Intent intent;
                switch (userMode)
                {
                    case INWARD :
                        // the user is inward type
                        Log.e("app","inward identified");
                        intent = new Intent(LoginActivity.this, InwardEntry.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        break;

                    case ADMIN:
                        // the user is inward type
                        Log.e("app","admin identified");
                        intent = new Intent(LoginActivity.this, Admin.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        break;

                    case DESIGN:
                        // the user is design type
                        break;

                    case DESPATCH:
                        // the user is despatch type
                        break;

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(LoginActivity.this,"DB Error : "+databaseError,Toast.LENGTH_SHORT).show();
            }
        });
        progress.dismiss();

    }


}
