package com.saludtec.testimages;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class ZoomActivity extends AppCompatActivity {

    private String TAG = ZoomActivity.class.getSimpleName();

    private float layoutOrgininalWidth = 0;
    private float layoutOriginalHeight = 0;
    private float layoutDiagonal = 0;
    private float scale = 1;
    private float scaleLayoutDiagonal = 0;
    private float scaleOffset = 0;
    private float scale_aux = 0;
    private float pixelsOffset = 0;


    float restrictedLeftX = 0;
    float restrictedRightX = -restrictedLeftX;
    float restrictedTopY = 0;
    float restrictedButtonY = -restrictedTopY;

    float scalePY = 0;
    float scalePX = 0;

    float layoutX = 0;
    float layoutY = 0;

    boolean getXY = false;


    int id = -1;
    float fingerOneX = 0;
    float fingerOneY = 0;
    float fingerTowX = 0;
    float fingerTowY = 0;
    float rawX = 0;
    float rawY = 0;
    float rawX2 = 0;
    float rawY2 = 0;

    float mFingersDist = 0;
    float mPrevFingersDist = 0;


    //Mover variables
    float prevX = 0;
    float prevY = 0;
    float auxRotation = 0;
    boolean moveEneable = true;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom);

    }


        @Override
        public void onWindowFocusChanged(boolean hasFocus) {
            super.onWindowFocusChanged(hasFocus);
            //Here you can get the size!

        final FrameLayout frameLayout = (FrameLayout)findViewById(R.id.zoomlayout);
        layoutOrgininalWidth = frameLayout.getWidth();
        layoutOriginalHeight = frameLayout.getHeight();
        layoutDiagonal = (int) Math.sqrt(Math.pow(layoutOrgininalWidth, 2) + Math.pow(layoutOriginalHeight, 2));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //frameLayout.setScaleX((float) 1.5);
                //frameLayout.setScaleY((float) 1.5);

            }
        });

        ImageView backImage = (ImageView)findViewById(R.id.zoomBackground);
        ImageView frontImage = (ImageView)findViewById(R.id.frontImageZoom);
        frontImage.setImageResource(R.drawable.chelsea);
        backImage.setImageResource(R.drawable.square);
        backImage.setBackgroundColor(Color.GRAY);
        frameLayout.setBackgroundColor(Color.BLACK);

        MovementsTouchListener movementsTouchListener = new MovementsTouchListener();
            movementsTouchListener.ZoomAction(frameLayout, getApplicationContext());





        }


    private void objectMoveX(float translationX, long duration, FrameLayout imageView){

        ObjectAnimator move = ObjectAnimator.ofFloat(imageView, "x", translationX);
        move.setDuration(duration);
        move.start();
    }

    private void objectMoveY(float translationY, long duration, FrameLayout imageView){

        ObjectAnimator move = ObjectAnimator.ofFloat(imageView, "y", translationY);
        move.setDuration(duration);
        move.start();
    }

    public static String actionToString(int action) {
        switch (action) {

            case MotionEvent.ACTION_DOWN:
                return "Down";
            //case MotionEvent.ACTION_MOVE:
            //    return "Move";
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
