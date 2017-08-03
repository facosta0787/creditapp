package com.creditapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.creditapp.db.clienteModel;

import java.text.DecimalFormat;

public class busquedaClienteActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private clienteModel model;
    private ListView lv;
    private SearchView sv = null;
    private SearchView.OnQueryTextListener listener;
    private Menu toolMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda_cliente);

        Toolbar toolbar = (Toolbar) findViewById(R.id.busquedaToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Busqueda clientes");
        lv = (ListView) findViewById(R.id.lvBusquedaClientes);
        model = new clienteModel(this);
        showClientes();
        lv.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        this.toolMenu = menu;
        menu.findItem(R.id.action_settings).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(true);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager sManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if(searchItem != null){
            sv = (SearchView) searchItem.getActionView();
            sv.setQueryHint("Buscar cliente");
        }
        if(sv != null){
            sv.setSearchableInfo(sManager.getSearchableInfo(this.getComponentName()));
            sv.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener(){
                @Override
                public void onFocusChange(View v, boolean hasFocus) {

                }
            });
            listener = new SearchView.OnQueryTextListener(){

                @Override
                public boolean onQueryTextSubmit(String query) {
                    execSearch(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if(newText.equals("")){
                        lv.setAdapter(null);
                        Cursor c = model.getClientes();
                        SimpleCursorAdapter adt = new SimpleCursorAdapter(getBaseContext(),
                                R.layout.simple_list_item3,
                                c,
                                new String[]{"Nombre","Nit","Dir","Saldo"},
                                new int[]{R.id.txLinea1, R.id.txLinea2,R.id.txLinea3,R.id.txLinea4}, 0);
                        adt.setViewBinder(new SimpleCursorAdapter.ViewBinder(){
                            @Override
                            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                                if(columnIndex == cursor.getColumnIndexOrThrow("Saldo")){
                                    Double saldo = cursor.getDouble(cursor.getColumnIndexOrThrow("Saldo"));
                                    TextView tv = (TextView) view;
                                    DecimalFormat formatea = new DecimalFormat("###,###.##");
                                    tv.setText("Saldo: $ " + formatea.format(saldo));
                                    return true;
                                }
                                return false;
                            }
                        });
                        lv.setAdapter(adt);
                    }
                    return false;
                }
            };
            sv.setOnQueryTextListener(listener);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Cursor c = (Cursor) lv.getItemAtPosition(position);
        final String Cedula = c.getString(c.getColumnIndexOrThrow("Codigo")).toString();
        Intent data = new Intent();
        data.setData(Uri.parse(Cedula));
        setResult(RESULT_OK,data);
        finish();
    }

    private void showClientes(){
        Cursor c = model.getClientes();
        SimpleCursorAdapter adt = new SimpleCursorAdapter(this,
                R.layout.simple_list_item3,
                c,
                new String[]{"Nombre","Nit","Dir","Saldo"},
                new int[]{R.id.txLinea1, R.id.txLinea2,R.id.txLinea3,R.id.txLinea4}, 0);
        adt.setViewBinder(new SimpleCursorAdapter.ViewBinder(){
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if(columnIndex == cursor.getColumnIndexOrThrow("Saldo")){
                    Double saldo = cursor.getDouble(cursor.getColumnIndexOrThrow("Saldo"));
                    TextView tv = (TextView) view;
                    DecimalFormat formatea = new DecimalFormat("###,###.##");
                    tv.setText("Saldo: $ " + formatea.format(saldo));
                    return true;
                }
                return false;
            }
        });
        lv.setAdapter(adt);
    }

    private void execSearch(String param){
        lv.setAdapter(null);
        Cursor c = model.searchClientes(param);
        SimpleCursorAdapter adt = new SimpleCursorAdapter(getBaseContext(),
                R.layout.simple_list_item3,
                c,
                new String[]{"Nombre","Nit","Dir","Saldo"},
                new int[]{R.id.txLinea1, R.id.txLinea2,R.id.txLinea3,R.id.txLinea4}, 0);
        adt.setViewBinder(new SimpleCursorAdapter.ViewBinder(){
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if(columnIndex == cursor.getColumnIndexOrThrow("Saldo")){
                    Double saldo = cursor.getDouble(cursor.getColumnIndexOrThrow("Saldo"));
                    TextView tv = (TextView) view;
                    DecimalFormat formatea = new DecimalFormat("###,###.##");
                    tv.setText("Saldo: $ " + formatea.format(saldo));
                    return true;
                }
                return false;
            }
        });
        lv.setAdapter(adt);

    }
}
