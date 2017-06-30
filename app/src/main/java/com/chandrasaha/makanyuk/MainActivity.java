package com.chandrasaha.makanyuk;


import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.chandrasaha.makanyuk.Fragment.FragmentBantuan;
import com.chandrasaha.makanyuk.Fragment.FragmentHome;
import com.chandrasaha.makanyuk.Fragment.FragmentTerdekat;
import com.chandrasaha.makanyuk.Fragment.FragmentMap;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

import java.util.logging.Handler;


public class MainActivity extends AppCompatActivity {
    private Fragment f;
    private Drawer result = null;
    private static final int TIME_INTERVAL = 2000; //
    private long mBackPressed;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerBuilder drawerBuilder = new DrawerBuilder();
        drawerBuilder.withActivity(this);
        drawerBuilder.withToolbar(toolbar);
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withName("Beranda").withIdentifier(1).withIcon(GoogleMaterial.Icon.gmd_home);
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withName("Tempat Makan Terdekat").withIdentifier(2).withIcon(GoogleMaterial.Icon.gmd_cake);
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withName("Tampilan Peta").withIdentifier(3).withIcon(GoogleMaterial.Icon.gmd_map);
        PrimaryDrawerItem item5 = new PrimaryDrawerItem().withName("Tentang").withIdentifier(5).withIcon(GoogleMaterial.Icon.gmd_help);
        f = FragmentHome.newInstance(getApplicationContext(), "");
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, f).commit();

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .build();


        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        item1,
                        new DividerDrawerItem(),
                        item2,
                        new DividerDrawerItem(),
                        item3,
                        new DividerDrawerItem(),
                        item5,
                        new DividerDrawerItem()

                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener()
                {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem)
                    {
                        if (drawerItem != null && drawerItem instanceof Nameable)
                        {

                            Fragment f = null;
                            switch (drawerItem.getIdentifier())
                            {
                                case 1:
                                    f = FragmentHome.newInstance(getApplicationContext(), "");
                                    toolbar.setTitle("Home");
                                    break;
                                case 2:
                                    f = FragmentTerdekat.newInstance(getApplicationContext(), "");
                                    toolbar.setTitle("Tempat Makan Terdekat");
                                    break;
                                case 3:
                                    f = FragmentMap.newInstance(getApplicationContext(), "");
                                    toolbar.setTitle("Map");
                                    break;
                                case 5:
                                    f = FragmentBantuan.newInstance(getApplicationContext(), "");
                                    toolbar.setTitle("Tentang" +
                                            "");
                                    break;

                                default:
                                    break;
                            }
                            if (f != null)
                            {
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, f).commit();
                            } else
                            {
                                f = FragmentHome.newInstance(getApplicationContext(), "");
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, f).commit();
                            }

                        }
                        return false;
                    }
                })
                .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            return false;
        }
        if (id == R.id.action_settings) {
            return false;
        }

        return false;
    }
    @Override
    public void onBackPressed()
    {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
        {
            super.onBackPressed();
            return;
        }
        else { Toast.makeText(getBaseContext(), "Tekan tombol back sekali lagi untuk keluar", Toast.LENGTH_SHORT).show(); }

        mBackPressed = System.currentTimeMillis();
    }
}
