package com.saludtec.testimages;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;

public class CloneActivity extends AppCompatActivity {

    float puntoInicioX = 0;
    float puntoInicioY = 0;

    float fingerOneX = 0;
    float fingerOneY = 0;
    float fingerTowX = 0;
    float fingerTowY = 0;

    float lowerX = 0;
    float lowerY = 0;
    float higherX = 0;
    float higherY = 0;
    float sizeX = 0;
    float sizeY = 0;

    Bitmap imageM;
    Canvas canvas;

    ArrayList<PointF> pathArray = new ArrayList<PointF>();

    private String TAG = CloneActivity.class.getSimpleName();

    MovementsTouchListener mMovementsTouchListener = new MovementsTouchListener();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clone);



    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //Here you can get the size!
        final FrameLayout frameLayout = (FrameLayout)findViewById(R.id.CloneContent);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //frameLayout.setScaleX((float) 1.5);
                //frameLayout.setScaleY((float) 1.5);
                //Log.d(TAG, String.valueOf(frameLayout.getX()));

            }
        });

        final ImageView toCloneImage = (ImageView)findViewById(R.id.toCloneImage);
        mMovementsTouchListener.CloneAction(toCloneImage, getApplicationContext());



    }

    public static String actionToString(int action) {
        switch (action) {

            case MotionEvent.ACTION_DOWN:
                return "Down";
            case MotionEvent.ACTION_MOVE:
                return "Move";
            case MotionEvent.ACTION_POINTER_DOWN:
                return "Pointer Down";
            case MotionEvent.ACTION_UP:
                return "Up";
            case MotionEvent.ACTION_POINTER_UP:
                return "Pointer Up";
            case MotionEvent.ACTION_OUTSIDE:
                return "Outside";
            case MotionEvent.ACTION_CANCEL:
                return "Cancel";
        }
        return "";
    }



}
