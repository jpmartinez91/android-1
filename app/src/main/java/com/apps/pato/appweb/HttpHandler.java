package com.apps.pato.appweb;


import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;


/**
 * Created by pato on 06/07/17.
 */

public class HttpHandler{

    public String post(String posturl, String nombres, String apellidos, String correo, String telefono) {
        try {
            /*
             * Creamos el objeto de HttpClient que nos permitira conectarnos
			 * mediante peticiones http.
			 */
            HttpClient httpclient = new DefaultHttpClient();

			/*
			 * El objeto HttpPost permite que enviemos una peticion de tipo POST
			 * a una URL especificada
			 */
            HttpPost httppost = new HttpPost(posturl);

            // Una lista de parametros,
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            // Se agregan parametros.
            params.add(new BasicNameValuePair("nombres", nombres));
            params.add(new BasicNameValuePair("apellidos", apellidos));
            params.add(new BasicNameValuePair("correo", correo));
            params.add(new BasicNameValuePair("telefono", telefono));
//            StringEntity se = new StringEntity("{ \"title\" : \"Sample post\" , \"type\" : \"article\" , \"body\" : { \"und\" :[{ \"value\" : \"comments about something\" }]} }");
//            StringEntity se = new StringEntity("{ \"title\" : \""+nombres+"\", \"type\" : \"personal_utpl\" , \"field_nombres\" : \""+ apellidos+"\",\"field_telefono\" : \"" + telefono + "\", \"field_correo\":\""+correo+"\"}");

            StringEntity se = new StringEntity("{ \"title\" : \"" + nombres + "\", \"type\" : \"personal\" , \"field_apellidos\" : \"" + apellidos + "\",\"field_telefono\" : \"" + telefono + "\", \"field_correo\":\"" + correo + "\"}");
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
//            se.setContentType(new BasicHeader("Cookie",session_name+"="+session_id));
//            se.setContentType(new BasicHeader("X-CSRF-Token",token));
            httppost.setEntity(se);
            HttpResponse response = httpclient.execute(httppost);
            Log.v("Return add article", EntityUtils.toString(response.getEntity()));

			/*
			 * Una vez añadidos los parametros actualizamos la entidad de
			 * httppost, esto quiere decir en pocas palabras anexamos los
			 * parametros al objeto para que al enviarse al servidor envien los
			 * datos que hemos añadido
			 */
//            httppost.setEntity(new UrlEncodedFormEntity(params));

            // Eejecutamos enviando la informacion al Server.
//            HttpResponse resp = httpclient.execute(httppost);

            // Obtenemos una respuesta.
            HttpEntity ent = response.getEntity();
            String text = EntityUtils.toString(ent);
            // Envia la respuesta del Server.
            return text;

        } catch (Exception e) {
            // Devuelve el mensaje de error, en caso que lo haya.
            return e.getMessage();
        }
    }

    public String listar(String url){
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        String resultado = "";
        HttpResponse response;
        try {
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            InputStream inputStream = entity.getContent();
            resultado = convertStreamToString(inputStream);
        }catch (ClientProtocolException ce){
            ce.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultado;
    }

    private String convertStreamToString(InputStream is) throws IOException {
        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line;
            try {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is, "UTF-8"));
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            }
            finally {
                is.close();
            }
            return sb.toString();
        } else {
            return "";
        }
    }
}
