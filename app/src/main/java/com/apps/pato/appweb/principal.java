package com.apps.pato.appweb;

import android.app.ListActivity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class principal extends AppCompatActivity {
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public String SERVIDOR = "http://92f5b48f.ngrok.io/uno/node.json?type=personal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Cargar los datos del WS

        listar listar = new listar();
        listar.execute();

        recycler = (RecyclerView)findViewById(R.id.recycler_view);
        recycler.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(layoutManager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private class listar extends AsyncTask<String, Integer, Boolean>{
        private String[] personas;

        protected Boolean doInBackground(String... paramas){
            boolean result = true;
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpGet = new HttpPost(SERVIDOR);

            httpGet.setHeader(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            try{
                HttpResponse httpResponse = httpClient.execute(httpGet);
                String res = EntityUtils.toString(httpResponse.getEntity());

                JSONArray json = new JSONArray(res);
                personas = new String[json.length()];

                for (int i = 0; i < json.length(); i++){
                    JSONObject obj = json.getJSONObject(i);
                    int idPerson = obj.getInt("id");
                    String name = obj.getString("nombres");
                    String last_name = obj.getString("apellidos");
                    String correo = obj.getString("correo");
                    String telefono = obj.getString("telefono");
                    personas[i] = ""+idPerson+" "+ name+" "+last_name+" "+correo+" "+telefono;
                }
            }catch (Exception e){
                Log.d("SERVICIO ", "Error al intentar obtener el servicio");
            }
            return result;
        }

        protected void onPostExecute(Boolean result){
            if (result){
                Log.d("primer" ,  "listo");
//                ArrayAdapter<String> adaptador =
//                        new ArrayAdapter<String>(principal.this,
//                                android.R.layout.simple_list_item_1, personas);
//
//                listaPersonas.setAdapter(adaptador);
            }
        }
    }
}

