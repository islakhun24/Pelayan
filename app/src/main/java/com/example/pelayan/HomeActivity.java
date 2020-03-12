package com.example.pelayan;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends AppCompatActivity implements TabLayout.BaseOnTabSelectedListener{

    private static final String URL_READ = "https://geprekrame.doktersoftware.my.id/notifchart.php";
    private static final String CEK = "https://geprekrame.doktersoftware.my.id/cek_notif.php";
    private TabLayout tabLayout;
    private ViewPager viewPager;
    ImageView imageButton,btnLogout;
    TextView name;

    private RelativeLayout notif;
    private ImageView bubble;
    @Override
    public void onBackPressed() {
        Toast.makeText(HomeActivity.this,"klik Logout untuk keluar akun !",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        notif = findViewById(R.id.rlNotif);
        bubble = findViewById(R.id.bubble);
        tabLayout = (TabLayout) findViewById(R.id.tablayout_id);
        imageButton = findViewById(R.id.btnCart);
        btnLogout = findViewById(R.id.btnExit);
        name = findViewById(R.id.idPelayan);
      /*  notif = findViewById(R.id.notifchart);
        notifchart();
*/
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,Cart.class);
                intent.putExtra("pelayan",name.getText().toString());
                startActivity(intent);
            }
        });




        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setTitle("Logout Akun ?");
                builder.setCancelable(false)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });







        Intent intent = getIntent();
        String nameS = intent.getStringExtra("email");
        name.setText(nameS);

        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_restaurant_black_24dp));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_free_breakfast_black_24dp));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_room_service_black_24dp));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) findViewById(R.id.viewpager_id);
        ViewpagerAdapter adapter = new ViewpagerAdapter(HomeActivity.this.getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        tabLayout.setOnTabSelectedListener(this);

        notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,PesananSelesaiAct.class));
            }
        });
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @SuppressWarnings("unchecked")
                    public void run() {
                        try {
                            cekNotif();
                        }
                        catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doTask, 0, 3000);
        cekNotif();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
/*
    public void notifchart(){
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, URL_READ, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Aaaa", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);

    int status = jsonObject.getInt("status");
                    if (status == 0) {
        notif.setVisibility(View.VISIBLE);
    }else {
        notif.setVisibility(View.GONE);
    }

} catch (JSONException e) {
        e.printStackTrace();
        }

        }

        }, new Response.ErrorListener(){
@Override
public void onErrorResponse(VolleyError error) {
        Log.e("Volley", error.toString());
        Toast.makeText(HomeActivity.this,""+error.toString(),Toast.LENGTH_SHORT).show();
        }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(HomeActivity.this);
        requestQueue.add(jsonObjectRequest);
        }*/

private void cekNotif(){



        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.GET,CEK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Aaaa",response);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    int success = jsonObject.getInt("status");
                    if (success == 1) {
                        bubble.setVisibility(View.GONE);
                    }else {
                        bubble.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }


}



