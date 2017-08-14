package com.example.dingu.axicut;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dingu.axicut.Design.DesignMainActivity;
import com.example.dingu.axicut.Inward.InwardMainActivity;
import com.example.dingu.axicut.Production.ProductionActivity;
import com.example.dingu.axicut.Utils.Effects.ButtonAnimator;
import com.example.dingu.axicut.Utils.General.MyDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.example.dingu.axicut.Admin.*;

public class LoginActivity extends AppCompatActivity {

    private EditText emailField;
    private EditText passwordField;
    private Button loginButton;
    private TextView forgotPassword;

    private ProgressBar progressBar;
    private TextView progressMessage;
    UserMode userMode;


    public static FirebaseAuth mAuth;
    public static DatabaseReference mdatabaseUsers;  // to reference the users details in the database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login...");
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressMessage = (TextView)findViewById(R.id.progressMessage);

        mAuth = FirebaseAuth.getInstance();
        mdatabaseUsers = MyDatabase.getDatabase().getInstance().getReference().child("Users");
        mdatabaseUsers.keepSynced(true);
        emailField = (EditText)findViewById(R.id.email);
        passwordField = (EditText)findViewById(R.id.password);
        passwordField.setTransformationMethod(new PasswordTransformationMethod());
        loginButton = (Button)findViewById(R.id.login);
        forgotPassword=(TextView)findViewById(R.id.ForgotPasswordText);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Editable username = emailField.getText();
                final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setCancelable(false);
                if(username!=null && !username.toString().isEmpty()){
                    builder.setTitle("Do you want to send a reset link to " + username.toString() + " ?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sendResetPasswordLink(username.toString().trim(),getApplicationContext());
                        }
                    });
                    builder.setNegativeButton("Cancel",null);
                    builder.show();
                }
                else
                    Toast.makeText(getApplicationContext(),"Please enter your username",Toast.LENGTH_LONG).show();

            }
        });
        ButtonAnimator.setEffect(loginButton, ButtonAnimator.Effects.SIMPLE_ON_TOUCH_GREY); // onClick animation defined in ButtonAnimator Class
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAndLogin();
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();

        progressMessage.setText("just a sec...");
            progressBar.setVisibility(View.GONE);
            progressMessage.setText("");


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



        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) //
        {
            progressBar.setVisibility(View.VISIBLE);
            progressMessage.setText("Logging in ...");

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
                    progressBar.setVisibility(View.GONE);
                    progressMessage.setText("");
                }
            });



        }
        else
        {
            Toast.makeText(LoginActivity.this,"Fields cannot be empty..",Toast.LENGTH_SHORT).show();
        }
    }


    private void getUserMode() {
        final String userID = mAuth.getCurrentUser().getUid();
        progressBar.setVisibility(View.VISIBLE);
        progressMessage.setText("Verifiying User...");

        mdatabaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

               if(dataSnapshot.hasChild(userID)) { // only if the user is present in the db
                   // getting the string userMode in DB to enum userMode
                   userMode = UserMode.valueOf(dataSnapshot.child(userID).child("userMode").getValue().toString());
                   Intent intent;
                   switch (userMode) {
                       case INWARD:
                           // the user is inward type
                           Log.e("app", "inward identified");
                           intent = new Intent(LoginActivity.this, InwardMainActivity.class);
                           intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                           startActivity(intent);
                           finish();
                           break;

                       case ADMIN:
                           // the user is inward type
                           Log.e("app", "admin identified");
                           intent = new Intent(LoginActivity.this, AdminActivity.class);
                           intent.putExtra("name", getUsername());
                           intent.putExtra("id", getUserID());
                           intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                           startActivity(intent);
                           finish();
                           break;

                       case DESIGN:
                           Log.e("app", "Design identified");
                           intent = new Intent(LoginActivity.this, DesignMainActivity.class);
                           startActivity(intent);
                           finish();
                           break;

                       case PRODUCTION:
                           Log.e("app", "Production identified");
                           intent = new Intent(LoginActivity.this, ProductionActivity.class);
                           startActivity(intent);
                           finish();
                           break;



                   }

               }
               else {
                   progressMessage.setText("");
                   progressBar.setVisibility(View.GONE);
                   Toast.makeText(LoginActivity.this,"User was not there in the database",Toast.LENGTH_SHORT).show();
               }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressMessage.setText("");
                progressBar.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this,"DB Error : "+databaseError,Toast.LENGTH_SHORT).show();
            }
        });


    }

    public static String getUserID(){
            return mAuth.getCurrentUser().getEmail();
    }
    public static String getUsername(){
        return mAuth.getCurrentUser().getDisplayName();
    }

    public static void sendResetPasswordLink(final String email, final Context context){
        FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context,"Password reset link sent to " + email, Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context,e.toString(),Toast.LENGTH_LONG).show();
            }
        });
    }

}
