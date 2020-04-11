package com.juanfer.androidmysql;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity  {

    Button btnMostrar, btnGuardar, btnLimpiar;
    EditText etIdUsuario, etNombreApellido, etAlias, etTelefono;
    String idusuario,nombreapellido,alias,telefono;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnMostrar = (Button)findViewById(R.id.btnMostrar);
        btnGuardar = (Button)findViewById(R.id.btnGuardar);
        btnLimpiar = (Button)findViewById(R.id.btnLimpiar);

        etIdUsuario = (EditText)findViewById(R.id.etIdusuario);
        etNombreApellido = (EditText)findViewById(R.id.etNOmbreApellido);
        etAlias = (EditText)findViewById(R.id.etAlias);
        etTelefono = (EditText)findViewById(R.id.etTelefono);

        btnLimpiar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                limpiarTextos();
            }
        });

        btnMostrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                idusuario = etIdUsuario.getText().toString().trim();

                String url = "https://androidsqldb.000webhostapp.com/consulta.php?idusuario="+idusuario;
                recuperaData(url);
            }
        });


        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nombreapellido = etNombreApellido.getText().toString().trim();
                alias = etAlias.getText().toString().trim();
                telefono = etTelefono.getText().toString().trim();

                String url = "https://androidsqldb.000webhostapp.com/registro.php?nombreapellido="+nombreapellido+"&alias="+alias+"&telefono="+telefono;
                insertaData(url);

            }
        });
    }

    private void recuperaData(String url) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Consultando...");

        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    String success = jsonObject.getString("success");

                    if (success.equalsIgnoreCase("true")) {

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object = jsonArray.getJSONObject(i);

                            etNombreApellido.setText(object.getString("nombreapellido"));
                            etAlias.setText(object.getString("alias"));
                            etTelefono.setText(object.getString("telefono"));

                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Registro no encontrado!", Toast.LENGTH_LONG).show();
                    }

                } catch(JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }

    private void insertaData(String url){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Procesando...");

        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equalsIgnoreCase("true")) {

                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Dato insertado", Toast.LENGTH_LONG).show();

                } else {

                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        limpiarTextos();

    }

    public void limpiarTextos() {
        etIdUsuario.setText(null);
        etNombreApellido.setText(null);
        etAlias.setText(null);
        etTelefono.setText(null);
    }

}