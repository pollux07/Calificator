package com.example.daniel.calificator21;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 11/08/2016.
 */
public class FragEditarPerfilPR extends Fragment{

    public FragEditarPerfilPR() {
        // Required empty public constructor

    }

    //SOCKET JSON
    JSONParser jsonParser = new JSONParser();

    private static final String FIND_PERFIL_PRF_URL = "http://calificator.com/app_movil/f_perfil_PR.php";
    private static final String UPDATE_PERFIL_PRF_URL = "http://calificator.com/app_movil/update_perfil_pr.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    public static final String TAG_ID_PR = "iduser";
    public static final String TAG_USERNAME_PR = "username_pr";
    public static final String TAG_PSW_OLD = "psw_old";
    public static final String TAG_PSW_PR = "psw_pr";

    Button update;
    TextView tl_perfil, tv_n_perfil, tv_psw_old, tv_psw_perfil;
    EditText et_n_perfil, et_psw_old, et_psw_perfil;

    String idPR, ID_PROF, USERNAME_PR, PSW_OLD_PR, PSW_PR, MESSAGE;

    boolean cancel = false;
    View focusView = null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.frag_perfil_pr, container, false);

        //FUENTE
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "DJB Chalk It Up.ttf");

        //DECLARANDO OBJETOS TEXTVIEW
        tl_perfil = (TextView) view.findViewById(R.id.title_perfil_pr);
        tv_n_perfil = (TextView) view.findViewById(R.id.tv_n_perfil);
        tv_psw_old = (TextView) view.findViewById(R.id.tv_psw__old_pr);
        tv_psw_perfil = (TextView) view.findViewById(R.id.tv_psw_pr);

        //DECLARANDO OBJETOS EDITTEXT
        et_n_perfil = (EditText) view.findViewById(R.id.et_n_perfil);
        et_psw_old = (EditText) view.findViewById(R.id.et_psw_old);
        et_psw_perfil = (EditText) view.findViewById(R.id.et_psw_pr);

        //DECLARANDO OBJETOS BUTTON
        update = (Button) view.findViewById(R.id.Update_pr_prerfil);

        //DECLARANDO FUENTES TEXT VIEW
        tl_perfil.setTypeface(font);
        tv_n_perfil.setTypeface(font);
        tv_psw_old.setTypeface(font);
        tv_psw_perfil.setTypeface(font);

        //DECLARANDO FUENTES EDIT TEXT
        et_n_perfil.setTypeface(font);
        et_psw_old.setTypeface(font);
        et_psw_perfil.setTypeface(font);

        //DECLARANDO FUENTES BUTTON
        update.setTypeface(font);

        //OBTENIENDO ID DEL PROFESOR
        Bundle bundle1 = getArguments();
        idPR = bundle1.getString("IDPR");

        new FindPerfilPR().execute();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                USERNAME_PR = et_n_perfil.getText().toString();
                PSW_OLD_PR = et_psw_old.getText().toString();
                PSW_PR = et_psw_perfil.getText().toString();

                if (TextUtils.isEmpty(USERNAME_PR)){
                    et_n_perfil.setError("Campo Requerido");
                    focusView = et_n_perfil;
                    cancel = true;
                }else if (TextUtils.isEmpty(PSW_OLD_PR)){
                    et_psw_old.setError("Campo Requerido");
                    focusView = et_psw_old;
                    cancel = true;
                }else if (TextUtils.isEmpty(PSW_PR)){
                    et_psw_perfil.setError("Campo Requerido");
                    focusView = et_psw_perfil;
                    cancel = true;
                }else {
                    new UpdatePerfilPR().execute();
                }
            }
        });

        return view;
    }

    private class FindPerfilPR extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                List findPR = new ArrayList();
                findPR.add(new BasicNameValuePair("id_user", idPR));
                JSONObject json = jsonParser.makeHttpRequest(FIND_PERFIL_PRF_URL, "POST",
                        findPR);
                //Log.d("Response:", "> " + json);

                ID_PROF = json.getString(TAG_ID_PR);
                USERNAME_PR = json.getString(TAG_USERNAME_PR);
                PSW_OLD_PR = json.getString(TAG_PSW_OLD);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            et_n_perfil.setText(USERNAME_PR);
            et_psw_old.setText(PSW_OLD_PR);

        }
    }


    private class UpdatePerfilPR extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            List update = new ArrayList();
            update.add(new BasicNameValuePair("id_user", idPR));
            update.add(new BasicNameValuePair("user_n", USERNAME_PR));
            update.add(new BasicNameValuePair("psw_old", PSW_OLD_PR));
            update.add(new BasicNameValuePair("psw_new", PSW_PR));
            JSONObject json1 = jsonParser.makeHttpRequest(UPDATE_PERFIL_PRF_URL, "POST", update);

            // check your log for json response
            Log.d("Response:", "> " + json1);
            try {
                MESSAGE = json1.getString(TAG_MESSAGE);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Toast.makeText(getActivity(), MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

}