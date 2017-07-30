package com.example.dingu.axicut.Admin.LotNumber;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.dingu.axicut.Admin.Materials.Material;
import com.example.dingu.axicut.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddLotNum extends AppCompatActivity {
    private EditText lotNumId;
    private Button addLotNum;
    private DatabaseReference lotNumRef= FirebaseDatabase.getInstance().getReference().child("Lot Numbers");
    private DatabaseReference lotNumberQuickRef= FirebaseDatabase.getInstance().getReference().child("InwardUtilities").child("lotNumberTypes");;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lot_num);
        lotNumId=(EditText)findViewById(R.id.lotNumberText);
        addLotNum=(Button)findViewById(R.id.AddLotNumber);
        addLotNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLotNum();
            }
        });
    }

    public void addLotNum(){
        String lotNum = lotNumId.getText().toString().trim();
        if(lotNum!=null) {
            LotNumber lotNumber = new LotNumber(lotNum);
            lotNumRef.child(lotNumber.getLotNum()).setValue(lotNumber);
            lotNumberQuickRef.child(lotNumber.getLotNum()).setValue(true);
            onBackPressed();
        }
    }
}

