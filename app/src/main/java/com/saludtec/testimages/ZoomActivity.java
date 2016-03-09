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
        frameLayout.setBackgroundColor(Color.BLACK);


        frameLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (getXY == false){
                    layoutX = frameLayout.getX();
                    layoutY = frameLayout.getY();
                    getXY = true;
                }
                int action = event.getActionMasked();

                Log.v(TAG, actionToString(event.getActionMasked()));

                //Para conseguir la posicion de los dedos
                int pointerCount = event.getPointerCount();
                for (int i = 0; i < pointerCount; i++) {

                    int pointerIndex = i;
                    id = event.getPointerId(pointerIndex);

                    if (id == 0) {
                        fingerOneX = event.getX(pointerIndex);
                        fingerOneY = event.getY(pointerIndex);

                        rawX = event.getRawX();
                        rawY = event.getRawY();

                    }

                    if (id == 1) {
                        fingerTowX = event.getX(pointerIndex);
                        fingerTowY = event.getY(pointerIndex);

                        //Codigo para obtener RawX & RawY de otros pointers diferentes al 1
                        int location[] = {0,0};
                        v.getLocationOnScreen(location);

                        double angle=Math.toDegrees(Math.atan2(fingerTowY, fingerTowX));
                        angle += v.getRotation();

                        final float length= PointF.length(fingerTowX, fingerTowY);

                        rawX2=(float)(length*Math.cos(Math.toRadians(angle)))+location[0];
                        rawY2=(float)(length*Math.sin(Math.toRadians(angle)))+location[1];
                    }

                }

                if (event.getPointerCount() > 1){
                    //Eventos multitouch

                    switch (action){
                        case MotionEvent.ACTION_POINTER_DOWN:
                            //Usado para movimiento
                            moveEneable= false;
                            Log.v(TAG, String.valueOf(moveEneable));

                            //Usado para escalar
                            mPrevFingersDist = (float) Math.sqrt(Math.pow(fingerOneX - fingerTowX, 2) + Math.pow(fingerOneY- fingerTowY, 2));

                            break;

                        case  MotionEvent.ACTION_MOVE:

                            mFingersDist = (float) Math.sqrt(Math.pow(fingerOneX - fingerTowX, 2) + Math.pow(fingerOneY- fingerTowY, 2));

                            pixelsOffset = mFingersDist - mPrevFingersDist;
                            scaleLayoutDiagonal = (layoutDiagonal + (mFingersDist - mPrevFingersDist));
                            scaleOffset = scaleLayoutDiagonal/layoutDiagonal;
                            scale = scaleOffset + scale_aux;
                            v.setScaleX(scale);
                            v.setScaleY(scale);


                            break;

                        case MotionEvent.ACTION_POINTER_UP:
                            scale_aux = (scale -1);
                            if (scale < 1){
                                v.setScaleX(1);
                                v.setScaleY(1);
                                scale_aux = 0;
                            }

                            if (v.getX() > restrictedLeftX + layoutX){
                                //v.setX(restrictedLeftX-1);
                                objectMoveX(restrictedLeftX , 0, frameLayout);
                            }else{
                                if (v.getX() < restrictedRightX + layoutX){
                                    //v.setX(restrictedRightX + 1);
                                    objectMoveX(restrictedRightX, 0, frameLayout);
                                }
                            }



                            if (v.getY() > restrictedTopY + layoutY){
                                //v.setY(restrictedTopY - 1);
                                objectMoveY(restrictedTopY + layoutY , 0, frameLayout);
                            }else{
                                if (v.getY() < restrictedButtonY + layoutY){
                                    //v.setY(restrictedButtonY + 1);
                                    objectMoveY(restrictedButtonY +layoutY , 0, frameLayout);
                                }

                            }


                            break;
                    }

                }else{
                    //Eventos singletouch

                    switch (action){
                        case MotionEvent.ACTION_DOWN:
                            prevX = event.getRawX();
                            //-----------------
                            prevY = event.getRawY();

                            auxRotation = frameLayout.getRotation();
                            break;

                        case  MotionEvent.ACTION_MOVE:


                            scalePX = (v.getScaleX() - 1)/2;
                            restrictedLeftX = scalePX * layoutOrgininalWidth;
                            restrictedRightX = -restrictedLeftX;


                            if (moveEneable == true && (v.getX() <= restrictedLeftX) && (v.getX() >= restrictedRightX)) {
                                float mX = frameLayout.getX();
                                float move_auxX = (event.getRawX() - prevX);
                                float currentX = (fingerOneX- prevX);
                                float movePointX = (mX + move_auxX);
                                //objectMoveX(movePointX, 0, frameLayout);
                                v.setX(movePointX);

                                prevX = (event.getRawX());
                                Log.v(TAG, "Movimiento en el layout");


                            }


                            //------------------------------

                            scalePY = (v.getScaleY() - 1)/2;
                            restrictedTopY = scalePY * layoutOriginalHeight;
                            restrictedButtonY = -restrictedTopY;

                            if (moveEneable == true && v.getY() <= (restrictedTopY + layoutY) && v.getY() >= (restrictedButtonY + layoutY) ) {
                                float mY = frameLayout.getY();
                                float move_auxY = (event.getRawY() - prevY);
                                float currentX = (fingerOneY - prevY);
                                float movePointY = (mY + move_auxY);
                                //objectMoveY(movePointY, 0, frameLayout);
                                v.setY(movePointY);

                                prevY = event.getRawY();
                                Log.v(TAG, String.valueOf(moveEneable));

                            }

                            break;

                        case MotionEvent.ACTION_UP:

                            if (v.getX() > restrictedLeftX + layoutX){
                                //v.setX(restrictedLeftX-1);
                                objectMoveX(restrictedLeftX , 0, frameLayout);
                            }else{
                                if (v.getX() < restrictedRightX + layoutX){
                                    //v.setX(restrictedRightX + 1);
                                    objectMoveX(restrictedRightX, 0, frameLayout);
                                }
                            }



                            if (v.getY() > restrictedTopY + layoutY){
                                //v.setY(restrictedTopY - 1);
                                objectMoveY(restrictedTopY + layoutY , 0, frameLayout);
                            }else{
                                if (v.getY() < restrictedButtonY + layoutY){
                                    //v.setY(restrictedButtonY + 1);
                                    objectMoveY(restrictedButtonY +layoutY , 0, frameLayout);
                                }

                            }
                            moveEneable = true;
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
