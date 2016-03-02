package com.saludtec.testimages;

import android.animation.ObjectAnimator;
import android.app.usage.UsageEvents;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class FotoguiasActivity extends AppCompatActivity {

    private String TAG = FotoguiasActivity.class.getSimpleName();

    float fingerOneX = 0;
    float fingerOneY = 0;
    float fingerTowX = 0;
    float rawX = 0;
    float rawY = 0;
    float rawX2 = 0;
    float rawY2= 0;
    float fingerTowY = 0;
    boolean moveEneable = true;
    float prevX = 0;
    float prevY = 0;
    float currentX = 0;


    float angle_aux = 0;
    float currentAngle_aux = 0;
    float auxRotation = 0;

    double mCurrAngle = 0;
    double mPrevAngle = 0;
    float centerY = 0;
    float centerX = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fotoguias);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);





    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //Here you can get the size!

        final FrameLayout frameLayout = (FrameLayout)findViewById(R.id.fotoguiasLayout);
        ImageView imageView = (ImageView)findViewById(R.id.backgroudFotoguias);
        imageView.setImageResource(R.drawable.square);

        Bitmap linea = BitmapFactory.decodeResource(getResources(), R.drawable.linea_verde);
        Bitmap lineaEscalada = Bitmap.createScaledBitmap(linea,linea.getWidth()*3,frameLayout.getWidth()*2,false);


        final ImageView guia1Image = new ImageView(getApplicationContext());
        final ImageView guia2Image = new ImageView(getApplicationContext());
        final ImageView guia3Image = new ImageView(getApplicationContext());

        guia2Image.setBackgroundColor(Color.BLACK);


        AbsoluteLayout.LayoutParams params = new AbsoluteLayout.LayoutParams(lineaEscalada.getWidth(),
                frameLayout.getHeight()*2,-frameLayout.getWidth()/2,-frameLayout.getHeight()/2);

        int amountOffscreen = (int)(guia2Image.getHeight() * 1.5); /* or whatever */
        boolean offscreen = true;/* true or false */


        int xOffset = (offscreen) ? amountOffscreen : 0;

        //params.setMargins(0, frameLayout.getHeight(), 0, frameLayout.getHeight());

        guia1Image.setLayoutParams(params);
        guia2Image.setLayoutParams(params);
        guia3Image.setLayoutParams(params);

        guia2Image.setY(-frameLayout.getHeight()/2);

        guia1Image.setImageBitmap(lineaEscalada);
        guia2Image.setImageBitmap(lineaEscalada);
        guia3Image.setImageBitmap(lineaEscalada);

        final int t1 =  frameLayout.getWidth()* 1 /4;
        final int t2 =  frameLayout.getWidth()* 2 /4;
        final int t3 =  frameLayout.getWidth()* 3 /4;

        frameLayout.addView(guia1Image);
        frameLayout.addView(guia2Image);
        frameLayout.addView(guia3Image);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


                guia1Image.setX(t2); guia1Image.setRotation(0);
                guia2Image.setX(t1); guia2Image.setRotation(0);
                guia3Image.setX(t3); guia3Image.setRotation(0);

            }
        });

        View.OnTouchListener touchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int id = 0;

                final int action = event.getActionMasked();


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

                        int location[] = {0,0};
                        v.getLocationOnScreen(location);

                        fingerTowX = event.getX(pointerIndex);
                        fingerTowY = event.getY(pointerIndex);

                        double angle=Math.toDegrees(Math.atan2(fingerTowY, fingerTowX));
                        angle += v.getRotation();

                        final float length= PointF.length(fingerTowX, fingerTowY);

                        rawX2=(float)(length*Math.cos(Math.toRadians(angle)))+location[0];
                        rawY2=(float)(length*Math.sin(Math.toRadians(angle)))+location[1];

                    }

                }

                if (event.getPointerCount() > 1){
                    //Eventos multitouch



                    final int xc = v.getWidth() / 2;
                    final int yc = v.getHeight() / 2;

                    switch (action){
                        case MotionEvent.ACTION_POINTER_DOWN:
                            //Usado para movimiento
                            moveEneable = false;

                            //Rotacion
                            centerX = v.getX() +((View)v.getParent()).getX() + (v.getWidth()/2);
                            centerY = v.getY() +((View)v.getParent()).getY() + (v.getHeight()/2);

                            mCurrAngle = Math.toDegrees(Math.atan2(rawY - rawY2, rawX - rawX2));
                            angle_aux = (float) mCurrAngle;



                            break;

                        case  MotionEvent.ACTION_MOVE:

                            //mCurrAngle = Math.toDegrees(Math.atan2(rawY - centerY, rawX - centerX));
                            mCurrAngle = Math.toDegrees(Math.atan2(rawY - rawY2, rawX - rawX2));


                            currentAngle_aux = (float) (mCurrAngle - angle_aux);

                            guia1Image.setRotation((float) (currentAngle_aux + mPrevAngle));
                            guia2Image.setRotation((float) (currentAngle_aux + mPrevAngle));
                            guia3Image.setRotation((float) (currentAngle_aux + mPrevAngle));


                            //Log.d(TAG, String.valueOf(mCurrAngle) + "   Rawx: " + String.valueOf(rawX)
                            //        + "     Rawy: " + String.valueOf(rawY) + "   Rawx2: " + String.valueOf(rawX2)
                            //        + "     Rawy2: " + String.valueOf(rawY2) );

                            break;

                        case MotionEvent.ACTION_POINTER_UP:

                            mPrevAngle = currentAngle_aux + mPrevAngle;
                            break;
                    }

                }else{
                    //Eventos singletouch

                    switch (action){
                        case MotionEvent.ACTION_DOWN:
                            prevX = rawX;
                            //-----------------
                            prevY = rawY;

                            auxRotation = v.getRotation();

                            break;

                        case  MotionEvent.ACTION_MOVE:

                            if (moveEneable == true) {
                                float mX = v.getX();
                                float move_auxX = (event.getRawX() - prevX);
                                currentX = (fingerOneX - prevX);
                                float movePointX = (mX + move_auxX);
                                objectMoveX(movePointX, 0, v);
                                prevX = (event.getRawX());


                            }

                            //------------------------------

                            break;

                        case MotionEvent.ACTION_UP:
                            moveEneable = true;
                            break;
                    }

                }

                //Log.d(TAG, String.valueOf(v.getX()) + "   " + String.valueOf(event.getX()));
                return true;
            }
        };

        guia1Image.setOnTouchListener(touchListener);
        guia2Image.setOnTouchListener(touchListener);
        guia3Image.setOnTouchListener(touchListener);

    }


    private void objectMoveX(float translationX, long duration, View imageView){

        ObjectAnimator move = ObjectAnimator.ofFloat(imageView, "x", translationX);
        move.setDuration(duration);
        move.start();
    }

    private void objectMoveY(float translationY, long duration, View imageView){

        ObjectAnimator move = ObjectAnimator.ofFloat(imageView, "y", translationY);
        move.setDuration(duration);
        move.start();
    }

    private void objectRotate(float fromDegrees, float toDegrees, int pivX, int pivY, long duration,View iv){

        ObjectAnimator rot = ObjectAnimator.ofFloat(iv, "rotation", fromDegrees, toDegrees);
        rot.setDuration(duration);
        rot.start();

    }

    private void animRotate(double fromDegrees, double toDegrees, int pivX, int pivY, long durationMillis,  View iv) {
        final RotateAnimation rotate = new RotateAnimation((float) fromDegrees, (float) toDegrees,
                 0.5f,
                 0.5f);


        rotate.setDuration(durationMillis);
        rotate.setFillEnabled(false);
        rotate.setFillAfter(true);
        rotate.reset();
        iv.startAnimation(rotate);


    }


}
