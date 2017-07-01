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

    public ProductControlllerFragment() {}

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
            protected void populateViewHolder(ProductRecycler viewHolder, Product product, int position) {
                product.uid = getRef(position).getKey();
                viewHolder.priceView.setText(product.title);
                Glide.with(ProductControlllerFragment.this).load(product.url).crossFade().centerCrop().into(viewHolder.imgView);
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

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
    public abstract Query getQuery(DatabaseReference databaseReference);
}
