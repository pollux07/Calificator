package com.example.daniel.calificator21;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sanborns on 13/10/2016.
 */

public class FragMatAlum extends Fragment {
    public FragMatAlum() {
        // Required empty public constructor

    }

    private ProgressDialog pDialog;

    // Creating JSON Parser object
    //JSONParser jParser = new JSONParser();
    ServiceHandler jsonParser = new ServiceHandler();

    /*public static final String carrera = "carrier";
    public static final String grupo = "group";*/

    ArrayList<HashMap<String, String>> materiaList;
    String id, name, mat;


    // url to get all products list
    private static String URL_MATERIAS = "http://calificator.com/app_movil/buscarMateriasAlu.php";

    ListView listaMaterias;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.frag_materias_alum, container, false);
        ListView lista;

        materiaList = new ArrayList<HashMap<String, String>>();

        // Cargar los productos en el Background Thread
        new LoadAllProducts().execute();
        lista = (ListView) view.findViewById(R.id.LM_Materias_Alu);

        /*carr = getIntent().getStringExtra("carrier");
        gr = getIntent().getStringExtra("group");*/


        return view;
    }

    class LoadAllProducts extends AsyncTask<String, String, String> {

        /**
         * Antes de empezar el background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Cargando Materias. Por favor espere...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * obteniendo todos los productos
         */
        protected String doInBackground(String... args) {
            // Building Parameters
            List materias = new ArrayList();
            materias.add(new BasicNameValuePair("mat_alu", mat));
            //grupos.add(new BasicNameValuePair("gr_alu", gr));
            // getting JSON string from URL
            String json = jsonParser.makeServiceCall(URL_MATERIAS, ServiceHandler.POST, materias);

            // Check your log cat for JSON reponse
            Log.d("All Products: ", json);

            try {
                // Checking for SUCCESS TAG
                JSONArray jsonArray = new JSONArray(json);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject c = jsonArray.getJSONObject(i);

                    // Storing each json item in variable
                    id = c.getString("idalumnos");
                    name = c.getString("nom_alu");

                    // creating new HashMap
                    HashMap map = new HashMap();

                    // adding each child node to HashMap key => value
                    map.put("idmateria", id);
                    map.put("nom_materia", name);

                    materiaList.add(map);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            getActivity(),
                            materiaList,
                            R.layout.lista_materias,
                            new String[]{
                                    "idmateria",
                                    "nom_materia",
                            },
                            new int[]{
                                    R.id.single_post_id_mat,
                                    R.id.single_post_nom_mat,
                            });
                    // updating listview
                    //setListAdapter(adapter);
                    listaMaterias.setAdapter(adapter);
                }
            });
        }
    }
}