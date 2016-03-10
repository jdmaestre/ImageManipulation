package com.saludtec.testimages;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Button moveRotateScaleButton = (Button) findViewById(R.id.moveRotateScaleButton);
        Button cloneButton = (Button) findViewById(R.id.cloneButton);
        Button zoomButton = (Button) findViewById(R.id.zoomButton);

        moveRotateScaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),RotateResizeMoveActivity.class);
                startActivity(intent);

            }
        });

        cloneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),CloneActivity.class);
                startActivity(intent);

            }
        });

        zoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ZoomActivity.class);
                startActivity(intent);

            }
        });


    }
}
