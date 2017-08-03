package com.creditapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import com.creditapp.db.dbManager;
import com.creditapp.db.keyLock;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    dbManager dbConex;
    keyLock crypt;
    Button btnEntrar;
    EditText edtUsuario,edtPwd;
    CheckBox chkRecordar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbConex = new dbManager(this);
        dbConex.creaUserDefault();
        crypt = new keyLock();

        Cursor c = dbConex.getUsuarioRecordado();
        if(c.getCount() > 0){
            goToMain(c,1);
        }else{
            btnEntrar   = (Button) findViewById(R.id.btnEntrar);
            edtUsuario  = (EditText) findViewById(R.id.edtUsuario);
            edtPwd      = (EditText) findViewById(R.id.edtPwd);
            chkRecordar = (CheckBox) findViewById(R.id.chkRecordarLogin);
            btnEntrar.setOnClickListener(this);
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnEntrar:
                String password = crypt.encriptarCadena(edtUsuario.getText().toString() + edtPwd.getText().toString());
                if(dbConex.doLogin(edtUsuario.getText().toString(),password,chkRecordar.isChecked())){
                        Cursor c = dbConex.getUsuario(edtUsuario.getText().toString());
                        goToMain(c,0);
                }else{
                    Toast.makeText(this,"Usuario o contraseña incorrectos",Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    private void goToMain(Cursor c,int recordar){
        if(c.moveToFirst()){
            Intent intPrincipal = new Intent(this,homeActivity.class);
            intPrincipal.putExtra("Usuario",c.getString(3));
            intPrincipal.putExtra("Perfil",c.getString(4));
            intPrincipal.putExtra("Recordar",recordar);
            startActivity(intPrincipal);
        }
    }
}
