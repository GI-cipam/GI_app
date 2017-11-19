package gov.cipam.gi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
import gov.cipam.gi.Adapters.ViewPageAdapter;
import gov.cipam.gi.Common.SharedPref;
import gov.cipam.gi.Interface.ItemClickListener;
import gov.cipam.gi.Model.Categories;
import gov.cipam.gi.Model.States;
import gov.cipam.gi.Model.Users;
import gov.cipam.gi.ViewHolder.CategoryViewHolder;
import gov.cipam.gi.ViewHolder.StateViewHolder;

public class HomePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseState,mDatabaseCategory;
    RecyclerView rvState,rvCategory;
    RecyclerView.LayoutManager layoutManager,layoutManager2;
    FirebaseRecyclerAdapter<States,StateViewHolder> adapter;
    FirebaseRecyclerAdapter<Categories,CategoryViewHolder>adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        mDatabaseState = FirebaseDatabase.getInstance().getReference("States");
        mDatabaseCategory = FirebaseDatabase.getInstance().getReference("Categories");



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView =  navigationView.getHeaderView(0);
        TextView nav_user = (TextView)hView.findViewById(R.id.nav_name);
        Users user = SharedPref.getSavedObjectFromPreference(HomePage.this,"userinfo","userdata",Users.class);
        if(user!=null) {
            nav_user.setText(user.getName());
        }


        AutoScrollViewPager autoScrollViewPager = (AutoScrollViewPager) findViewById(R.id.viewpager);
        autoScrollViewPager.setAdapter(new ViewPageAdapter(this));
        autoScrollViewPager.startAutoScroll();
        autoScrollViewPager.setScrollDurationFactor(5);
        autoScrollViewPager.setStopScrollWhenTouch(true);
        autoScrollViewPager.setInterval(3000);

        rvState = (RecyclerView) findViewById(R.id.recycler_states);
        rvCategory = (RecyclerView) findViewById(R.id.recycler_categories);

        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        layoutManager2 = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        rvState.setLayoutManager(layoutManager);
        rvCategory.setLayoutManager(layoutManager2);

        LoadStates();
        LoadCategories();

    }


    private void LoadStates() {
        adapter = new FirebaseRecyclerAdapter<States, StateViewHolder>(States.class,R.layout.state_item,StateViewHolder.class,mDatabaseState) {
            @Override
            protected void populateViewHolder(StateViewHolder viewHolder, final States model, int position) {
                viewHolder.mName.setText(model.getName());
                final Uri uri =Uri.parse(model.getDpurl());
                Glide.with(getBaseContext())
                        .load(uri)
                        .fitCenter()
                        .into(viewHolder.mDp);
                final States clickitem = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(HomePage.this,model.getName(),Toast.LENGTH_LONG).show();




                    }
                });

            }
        };
        rvState.setAdapter(adapter);


    }
    private void LoadCategories() {
        adapter2 = new FirebaseRecyclerAdapter<Categories,CategoryViewHolder>(Categories.class,R.layout.category_item,CategoryViewHolder.class,mDatabaseCategory) {
            @Override
            protected void populateViewHolder(CategoryViewHolder viewHolder, final Categories model, int position) {
                viewHolder.mName.setText(model.getName());
                final Uri uri =Uri.parse(model.getDpurl());
                Glide.with(getBaseContext())
                        .load(uri)
                        .fitCenter()
                        .into(viewHolder.mDp);

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(HomePage.this,model.getName(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        };
        rvCategory.setAdapter(adapter2);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_signout) {
            mAuth.signOut();
            startActivity(new Intent(HomePage.this,SignIn.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_account) {
          startActivity(new Intent(HomePage.this,AccountInfo.class));
        }
        else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    protected void onStart() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser ==null){
            startActivity(new Intent(HomePage.this,SignIn.class));
        }
        super.onStart();
    }
}
