package com.example.daniel.calificator21;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MenuPrincipalPR extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //FUENTE
    Typeface font;

    public static final String iduser = "idprofesor";
    public static final String TAG_ID_prof = "id_Tprof";
    public static final String TAG_NAMER = "name_pr";
    public static final String TAG_APPPR = "ap_prof";
    public static final String TAG_APMPR = "am_prof";

    TextView usuario, welcome, wm;
    String id_user, idFindPR, id_prof, NMprof, APprof, AMprof;

    //SOCKET JSON
    JSONParser jsonParser = new JSONParser();

    //private LinearLayout layout;

    //URL PHP
    private static final String FINDPRF_URL = "http://calificator.com/app_movil/buscarProf.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //layout = (LinearLayout) findViewById(R.id.MPPR);

        //FUENTE
        font = Typeface.createFromAsset(this.getAssets(), "DJB Chalk It Up.ttf");

        setContentView(R.layout.activity_menu_principal_pr);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MenuPrincipalPR.this, R.string.up, Toast.LENGTH_SHORT).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        id_user = getIntent().getStringExtra("idprofesor");

        idFindPR = id_user;

        usuario = (TextView) navigationView.getHeaderView(0).findViewById(R.id.Title_usr_pr);
        //welcome = (TextView) findViewById(R.id.TV_Welcome);
        //wm = (TextView) findViewById(R.id.TV_WM);

        //FUENTES TEXTVIEW
        usuario.setTypeface(font);
        //welcome.setTypeface(font);
        //wm.setTypeface(font);


        new FINDTEACH().execute();

        //Toast.makeText(this, id_user, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal_pr, menu);

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
            //Toast.makeText(this, "seleccionado", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        boolean fragmentTransaction = false;
        Fragment fragment = null;
        FragmentManager manager = getFragmentManager();
        android.app.FragmentTransaction transaction = manager.beginTransaction();
        Bundle bundle1 = new Bundle();

        switch (item.getItemId()) {
            case R.id.nav_pond:
                fragment = new FragPonderacionesPR();
                bundle1.putString("IDPR",id_prof);
                fragment.setArguments(bundle1);
                fragmentTransaction = true;
                //layout.setVisibility(View.GONE);
                break;
            case R.id.nav_calif_mod:
                fragment = new FragCalificaPR();
                bundle1.putString("IDPR",id_prof);
                fragment.setArguments(bundle1);
                fragmentTransaction = true;
                break;
            case R.id.nav_group_pr:
                fragment = new FragGruposPR();
                bundle1.putString("IDPR",id_prof);
                fragment.setArguments(bundle1);
                fragmentTransaction = true;
                break;
            case R.id.nav_perfil:
                fragment = new FragEditarPerfilPR();
                bundle1.putString("IDPR",id_user);
                fragment.setArguments(bundle1);
                fragmentTransaction = true;
                break;
            case R.id.nav_close:
                Intent back = new Intent(MenuPrincipalPR.this, Inicio_Sesion.class);
                finish();
                startActivity(back);
        }

        if(fragmentTransaction) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_menu_principal_pr, fragment)
                    .commit();

            item.setChecked(true);
            getSupportActionBar().setTitle(item.getTitle());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    class FINDTEACH extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {}

        @Override
        protected String doInBackground(String... args) {
            try {
            List findID = new ArrayList();
            findID.add(new BasicNameValuePair("id_prof", idFindPR));

            JSONObject json = jsonParser.makeHttpRequest(FINDPRF_URL, "POST",
                    findID);


                id_prof = json.getString(TAG_ID_prof);
                NMprof = json.getString(TAG_NAMER);
                APprof = json.getString(TAG_APPPR);
                AMprof = json.getString(TAG_APMPR);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            //pDialog.dismiss();

            usuario.append(NMprof.toUpperCase()+" "+APprof.toUpperCase()+" "+AMprof.toUpperCase());
            //Toast.makeText(MenuPrincipalPR.this, file_url, Toast.LENGTH_LONG).show();
        }
    }

}