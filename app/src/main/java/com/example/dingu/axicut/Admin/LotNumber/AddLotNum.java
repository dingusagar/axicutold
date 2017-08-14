package com.example.dingu.axicut.Admin.LotNumber;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dingu.axicut.Admin.Materials.Material;
import com.example.dingu.axicut.R;
import com.example.dingu.axicut.Utils.General.MyDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddLotNum extends AppCompatActivity {
    private EditText lotNumId;
    private Button addLotNum;
    private DatabaseReference lotNumRef= FirebaseDatabase.getInstance().getReference().child("Lot Numbers");
    private DatabaseReference lotNumberQuickRef= FirebaseDatabase.getInstance().getReference().child("InwardUtilities").child("lotNumberTypes");
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lot_num);
        lotNumId=(EditText)findViewById(R.id.lotNumberText);
        addLotNum=(Button)findViewById(R.id.AddLotNumber);
        progressDialog = new ProgressDialog(this);
        addLotNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLotNum();
            }
        });
    }

    public void addLotNum(){
        final String lotNum = lotNumId.getText().toString().trim();
        progressDialog.setMessage("Adding new Lot number......");
        progressDialog.show();
        DatabaseReference dbRootRef= MyDatabase.getDatabase().getInstance().getReference();
        Map<String, Object> update = new HashMap<>();
        if(lotNum!=null) {
            LotNumber lotNumber = new LotNumber(lotNum);
            update.put("Lot Numbers/"+lotNumber.getLotNum(),lotNumber);
            update.put("InwardUtilities/lotNumberTypes/"+lotNumber.getLotNum(),true);
//            lotNumRef.child(lotNumber.getLotNum()).setValue(lotNumber);
//            lotNumberQuickRef.child(lotNumber.getLotNum()).setValue(true);
            dbRootRef.updateChildren(update).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    progressDialog.dismiss();

                    Toast.makeText(getApplicationContext(), "Added lot Number : "+ lotNum,Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Error adding lot number : "+ e.toString(),Toast.LENGTH_LONG).show();
                    onBackPressed();
                }
            });

        }
    }
}

