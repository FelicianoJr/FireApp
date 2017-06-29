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
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by feliciano on 21/02/17.
 */

public class AppActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AppContract.View {

    private DatabaseReference mDatabase;
    private AppContract.ActionListener listener;
    private FireAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        listener = new AppPresenter(this, mDatabase);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewMenu);
        recyclerView.setHasFixedSize(true);
        adapter = new FireAdapter();
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        StaggeredGridLayoutManager st = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(st);
        recyclerView.setAdapter(adapter);

        Intent intent = new Intent(this, FcmRegisterService.class);
        startService(intent);
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

    public final MyListener mylistener = new MyListener() {
        @Override
        public void onMyClick(Product product) {
            listener.saveFavorite(product);
            Toast.makeText(AppActivity.this, "OK", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            //adapter = new FireAdapter();
            listener.getnecklace();
        } else if (id == R.id.nav_gallery) {
            listener.getErraing();

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
    public void addPath(Product product) {
        adapter.addPath(product);
    }

    @Override
    public void clearView() {
        recyclerView.removeAllViews();
    }

    @Override
    public void setAction(AppContract.ActionListener listener) {
        this.listener = listener;
    }

    public class FireAdapter extends RecyclerView.Adapter<FireAdapter.ItemRecycler> {

        private List<Product> mPostPaths;

        public FireAdapter() {
            mPostPaths = new ArrayList<Product>();
        }

        @Override
        public ItemRecycler onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(AppActivity.this);
            final View view = inflater.inflate(R.layout.itens_list, parent, false);
            return new ItemRecycler(view, mylistener);
        }

        @Override
        public void onBindViewHolder(final ItemRecycler holder, int position) {
            final Product product = mPostPaths.get(position);
            //  holder.titleView.setText(product.getPrice());
            holder.priceView.setText(product.title);
            Glide.with(AppActivity.this).load(product.url).crossFade().centerCrop().into(holder.imgView);
        }

        @Override
        public int getItemCount() {
            return mPostPaths.size();
        }

        public void addPath(Product path) {
            mPostPaths.add(path);
            notifyItemInserted(mPostPaths.size());
        }

        public void addPaths(List<Product> paths) {
            int startIndex = mPostPaths.size();
            mPostPaths.addAll(paths);
            notifyItemRangeInserted(startIndex, mPostPaths.size());
        }

        public Product getItem(int position) {
            return mPostPaths.get(position);
        }


        public class ItemRecycler extends RecyclerView.ViewHolder implements View.OnClickListener {

            public final TextView titleView;
            public final ImageView imgView;
            public final TextView priceView;
            public final AppCompatImageButton btn;
            //  View view;
            private final MyListener myListener;

            public ItemRecycler(View itemView, MyListener myListener) {
                super(itemView);
                // view = itemView;
                this.myListener = myListener;
                titleView = (TextView) itemView.findViewById(R.id.name);
                imgView = (ImageView) itemView.findViewById(R.id.comment_photo);
                priceView = (TextView) itemView.findViewById(R.id.artist);
                btn = (AppCompatImageButton) itemView.findViewById(R.id.btnfavorite);
                btn.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                Log.i("TAG",String.valueOf(position));
                Product product = getItem(position);
                myListener.onMyClick(product);
            }
        }

    }

    public interface MyListener {
        void onMyClick(Product product);
    }
}
