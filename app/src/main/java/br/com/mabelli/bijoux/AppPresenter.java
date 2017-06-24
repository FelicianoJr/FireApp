package br.com.mabelli.bijoux;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by feliciano on 07/03/17.
 */

public class AppPresenter implements AppContract.ActionListener {

    private final AppContract.View view;
    private final DatabaseReference dataRefe;

    public AppPresenter(AppContract.View view, DatabaseReference databaseReference) {
        this.view = view;
        this.dataRefe = databaseReference;
    }

    @Override
    public void getnecklace() {
        dataRefe.child("product/").orderByChild("type").equalTo("necklace").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Product product = data.getValue(Product.class);
                    view.addPath(product);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void getErraing() {
        dataRefe.child("product/").orderByChild("type").equalTo("erraing").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Product product = data.getValue(Product.class);
                    view.addPath(product);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
