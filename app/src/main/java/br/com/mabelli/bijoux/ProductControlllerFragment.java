package br.com.mabelli.bijoux;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * Created by feliciano on 01/07/17.
 */

public abstract class ProductControlllerFragment extends Fragment {

    private DatabaseReference mDatabase;

    private FirebaseRecyclerAdapter<Product, ProductRecycler> mAdapter;
    private RecyclerView mRecycler;

    public ProductControlllerFragment() {
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
        mRecycler.setItemAnimator(new DefaultItemAnimator());
        StaggeredGridLayoutManager st = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecycler.setLayoutManager(st);

        Query query = getQuery(mDatabase);
        mAdapter = new FirebaseRecyclerAdapter<Product, ProductRecycler>(Product.class,
                R.layout.itens_list, ProductRecycler.class, query) {
            @Override
            protected void populateViewHolder(ProductRecycler viewHolder, final Product product, int position) {
                product.uid = getRef(position).getKey();
                viewHolder.priceView.setText(product.title);
                Glide.with(ProductControlllerFragment.this)
                        .load(product.url).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop().into(viewHolder.imgView);
                viewHolder.btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveFavorite(product.uid);
                    }
                });
            }
        };
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }

    private void saveFavorite(String userid) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {

            final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "Aguarde",
                    "necessário está logado!");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                    startActivity(new Intent(getActivity(), AuthActivity.class));
                }
            }, 4000);
        } else {
            mDatabase.child("favorite").child(getUid() + "/" + userid).setValue(true)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public abstract Query getQuery(DatabaseReference databaseReference);
}
