package br.com.mabelli.bijoux;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


/**
 * Created by feliciano on 21/02/17.
 */

public class AppActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseReference mDatabase;
    private FirebaseAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent = new Intent(this, FcmRegisterService.class);
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    private void init() {
        final RecyclerView recyclerView;
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewMenu);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        StaggeredGridLayoutManager st = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(st);
        adapter = new FirebaseAdapter(Product.class, R.layout.itens_list, ProductRecycler.class,
                mDatabase.child("product/").orderByChild("type").equalTo("necklace").getRef()
        );
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            init();
            //listener.getnecklace();
        } else if (id == R.id.nav_gallery) {
            // listener.getErraing();

        } else if (id == R.id.nav_slideshow) {
            startActivity(new Intent(this, AuthActivity.class));

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.cleanup();
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public class FirebaseAdapter extends FirebaseRecyclerAdapter<Product, ProductRecycler> {

        /**
         * @param modelClass      Firebase will marshall the data at a location into an instance of a class that you provide
         * @param modelLayout     This is the layout used to represent a single item in the list. You will be responsible for populating an
         *                        instance of the corresponding view with the data from an instance of modelClass.
         * @param viewHolderClass The class that hold references to all sub-views in an instance modelLayout.
         * @param ref             The Firebase location to watch for data changes. Can also be a slice of a location, using some
         *                        combination of {@code limit()}, {@code startAt()}, and {@code endAt()}.
         */
        public FirebaseAdapter(Class<Product> modelClass, int modelLayout, Class<ProductRecycler> viewHolderClass, Query ref) {
            super(modelClass, modelLayout, viewHolderClass, ref);
        }

        @Override
        protected void populateViewHolder(ProductRecycler viewHolder, final Product product, int position) {
            product.uid = getRef(position).getKey();
            viewHolder.priceView.setText(product.title);
            Glide.with(AppActivity.this).load(product.url).crossFade().centerCrop().into(viewHolder.imgView);
            viewHolder.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mDatabase.child("favorite/").child(getUid()).child(product.uid)
                            .setValue(true);
                    Toast.makeText(AppActivity.this, "Clickou", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public static class ProductRecycler extends RecyclerView.ViewHolder {

        public final TextView titleView;
        public final ImageView imgView;
        public final TextView priceView;
        public final AppCompatImageButton btn;

        public ProductRecycler(View itemView) {
            super(itemView);
            titleView = (TextView) itemView.findViewById(R.id.name);
            imgView = (ImageView) itemView.findViewById(R.id.comment_photo);
            priceView = (TextView) itemView.findViewById(R.id.artist);
            btn = (AppCompatImageButton) itemView.findViewById(R.id.btnfavorite);
        }

    }
}
