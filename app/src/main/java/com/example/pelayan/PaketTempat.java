package com.example.pelayan;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaketTempat extends Fragment implements FoodAdapter.OnShareClickedListener{

    private static final String URL_PAKET = "https://geprekrame.doktersoftware.my.id/getPaket.php";
    private static final String URL_CART = "https://geprekrame.doktersoftware.my.id/upload_cart.php";
    ImageView imageView;
    TextView tvName, tvHarga, subTotal,namaPelayan;
    CardView cvList;
    List<Food> foodList;
    private RecyclerView recyclerView;
    private LinearLayoutManager llm;
    private DividerItemDecoration did;
    private RecyclerView.Adapter adapter;
    private View BottomSheet;
    private BottomSheetBehavior SheetBehavior;
    Button pesan, kurang, tambah;
    private int qty = 0;
    EditText qtypesan;
    String totalCOunt;
    Spinner spinner;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_paket_tempat, container, false);

        namaPelayan = getActivity().findViewById(R.id.idPelayan);
        tvName = v.findViewById(R.id.tvNamamenu);
        tvHarga = v.findViewById(R.id.tvHargamenu);
        imageView = v.findViewById(R.id.imageTampil);
        cvList = v.findViewById(R.id.cvList);
        spinner = (Spinner) v.findViewById(R.id.spinner);
        recyclerView = v.findViewById(R.id.recyclerView);
        foodList = new ArrayList<>();
        adapter = new FoodAdapter(getActivity().getApplicationContext(),foodList,PaketTempat.this);
        llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        did = new DividerItemDecoration(recyclerView.getContext(), llm.getOrientation());
        BottomSheet = v.findViewById(R.id.bottomSheet);
        SheetBehavior = BottomSheetBehavior.from(BottomSheet);
        SheetBehavior.setHideable(true);

        pesan = v.findViewById(R.id.pesan);
        kurang = v.findViewById(R.id.btn_kurang);
        tambah = v.findViewById(R.id.btn_tambah);
        qtypesan = v.findViewById(R.id.et_Qty);
        subTotal = v.findViewById(R.id.subtotal);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llm);
        recyclerView.addItemDecoration(did);
        recyclerView.setAdapter(adapter);

        String[] kas = new String[]{
                "Makan Ditempat",
                "Dibungkus"
        };

        ArrayAdapter<String> spinnerArray = new ArrayAdapter<String>(
                getActivity(),R.layout.spinner,kas
        );
        spinnerArray.setDropDownViewResource(R.layout.spinner);
        spinner.setAdapter(spinnerArray);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qty = Integer.parseInt(qtypesan.getText().toString());
                qty++;
                qtypesan.setText(String.valueOf(qty));
                totalCOunt = String.valueOf(qty * Integer.parseInt(tvHarga.getText().toString()));
                subTotal.setText(totalCOunt);
            }
        });
        kurang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(qty>0){
                    qty = Integer.parseInt(qtypesan.getText().toString());
                    qty--;
                    qtypesan.setText(String.valueOf(qty));
                    totalCOunt = String.valueOf(qty * Integer.parseInt(tvHarga.getText().toString()));
                    subTotal.setText(totalCOunt);
                }
            }
        });

        pesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cart();
                SheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        loadfood();

        return v;
    }

    private void loadfood() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.GET,URL_PAKET, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Aaaa",response);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    int success = jsonObject.getInt("success");
                    if (success == 1) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject object = jsonArray.getJSONObject(i);
                                Food food = new Food();
                                food.setId(object.getInt("id"));
                                food.setName(object.getString("name"));
                                food.setHarga(object.getString("harga"));
                                food.setImage(object.getString("image"));
                                foodList.add(food);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                progressDialog.dismiss();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonObjectRequest);
    }

    private void cart(){
        final String nama = this.tvName.getText().toString().trim();
        final String harga = this.tvHarga.getText().toString().trim();
        final String qty = this.qtypesan.getText().toString().trim();
        final String subtotal = this.subTotal.getText().toString().trim();
        final String keterangan = this.spinner.getSelectedItem().toString().trim();
        final String pelayan = this.namaPelayan.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CART,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(getActivity(), "Sending to Cart", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Failed to Sending order "+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Failed to Sending order "+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("nama", nama);
                params.put("harga", harga);
                params.put("qty",qty);
                params.put("subtotal",subtotal);
                params.put("keterangan", keterangan);
                params.put("pelayan",pelayan);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }


    @Override
    public void ShareClicked(String harga, String image, String id, String nama) {

        if (SheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            SheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            tvName.setText(nama);
            tvHarga.setText(harga);
            Glide.with(getActivity()).load(image).into(imageView);
            qtypesan.setText("0");
            subTotal.setText("000");

        } else {
            SheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            tvName.setText(nama);
            tvHarga.setText(harga);
            Glide.with(getActivity()).load(image).into(imageView);
            qtypesan.setText("0");
            subTotal.setText("000");
        }
    }
}
