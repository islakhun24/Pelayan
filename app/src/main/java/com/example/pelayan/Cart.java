package com.example.pelayan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart extends AppCompatActivity {

    private static final String URL_CART = "https://geprekrame.doktersoftware.my.id/getCart.php";
    private static final String URL_CHECK = "https://geprekrame.doktersoftware.my.id/check_pesanan.php";
    private static final String URL_DELETE = "https://geprekrame.doktersoftware.my.id/delete_cart.php";
    private static final String URL_SUM = "https://geprekrame.doktersoftware.my.id/totalbayar.php";
    private static final String URL_UPLOADORDERMENU = "https://geprekrame.doktersoftware.my.id/upload_pesanan.php";
    private static final String URL_DELETEALL = "https://geprekrame.doktersoftware.my.id/deleteAllCart.php";
    String[] daftar;
    protected Cursor cursor;
    private RecyclerView recyclerView;
    EditText atasNama;
    Button konfirmasi;
    Spinner nomorMejaSpin;
    TextView tvQty, tvName, tvHarga, tvSubTotal, totalbayar, keterangan,tvpelayan;
    CardView cvList;
    List<CartParameter> cartList;
    private LinearLayoutManager llm;
    private DividerItemDecoration did;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.recyclerView);
        atasNama = findViewById(R.id.etAtasNama);
        totalbayar = findViewById(R.id.tvTotalBayar);
        konfirmasi = findViewById(R.id.btnKonfirmasi);
        keterangan = findViewById(R.id.tvKeterangan);
        tvQty = findViewById(R.id.tvQty);
        nomorMejaSpin = (Spinner) findViewById(R.id.etNomorMeja);
        tvName = findViewById(R.id.tvNamamenu);
        tvpelayan = findViewById(R.id.tvpelayan);
        tvHarga = findViewById(R.id.textViewHarga);
        tvSubTotal = findViewById(R.id.textViewSubtotal);
        cvList = findViewById(R.id.cvList2);
        cartList = new ArrayList<>();
        adapter = new CartAdapter(cartList);
        llm = new LinearLayoutManager(Cart.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        did = new DividerItemDecoration(recyclerView.getContext(), llm.getOrientation());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llm);
        recyclerView.addItemDecoration(did);
        recyclerView.setAdapter(adapter);

        Intent intent = getIntent();
        String pelayan = intent.getStringExtra("pelayan");
        tvpelayan.setText(pelayan);

        String[] nomorMeja = new String[]{
                "Pilih Nomor Meja !",
                "1",
                "2",
                "3",
                "4",
                "5",
                "6",
                "7",
                "8",
                "9",
                "10",
                "bungkus 1", "bungkus 2", "bungkus 3", "bungkus 4", "bungkus 5", "bungkus 6", "bungkus 7", "bungkus 8",


        };

        final ArrayAdapter<String> spinnerArray2 = new ArrayAdapter<String>(
                this,R.layout.spinner,nomorMeja
        );
        spinnerArray2.setDropDownViewResource(R.layout.spinner);
        nomorMejaSpin.setAdapter(spinnerArray2);

        nomorMejaSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                switch (i){
                    case 0:
                        /*Toast.makeText(getActivity(),"Makan Ditempat",Toast.LENGTH_SHORT).show();*/
                        break;
                    case 1:
                        /*Toast.makeText(getActivity(),"Dibungkus",Toast.LENGTH_SHORT).show();*/
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this.getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                final CartParameter cart = cartList.get(position);
                final String nama = cart.getNama();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Cart.this);
                alertDialogBuilder.setTitle("Hapus Pesanan "+nama+ " ?");
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("YA", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DELETE,
                                        new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Log.e("Aaaa",response);
                                        try{
                                            JSONObject jsonObject = new JSONObject(response);
                                            String value = jsonObject.getString("value");
                                            if(value.equals("1")){
                                                loadCart();
                                            }
                                        }catch (JSONException e){
                                            e.printStackTrace();
                                            Toast.makeText(Cart.this,"Failed to Delete !"+e.toString(),Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }, new Response.ErrorListener(){
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.e("Volley", error.toString());
                                    }
                                })
                                {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String,String> params = new HashMap<>();
                                        params.put("nama", nama);
                                        return params;
                                    }
                                };
                                RequestQueue requestQueue = Volley.newRequestQueue(Cart.this);
                                requestQueue.add(stringRequest);
                            }
                        })
                        .setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        }));

        loadCart();
        sum();

        konfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String atasnama = atasNama.getText().toString().trim();
                final String status1 = "0";
                final String noMeja = nomorMejaSpin.getSelectedItem().toString().trim();

                if(!atasnama.isEmpty()){
                    StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.GET,URL_CHECK+"?no_meja="+noMeja,
                            new Response.Listener<String>(){
                                @Override
                                public void onResponse(String response) {
                                    Log.e("Aaaa",response);
                                    try{
                                        JSONObject jsonObject = new JSONObject(response);
                                        int status = jsonObject.getInt("status");
                                        if (status == 1) {
                                            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPLOADORDERMENU,
                                                    new Response.Listener<String>(){
                                                        @Override
                                                        public void onResponse(String response) {
                                                            Log.e("response",response);

                                                        }
                                                    },
                                                    new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {
                                                            Toast.makeText(Cart.this,"Gagal melakukan pesanan "+error.toString(),Toast.LENGTH_SHORT).show();
                                                        }
                                                    }){
                                                @Override
                                                protected Map<String, String> getParams() throws AuthFailureError {
                                                    Map<String,String> params = new HashMap<>();
                                                    String nama = "",harga="",qty="",subtotal="",keterangan="";
                                                    for(int i=0;i<cartList.size();i++){
                                                        nama += cartList.get(i).getNama()+",";
                                                        harga += cartList.get(i).getHarga()+",";
                                                        qty += cartList.get(i).getQty()+",";
                                                        subtotal += cartList.get(i).getSubtotal()+",";
                                                        keterangan += cartList.get(i).getKeterangan()+",";
                                                    }
                                                    params.put("name", nama);
                                                    params.put("harga", harga);
                                                    params.put("qty", qty);
                                                    params.put("count",String.valueOf(cartList.size()));
                                                    params.put("subtotal", subtotal);
                                                    params.put("atasnama", atasnama);
                                                    params.put("status", status1);
                                                    params.put("keterangan",keterangan);
                                                    params.put("no_meja",noMeja);
                                                    return params;
                                                }
                                            };

                                            RequestQueue requestQueue = Volley.newRequestQueue(Cart.this);
                                            requestQueue.add(stringRequest);
                                            totalbayar.setText("");
                                            DeleteAll();
                                        } else {
                                            Toast.makeText(Cart.this,"Meja sudah digunakan !",Toast.LENGTH_SHORT).show();
                                        }
                                    }catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(Cart.this,""+e,Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Volley", error.toString());
                        }
                    });
                    RequestQueue requestQueue = Volley.newRequestQueue(Cart.this);
                    requestQueue.add(jsonObjectRequest);

                }else{
                    Toast.makeText(Cart.this,"Isi Nomor Meja !",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void loadCart() {
        cartList.clear();
        final ProgressDialog progressDialog = new ProgressDialog(Cart.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        Intent intent = getIntent();
        String pelayan = intent.getStringExtra("pelayan");

        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.GET,URL_CART+"?pelayan="+pelayan,
                new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                Log.e("Aaaa",response);
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    int success = jsonObject.getInt("success");
                    if (success == 1) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject object = jsonArray.getJSONObject(i);
                                CartParameter cartParameter = new CartParameter();
                                cartParameter.setId(object.getInt("id"));
                                cartParameter.setNama(object.getString("nama"));
                                cartParameter.setHarga(object.getString("harga"));
                                cartParameter.setQty(object.getString("qty"));
                                cartParameter.setSubtotal(object.getString("subtotal"));
                                cartParameter.setKeterangan(object.getString("keterangan"));
                                cartList.add(cartParameter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                            }
                        }
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                progressDialog.dismiss();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    public void sum(){
        Intent intent = getIntent();
        String pelayan = intent.getStringExtra("pelayan");
        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.GET,URL_SUM+"?pelayan="+pelayan,
                new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                Log.e("Aaaa",response);
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    int success = jsonObject.getInt("success");
                    String result = jsonObject.getString("result");

                    totalbayar.setText(result);

                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);

    }

    public void DeleteAll(){
        Intent intent = getIntent();
        String pelayan = intent.getStringExtra("pelayan");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DELETEALL+"?pelayan="+pelayan,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Aaaa",response);
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    String value = jsonObject.getString("value");
                    if(value.equals("1")){
                        //Toast.makeText(Cart.this,"Delete Succes !",Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    //Toast.makeText(Cart.this,"Failed to Delete !"+e.toString(),Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Cart.this);
        requestQueue.add(stringRequest);
        Intent intent1 = new Intent(Cart.this, HomeActivity.class);
        intent1.putExtra("email",tvpelayan.getText().toString());
        startActivity(intent1);
    }
}
