package br.com.mabelli.bijoux;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by feliciano on 07/03/17.
 */

public class AppPresenter implements AppContract.ActionListener {

    private final AppContract.View view;
    private final DatabaseReference dataRefe;
    private ValueEventListener valueEventListener;

    public AppPresenter(AppContract.View view, DatabaseReference databaseReference) {
        this.view = view;
        this.dataRefe = databaseReference;
    }

    @Override
    public void getnecklace() {
       Query query =  dataRefe.child("product/").orderByChild("type").equalTo("necklace");
       valueEventListener = query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Log.i("TAG", data.toString());
                    Product product = data.getValue(Product.class);
                    product.uid = data.getKey();
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
        String uid = getUid();
        dataRefe.child("favorite/").child(uid).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    dataRefe.child("product/").child(data.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                                Product product = dataSnapshot.getValue(Product.class);
                                product.uid = dataSnapshot.getKey();
                                view.addPath(product);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void saveFavorite(Product product) {
        String uid = getUid();
        dataRefe.child("favorite/").child(uid).child(product.uid)
                .setValue(true);
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

}
