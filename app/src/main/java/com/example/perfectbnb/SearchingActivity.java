package com.example.perfectbnb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class SearchingActivity extends AppCompatActivity {
double longitute,lat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searching);
        Intent i = getIntent();

        longitute = i.getDoubleExtra("long",0.00);
        lat = i.getDoubleExtra("lat",0.00);

        Button btn = (Button)findViewById(R.id.search_detials_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ResultActivity.class);
                i.putExtra("long",longitute);
                i.putExtra("lat",lat);
                startActivity(i);
            }
        });
    }
}
