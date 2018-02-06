package com.example.daniel.calificator21;

import android.app.ProgressDialog;
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
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class pond_porcentaje_pr extends AppCompatActivity {


    JSONParser jsonParser = new JSONParser();

    private static final String INSERT_POND_URL = "http://calificator.com/app_movil/insertPOND.php";

    public static final String NO_pond = "NOPOND";
    public static final String MAT = "nom_mat";
    public static final String id_prof = "idPROF";
    public static final String vig_date = "fecha_v";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "mensaje";

    int pond, vipond1, vipond2, vipond3, vipond4, vipond5, porcentaje_total;
    int success;
    String vpond1, vpond2, vpond3, vpond4, vpond5;
    String NPOND1, NPOND2, NPOND3, NPOND4, NPOND5;
    String PR_ID, materia, fecha_vig, Numero_pond;
    String message, complete;
    String percent1, percent2, percent3, percent4, percent5;

    EditText npond1, npond2, npond3, npond4, npond5;
    TextView cant_e, pond1, pond2, pond3, pond4, pond5, perc, total_p;
    SeekBar pd1, pd2, pd3, pd4, pd5;
    Button acp;

    boolean cancel = false;
    View focusView = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pond_porcentaje_pr);

        //AGREGA EL BOTON DE REGRESAR CON EL METODO ONITEMSELECTED
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //OBTENER VALORES DEL INTENT
        pond = Integer.parseInt(getIntent().getStringExtra("NOPOND"));
        Numero_pond = Integer.toString(pond);
        PR_ID = getIntent().getStringExtra("idPROF");
        materia = getIntent().getStringExtra("nom_mat");
        fecha_vig = getIntent().getStringExtra("fecha_v");

        //Toast.makeText(pond_porcentaje_pr.this, Numero_pond+" "+PR_ID+" "+materia+" "+fecha_vig, Toast.LENGTH_SHORT).show();

        //DECLARANDO OBJETOS EDIT TEXT
        npond1 = (EditText) findViewById(R.id.et_pnd1);
        npond2 = (EditText) findViewById(R.id.et_pnd2);
        npond3 = (EditText) findViewById(R.id.et_pnd3);
        npond4 = (EditText) findViewById(R.id.et_pnd4);
        npond5 = (EditText) findViewById(R.id.et_pnd5);

        //DECLARANDO OBJETOS TEXTVIEW
        pond1 = (TextView) findViewById(R.id.Pond1ID);
        pond2 = (TextView) findViewById(R.id.Pond2ID);
        pond3 = (TextView) findViewById(R.id.Pond3ID);
        pond4 = (TextView) findViewById(R.id.Pond4ID);
        pond5 = (TextView) findViewById(R.id.Pond5ID);
        perc = (TextView) findViewById(R.id.title_porciento);
        total_p = (TextView) findViewById(R.id.txt_total);

        //DECLARANDO OJETOS SEEKBAR
        pd1 = (SeekBar) findViewById(R.id.idPorPon1);
        pd2 = (SeekBar) findViewById(R.id.idPorPon2);
        pd3 = (SeekBar) findViewById(R.id.idPorPon3);
        pd4 = (SeekBar) findViewById(R.id.idPorPon4);
        pd5 = (SeekBar) findViewById(R.id.idPorPon5);

        //DECLARANDO OBJETO BOTON
        acp = (Button) findViewById(R.id.btn_acp_porc);

        //FUENTE
        Typeface font = Typeface.createFromAsset(getAssets(), "DJB Chalk It Up.ttf");

        //FUENTES DE LOS EDIT TEXT
        npond1.setTypeface(font);
        npond2.setTypeface(font);
        npond3.setTypeface(font);
        npond4.setTypeface(font);
        npond5.setTypeface(font);


        //FUENTES DE LOS TEXTVIEW
        pond1.setTypeface(font);
        pond2.setTypeface(font);
        pond3.setTypeface(font);
        pond4.setTypeface(font);
        pond5.setTypeface(font);
        total_p.setTypeface(font);

        //FUNETE BOTON
        acp.setTypeface(font);

        switch (pond){
            case 1:

                //EDIT TEXT
                npond2.setVisibility(View.INVISIBLE);
                npond3.setVisibility(View.INVISIBLE);
                npond4.setVisibility(View.INVISIBLE);
                npond5.setVisibility(View.INVISIBLE);

                //TEXT VIEW
                pond2.setVisibility(View.INVISIBLE);
                pond3.setVisibility(View.INVISIBLE);
                pond4.setVisibility(View.INVISIBLE);
                pond5.setVisibility(View.INVISIBLE);

                //SEEK BAR
                pd2.setVisibility(View.INVISIBLE);
                pd3.setVisibility(View.INVISIBLE);
                pd4.setVisibility(View.INVISIBLE);
                pd5.setVisibility(View.INVISIBLE);

                pd1 = (SeekBar) findViewById(R.id.idPorPon1);
                pd1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                        pond1.setText(progress + " %");
                        vpond1 = pond1.getText().toString();
                        vipond1 = progress;

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                break;
            case 2:


                npond3.setVisibility(View.INVISIBLE);
                npond4.setVisibility(View.INVISIBLE);
                npond5.setVisibility(View.INVISIBLE);

                pond3.setVisibility(View.INVISIBLE);
                pond4.setVisibility(View.INVISIBLE);
                pond5.setVisibility(View.INVISIBLE);

                pd3.setVisibility(View.INVISIBLE);
                pd4.setVisibility(View.INVISIBLE);
                pd5.setVisibility(View.INVISIBLE);

               pd1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                        pond1.setText(progress + " %");
                        vpond1 = pond1.getText().toString();
                        vipond1 = progress;

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });


                pd2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                        pond2.setText(progress + " %");
                        vpond2 = pond2.getText().toString();
                        vipond2 = progress;

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                break;
            case 3:

                npond4.setVisibility(View.INVISIBLE);
                npond5.setVisibility(View.INVISIBLE);

                pond4.setVisibility(View.INVISIBLE);
                pond5.setVisibility(View.INVISIBLE);

                pd4.setVisibility(View.INVISIBLE);
                pd5.setVisibility(View.INVISIBLE);

                pd1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                        pond1.setText(progress + " %");
                        vpond1 = pond1.getText().toString();
                        vipond1 = progress;

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                pd2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                        pond2.setText(progress + " %");
                        vpond2 = pond2.getText().toString();
                        vipond2 = progress;

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                pd3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                        pond3.setText(progress + " %");
                        vpond3 = pond3.getText().toString();
                        vipond3 = progress;

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                break;
            case 4:

                npond5.setVisibility(View.INVISIBLE);

                pond5.setVisibility(View.INVISIBLE);

                pd5.setVisibility(View.INVISIBLE);

                pd1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                        pond1.setText(progress + " %");
                        vpond1 = pond1.getText().toString();
                        vipond1 = progress;

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                pd2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                        pond2.setText(progress + " %");
                        vpond2 = pond2.getText().toString();
                        vipond2 = progress;

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                pd3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                        pond3.setText(progress + " %");
                        vpond3 = pond3.getText().toString();
                        vipond3 = progress;

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                pd4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                        pond4.setText(progress + " %");
                        vpond4 = pond4.getText().toString();
                        vipond4 = progress;

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                break;
            case 5:

                pd1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                        pond1.setText(progress + " %");
                        vpond1 = pond1.getText().toString();
                        vipond1 = progress;

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                pd2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                        pond2.setText(progress + " %");
                        vpond2 = pond2.getText().toString();
                        vipond2 = progress;

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                pd3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                        pond3.setText(progress + " %");
                        vpond3 = pond3.getText().toString();
                        vipond3 = progress;

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                pd4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                        pond4.setText(progress + " %");
                        vpond4 = pond4.getText().toString();
                        vipond4 = progress;

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                pd5.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                        pond5.setText(progress + " %");
                        vpond5 = pond5.getText().toString();
                        vipond5 = progress;
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                break;
        }

        acp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (pond){
                    case 1:
                        porcentaje_total = vipond1;
                        complete = Integer.toString(porcentaje_total);
                        if (vipond1<100){
                            Toast.makeText(pond_porcentaje_pr.this, "el porcentaje total debe ser igual a 100",Toast.LENGTH_SHORT).show();
                            total_p.setText(vpond1);
                        }else if (vipond1 == 100){
                            total_p.setText(vpond1);

                            NPOND1 = npond1.getText().toString();
                            NPOND1 = NPOND1.toLowerCase();
                            if (TextUtils.isEmpty(NPOND1)){
                                npond1.setError("Campo Requerido");
                                focusView = npond1;
                                cancel = true;
                            }else {
                                //AÑADIR METODO DE INSERCION
                                new InsertPond().execute();

                            }
                        }
                        break;
                    case 2:
                        porcentaje_total = vipond1 + vipond2;
                        percent1 = Integer.toString(vipond1);
                        percent2 = Integer.toString(vipond2);

                        if (porcentaje_total < 100 || porcentaje_total > 100){
                            Toast.makeText(pond_porcentaje_pr.this, "el porcentaje total debe ser igual a 100",Toast.LENGTH_SHORT).show();
                            total_p.setText(Integer.toString(porcentaje_total)+"%");
                        }else if (porcentaje_total == 100){
                            total_p.setText(Integer.toString(porcentaje_total)+"%");
                            NPOND1 = npond1.getText().toString();
                            NPOND2 = npond2.getText().toString();

                            NPOND1 = NPOND1.toLowerCase();
                            NPOND2 = NPOND2.toLowerCase();

                            if (TextUtils.isEmpty(NPOND1)){
                                npond1.setError("Campo Requerido");
                                focusView = npond1;
                                cancel = true;
                            }else if (TextUtils.isEmpty(NPOND2)){
                                npond2.setError("Campo Requerido");
                                focusView = npond2;
                                cancel = true;
                            }else {
                                //AÑADIR METODO DE INSERCION
                                new InsertPond().execute();

                            }
                        }
                        break;
                    case 3:
                        porcentaje_total = vipond1 + vipond2 + vipond3;
                        percent1 = Integer.toString(vipond1);
                        percent2 = Integer.toString(vipond2);
                        percent3 = Integer.toString(vipond3);

                        if (porcentaje_total < 100 || porcentaje_total > 100){
                            Toast.makeText(pond_porcentaje_pr.this, "el porcentaje total debe ser igual a 100",Toast.LENGTH_SHORT).show();
                            total_p.setText(Integer.toString(porcentaje_total)+"%");
                        }else if (porcentaje_total == 100){
                            total_p.setText(Integer.toString(porcentaje_total)+"%");
                            NPOND1 = npond1.getText().toString();
                            NPOND2 = npond2.getText().toString();
                            NPOND3 = npond3.getText().toString();

                            NPOND1 = NPOND1.toLowerCase();
                            NPOND2 = NPOND2.toLowerCase();
                            NPOND3 = NPOND3.toLowerCase();

                            if (TextUtils.isEmpty(NPOND1)){
                                npond1.setError("Campo Requerido");
                                focusView = npond1;
                                cancel = true;
                            }else if (TextUtils.isEmpty(NPOND2)){
                                npond2.setError("Campo Requerido");
                                focusView = npond2;
                                cancel = true;
                            }else if (TextUtils.isEmpty(NPOND3)){
                                npond3.setError("Campo Requerido");
                                focusView = npond3;
                                cancel = true;
                            }else {
                                //AÑADIR METODO DE INSERCION
                                new InsertPond().execute();

                            }
                        }
                        break;
                    case 4:
                        porcentaje_total = vipond1 + vipond2 + vipond3 + vipond4;
                        percent1 = Integer.toString(vipond1);
                        percent2 = Integer.toString(vipond2);
                        percent3 = Integer.toString(vipond3);
                        percent4 = Integer.toString(vipond4);

                        if (porcentaje_total < 100 || porcentaje_total > 100){
                            Toast.makeText(pond_porcentaje_pr.this, "el porcentaje total debe ser igual a 100",Toast.LENGTH_SHORT).show();
                            total_p.setText(Integer.toString(porcentaje_total)+"%");
                        }else if (porcentaje_total == 100){
                            total_p.setText(Integer.toString(porcentaje_total)+"%");
                            NPOND1 = npond1.getText().toString();
                            NPOND2 = npond2.getText().toString();
                            NPOND3 = npond3.getText().toString();
                            NPOND4 = npond4.getText().toString();

                            NPOND1 = NPOND1.toLowerCase();
                            NPOND2 = NPOND2.toLowerCase();
                            NPOND3 = NPOND3.toLowerCase();
                            NPOND4 = NPOND4.toLowerCase();

                            if (TextUtils.isEmpty(NPOND1)){
                                npond1.setError("Campo Requerido");
                                focusView = npond1;
                                cancel = true;
                            }else if (TextUtils.isEmpty(NPOND2)){
                                npond2.setError("Campo Requerido");
                                focusView = npond2;
                                cancel = true;
                            }else if (TextUtils.isEmpty(NPOND3)){
                                npond3.setError("Campo Requerido");
                                focusView = npond3;
                                cancel = true;
                            }else if (TextUtils.isEmpty(NPOND4)){
                                npond4.setError("Campo Requerido");
                                focusView = npond4;
                                cancel = true;
                            }else {
                                //AÑADIR METODO DE INSERCION
                                new InsertPond().execute();

                            }
                        }
                        break;
                    case 5:
                        porcentaje_total = vipond1 + vipond2 + vipond3 + vipond4 + vipond5;
                        percent1 = Integer.toString(vipond1);
                        percent2 = Integer.toString(vipond2);
                        percent3 = Integer.toString(vipond3);
                        percent4 = Integer.toString(vipond4);
                        percent5 = Integer.toString(vipond5);

                        if (porcentaje_total < 100 || porcentaje_total > 100){
                            Toast.makeText(pond_porcentaje_pr.this, "el porcentaje total debe ser igual a 100",Toast.LENGTH_SHORT).show();
                            total_p.setText(Integer.toString(porcentaje_total)+"%");
                        }else if (porcentaje_total == 100){
                            total_p.setText(Integer.toString(porcentaje_total)+"%");
                            NPOND1 = npond1.getText().toString();
                            NPOND2 = npond2.getText().toString();
                            NPOND3 = npond3.getText().toString();
                            NPOND4 = npond4.getText().toString();
                            NPOND5 = npond5.getText().toString();

                            NPOND1 = NPOND1.toLowerCase();
                            NPOND2 = NPOND2.toLowerCase();
                            NPOND3 = NPOND3.toLowerCase();
                            NPOND4 = NPOND4.toLowerCase();
                            NPOND5 = NPOND5.toLowerCase();

                            if (TextUtils.isEmpty(NPOND1)){
                                npond1.setError("Campo Requerido");
                                focusView = npond1;
                                cancel = true;
                            }else if (TextUtils.isEmpty(NPOND2)){
                                npond2.setError("Campo Requerido");
                                focusView = npond2;
                                cancel = true;
                            }else if (TextUtils.isEmpty(NPOND3)){
                                npond3.setError("Campo Requerido");
                                focusView = npond3;
                                cancel = true;
                            }else if (TextUtils.isEmpty(NPOND4)){
                                npond4.setError("Campo Requerido");
                                focusView = npond4;
                                cancel = true;
                            }else if (TextUtils.isEmpty(NPOND5)){
                                npond5.setError("Campo Requerido");
                                focusView = npond5;
                                cancel = true;
                            }else {
                                //AÑADIR METODO DE INSERCION
                                new InsertPond().execute();

                            }
                        }
                        break;
                }
            }
        });
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

    class InsertPond extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            try {
            List pond_dates = new ArrayList();
            pond_dates.add(new BasicNameValuePair("ID_Prof", PR_ID));
            pond_dates.add(new BasicNameValuePair("numpond", Numero_pond));
            pond_dates.add(new BasicNameValuePair("MATERIA", materia));
            pond_dates.add(new BasicNameValuePair("FECHA", fecha_vig));

            switch (pond){
                case 1:
                    //NOMBRE DE LA PONDERACION
                    pond_dates.add(new BasicNameValuePair("Pn1", NPOND1));

                    //PORCENTAJE DE LA PONDERACION
                    pond_dates.add(new BasicNameValuePair("Por1", complete));

                    break;
                case 2:
                    //NOMBRE DE LA PONDERACION
                    pond_dates.add(new BasicNameValuePair("Pn1", NPOND1));
                    pond_dates.add(new BasicNameValuePair("Pn2", NPOND2));

                    //PORCENTAJE DE LA PONDERACION
                    pond_dates.add(new BasicNameValuePair("Por1", percent1));
                    pond_dates.add(new BasicNameValuePair("Por2", percent2));

                    break;
                case 3:
                    //NOMBRE DE LA PONDERACION
                    pond_dates.add(new BasicNameValuePair("Pn1", NPOND1));
                    pond_dates.add(new BasicNameValuePair("Pn2", NPOND2));
                    pond_dates.add(new BasicNameValuePair("Pn3", NPOND3));

                    //PORCENTAJE DE LA PONDERACION
                    pond_dates.add(new BasicNameValuePair("Por1", percent1));
                    pond_dates.add(new BasicNameValuePair("Por2", percent2));
                    pond_dates.add(new BasicNameValuePair("Por3", percent3));

                    break;
                case 4:
                    //NOMBRE DE LA PONDERACION
                    pond_dates.add(new BasicNameValuePair("Pn1", NPOND1));
                    pond_dates.add(new BasicNameValuePair("Pn2", NPOND2));
                    pond_dates.add(new BasicNameValuePair("Pn3", NPOND3));
                    pond_dates.add(new BasicNameValuePair("Pn4", NPOND4));

                    //PORCENTAJE DE LA PONDERACION
                    pond_dates.add(new BasicNameValuePair("Por1", percent1));
                    pond_dates.add(new BasicNameValuePair("Por2", percent2));
                    pond_dates.add(new BasicNameValuePair("Por3", percent3));
                    pond_dates.add(new BasicNameValuePair("Por4", percent4));

                    break;
                case 5:
                    //NOMBRE DE LA PONDERACION
                    pond_dates.add(new BasicNameValuePair("Pn1", NPOND1));
                    pond_dates.add(new BasicNameValuePair("Pn2", NPOND2));
                    pond_dates.add(new BasicNameValuePair("Pn3", NPOND3));
                    pond_dates.add(new BasicNameValuePair("Pn4", NPOND4));
                    pond_dates.add(new BasicNameValuePair("Pn5", NPOND5));

                    //PORCENTAJE DE LA PONDERACION
                    pond_dates.add(new BasicNameValuePair("Por1", percent1));
                    pond_dates.add(new BasicNameValuePair("Por2", percent2));
                    pond_dates.add(new BasicNameValuePair("Por3", percent3));
                    pond_dates.add(new BasicNameValuePair("Por4", percent4));
                    pond_dates.add(new BasicNameValuePair("Por5", percent5));

                    break;
                }
                Log.d("request!", "starting");

                //Posting user data to script
                JSONObject json = jsonParser.makeHttpRequest(INSERT_POND_URL, "POST", pond_dates);

                // full json response
                Log.d("Registering attempt", json.toString());

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

            return null;

        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            //pDialog.dismiss();
            Toast.makeText(pond_porcentaje_pr.this, message, Toast.LENGTH_LONG).show();

        }
    }

    private class UpdatePond extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

        }
    }
}
