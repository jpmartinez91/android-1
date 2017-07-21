package com.apps.pato.appweb;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    private Button btnGuardar;
    private Button btnPersonal;
    private EditText nombres;
    private EditText apellidos;
    private EditText correo;
    private EditText celular;

    //    private final String SERVER = "http://4aaea5b4.ngrok.io/web/node";
    public String baseUrl = "http://39d56310.ngrok.io/";
    public String SERVER = baseUrl+"uno/node";
    public String SERVIDOR = baseUrl+"uno/node.json?type=personal";
    public String session_id;
    public String session_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listar listar = new listar();
        listar.execute();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_main);

        btnGuardar = (Button) findViewById(R.id.guardar);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nombres = (EditText) findViewById(R.id.nombre);
                apellidos = (EditText) findViewById(R.id.apllidos);
                correo = (EditText) findViewById(R.id.correo);
                celular = (EditText) findViewById(R.id.celular);
                revision();
            }
        });

        btnPersonal = (Button)findViewById(R.id.listAll);
        btnPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), principal.class);
                startActivity(intent);
            }
        });

    }

    private void revision() {
        nombres.setError(null);
        apellidos.setError(null);
        correo.setError(null);
        celular.setError(null);

        String f_name = nombres.getText().toString();
        String l_name = apellidos.getText().toString();
        String email = correo.getText().toString();
        String phone = celular.getText().toString();

        boolean cancelar = false;
        View focus = null;

        if (TextUtils.isEmpty(f_name)) {
            nombres.setError(getString(R.string.Error_campo));
            focus = nombres;
            cancelar = true;
        }
        if (TextUtils.isEmpty(l_name)) {
            apellidos.setError(getString(R.string.Error_campo));
            focus = apellidos;
            cancelar = true;
        }

        if (TextUtils.isEmpty(email)) {
            correo.setError(getString(R.string.Error_campo));
            focus = apellidos;
            cancelar = true;
        }
        if (!isEmailValid(email)) {
            correo.setError(getString(R.string.Error_email));
            focus = apellidos;
            cancelar = true;
        }

        if (TextUtils.isEmpty(phone)) {
            celular.setError(getString(R.string.Error_campo));
            focus = celular;
            cancelar = true;
        }
        if (!isPhone(phone)) {
            celular.setError(getString(R.string.Error_phone));
            focus = celular;
            cancelar = true;
        }

        if (cancelar) {
            focus.requestFocus();
        } else {
            HttpHandler http = new HttpHandler();
            String txt = http.post(SERVER, f_name, l_name, email, phone);
            if (txt == "Content has been consumed"){
                Toast.makeText(getApplicationContext(), R.string.guardado, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), R.string.error_guardado, Toast.LENGTH_SHORT).show();
            }
            borrarCajas();
        }
    }

    private void borrarCajas() {
        nombres.setText("");
        apellidos.setText("");
        correo.setText("");
        celular.setText("");
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPhone(String password) {
        return password.length() == 10;
    }

    private class listar extends AsyncTask<String, Integer, Boolean> {
        private String[] personas;

        protected Boolean doInBackground(String... paramas){
            boolean result = true;
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(SERVIDOR);
            httpPost.setHeader("content-type", "application/json");
//            httpPost.setHeader(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            try{
                HttpResponse httpResponse = httpClient.execute(httpPost);
                String res = EntityUtils.toString(httpResponse.getEntity());
                Log.d("Contenido ", res);
                int s = httpResponse.getStatusLine().getStatusCode();
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
                Log.e("error ", e.getMessage());
                Log.d("SERVICIO ", "Error al intentar obtener el servicio");
            }
            return result;
        }

        protected void onPostExecute(Boolean result){
            if (result){

//                Log.d("primer" ,  personas.toString());
//                ArrayAdapter<String> adaptador =
//                        new ArrayAdapter<String>(principal.this,
//                                android.R.layout.simple_list_item_1, personas);
//
//                listaPersonas.setAdapter(adaptador);
            }
        }
    }
}
