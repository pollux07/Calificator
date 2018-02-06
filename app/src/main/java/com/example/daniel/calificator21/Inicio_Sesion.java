package com.example.daniel.calificator21;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Inicio_Sesion extends AppCompatActivity implements View.OnClickListener{

    private EditText user, pass;
    private Button mSubmit, mRegister;
    private TextView inicio;

    private ProgressDialog pDialog;

    // Clase JSONParser
    JSONParser jsonParser = new JSONParser();


    // si trabajan de manera local "localhost" :
    // En windows tienen que ir, run CMD > ipconfig
    // buscar su IP
    // y poner de la siguiente manera
    // "http://xxx.xxx.x.x:1234/cas/login.php";

    private static final String LOGIN_URL = "http://calificator.com/app_movil/login.php";

    // La respuesta del JSON es
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_IDUSER = "iduser";
    private static final String TAG_NAME = "usuario";
    private static final String TAG_ROL = "rol";
    String message;

    boolean cancel = false;
    View focusView = null;

    int success;

    String rol, idusers, username, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio__sesion);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        //DECLARAMOS LA FUENTE
        Typeface font = Typeface.createFromAsset(getAssets(), "DJB Chalk It Up.ttf");

        // setup input fields
        user = (EditText) findViewById(R.id.user);
        pass = (EditText) findViewById(R.id.password);

        //OBJETOS TEXT
        inicio = (TextView) findViewById(R.id.TV_sesion);

        // setup buttons
        mSubmit = (Button) findViewById(R.id.LogIN);
        //mRegister = (Button) findViewById(R.id.register);

        //FONTS PARA LOS ELEMENTOS
        user.setTypeface(font);
        pass.setTypeface(font);
        inicio.setTypeface(font);
        mSubmit.setTypeface(font);

        // register listeners
        mSubmit.setOnClickListener(this);
        //mRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        username = user.getText().toString();
        password = pass.getText().toString();

        if (TextUtils.isEmpty(username)){
            user.setError("Campo requerido");
            focusView = user;
            cancel = true;
        }else if (TextUtils.isEmpty(password)){
            pass.setError("Campo requerido");
            focusView = pass;
            cancel = true;
        }else {
            switch (v.getId()) {
                case R.id.LogIN:
                    new AttemptLogin().execute();
                    break;
                default:
                    break;
            }
        }
    }

    class AttemptLogin extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Inicio_Sesion.this);
            pDialog.setMessage("Iniciando sesion...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {


            try {
                // Building Parameters
                //IMPORTANTE
                List params = new ArrayList();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", password));

                //SE REALIZA EL ENVIO DE DATOS A SERVIDOR POR MEDIO DEL ARREGLO
                Log.d("request!", "starting");
                // getting product details by making HTTP request
                //IMPORTANTE
                JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST",
                        params);
                message = json.getString(TAG_MESSAGE);

                // check your log for json response
                Log.d("Login attempt", json.toString());

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                rol = json.getString(TAG_ROL);
                idusers = json.getString(TAG_IDUSER);
                //nameuser = json.getString(TAG_NAME);

                if (success == 1) {
                    Log.d("Login Successful!", json.toString());
                    // save user data
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(Inicio_Sesion.this);
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putString("username", username);
                    edit.commit();

                    if (rol.equals("profesor")){
                        Intent i = new Intent(Inicio_Sesion.this, MenuPrincipalPR.class);
                        i.putExtra(MenuPrincipalPR.iduser, idusers);
                        //i.putExtra(MenuPrincipalPR.nombrepr, nameuser);
                        //EXTRER NOMBRE
                        finish();
                        startActivity(i);

                    }else if (rol.equals("alumno")){
                        Intent in = new Intent(Inicio_Sesion.this, Menu_Principal_Alumno.class);
                        finish();
                        startActivity(in);

                    }
                } else if (success == 0){
                    Log.d("Login Failure!", json.getString(TAG_MESSAGE));
                    //Toast.makeText(Inicio_Sesion.this, json.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            Toast.makeText(Inicio_Sesion.this, message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inicio__sesion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
