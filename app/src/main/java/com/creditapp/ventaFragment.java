package com.creditapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import com.creditapp.db.clienteModel;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

public class ventaFragment extends Fragment implements View.OnClickListener, View.OnFocusChangeListener {

    private OnFragmentInteractionListener mListener;
    private EditText edtNumero,edtFecha,edtPlazo,edtCedula,edtNombre,edtConcepto, edtValor;
    private clienteModel model;
    int cod_request = 1;
    int mYear, mMonth, mDay;

    public ventaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_ventas, container, false);
        model = new clienteModel(getContext());
        edtNumero   =   (EditText) view.findViewById(R.id.txVentaFactura);
        edtFecha    =   (EditText) view.findViewById(R.id.txVentaFecha);
        edtPlazo    =   (EditText) view.findViewById(R.id.txVentaPlazo);
        edtCedula   =   (EditText) view.findViewById(R.id.txVentaCedula);
        edtNombre   =   (EditText) view.findViewById(R.id.txVentaNombre);
        edtConcepto =   (EditText) view.findViewById(R.id.txVentaConcepto);
        edtValor    =   (EditText) view.findViewById(R.id.txVentaValor);

        edtFecha.setText(fecha());
        edtCedula.requestFocus();

        edtFecha.setOnClickListener(this);
        edtCedula.setOnClickListener(this);

        edtFecha.setOnFocusChangeListener(this);
        edtCedula.setOnFocusChangeListener(this);

        watchers();

        return view;
    }

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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        edtCedula.requestFocus();
    }

    @Override
    public void onResume() {
        super.onResume();
        edtCedula.requestFocus();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txVentaFecha:
                    setDates(edtFecha);
                break;
            case R.id.txVentaCedula:
                if (edtCedula.getText().toString().equals("")) {
                    Intent i = new Intent(getActivity(), busquedaClienteActivity.class);
                    startActivityForResult(i, cod_request);
                }
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == cod_request) && (resultCode == -1)) {
            final String resultCC = data.getDataString();
            if (!resultCC.equals("") && resultCC != null) {
                final Cursor c = model.searchClientes(resultCC);
                if (c != null) {
                    c.moveToFirst();
                    edtCedula.setText(c.getString(c.getColumnIndexOrThrow("Codigo")).toString());
                    edtNombre.setText(c.getString(c.getColumnIndexOrThrow("Nombre")).toString());

                }
            }
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()){
            case R.id.txVentaFecha:
                if(hasFocus){
                    //if(edtFecha.getText().toString().trim().equals("")){
                        setDates(edtFecha);
                        edtPlazo.requestFocus();
                    //}
                }else{

                }
                break;
            case R.id.txVentaCedula:
                if(hasFocus){

                }else{
                    if (edtCedula.getText().toString().equals("")) {
                        edtCedula.setError("Campo obligatorio");
                    }else{
                        Cursor c = model.searchClientes(edtCedula.getText().toString().trim());
                        if(c.moveToFirst()){
                            edtNombre.setText(c.getString(c.getColumnIndexOrThrow("Nombre")).toString());
                        }else{
                            edtNombre.setText("");
                            edtCedula.setError("Cliente no existe");
                        }
                    }
                }
                break;
        }
    }

    private void setDates(EditText tx) {
        Locale locale = getResources().getConfiguration().locale;
        Locale.setDefault(locale);
        final Calendar calendar = Calendar.getInstance();
        final EditText edt = tx;
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String Mes, Dia;
                if (monthOfYear <= 9) {
                    Mes = "0" + (monthOfYear + 1);
                } else {
                    Mes = "" + (monthOfYear + 1);
                }
                if (dayOfMonth <= 9) {
                    Dia = "0" + dayOfMonth;
                } else {
                    Dia = "" + dayOfMonth;
                }
                edt.setText(Dia + "/" + Mes + "/" + year);
            }
        }, mYear, mMonth, mDay);
        dpd.getWindow().setBackgroundDrawableResource(R.color.transparent);
        dpd.setTitle("Seleccione fecha");
        dpd.setCancelable(false);
        dpd.setCanceledOnTouchOutside(false);
        dpd.show();
    }

    private String fecha() {
        final Calendar date = Calendar.getInstance();
        final String year = "" + date.get(Calendar.YEAR);
        final String mes;
        final String dia;
        String fecha;
        if ((date.get(Calendar.MONTH) + 1) <= 9) {
            mes = "0" + (date.get(Calendar.MONTH) + 1);
        } else {
            mes = "" + (date.get(Calendar.MONTH) + 1);
        }
        if (date.get(Calendar.DAY_OF_MONTH) <= 9) {
            dia = "0" + date.get(Calendar.DAY_OF_MONTH);
        } else {
            dia = "" + date.get(Calendar.DAY_OF_MONTH);
        }
        fecha = dia + "/" + mes + "/" + year;
        return fecha;

    }

    private void watchers() {
        edtValor.addTextChangedListener(new TextWatcher() {
            String current = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int index = s.toString().indexOf(".");
                if (index == -1) {
                    if (!s.toString().equals(current)) {
                        edtValor.removeTextChangedListener(this);
                        String cleanString = s.toString().replaceAll("[$ ,]", "");
                        cleanString = (cleanString.trim().equals("")) ? "0" : cleanString;
                        double parsed = Double.parseDouble(cleanString);
                        //String formatted = NumberFormat.getCurrencyInstance().format((parsed/100));
                        //String formatted = NumberFormat.getCurrencyInstance(Locale.US).format((parsed/100));
                        DecimalFormat formatea = new DecimalFormat("###,###.##");
                        String formatted = "$ " + formatea.format(parsed);
                        current = formatted;
                        edtValor.setText(formatted);
                        edtValor.setSelection(formatted.length());
                        edtValor.addTextChangedListener(this);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

    }

}
