package com.example.daniel.calificator21;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 22/11/2016.
 */

public class FragGruposPR extends Fragment {
    public FragGruposPR() {

    }

    //SOCKET JSON
    ServiceHandler jsonParser = new ServiceHandler();

    //AARRAY
    ArrayList<String> listC, listG;
    ArrayList<String> listItemsC = new ArrayList<>();
    ArrayList<String> listItemsG = new ArrayList<>();
    ArrayAdapter<String> adapterC, adapterG;

    String id_prof, carrier, group;

    TextView txt_title_G, txt_C_pr, txt_G_pr;
    Spinner spinnerCarreraspr, spinnerGrupospr;
    Button btn_c;

    //URL PHP
    private static final String FIND_CARR_PRF_URL = "http://calificator.com/app_movil/buscarCarrerasPR.php";
    //URL PHP
    private static final String FIND_GROUP_PRF_URL = "http://calificator.com/app_movil/buscarGruposPR.php";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.frag_grupos_pr, container, false);

        //FUENTE
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "DJB Chalk It Up.ttf");

        //BUTTON
        btn_c = (Button) view.findViewById(R.id.BTN_cont_group);

        //TEXT VIEW
        txt_title_G = (TextView) view.findViewById(R.id.title_gp_pr);
        txt_C_pr = (TextView) view.findViewById(R.id.txt_carrier_pr);
        txt_G_pr = (TextView) view.findViewById(R.id.txt_groups_pr);

        //SPINNER CARRERAS
        spinnerCarreraspr = (Spinner) view.findViewById(R.id.sp_carrier_pr);
        adapterC = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listItemsC);
        adapterC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCarreraspr.setAdapter(adapterC);
        spinnerCarreraspr.setOnItemSelectedListener(new carreras_pick());

        //SPINNER GRUPOS
        spinnerGrupospr = (Spinner) view.findViewById(R.id.sp_groups_pr);
        adapterG = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listItemsG);
        adapterG.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGrupospr.setAdapter(adapterG);
        spinnerGrupospr.setOnItemSelectedListener(new grupos_pick());

        //DECLARANDO FUENTES
        //FUENTE BUTTON
        btn_c.setTypeface(font);

        //FUENTE TEXT VIEW
        txt_title_G.setTypeface(font);
        txt_C_pr.setTypeface(font);
        txt_G_pr.setTypeface(font);

        //OBTENER ID PROFESOR
        Bundle bundle1 = getArguments();
        id_prof = bundle1.getString("IDPR");

        //METODOS ASYNC
        new getCarrerasPR().execute();

        btn_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent I_Alumnos = new Intent(view.getContext(), Grupos_PR.class);
                I_Alumnos.putExtra(Grupos_PR.carrera, carrier);
                I_Alumnos.putExtra(Grupos_PR.grupo, group);
                startActivity(I_Alumnos);
            }
        });
        return view;
    }

    public class carreras_pick implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            carrier = parent.getItemAtPosition(pos).toString();
            new getGruposPR().execute();
        }

        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }
    }

    public class grupos_pick implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            group = parent.getItemAtPosition(pos).toString();
        }

        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }
    }

    private class getCarrerasPR extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listC = new ArrayList<>();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String jsonC = jsonParser.makeServiceCall(FIND_CARR_PRF_URL, ServiceHandler.GET);
            Log.d("Response:", "> " + jsonC);

            if (jsonC != null) {
                try {
                    JSONArray jsonArray = new JSONArray(jsonC);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        listC.add(jsonObject.getString("nom_carrera"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("JSON Data", "¿No ha recibido ningún dato desde el servidor!");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            listItemsC.addAll(listC);
            adapterC.notifyDataSetChanged();
        }
    }

    private class getGruposPR extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listG = new ArrayList<>();
            //Toast.makeText(getActivity(), carrier, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            List findGR = new ArrayList();
            findGR.add(new BasicNameValuePair("crr", carrier));
            String jsonG = jsonParser.makeServiceCall(FIND_GROUP_PRF_URL, ServiceHandler.POST, findGR);

            Log.d("Response:", "> " + jsonG);

            if (jsonG != null) {
                try {
                    JSONArray jsonArray = new JSONArray(jsonG);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        listG.add(jsonObject.getString("grupo"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("JSON Data", "¿No ha recibido ningún dato desde el servidor!");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            listItemsG.clear();
            listItemsG.addAll(listG);
            adapterG.notifyDataSetChanged();
        }
    }
}

