package com.creditapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.creditapp.db.dbManager;
import com.creditapp.db.keyLock;

import java.util.List;

public class usuariosActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    dbManager dbConex;
    keyLock crypt;
        ListView lv;
    List<String> item = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newUser("");
               /* Snackbar.make(view, "Agregar nuevo usuario", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });

        dbConex = new dbManager(this);
        crypt = new keyLock();
        lv = (ListView) findViewById(R.id.lvUsuarios);
        showUsers();
        lv.setOnItemClickListener(this);
    }

    @Override
    public void onPause(){
        super.onPause();
        finish();
    }

    private void showUsers() {
        Cursor c = dbConex.getUsuarios();
        ListAdapter adt = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2,
                c,
                new String[]{"Nombre", "Usuario"},
                new int[]{android.R.id.text1, android.R.id.text2}, 0);
        /*ListAdapter adt = new SimpleCursorAdapter(this,
                R.layout.simple_list_item3,
                c,
                new String[]{"Nombre", "Usuario","Perfil"},
                new int[]{R.id.txLinea1, R.id.txLinea2,R.id.txLinea3}, 0);*/
        lv.setAdapter(adt);
        /*if (c.moveToFirst()) {
            do {
                Log.i("**** Cursor: " + c.getString(0), c.getString(3) + ' ' + c.getString(1));
            } while (c.moveToNext());
        }*/


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        //Dialog -> Opciones de Accion sobre registros de Usuario
        AlertDialog.Builder builer  = new AlertDialog.Builder(usuariosActivity.this);
        ListView lvAcciones = new ListView(usuariosActivity.this);
        String[] Acciones = new String[]{"Editar Usuario","Eliminar Usuario"};
        ArrayAdapter<String> AccionesAdt = new ArrayAdapter<String>(usuariosActivity.this,
                android.R.layout.simple_list_item_1,android.R.id.text1,Acciones);
        lvAcciones.setAdapter(AccionesAdt);
        builer.setView(lvAcciones);
        final Dialog AccionesDialog = builer.create();
        AccionesDialog.show();
        final Cursor c = (Cursor) lv.getItemAtPosition(position);

        lvAcciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String _id = c.getString(c.getColumnIndexOrThrow("_id")).toString();
                final String Usuario = c.getString(c.getColumnIndexOrThrow("Usuario")).toString();
                final String Nombre = c.getString(c.getColumnIndexOrThrow("Nombre")).toString();
                switch (position){
                    case 0:
                        newUser(Usuario);
                        /*AlertDialog.Builder msj = new AlertDialog.Builder(usuariosActivity.this);
                        msj.setTitle("CreditApp");
                        msj.setMessage(_id + " | " +Usuario + " | " + Nombre);
                        msj.setNeutralButton("Aceptar",null);
                        msj.show();*/

                        break;
                    case 1:
                        //Confirmación para eliminación de Usuario
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(usuariosActivity.this);
                        alertBuilder.setMessage("Eliminar usuario "+ Usuario +"?")
                                .setCancelable(false)
                                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dbConex.delUsuario(Usuario);
                                        lv.setAdapter(null);
                                        showUsers();
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

                        break;
                }
                AccionesDialog.dismiss();
            }
        });
    }

    private void newUser(final String usuario){
        final Dialog dlgNewUser = new Dialog(usuariosActivity.this);
        dlgNewUser.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlgNewUser.setContentView(R.layout.content_usuario_nuevo);
        final TextView tvTitulo = (TextView) dlgNewUser.findViewById(R.id.tvNewTittle);
        final EditText txUsuario = (EditText) dlgNewUser.findViewById(R.id.txNewUser);
        final EditText txPwd = (EditText) dlgNewUser.findViewById(R.id.txNewPwd);
        final EditText txConfirm = (EditText) dlgNewUser.findViewById(R.id.txNewConfirm);
        final EditText txNombre = (EditText) dlgNewUser.findViewById(R.id.txNewNombre);
        final EditText txPerfil = (EditText) dlgNewUser.findViewById(R.id.txNewPerfil);
        final Button btnGuardar = (Button) dlgNewUser.findViewById(R.id.btNewGuardar);
        final Button btnCancelar = (Button) dlgNewUser.findViewById(R.id.btNewCancelar);
        if (!usuario.equals("")){
            Cursor c = dbConex.getUsuario(usuario);
            if(c.moveToFirst()){
                txUsuario.setText(c.getString(1));
                txUsuario.setFocusable(false);
                String pwd = crypt.desencriptarCadena(c.getString(2)).substring(c.getString(1).length(),c.getString(2).length());
                txPwd.setText(pwd);
                txConfirm.setText(pwd);
                txNombre.setText(c.getString(3));
                txPerfil.setText(c.getString(4));
            }

        }
        tvTitulo.setTextColor(ContextCompat.getColor(this,R.color.Verde));
        txUsuario.requestFocus();
        txUsuario.setOnFocusChangeListener(new View.OnFocusChangeListener(){

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){

                }else if (hasFocus){

                }
            }
        });


        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txUsuario.getText().toString().trim().equals("")){
                    txUsuario.setError("Usuari requerido");
                }else if(txPwd.getText().toString().trim().equals("") || txConfirm.getText().toString().trim().equals("")){
                    //txPwd.setError("");
                    txConfirm.setError("Campos requeridos");
                }else if(txNombre.getText().toString().trim().equals("")){
                    txNombre.setError("Campo reuqerido");
                }else if(txPerfil.getText().toString().trim().equals("")){
                    txPerfil.setError("Campo requerido");
                }else{
                    if(txPwd.getText().toString().trim().length() < 6){
                        Toast.makeText(usuariosActivity.this,"La contraseña debe tener minimo 6 caracteres",Toast.LENGTH_LONG).show();
                    }else{
                        if(!txPwd.getText().toString().trim().equals(txConfirm.getText().toString().trim())){
                            Toast.makeText(usuariosActivity.this,"Las contraseñas no coinciden",Toast.LENGTH_LONG).show();
                        }else{

                            Cursor c = dbConex.getUsuario(txUsuario.getText().toString().trim());
                            int exist = c.getCount();
                            if(exist == 1 && usuario.equals("")){
                                Toast.makeText(usuariosActivity.this,"Usuario ya existe",Toast.LENGTH_LONG).show();
                            }else{
                                if(dbConex.delUsuario(txUsuario.getText().toString())){
                                    if(dbConex.putUsuario(
                                            txUsuario.getText().toString().trim(),
                                            txPwd.getText().toString().trim(),
                                            txNombre.getText().toString().trim(),
                                            txPerfil.getText().toString().trim())){
                                        dlgNewUser.dismiss();
                                        lv.setAdapter(null);
                                        showUsers();
                                        if(usuario.equals("")){
                                            Toast.makeText(usuariosActivity.this,"Usuario creado exitosamente",Toast.LENGTH_LONG).show();
                                        }else{
                                            Toast.makeText(usuariosActivity.this,"Usuario modificado exitosamente",Toast.LENGTH_LONG).show();
                                        }


                                    }
                                }
                            }

                        }
                    }

                }

            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlgNewUser.dismiss();
            }
        });
        dlgNewUser.show();
    }
}