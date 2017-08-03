package com.creditapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class toolsFragment extends ListFragment {

    private String[] itemsTools = {"Usuarios","Sincronizar","Acerca de"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_tools, container, false);
        //ArrayAdapter<String> adt = new ArrayAdapter<String>(getActivity(),R.layout.rowlayout,R.id.txtItem,itemsTools);
        ArrayAdapter<String> adt = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,itemsTools);
        setListAdapter(adt);
        setRetainInstance(true);
        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);

        /*Toast.makeText(getActivity(), "Opcion seleccionada: " + itemsTools[position],
                Toast.LENGTH_LONG).show();*/
        switch (position){
            case 0:
                Intent I = new Intent(getActivity(),usuariosActivity.class);
                startActivity(I);
                break;
            case 1:
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                alertBuilder.setMessage("Desea sincronizar la base de datos ?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            getActivity().finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                    }
                });
                AlertDialog alert = alertBuilder.create();
                alert.setTitle("Sincronizar");
                alert.show();
                break;


        }
    }

    @Override
    public void onStart(){
        super.onStart();

    }


}
