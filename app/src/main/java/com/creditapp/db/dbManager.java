package com.creditapp.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.annotation.IntegerRes;
import android.util.Log;

import java.sql.SQLClientInfoException;

public class dbManager {

    public static final String tUsuarios = "create table tUsuarios ("
            + " _id integer primary key autoincrement,"
            + " Usuario text not null unique,"
            + " Passwd text not null,"
            + " Nombre text,"
            + " Perfil text,"
            + " Recordar integer not null);";



    private dbHelper odbHelper;
    private SQLiteDatabase odb;
    private keyLock crypt;

    public dbManager(Context context){
        odbHelper = new dbHelper(context);
        odb = odbHelper.getWritableDatabase();
        crypt = new keyLock();
    }

    public void creaUserDefault(){
        //odb.execSQL("drop table tUsuarios");
        //odb.execSQL(tUsuarios);
        int numUsuarios = numRows("Select Usuario from tUsuarios;");
        if (numUsuarios == 0){
            String query =
                    "insert into tUsuarios values (null,'admin','"+crypt.encriptarCadena("adminadmin")+"','Usuario Admin','Vendedor',0)," +
                    "(null,'facosta','"+crypt.encriptarCadena("facostaporque")+"','Felipe Acosta','Vendedor',0);";

            odb.execSQL(query);

        }
    }

    public boolean putUsuario(String usuario,String pwd,String nombre,String perfil){
        boolean result = false;
        try{
            String query = "insert into tUsuarios values " +
                    "(null,'"+usuario+"','"+crypt.encriptarCadena(usuario+pwd)+"','"+nombre+"','"+perfil+"',0);";
                    odb.execSQL(query);
            result = true;
        }catch (SQLiteException e){
            Log.i("Error Sql",e.getMessage().toString());
        }
            return result;
    }

    public boolean doLogin(String usuario, String pwd, boolean Remember){
        boolean result = false;
        String query = "Select * from tUsuarios where Usuario = '" + usuario + "' and Passwd = '" + pwd + "';";
        Cursor c = odb.rawQuery(query,null);
        int count = c.getCount();
        if(count > 0){
            if(Remember){
                odb.execSQL("Update tUsuarios set Recordar = 0");
                odb.execSQL("Update tUsuarios set Recordar = 1 where Usuario = '"+ usuario +"';");
            }
            result = true;
        }

        return result;
    }

    public void doLogOut(){
        odb.execSQL("Update tUsuarios set Recordar = 0");
    }

    public Cursor getUsuarioRecordado(){
        Cursor c = null;
        try{
            c = odb.rawQuery("Select * from tUsuarios where Recordar = 1",null);
        }catch (SQLiteException e){
            Log.i("Error Sql",e.getMessage().toString());
        }

        return c;
    }

    public Cursor getUsuario(String usuario){
        String query = "Select * from tUsuarios where Usuario = '" + usuario + "';";
        Cursor c = odb.rawQuery(query,null);

        return c;
    }

    public Cursor getUsuarios(){
        String query = "Select * from tUsuarios;";
        Cursor c = odb.rawQuery(query,null);
        return c;
    }

    public boolean delUsuario(String usuario){
        boolean result = false;
                try{
                    String query = "delete from tUsuarios where Usuario = '" + usuario + "';";
                    odb.execSQL(query);
                    result = true;
                }catch (SQLiteException e){
                    Log.i("Error Sql",e.getMessage().toString());
                }
            return result;
    }

    public int numRows (String query){
        int result = 0;
        /*Cursor c = db.rawQuery("Select 1 from tblCalificaciones where Async = 0",null);*/
        Cursor c = odb.rawQuery(query,null);
        result = c.getCount();
        c.close();

        return result;
    }

}
