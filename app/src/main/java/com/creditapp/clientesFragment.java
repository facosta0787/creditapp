package com.creditapp;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.support.v7.widget.SearchView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.creditapp.db.clienteModel;
import com.github.clans.fab.FloatingActionButton;
import java.text.DecimalFormat;
import java.util.HashMap;


public class clientesFragment extends Fragment implements AdapterView.OnItemClickListener {

    private OnFragmentInteractionListener mListener;
    private clienteModel model;
    private ListView lv;
    private SearchView sv = null;
    private SearchView.OnQueryTextListener listener;
    private Menu toolMenu;
    FloatingActionButton fab;


    public clientesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_clientes, container, false);
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fabMain);
        fab.show(true);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(),clientesActivity.class);
                startActivity(i);
               /*Snackbar.make(view, "Agregar nuevo cliente", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });
        lv = (ListView) view.findViewById(R.id.lvClientes);
        model = new clienteModel(getActivity());
        //model.putCliente();
        showClientes();
        lv.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume(){
        super.onResume();
        showClientes();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home, menu);
        this.toolMenu = menu;
        menu.findItem(R.id.action_search).setVisible(true);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            sv = (SearchView) searchItem.getActionView();
            sv.setQueryHint("Buscar cliente");

        }
        if (sv != null) {
            sv.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            sv.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){
                        fab.hide(false);
                    }else if(!hasFocus){
                        Handler hand = new Handler();
                        hand.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                fab.show(true);
                            }
                        },500);


                    }
                }
            });
            listener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    if(newText.equals("")){
                        lv.setAdapter(null);
                        Cursor c = model.getClientes();
                        SimpleCursorAdapter adt = new SimpleCursorAdapter(getActivity(),
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
                    return true;
                }
                @Override
                public boolean onQueryTextSubmit(String query) {
                    execSearch(query);
                    return true;
                }
            };
            sv.setOnQueryTextListener(listener);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                // Not implemented here
                return false;
            default:
                break;
        }
        sv.setOnQueryTextListener(listener);
        return super.onOptionsItemSelected(item);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fab.hide(true);
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Dialog -> Opciones de Accion sobre registros de Usuario
        AlertDialog.Builder builer  = new AlertDialog.Builder(getContext());

        ListView lvAcciones = new ListView(getContext());
        String[] Acciones = new String[]{"Editar Cliente","Eliminar Cliente"};
        ArrayAdapter<String> AccionesAdt = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1,android.R.id.text1,Acciones);
        lvAcciones.setAdapter(AccionesAdt);
        builer.setView(lvAcciones);

        final Dialog AccionesDialog = builer.create();
        AccionesDialog.show();
        final Cursor c = (Cursor) lv.getItemAtPosition(position);
        lvAcciones.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String _id = c.getString(c.getColumnIndexOrThrow("_id")).toString();
                final String codigo = c.getString(c.getColumnIndexOrThrow("Codigo")).toString();
                final String nombre = c.getString(c.getColumnIndexOrThrow("Nombre")).toString();
                final String tel = c.getString(c.getColumnIndexOrThrow("Telefono")).toString();
                final String dir = c.getString(c.getColumnIndexOrThrow("Direccion")).toString();
                final String email = c.getString(c.getColumnIndexOrThrow("Correo")).toString();
                final Double saldo = c.getDouble(c.getColumnIndexOrThrow("Saldo"));

                switch (position){
                    case 0:
                        Intent i = new Intent(getActivity(),clientesActivity.class);
                        i.putExtra("Cod",codigo);
                        i.putExtra("Nom",nombre);
                        i.putExtra("Tel",tel);
                        i.putExtra("Dir",dir);
                        i.putExtra("Mail",email);
                        i.putExtra("Sal",saldo);
                        startActivity(i);
                        AccionesDialog.dismiss();
                        break;
                    case 1:
                        delCliente(codigo,nombre,saldo);
                        AccionesDialog.dismiss();
                        break;
                }
            }
        });
    }
    
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private void showClientes(){
        Cursor c = model.getClientes();
        SimpleCursorAdapter adt = new SimpleCursorAdapter(getActivity(),
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
        SimpleCursorAdapter adt = new SimpleCursorAdapter(getActivity(),
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

    private void delCliente(final String codCliente,final String nombre,final Double saldo){

        if (saldo == 0){
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
            alertBuilder.setMessage("Eliminar cliente "+ nombre +"?")
                    .setCancelable(false)
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            model.delCliente(codCliente);
                            lv.setAdapter(null);
                            showClientes();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = alertBuilder.create();
            alert.setTitle("Eliminar");
            alert.show();
        }else{
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
            alertBuilder.setMessage("Cliente con saldo no se puede eliminar")
                    .setCancelable(false)
                    .setPositiveButton("Aceptar",new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog alert = alertBuilder.create();
            alert.setTitle("Error");
            alert.show();

        }



    }

}
