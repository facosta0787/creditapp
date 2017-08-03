package com.creditapp.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

/**
 * Created by felipe on 23/05/16.
 */
public class clienteModel {

    public static final String tClientes = "create table tClientes ("
            + " _id integer primary key autoincrement,"
            + " Codigo text not null unique,"
            + " Nombre text not null,"
            + " Direccion text,"
            + " Telefono text,"
            + " Correo text,"
            + " Saldo real);";

    private dbHelper odbHelper;
    private SQLiteDatabase odb;
    private keyLock crypt;


    public clienteModel(Context context){
        odbHelper = new dbHelper(context);
        crypt = new keyLock();
    }

    public void putCliente(){

        try{
            odb = odbHelper.getWritableDatabase();
            String query = "insert into tClientes values " +
                    "(null,'811042078','Firlan SAS','Cra 52 # 46 - 68 int 901','4441078','info@babalufashion.com',0);" ;
                    /*+"(null,'811041386','Ramasu SAS','Palacio Nacional 451','4449515','info@tarraointerior.com',0)," +
                    "(null,'1036609137','Felipe Acosta','Calle 42 # 70 - 79','4130197','facosta0787@gmail.com',0)," +
                    "(null,'900746054','@PC Mayoristas','Cra 51 51-17 ED Henry lc 2010','5116179','info@apcmayoristas.com',0)," +
                    "(null,'1040730490','Cristian Ramirez','Cl 74b Sur Cr 57 - 317 int 203','4183106','',0)";*/
            odb.execSQL(query);
            odb.close();
        }catch (SQLiteException e){
            if(odb.isOpen()){
               odb.close();
            }
            Log.i("Error SQLITE: ",e.getMessage().toString());
        }

    }

    public boolean insertCliente(String codigo,String nombre,String dir,String tel,String email){
        boolean resp = false;
        try{
        String[] params = {codigo,nombre,dir,tel,email};
        String query = "Insert into tClientes values (null,?,?,?,?,?,0);";
        odb = odbHelper.getWritableDatabase();
        odb.execSQL(query,params);
        odb.close();
        resp = true;
        }catch (SQLiteException e){
            if(odb.isOpen()){
                odb.close();
            }
            Log.i("Error SQLITE: ",e.getMessage().toString());
        }
        return resp;
    }

    public boolean updateCliente(String codigo,String nombre,String dir,String tel,String email){
        boolean resp = false;
        try{
            String[] params = {nombre,dir,tel,email,codigo};
            String query = "update tClientes set Nombre = ?,Direccion = ?,Telefono = ?,Correo = ? where Codigo = ?;";
            odb = odbHelper.getWritableDatabase();
            odb.execSQL(query,params);
            odb.close();
            resp = true;
        }catch (SQLiteException e){
            if(odb.isOpen()){
                odb.close();
            }
            Log.i("Error SQLITE: ",e.getMessage().toString());
        }
        return resp;
    }

    public Cursor getClientes(){

        odb = odbHelper.getWritableDatabase();
        String query = "Select *," +
                "'Nit: ' || codigo as Nit," +
                "'Dir: ' || Direccion as Dir from tClientes;";
        Cursor c = odb.rawQuery(query,null);

        return c;
    }

    public Cursor searchClientes(String param){
        odb = odbHelper.getWritableDatabase();
        String query = "Select *," +
                "'Nit: ' || codigo as Nit," +
                "'Dir: ' || Direccion as Dir from tClientes " +
                "where Codigo like '%"+param+"%' or Nombre like '%"+param+"%';";
        Cursor c = odb.rawQuery(query,null);

        return c;
    }

    public void delCliente(String codigo){
        odb = odbHelper.getWritableDatabase();
        String query = "Delete from tClientes where Codigo = '" + codigo + "' and Saldo = 0;";
        odb.execSQL(query);
        odb.close();

    }

}
