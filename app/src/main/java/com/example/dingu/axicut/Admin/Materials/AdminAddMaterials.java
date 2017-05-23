package com.example.dingu.axicut.Admin.Materials;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.dingu.axicut.R;
import com.example.dingu.axicut.Utils.General.ButtonAnimator;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminAddMaterials extends AppCompatActivity {
    private EditText desc;
    private EditText id;
    private Button addMaterial;
    private DatabaseReference materialRef= FirebaseDatabase.getInstance().getReference().child("Material");
    private DatabaseReference materialRefQuickAccess= FirebaseDatabase.getInstance().getReference().child("InwardUtilities").child("materialTypes");;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_materials);
        desc=(EditText)findViewById(R.id.MaterialName);
        id=(EditText)findViewById(R.id.MaterialId);
        addMaterial=(Button)findViewById(R.id.AddMaterial);
        addMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMaterial();
            }
        });
    }

    public void addMaterial(){
        String materialDesc = desc.getText().toString().trim();
        String materialId = id.getText().toString().trim();
        if(materialDesc!=null && materialId!=null) {
            Material material = new Material(materialDesc, materialId);
            materialRef.child(material.getId()).setValue(material);
            materialRefQuickAccess.child(material.getId()).setValue(true);
            onBackPressed();
        }
    }
}
