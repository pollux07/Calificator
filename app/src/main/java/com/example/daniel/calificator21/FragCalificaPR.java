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
import java.util.HashSet;
import java.util.List;

/**
 * Created by Daniel on 05/10/2016.
 */

public class FragCalificaPR extends Fragment{
    public FragCalificaPR(){

    }

    //SOCKET JSON
    ServiceHandler jsonParser = new ServiceHandler();

    TextView TLT_CALIF, tv_mat, tv_alu_pr;
    Spinner sp_mat, sp_al;
    Button btn_next;

    String id_prof, n_mat, n_alu;

    //AARRAY
    ArrayList<String> MATERIAS;
    ArrayList<String> MATITEMS = new ArrayList<>();
    ArrayAdapter<String> adapter;

    ArrayList<String> ALUMNOS;
    ArrayList<String> ALUITEMS = new ArrayList<>();
    ArrayAdapter<String> adapter2;

    //URL PHP
    private static final String FIND_MAT_PRF_URL = "http://calificator.com/app_movil/buscarMateriaPR.php";
    private static final String FIND_ALU_PRF_URL = "http://calificator.com/app_movil/buscarAluPR_mat.php";



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        final View view = inflater.inflate(R.layout.frag_calif_mod, container, false);

        //FUENTE
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "DJB Chalk It Up.ttf");
        //DECLARANDO OBJETOS
        //TEXT VIEW
        TLT_CALIF = (TextView) view.findViewById(R.id.TV_tl_mat);
        tv_mat = (TextView) view.findViewById(R.id.TV_mat_cal);
        tv_alu_pr = (TextView) view.findViewById(R.id.TV_alu_cal);


        //SPINNER
        sp_mat = (Spinner) view.findViewById(R.id.SP_mat_calif);
        adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, MATITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_mat.setAdapter(adapter);
        sp_mat.setOnItemSelectedListener(new sp_mat_cal());

        sp_al = (Spinner) view.findViewById(R.id.SP_alu_calif);
        adapter2 = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, ALUITEMS);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_al.setAdapter(adapter2);
        sp_al.setOnItemSelectedListener(new sp_alu_cal());

        //OBTENIENDO ID DEL PROFESOR
        Bundle bundle1 = getArguments();
        id_prof = bundle1.getString("IDPR");

        new GetMateriasPRC().execute();


        //BUTTON
        btn_next = (Button) view.findViewById(R.id.BTN_cont_calif);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ev = new Intent(view.getContext(), Calif_Alu_PR.class);
                ev.putExtra(Calif_Alu_PR.IDPR, id_prof);
                ev.putExtra(Calif_Alu_PR.MATNAME, n_mat);
                ev.putExtra(Calif_Alu_PR.ALUNAME, n_alu);
                startActivity(ev);
            }
        });

        //FUENTES
        //FUENTES TEXT VIEW
        TLT_CALIF.setTypeface(font);
        tv_mat.setTypeface(font);
        tv_alu_pr.setTypeface(font);


        //FUENTES BUTTON
        btn_next.setTypeface(font);

        return view;
    }

    //SELECCIONANDO MATERIA
    public class sp_mat_cal implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            n_mat = parent.getItemAtPosition(pos).toString();
            //Toast.makeText(getActivity(), n_mat, Toast.LENGTH_SHORT).show();
            new GetAlumnosPRC().execute();


        }

        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }
    }

    //SELECCIONANDO ALUMNOS
    public class sp_alu_cal implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            n_alu = parent.getItemAtPosition(pos).toString();
        }

        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }
    }

    private class GetMateriasPRC extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MATERIAS = new ArrayList<>();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            List findmat = new ArrayList();
            findmat.add(new BasicNameValuePair("id_prof", id_prof));
            String json = jsonParser.makeServiceCall(FIND_MAT_PRF_URL, ServiceHandler.POST, findmat);

            Log.d("Response:", "> " + json);

            if (json != null) {
                try {
                    JSONArray jsonArray = new JSONArray(json);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        MATERIAS.add(jsonObject.getString("nom_materia"));
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
        protected void onPostExecute(Void result) {
            MATITEMS.addAll(MATERIAS);
            adapter.notifyDataSetChanged();
        }
    }

    private class GetAlumnosPRC extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ALUMNOS = new ArrayList<>();
            //Toast.makeText(getActivity(), n_mat, Toast.LENGTH_SHORT).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            List findal = new ArrayList();
            findal.add(new BasicNameValuePair("mat", n_mat));
            String json = jsonParser.makeServiceCall(FIND_ALU_PRF_URL, ServiceHandler.POST, findal);

            Log.d("Response:", "> " + json);

            if (json != null) {
                try {
                    JSONArray jsonArray = new JSONArray(json);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        ALUMNOS.add(jsonObject.getString("nom_alu"));
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
        protected void onPostExecute(Void result) {
            ALUITEMS.clear();
            ALUITEMS.addAll(ALUMNOS);
            adapter2.notifyDataSetChanged();
        }
    }
}
