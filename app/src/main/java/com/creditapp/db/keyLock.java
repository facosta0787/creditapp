package com.creditapp.db;

/**
 * Created by facosta on 11/05/2016.
 */
public class keyLock {

    private final String patron_busqueda = "ABñopqrNÑOPFGHIcdefghiJKLMQRSTVWXvw89xyz12YZabjklmnst3456CDE70";
    private final String patron_encripta = "XYZabcdeBCDETVfghFGstvwHIJKLMN456ñop789ÑOPQRSijklmnqrxyz12AW30";

    public String encriptarCadena(String cadena){
        int idx;
        int to = cadena.length();
        String result = "";
        for(idx = 0; idx < to ; idx++ ){
            result = result + encriptarCaracter(cadena.substring(idx,idx+1),cadena.length(),idx);
        }
        return result;
    }

    private String encriptarCaracter(String caracter,int variable,int a_indice){
        //String caracterEncriptado = "";
        int indice;
        if(patron_busqueda.indexOf(caracter) != -1){
            indice = (patron_busqueda.indexOf(caracter) + variable + a_indice) % patron_busqueda.length();
            return patron_encripta.substring(indice,indice+1);
        }
        return caracter;
    }

    public String desencriptarCadena(String cadena){
        int idx;
        String result = "";
        for(idx = 0;idx < cadena.length(); idx++){
            result += desencriptarCaracter(cadena.substring(idx,idx+1),cadena.length(),idx);
        }
        return result;
    }

    private String desencriptarCaracter(String caracter,int variable,int a_indice){
        int indice;

        if(patron_encripta.indexOf(caracter) != -1){
           if((patron_encripta.indexOf(caracter) - variable - a_indice) > 0){
               indice = (patron_encripta.indexOf(caracter) - variable - a_indice) % patron_encripta.length();
           }else{
               indice = patron_busqueda.length() + ((patron_encripta.indexOf(caracter) - variable - a_indice) % patron_encripta.length());
           }
            indice = indice % patron_encripta.length();
            return patron_busqueda.substring(indice,indice+1);
        }else{
            return caracter;
        }
    }



}
