package com.saludtec.testimages;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ZoomActivity extends AppCompatActivity {

    private String TAG = ZoomActivity.class.getSimpleName();

    private int layoutOrgininalWidth = 0;
    private int layoutOriginalHeight = 0;

    float restrictedLeftX = 0;
    float restrictedRightX = -restrictedLeftX;
    float restrictedTopY = 0;
    float restrictedButtonY = -restrictedTopY;

    float scalePY = 0;
    float scalePX = 0;

    float layoutX = 0;
    float layoutY = 0;

    boolean getXY = false;

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();

        final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                frameLayout.setScaleX((float) 1.5);
                frameLayout.setScaleY((float) 1.5);

            }
        });

        ImageView backImage = (ImageView)findViewById(R.id.zoomBackground);
        ImageView frontImage = (ImageView)findViewById(R.id.frontImageZoom);
        frontImage.setImageResource(R.drawable.chelsea);
        backImage.setImageResource(R.drawable.square);


        backImage.setBackgroundColor(Color.GRAY);
        frameLayout.setBackgroundColor(Color.BLUE);



        final int[] id = {-1};
        final float[] fingerOneX = {0};
        final float[] fingerOneY = {0};
        final float[] fingerTowX = {0};
        final float[] fingerTowY = {0};

        //Mover variables
        final float[] prevX = {0};
        final float[] prevY = {0};
        final float[] auxRotation = {0};
        final boolean[] moveEneable = {true};

        frameLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (getXY == false){
                    layoutX = frameLayout.getX();
                    layoutY = frameLayout.getY();
                    getXY = true;
                }

                //Para conseguir la posicion de los dedos
                int pointerCount = event.getPointerCount();
                for (int i = 0; i < pointerCount; i++) {

                    int pointerIndex = i;
                    id[0] = event.getPointerId(pointerIndex);

                    if (id[0] == 0) {
                        fingerOneX[0] = event.getX(pointerIndex);
                        fingerOneY[0] = event.getY(pointerIndex);


                    }

                    if (id[0] == 1) {
                        fingerTowX[0] = event.getX(pointerIndex);
                        fingerTowY[0] = event.getY(pointerIndex);
                    }

                }

                if (event.getPointerCount() > 1){
                    //Eventos multitouch

                    switch (event.getAction()){
                        case MotionEvent.ACTION_POINTER_DOWN:
                            //Usado para movimiento
                            moveEneable[0] = true;
                            break;

                        case  MotionEvent.ACTION_MOVE:
                            break;

                        case MotionEvent.ACTION_POINTER_UP:
                            break;
                    }

                }else{
                    //Eventos singletouch

                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            prevX[0] = event.getRawX();
                            //-----------------
                            prevY[0] = event.getRawY();

                            auxRotation[0] = frameLayout.getRotation();
                            break;

                        case  MotionEvent.ACTION_MOVE:


                            scalePX = (v.getScaleX() - 1)/2;
                            restrictedLeftX = scalePX * v.getWidth();
                            restrictedRightX = -restrictedLeftX;


                            if (moveEneable[0] == true && v.getX() < restrictedLeftX && v.getX() > restrictedRightX) {
                                float mX = frameLayout.getX();
                                float move_auxX = (event.getRawX() - prevX[0]);
                                float currentX = (fingerOneX[0] - prevX[0]);
                                float movePointX = (mX + move_auxX);
                                //objectMoveX(movePointX, 0, frameLayout);
                                v.setX(movePointX);

                                prevX[0] = (event.getRawX());


                            }


                            //------------------------------

                            scalePY = (v.getScaleY() - 1)/2;
                            restrictedTopY = scalePY * v.getHeight();
                            restrictedButtonY = -restrictedTopY;

                            if (moveEneable[0] == true && v.getY() < (restrictedTopY + layoutY) && v.getY() > (restrictedButtonY + layoutY) ) {
                                float mY = frameLayout.getY();
                                float move_auxY = (event.getRawY() - prevY[0]);
                                float currentX = (fingerOneY[0] - prevY[0]);
                                float movePointY = (mY + move_auxY);
                                //objectMoveY(movePointY, 0, frameLayout);
                                v.setY(movePointY);

                                prevY[0] = event.getRawY();

                            }

                            Log.d(TAG,String.valueOf(frameLayout.getX())  +"   "+String.valueOf(frameLayout.getY())  +" || "
                            + String.valueOf(restrictedLeftX) + "    " + String.valueOf(restrictedRightX) +" || "
                                    + String.valueOf(restrictedTopY) + "    " + String.valueOf(restrictedButtonY) +" || "
                                    + String.valueOf(v.getWidth()) + "    " + String.valueOf(v.getHeight()) );

                            break;

                        case MotionEvent.ACTION_UP:

                            if (v.getX() > restrictedLeftX + layoutX){
                                //v.setX(restrictedLeftX-1);
                                objectMoveX(restrictedLeftX - 1, 1000, frameLayout);
                            }else{
                                if (v.getX() < restrictedRightX + layoutX){
                                    //v.setX(restrictedRightX + 1);
                                    objectMoveX(restrictedRightX+1, 1000, frameLayout);
                                }
                            }



                            if (v.getY() > restrictedTopY + layoutY){
                                //v.setY(restrictedTopY - 1);
                                objectMoveY(restrictedTopY + layoutY - 1, 1000, frameLayout);
                            }else{
                                if (v.getY() < restrictedButtonY + layoutY){
                                    //v.setY(restrictedButtonY + 1);
                                    objectMoveY(restrictedButtonY +layoutY + 1, 1000, frameLayout);
                                }

                            }


                            break;
                    }

                }

                return true;
            }
        });

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



}
