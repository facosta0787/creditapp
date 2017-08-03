package com.creditapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.creditapp.db.clienteModel;
import java.text.DecimalFormat;


public class clientesActivity extends AppCompatActivity implements View.OnClickListener {

    private clienteModel model;
    private EditText txCedula,txNombre,txTel,txDir,txEmail,txSaldo;
    private Button btGuardar;
    private Bundle bdl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Clientes");
        //getSupportActionBar().setSubtitle("Crear o modificar clientes");
        model = new clienteModel(this);
        model.putCliente();
        txCedula = (EditText) findViewById(R.id.txCedulaClient);
        txNombre = (EditText) findViewById(R.id.txNombreClient);
        txTel = (EditText) findViewById(R.id.txTelCliente);
        txDir = (EditText) findViewById(R.id.txDirCliente);
        txEmail = (EditText) findViewById(R.id.txEmailCliente);
        txSaldo = (EditText) findViewById(R.id.txSaldoCliente);
        btGuardar = (Button) findViewById(R.id.btGuardarCliente);
        txSaldo.setFocusable(false);
        btGuardar.setOnClickListener(this);

        bdl = this.getIntent().getExtras();
        if(bdl != null){
            final String codigo = bdl.getString("Cod").toString();
            final String nombre = bdl.getString("Nom").toString();
            final String tel = bdl.getString("Tel").toString();
            final String dir = bdl.getString("Dir").toString();
            final String mail = bdl.getString("Mail").toString();
            final Double saldo = bdl.getDouble("Sal");
            DecimalFormat formatea = new DecimalFormat("###,###.##");
            txCedula.setText(codigo);
            txNombre.setText(nombre);
            txTel.setText(tel);
            txDir.setText(dir);
            txEmail.setText(mail);
            txSaldo.setText(formatea.format(saldo));
            txCedula.setFocusable(false);
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        finish();
    }

    @Override
    public void onStop(){
        super.onStop();

    }

    @Override
    public void onDestroy(){
        super.onDestroy();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btGuardarCliente:
                guardarCliente();
                break;
        }
    }

    private void guardarCliente(){

        if(bdl == null){
            if(model.insertCliente(txCedula.getText().toString().trim(),txNombre.getText().toString().trim(),txDir.getText().toString().trim(),txTel.getText().toString().trim(),txEmail.getText().toString().trim())){
                Toast.makeText(clientesActivity.this,"Cliente creado correctamente",Toast.LENGTH_LONG).show();
                this.onPause();
            }else{
                Toast.makeText(clientesActivity.this,"Cliente duplicado",Toast.LENGTH_LONG).show();
            }
        }else{
            if(model.updateCliente(txCedula.getText().toString().trim(),
                    txNombre.getText().toString().trim(),
                    txDir.getText().toString().trim(),
                    txTel.getText().toString().trim(),
                    txEmail.getText().toString().trim())){

                Toast.makeText(clientesActivity.this,"Cliente modificado correctamente",Toast.LENGTH_LONG).show();
                this.onPause();
            }else{
                Toast.makeText(clientesActivity.this,"Error al modificar cliente",Toast.LENGTH_LONG).show();
            }
        }


    }
}
