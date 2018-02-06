package com.example.daniel.calificator21;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
public class Menu_Principal_Alumno extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentManager manager2;
    private android.app.FragmentTransaction transaction2;
    public static final String idusalum = "idalumno";
    public static final String TAG_ID_ALUM = "id_alum";
    public static final String TAG_NAMEALUM = "name_alum";
    public static final String TAG_APPALUM = "ap_alum";
    public static final String TAG_APMALUM = "am_alum";

    TextView user;
    String id_user, idFindAlum, id_alum, NMalum, APalum, AMalum;

    //SOCKET JSON
    JSONParser jsonParser = new JSONParser();

    //URL PHP
    private static final String LOGIN_URL = "http://calificator.com/app_movil/buscaralum.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu__principal__alumno);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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
                Toast.makeText(Menu_Principal_Alumno.this, R.string.up, Toast.LENGTH_SHORT).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        id_user = getIntent().getStringExtra("idalumno");

        idFindAlum = id_user;

        user = (TextView) navigationView.getHeaderView(0).findViewById(R.id.Title_alum);

        new FINDTEACH().execute();
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
        getMenuInflater().inflate(R.menu.menu__principal__alumno, menu);
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
            Toast.makeText(this, "seleccionado", Toast.LENGTH_SHORT).show();
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
        manager2 = getFragmentManager();
        transaction2 = manager2.beginTransaction();
        Bundle bundle2 = new Bundle();
        switch (item.getItemId()) {
            case R.id.nav_Horario:
                fragment = new FragHorAlum();
                fragment.setArguments(bundle2);
                fragmentTransaction = true;
                break;
            case R.id.nav_Materias:
                fragment = new FragMatAlum();
                fragment.setArguments(bundle2);
                fragmentTransaction = true;
                break;
            case R.id.nav_Calificaciones:
                fragment = new FragCalAlum();
                fragment.setArguments(bundle2);
                fragmentTransaction = true;
                break;
            case R.id.nav_Examenes:
                fragment = new FragExamAlum();
                fragment.setArguments(bundle2);
                fragmentTransaction = true;
                break;
            case R.id.nav_perfil:
                fragment = new FragEditarPerfilPR();
                fragment.setArguments(bundle2);
                fragmentTransaction = true;
                break;
            case R.id.nav_Logout:
                Intent back = new Intent(Menu_Principal_Alumno.this, Inicio_Sesion.class);
                finish();
                startActivity(back);
        }
        if (fragmentTransaction) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_menu__principal__alumno, fragment)
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
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... args) {
            try {
                List findID = new ArrayList();
                findID.add(new BasicNameValuePair("id_alum", idFindAlum));

                JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST",
                        findID);


                id_alum = json.getString(TAG_ID_ALUM);
                NMalum = json.getString(TAG_NAMEALUM);
                APalum = json.getString(TAG_APPALUM);
                AMalum = json.getString(TAG_APMALUM);

                user.append(NMalum.toUpperCase() + " " + APalum.toUpperCase() + " " + AMalum.toUpperCase());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            //pDialog.dismiss();
            if (file_url != null) {
                Toast.makeText(Menu_Principal_Alumno.this, file_url, Toast.LENGTH_LONG).show();
            }
        }
    }

}