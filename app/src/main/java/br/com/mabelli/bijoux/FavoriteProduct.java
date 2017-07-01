package br.com.mabelli.bijoux;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by feliciano on 01/07/17.
 */

public class FavoriteProduct extends Fragment {

    private DatabaseReference mDatabase;

    private RecyclerView mRecycler;
    private FirebaseRecyclerAdapter<Boolean, ProductRecycler> fr;
    private DatabaseReference refFavorite;

    public FavoriteProduct() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.content_main, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mRecycler = (RecyclerView) rootView.findViewById(R.id.recyclerViewMenu);
        mRecycler.setHasFixedSize(true);

        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        refFavorite = mDatabase.child("favorite/").child(getUid());

        mRecycler.setItemAnimator(new DefaultItemAnimator());
        StaggeredGridLayoutManager st = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecycler.setLayoutManager(st);

        fr = new FirebaseRecyclerAdapter<Boolean,
                ProductRecycler>(Boolean.class,
                R.layout.itens_list, ProductRecycler.class, refFavorite) {
            @Override
            protected void populateViewHolder(final ProductRecycler viewHolder, Boolean model, int position) {
                String key = this.getRef(position).getKey();
                mDatabase.child("product").child(key)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                Product product = dataSnapshot.getValue(Product.class);
                                viewHolder.priceView.setText(product.title);
                                Glide.with(FavoriteProduct.this).load(product.url).crossFade().centerCrop().into(viewHolder.imgView);
                            }


                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }
        };
        mRecycler.setAdapter(fr);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (fr != null) {
            fr.cleanup();
        }
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }


}
