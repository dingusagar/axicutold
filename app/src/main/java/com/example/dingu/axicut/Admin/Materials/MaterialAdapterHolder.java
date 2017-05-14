package com.example.dingu.axicut.Admin.Materials;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import com.example.dingu.axicut.R;
import com.example.dingu.axicut.Utils.Navigation.CustomAdapterHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by root on 15/5/17.
 */

public class MaterialAdapterHolder implements CustomAdapterHolder {
    private DatabaseReference databaseRef= FirebaseDatabase.getInstance().getReference().child("Material");
    private  FirebaseRecyclerAdapter<Material,MaterialViewHolder> materialAdapter;
    @Override
    public View.OnClickListener onPlusClicked() {
        View.OnClickListener fabClicked = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context,AdminAddMaterials.class);
                context.startActivity(intent);
            }
        };
        return fabClicked;
    }

    @Override
    public FirebaseRecyclerAdapter getAdapter() {
        materialAdapter = new FirebaseRecyclerAdapter<Material, MaterialViewHolder>(Material.class, R.layout.material_card_view,MaterialViewHolder.class,databaseRef) {
            @Override
            protected void populateViewHolder(final MaterialViewHolder viewHolder, Material model, int position) {
                Button removeButton = (Button)viewHolder.mView.findViewById(R.id.MaterialRemoveButton);
                viewHolder.setName(model.getDesc());
                viewHolder.setId(model.getId());
                removeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getRef(viewHolder.getAdapterPosition()).removeValue();
                        notifyDataSetChanged();
                    }
                });
            }
        };
        return materialAdapter;
    }
}
