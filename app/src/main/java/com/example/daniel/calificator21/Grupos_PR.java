package com.example.daniel.calificator21;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Grupos_PR extends ActionBarActivity {

    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    //JSONParser jParser = new JSONParser();
    ServiceHandler jsonParser = new ServiceHandler();

    public static final String carrera = "carrier";
    public static final String grupo = "group";

    ArrayList<HashMap<String, String>> alumnoList;
    String id, name, carr, gr;


    // url to get all products list
    private static String FIND_ALUMNO_GROUP = "http://calificator.com/app_movil/buscarAlumnosGR_PR.php";

    TextView TLT_GR;

    ListView lista;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupos__pr);

        //FUENTE
        Typeface font = Typeface.createFromAsset(this.getAssets(), "DJB Chalk It Up.ttf");

        // Hashmap para el ListView
        alumnoList = new ArrayList<HashMap<String, String>>();

        // Cargar los productos en el Background Thread
        new LoadAllProducts().execute();
        lista = (ListView) findViewById(R.id.LV_Alumnos_PR);

        TLT_GR = (TextView) findViewById(R.id.title_LA_pr);

        carr = getIntent().getStringExtra("carrier");
        gr = getIntent().getStringExtra("group");

        //Toast.makeText(this, carr+" "+gr, Toast.LENGTH_SHORT).show();

        //FONTS
        TLT_GR.setTypeface(font);

        //AGREGA EL BOTON DE REGRESAR CON EL METODO ONITEMSELECTED
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    //ACCION PARA REGRESAR A LA ACTIVIDAD ANTERIOR
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //hago un case por si en un futuro agrego mas opciones
                Log.i("ActionBar", "Atrás!");
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    class LoadAllProducts extends AsyncTask<String, String, String> {

        /**
         * Antes de empezar el background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Grupos_PR.this);
            pDialog.setMessage("Cargando Alumnos. Por favor espere...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * obteniendo todos los productos
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
            List grupos = new ArrayList();
            grupos.add(new BasicNameValuePair("carr_alu", carr));
            grupos.add(new BasicNameValuePair("gr_alu", gr));
            // getting JSON string from URL
            String json = jsonParser.makeServiceCall(FIND_ALUMNO_GROUP, ServiceHandler.POST, grupos);

            // Check your log cat for JSON reponse
            Log.d("All Products: ", json);

            try {
                // Checking for SUCCESS TAG
                JSONArray jsonArray = new JSONArray(json);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);

                        // Storing each json item in variable
                        id = c.getString("idalumnos");
                        name = c.getString("nom_alu")+" "+c.getString("app_alu")+" "+c.getString("apm_alu");

                        // creating new HashMap
                        HashMap map = new HashMap();

                        // adding each child node to HashMap key => value
                        map.put("idalumnos", id);
                        map.put("nom_alu"+" "+"app_alu"+" "+"apm_alu", name);

                        alumnoList.add(map);
                    }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            Grupos_PR.this,
                            alumnoList,
                            R.layout.lista_alumno,
                            new String[] {
                                    "idalumnos",
                                    "nom_alu"+" "+"app_alu"+" "+"apm_alu",
                            },
                            new int[] {
                                    R.id.single_post_tv_id,
                                    R.id.single_post_tv_nombre,
                            });
                    // updating listview
                    //setListAdapter(adapter);
                    lista.setAdapter(adapter);
                }
            });
        }
    }
}