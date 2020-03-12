package com.example.pelayan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class PesananSelesaiAct extends AppCompatActivity {
    private RecyclerView rvPesan;
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesanan_selesai);
        rvPesan = findViewById(R.id.rvPesanan);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rvPesan.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
    }

}
