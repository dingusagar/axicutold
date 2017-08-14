package com.example.dingu.axicut.Admin.Company;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dingu.axicut.R;
import com.example.dingu.axicut.Utils.General.MyDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AdminAddCompany extends AppCompatActivity {
    private DatabaseReference dbRef , dbRefQuickAccess;
    private EditText companyNameText;
    private EditText companyIdText;
    private Button addComapanyButton;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_company);
        dbRef = FirebaseDatabase.getInstance().getReference().child("Company");
        dbRefQuickAccess = FirebaseDatabase.getInstance().getReference().child("InwardUtilities").child("customerIDs");
        companyNameText = (EditText) findViewById(R.id.CompanyName);
        companyIdText = (EditText) findViewById(R.id.ComapanyId);
        addComapanyButton = (Button) findViewById(R.id.AddCompany);
        progress = new ProgressDialog(this);
        addComapanyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCompanyToDatabase();
            }
        });

    }
    public void addCompanyToDatabase(){
        progress.setMessage("Adding new Company......");
        progress.show();
        DatabaseReference dbRootRef= MyDatabase.getDatabase().getInstance().getReference();
        Map<String, Object> update = new HashMap<>();
        String companyName =companyNameText.getText().toString().trim();
        final String companyId = companyIdText.getText().toString().trim();
        final Company company = new Company(companyName,companyId);
        if(companyName!=null && companyId !=null) {
            update.put("Company/"+company.getCompanyId(),company);
            update.put("InwardUtilities/customerIDs/"+companyId,true);
//            dbRef.child(company.getCompanyId()).setValue(company);
//            dbRefQuickAccess.child(companyId).setValue(true);
            dbRootRef.updateChildren(update).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        progress.dismiss();
                        Toast.makeText(getApplicationContext(), "Added new Company :"+ companyId ,Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(), "Error adding : " +e.toString(),Toast.LENGTH_LONG).show();
                    onBackPressed();
                }
            });
        }
    }
}