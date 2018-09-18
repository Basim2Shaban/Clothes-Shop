package com.basim.outfitters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.basim.outfitters.firebase_use.FireBaseVarConnect;
import com.basim.outfitters.single_classes.ClassMakeUserOnAndOff;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Fragment fragment ;
    private FirebaseAuth mAuth;
    private FireBaseVarConnect varConnect = new FireBaseVarConnect();
    String getInt = "no" ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        CheckOfStatusLoginUser();





        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);
            }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.Action_Notification) {

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // good d56073  //  ec9e69

        if (id == R.id.nav_home) {
            getSupportActionBar().setTitle("Clothes Shop");
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#7c203a")));
            fragment = new Fragment_HomePage();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.FrameLayoutFragment,fragment);
            ft.commit();

        } else if (id == R.id.nav_message) {
            getSupportActionBar().setTitle("Messages");
            getSupportActionBar().getThemedContext().setTheme(R.style.StyleFragmentMessages);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#DF7782")));
            fragment = new Fragment_Messages();
            android.app.FragmentManager frgManager = getFragmentManager();
            frgManager.beginTransaction().replace(R.id.FrameLayoutFragment, fragment).commit();



        } else if (id == R.id.nav_my_account) {
            getSupportActionBar().setTitle("My Account");
            fragment = new Fragment_MyProfile();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.FrameLayoutFragment,fragment);
            ft.commit();

        } else if (id == R.id.nav_Settings) {
            getSupportActionBar().setTitle("Settings");
            fragment = new Fragment_SettingAccount();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.FrameLayoutFragment,fragment);
            ft.commit();

        } else if (id == R.id.nav_signOut) {
            ClassMakeUserOnAndOff makeUserOnAndOff = new ClassMakeUserOnAndOff();
            varConnect.bUserStateConn.child(varConnect.bCurrentUser.getUid()).child("state")
                    .onDisconnect().setValue(makeUserOnAndOff.timeNowMilliSecond());
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(MainActivity.this,StartActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }






    public void SendToStartActivity (){
        Intent intent = new Intent(MainActivity.this , StartActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void SendToRegiester_Iformation(){
        Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
        intent.putExtra("start","main");
        startActivity(intent);
    }

    public void CheckOfStatusLoginUser(){
        final FirebaseUser mAuthCurrentUser = mAuth.getCurrentUser();

        if (mAuthCurrentUser == null) {
            SendToStartActivity();

        }else{
            getSupportActionBar().setTitle("Clothes Shop");
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#7c203a")));
            Fragment_HomePage fragmentHomePage = new Fragment_HomePage();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.FrameLayoutFragment,fragmentHomePage);
            ft.commit();

            ClassMakeUserOnAndOff makeUserOnAndOff = new ClassMakeUserOnAndOff();
            makeUserOnAndOff.makeUserOn();


            varConnect.bUserStateConn.child(varConnect.bCurrentUser.getUid()).child("state")
                    .onDisconnect().setValue(makeUserOnAndOff.timeNowMilliSecond());

        }


    }


    @Override
    protected void onResume() {
        super.onResume();
    }
}
