package com.creditapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.creditapp.db.dbManager;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class homeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, clientesFragment.OnFragmentInteractionListener,
        ventaFragment.OnFragmentInteractionListener {

    String Usuario, Perfil;
    int Recordar;
    dbManager oUserModel;
    Bundle bdl;
    private Menu toolMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        oUserModel = new dbManager(this);
        bdl = this.getIntent().getExtras();
        Usuario = bdl.getString("Usuario").toString();
        Perfil = bdl.getString("Perfil").toString();
        Recordar = bdl.getInt("Recordar");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View Header = navigationView.getHeaderView(0);
        final TextView tvUsuario = (TextView) Header.findViewById(R.id.tvUsuario);
        final TextView tvPerfil = (TextView) Header.findViewById(R.id.tvPerfil);
        tvUsuario.setText(Usuario);
        tvPerfil.setText(Perfil);
        if (Recordar == 0) {
            Toast.makeText(this, "Bienvenido " + Usuario, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private static long backPressed;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (backPressed + 2000 > System.currentTimeMillis()) {
                super.onBackPressed();
            } else {
                Toast.makeText(this, "Pulse de nuevo para salir", Toast.LENGTH_SHORT).show();
                backPressed = System.currentTimeMillis();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        this.toolMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Boolean frgTransaction = false;
        Fragment frg = null;

        if (id == R.id.nav_clientes) {

            frg = new clientesFragment();
            frgTransaction = true;

        } else if (id == R.id.nav_venta) {
            frg = new ventaFragment();
            toolMenu.findItem(R.id.action_search).setVisible(false);
            frgTransaction = true;
        } else if (id == R.id.nav_cxc) {
            toolMenu.findItem(R.id.action_search).setVisible(false);
            Toast.makeText(this, "Cuentas por cobrar ", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_utilidades) {
            toolMenu.findItem(R.id.action_search).setVisible(false);
            frg = new toolsFragment();
            frgTransaction = true;
        } else if (id == R.id.nav_salir) {
            toolMenu.findItem(R.id.action_search).setVisible(false);
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
            alertBuilder.setMessage("Desea cerrar sesión ?")
                    .setCancelable(false)
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            oUserModel.doLogOut();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = alertBuilder.create();
            alert.setTitle("Confirmación");
            alert.show();
            //} else if (id == R.id.nav_send) {
        }
        if (frgTransaction) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_home, frg)
                    .commit();
            item.setChecked(true);
            getSupportActionBar().setTitle(item.getTitle());
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }
}
