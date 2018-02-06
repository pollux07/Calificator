package com.example.daniel.calificator21;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Calif_Alu_PR extends AppCompatActivity {

    //SOCKET JSON
    ServiceHandler jsonParser = new ServiceHandler();

    JSONParser jsonP = new JSONParser();


    TextView TLT_EV, tv_pond, tv_date, tv_fecha, tv_fv;
    EditText et_descr, et_calif;
    Spinner nomp, sp_fv;
    Button btn_date, btn_ev;

    int mYear;
    int mMonth;
    int mDay;
    int success;

    double cl;

    static final int DATE_DIALOG_ID = 0;
    static DatePickerDialog.OnDateSetListener listenerDate;

    String materia_n, n_alu, profiID, descripcion, calif, pond_name, fv_cal, message;

    public static final String MATNAME = "n_mat";
    public static final String ALUNAME = "al_n";
    public static final String IDPR = "id_prof";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "mensaje";

    //AARRAY
    ArrayList<String> PONDERACIONES;
    ArrayList<String> PONDITEMS = new ArrayList<>();
    ArrayAdapter<String> adapter;

    ArrayList<String> FECHAV;
    ArrayList<String> FECHAVITEMS = new ArrayList<>();
    ArrayAdapter<String> adapter3;

    boolean cancel = false;
    View focusView = null;

    //URL PHP
    private static final String FIND_POND_CALIF_URL = "http://calificator.com/app_movil/buscarpond_PR.php";
    private static final String FIND_fechav_PRF_URL = "http://calificator.com/app_movil/fechav_pr.php";
    private static final String INSER_CALIF_URL = "http://calificator.com/app_movil/insertcalif.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calif__alu__pr);

        //AGREGA EL BOTON DE REGRESAR CON EL METODO ONITEMSELECTED
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //FUENTE
        Typeface font = Typeface.createFromAsset(this.getAssets(), "DJB Chalk It Up.ttf");

        //OBTENIENDO VARIABLES
        profiID = getIntent().getStringExtra("id_prof");
        n_alu = getIntent().getStringExtra("al_n");
        materia_n = getIntent().getStringExtra("n_mat");

        //DECLARANDO OBJETOS
        //TEXT VIEW
        TLT_EV = (TextView) findViewById(R.id.TV_TL_EV);
        tv_pond = (TextView) findViewById(R.id.TV_NOM_POND);
        tv_date = (TextView) findViewById(R.id.TV_FCAL);
        tv_fecha = (TextView) findViewById(R.id.FC);
        tv_fv = (TextView) findViewById(R.id.TV_periodo_pr);

        //EDIT TEXT
        et_descr = (EditText) findViewById(R.id.ET_DESC);
        et_calif = (EditText) findViewById(R.id.ET_CALIF);

        //SPINER
        nomp = (Spinner) findViewById(R.id.SP_NomPON_PR);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, PONDITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nomp.setAdapter(adapter);
        nomp.setOnItemSelectedListener(new sp_pond());

        sp_fv = (Spinner) findViewById(R.id.SP_FV_PR);
        adapter3 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, FECHAVITEMS);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_fv.setAdapter(adapter3);
        sp_fv.setOnItemSelectedListener(new sp_fv_cal());

        //BUTTON
        btn_date = (Button) findViewById(R.id.BTN_FC);
        btn_ev = (Button) findViewById(R.id.BTN_EV);

        //FONTS

        //TEXTVIEW
        TLT_EV.setTypeface(font);
        tv_pond.setTypeface(font);
        tv_date.setTypeface(font);
        tv_fecha.setTypeface(font);
        tv_fv.setTypeface(font);

        //EDIT TEXT
        et_descr.setTypeface(font);
        et_calif.setTypeface(font);

        //BUTTON
        btn_date.setTypeface(font);
        btn_ev.setTypeface(font);

        new GetFechaVPRC().execute();

        Calendar calendario = Calendar.getInstance();
        mYear = calendario.get(Calendar.YEAR);
        mMonth = calendario.get(Calendar.MONTH);
        mDay = calendario.get(Calendar.DAY_OF_MONTH);

        showDate();

        listenerDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mYear = year;
                mMonth = monthOfYear+1;
                mDay = dayOfMonth;

                showDate();
            }
        };

        //ACTION BUTTONS
        btn_ev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                descripcion = et_descr.getText().toString();
                calif = et_calif.getText().toString();
                cl = Double.parseDouble(calif);

                if (TextUtils.isEmpty(descripcion)){
                    et_descr.setError("Campo Requerido");
                    focusView = et_descr;
                    cancel = true;
                }else if (TextUtils.isEmpty(calif)){
                    et_calif.setError("Campo Requerido");
                    focusView = et_calif;
                    cancel = true;
                }else if(invalidCHAR(calif)) {
                    et_calif.setError("No se aceptarn numero decimales");
                    focusView = et_calif;
                    cancel = true;
                }else if (cl < 0 || cl > 10){
                    et_calif.setError("La calificación debe estar entre 0 y 10");
                    focusView = et_calif;
                    cancel = true;
                }else {
                    new INSERTCalif().execute();
                }
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id){
            case 0:
                return new DatePickerDialog(this,listenerDate, mYear, mMonth,mDay);
        }

        return null;
    }

    public void showCalendar(View control){
        showDialog(DATE_DIALOG_ID);
    }

    public void showDate(){
        tv_fecha.setText(mYear+"-"+mMonth+"-"+mDay);
    }

    //SELEECIONANDO PERIDO
    public class sp_fv_cal implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            fv_cal = parent.getItemAtPosition(pos).toString();
            new GetPond().execute();
        }

        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }
    }

    //SELECCIONANDO MATERIA
    public class sp_pond implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            pond_name = parent.getItemAtPosition(pos).toString();

        }

        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }
    }

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

    private class GetFechaVPRC extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            FECHAV = new ArrayList<>();
            //Toast.makeText(Calif_Alu_PR.this, materia_n, Toast.LENGTH_SHORT).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            List findfv = new ArrayList();
            findfv.add(new BasicNameValuePair("mat", materia_n));
            String json = jsonParser.makeServiceCall(FIND_fechav_PRF_URL, ServiceHandler.POST, findfv);

            Log.d("fecha:", "> " + json);

            if (json != null) {
                try {
                    JSONArray jsonArray = new JSONArray(json);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        FECHAV.add(jsonObject.getString("fecha_vigencia"));
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
            FECHAVITEMS.addAll(FECHAV);
            adapter3.notifyDataSetChanged();
        }
    }

    private class GetPond extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            PONDERACIONES = new ArrayList<>();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            List findPOND = new ArrayList();
            findPOND.add(new BasicNameValuePair("id_prof", profiID));
            findPOND.add(new BasicNameValuePair("nmat", materia_n));
            findPOND.add(new BasicNameValuePair("fv", fv_cal));
            String json = jsonParser.makeServiceCall(FIND_POND_CALIF_URL, ServiceHandler.POST, findPOND);

            Log.d("Response:", "> " + json);

            if (json != null) {
                try {
                    JSONArray jsonArray = new JSONArray(json);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        PONDERACIONES.add(jsonObject.getString("criterios"));
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
            PONDITEMS.clear();
            PONDITEMS.addAll(PONDERACIONES);
            adapter.notifyDataSetChanged();
        }
    }

    private class INSERTCalif extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            List insertCAL = new ArrayList();
            insertCAL.add(new BasicNameValuePair("nmat", materia_n));
            insertCAL.add(new BasicNameValuePair("id_prof", profiID));
            insertCAL.add(new BasicNameValuePair("nal", n_alu));
            insertCAL.add(new BasicNameValuePair("descr", descripcion));
            insertCAL.add(new BasicNameValuePair("calif", calif));
            insertCAL.add(new BasicNameValuePair("fv", fv_cal));
            insertCAL.add(new BasicNameValuePair("pond", pond_name));
            JSONObject json = jsonP.makeHttpRequest(INSER_CALIF_URL, "POST", insertCAL);

            Log.d("Response:", "> " + json);

            if (json != null) {
                try {
                    // json success element
                    success = json.getInt(TAG_SUCCESS);
                    if (success == 1) {
                        Log.d("Ponderación creada!", json.toString());
                        message = json.getString(TAG_MESSAGE);
                    }else{
                        Log.d("Fallo en el registro!", json.getString(TAG_MESSAGE));
                        message = json.getString(TAG_MESSAGE);

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
            Toast.makeText(Calif_Alu_PR.this, message, Toast.LENGTH_LONG).show();
        }
    }



    private boolean invalidCHAR(String extencion){
        //TODO: Replace this with your own logic
        return extencion.contains(".");
    }
}
