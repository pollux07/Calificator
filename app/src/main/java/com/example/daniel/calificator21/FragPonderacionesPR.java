package com.example.daniel.calificator21;

import android.app.ProgressDialog;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragPonderacionesPR extends Fragment {

    public FragPonderacionesPR() {
        // Required empty public constructor
    }

    DatePickerFragmentI newFragmentI = new DatePickerFragmentI();
    //SOCKET JSON
    ServiceHandler jsonParser = new ServiceHandler();

    //AARRAY
    ArrayList<String> list;
    ArrayList<String> listItems = new ArrayList<>();
    ArrayAdapter<String> adapter;

    String idPR, mat, n_pond;
    String DDI;

    //OBJETOS
    Button fechaI, btn_c;
    Spinner spinnerMateriaspr, sp_pond;
    TextView tl_pond, tx_mat, tx_nopond, txt_date, DATA;

    //URL PHP
    private static final String FIND_MAT_PRF_URL = "http://calificator.com/app_movil/buscarMateriaPR.php";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.frag_pond_pr, container, false);
        //FUENTE
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "DJB Chalk It Up.ttf");

        //DECLARANDO OBJETOS BUTTON
        fechaI = (Button) view.findViewById(R.id.BTN_FI);
        btn_c = (Button) view.findViewById(R.id.BTN_cont);

        //DECLARANDO TEXTVIEW
        tl_pond = (TextView) view.findViewById(R.id.title_pond_pr);
        tx_mat = (TextView) view.findViewById(R.id.txt_mat_pr);
        tx_nopond = (TextView) view.findViewById(R.id.txt_no_pond);
        txt_date = (TextView) view.findViewById(R.id.TxtV_FI);
        DATA  = (TextView) view.findViewById(R.id.FI);

        //DECLARANDO SPINNERMAT
        spinnerMateriaspr = (Spinner) view.findViewById(R.id.sp_pond_mt_pr);
        adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, listItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMateriaspr.setAdapter(adapter);
        spinnerMateriaspr.setOnItemSelectedListener(new sp_mat());

        //DECLARANDO SPINNER NO. PONDERACION
        sp_pond = (Spinner) view.findViewById(R.id.sp_no_pond_pr);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                getActivity(), R.array.pond_pr, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_pond.setAdapter(adapter2);
        sp_pond.setOnItemSelectedListener(new sp_n_pon());

        //DECLARANDO FUENTES

        //DECLARANDO BOTONES
        fechaI.setTypeface(font);
        btn_c.setTypeface(font);

        //FUENTES TEXTVIEW
        tl_pond.setTypeface(font);
        tx_mat.setTypeface(font);
        tx_nopond.setTypeface(font);
        txt_date.setTypeface(font);

        //FUENTES SPINNER

        //OBTENIENDO ID DEL PROFESOR
        Bundle bundle1 = getArguments();
        idPR = bundle1.getString("IDPR");

        //Toast.makeText(view.getContext(), idPR, Toast.LENGTH_SHORT).show();

        new GetMateriasPR().execute();
        //Toast.makeText(view.getContext(), idPR, Toast.LENGTH_SHORT).show();

        //FUNCION DEL BOTON CALENDARIO
        fechaI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newFragmentI.show(getFragmentManager(), "fechai");

            }
        });


        btn_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DDI = DATA.getText().toString();
                //Toast.makeText(view.getContext(), DDI, Toast.LENGTH_SHORT).show();
                if (DDI.isEmpty()){
                    Toast.makeText(view.getContext(), "Fecha de vigencia vacia", Toast.LENGTH_SHORT).show();
                }else {
                    Intent porcentaje_pr_pond = new Intent(view.getContext(), pond_porcentaje_pr.class);
                    porcentaje_pr_pond.putExtra(pond_porcentaje_pr.MAT, mat);
                    porcentaje_pr_pond.putExtra(pond_porcentaje_pr.NO_pond, n_pond);
                    porcentaje_pr_pond.putExtra(pond_porcentaje_pr.id_prof, idPR);
                    porcentaje_pr_pond.putExtra(pond_porcentaje_pr.vig_date, DDI);
                    startActivity(porcentaje_pr_pond);
                }
            }
        });
        return view;
    }

    //SELECCIONANDO MATERIA
    public class sp_mat implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            mat = parent.getItemAtPosition(pos).toString();
        }

        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }
    }

    //SELECCIONANDO NO. PONDERACION
    public class sp_n_pon implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            n_pond = parent.getItemAtPosition(pos).toString();
        }

        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }
    }

    private class GetMateriasPR extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            list = new ArrayList<>();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            List findID = new ArrayList();
            findID.add(new BasicNameValuePair("id_prof", idPR));
            String json = jsonParser.makeServiceCall(FIND_MAT_PRF_URL, ServiceHandler.POST, findID);

            Log.d("Response:", "> " + json);

            if (json != null) {
                try {
                    JSONArray jsonArray = new JSONArray(json);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        list.add(jsonObject.getString("nom_materia"));
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
            listItems.addAll(list);
            adapter.notifyDataSetChanged();
        }

    }
}