package com.example.dingu.axicut.Admin.Company;

import android.support.v4.content.SharedPreferencesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.dingu.axicut.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminAddCompany extends AppCompatActivity {
    private DatabaseReference dbRef , dbRefQuickAccess;
    private EditText companyNameText;
    private EditText companyIdText;
    private Button addComapanyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_company);
        dbRef = FirebaseDatabase.getInstance().getReference().child("Company");
        dbRefQuickAccess = FirebaseDatabase.getInstance().getReference().child("InwardUtilities").child("customerDCNumbers");
        companyNameText = (EditText) findViewById(R.id.CompanyName);
        companyIdText = (EditText) findViewById(R.id.ComapanyId);
        addComapanyButton = (Button) findViewById(R.id.AddCompany);
        addComapanyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCompanyToDatabase();
            }
        });

    }
    public void addCompanyToDatabase(){
        String companyName =companyNameText.getText().toString().trim();
        String companyId = companyIdText.getText().toString().trim();
        final Company company = new Company(companyName,companyId);
        if(companyName!=null && companyId !=null)
        dbRef.push().setValue(company);
        dbRefQuickAccess.child(companyId).setValue(true);
        onBackPressed();
    }
}