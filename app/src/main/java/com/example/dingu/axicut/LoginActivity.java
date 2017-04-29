package com.example.dingu.axicut;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText emailField;
    private EditText passwordField;
    private Button loginButton;


    private ProgressDialog progress;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        emailField = (EditText)findViewById(R.id.email);
        passwordField = (EditText)findViewById(R.id.password);
        loginButton = (Button)findViewById(R.id.login);


        ButtonAnimator.buttonEffect(loginButton); // onClick animation defined in ButtonAnimator Class

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAndLogin();
            }
        });



    }

    private void checkAndLogin() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        progress.setMessage("Logging in ..");

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password))
        {
            progress.show();
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                       // successfully logged in
                    }else
                    {
                        Toast.makeText(LoginActivity.this,"Error Login...",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            progress.dismiss();

        }
    }
}
