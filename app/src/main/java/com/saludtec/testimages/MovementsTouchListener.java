package com.saludtec.testimages;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by saludtec on 25/02/16.
 */
public class MovementsTouchListener {

    private String TAG = MovementsTouchListener.class.getSimpleName();


    public void ClonedImage(final View imageView, final ArrayList<View> viewArrayList, final Context context){

        final int[] id = {-1};
        final float[] fingerOneX = {0};
        final float[] fingerOneY = {0};
        final float[] fingerTowX = {0};
        final float[] fingerTowY = {0};
        final boolean[] trhowEvent = {false};

        //Mover variables
        final float[] prevX = {0};
        final float[] prevY = {0};
        final float[] auxRotation = {0};
        final float[] rawX = {0};
        final float[] rawY = {0};
        final float[] rawX2 = {0};
        final float[] rawY2 = {0};
        final boolean[] moveEneable = {true};

        //rotar variables
        final float[] mCurrAngle = {0};
        final float[] angle_aux = {0};
        final float[] currentAngle_aux = {0};
        final float[] mPrevAngle = {0};

        //scale variables
        final float[] mPrevFingersDist = {0};
        final float[] mWidth = {0};
        final float[] mHeingh = {0};
        final float[] mFingersDist = {0};

        //double click variables
        final long[] lastPressTime = {0};

        //final FrameLayout frameLayout = (FrameLayout) imageView.getParent();

        imageView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int action = event.getActionMasked();

                //Para conseguir la posicion de los dedos
                int pointerCount = event.getPointerCount();
                for (int i = 0; i < pointerCount; i++) {

                    int pointerIndex = i;
                    id[0] = event.getPointerId(pointerIndex);

                    if (id[0] == 0) {
                        fingerOneX[0] = event.getX(pointerIndex);
                        fingerOneY[0] = event.getY(pointerIndex);

                        rawX[0] = event.getRawX();
                        rawY[0] = event.getRawY();

                        //Eviar touch a otra vista


                        Log.v(TAG, String.valueOf(rawY[0]) + " " + String.valueOf(v.getY()));

                    }

                    if (id[0] == 1) {
                        fingerTowX[0] = event.getX(pointerIndex);
                        fingerTowY[0] = event.getY(pointerIndex);

                        //Codigo para obtener RawX & RawY de otros pointer diferentes al 1
                        int location[] = {0,0};
                        v.getLocationOnScreen(location);

                        double angle=Math.toDegrees(Math.atan2(fingerTowY[0], fingerTowX[0]));
                        angle += v.getRotation();

                        final float length= PointF.length(fingerTowX[0], fingerTowY[0]);

                        rawX2[0] =(float)(length*Math.cos(Math.toRadians(angle)))+location[0];
                        rawY2[0] =(float)(length*Math.sin(Math.toRadians(angle)))+location[1];

                    }

                }


                //Double tap event
                final long DOUBLE_PRESS_INTERVAL = 350; // in millis
                // If double click...
                long pressTime = event.getDownTime();
                Log.v(TAG, String.valueOf(pressTime));

                if (pressTime - lastPressTime[0] <= DOUBLE_PRESS_INTERVAL && action == MotionEvent.ACTION_DOWN) {


                    if (trhowEvent[0] == true){
                        trhowEvent[0] = false;
                        //Muestra lo q no es la imagen
                        for (int n=0; n<viewArrayList.size() ; n++){
                            viewArrayList.get(n).setVisibility(View.VISIBLE);
                        }

                        for (int n=0; n<((FrameLayout)v).getChildCount()-1; n++){
                            (((FrameLayout)v).getChildAt(n)).getBackground().setAlpha(25);
                        }


                    }else{
                        trhowEvent[0] = true;
                        //Esconde lo q no es la image
                        for (int n=0; n<viewArrayList.size() ; n++){
                            viewArrayList.get(n).setVisibility(View.INVISIBLE);
                        }

                        for (int n=0; n<((FrameLayout)v).getChildCount()-1; n++){
                            (((FrameLayout)v).getChildAt(n)).getBackground().setAlpha(0);
                        }
                    }


                    Toast.makeText(context, "Double Click Event", Toast.LENGTH_SHORT).show();

                }
                lastPressTime[0] = pressTime;



                if (trhowEvent[0] == false){

                    if (event.getPointerCount() > 1){
                        //Eventos multitouch

                        switch (action){
                            case MotionEvent.ACTION_POINTER_DOWN:

                                //Usado para movimiento
                                moveEneable[0] = false;

                                //Usado para la rotacion
                                mCurrAngle[0] = (float) Math.toDegrees(Math.atan2(rawY[0] - rawY2[0], rawX[0] - rawX2[0]));
                                angle_aux[0] = (float) (mCurrAngle[0]);

                                //Usado para scale
                                mPrevFingersDist[0] = (float) Math.sqrt(Math.pow(rawX[0] - rawX2[0], 2) + Math.pow(rawY[0] - rawY2[0], 2));
                                mHeingh[0] = v.getHeight();
                                mWidth[0] = v.getWidth();


                                break;

                            case  MotionEvent.ACTION_MOVE:

                                //Usado para la rotacion

                                //Formula angulo en un punt teniendo como pivote el otro punto Math.toDegrees(Math.atan2(Y - Y2, X - X2));
                                mCurrAngle[0] = (float) Math.toDegrees(Math.atan2(rawY[0] - rawY2[0], rawX[0] - rawX2[0]));
                                currentAngle_aux[0] = (float) (mCurrAngle[0] - angle_aux[0]);
                                v.setRotation((currentAngle_aux[0] + mPrevAngle[0]));

                                //Usado para Scale
                                //Se usa la distancia entre los dedos para aumentar o dismminuir el tamaño
                                //Formula distancia entre dos puntos Math.sqrt(Math.pow(X - 2, 2) + Math.pow(Y- 2, 2));
                                mFingersDist[0] = (float) Math.sqrt(Math.pow(rawX[0] - rawX2[0], 2) + Math.pow(rawY[0]- rawY2[0], 2));

                                if (mFingersDist[0] > mPrevFingersDist[0]) {
                                    v.getLayoutParams().height = (int) (mHeingh[0] + (Math.abs(mPrevFingersDist[0] - mFingersDist[0])));
                                    v.getLayoutParams().width = (int) (mWidth[0] + (Math.abs(mPrevFingersDist[0] - mFingersDist[0])));
                                } else {
                                    v.getLayoutParams().height = (int) (mHeingh[0] - (Math.abs(mPrevFingersDist[0] - mFingersDist[0])));
                                    v.getLayoutParams().width = (int) (mWidth[0] - (Math.abs(mPrevFingersDist[0] - mFingersDist[0])));
                                }
                                v.requestLayout();


                                break;

                            case MotionEvent.ACTION_POINTER_UP:

                                //Usado para la rotacion
                                mPrevAngle[0] = currentAngle_aux[0] + mPrevAngle[0];
                                break;
                        }

                    }else{
                        //Eventos singletouch

                        switch (action){
                            case MotionEvent.ACTION_DOWN:
                                prevX[0] = event.getRawX();
                                //-----------------
                                prevY[0] = event.getRawY();


                                break;

                            case  MotionEvent.ACTION_MOVE:

                                if (moveEneable[0] == true) {
                                    float mX = v.getX();
                                    float move_auxX = (event.getRawX() - prevX[0]);
                                    float currentX = (fingerOneX[0] - prevX[0]);
                                    float movePointX = (mX + move_auxX);
                                    objectMoveX(movePointX, 0, v);

                                    prevX[0] = (event.getRawX());


                                }

                                //------------------------------
                                if (moveEneable[0] == true) {
                                    float mY = v.getY();
                                    float move_auxY = (event.getRawY() - prevY[0]);
                                    float currentX = (fingerOneY[0] - prevY[0]);
                                    float movePointY = (mY + move_auxY);
                                    objectMoveY(movePointY, 0, v);
                                    prevY[0] = event.getRawY();

                                }

                                break;

                            case MotionEvent.ACTION_UP:
                                moveEneable[0] = true;
                                break;
                        }

                    }

                    return true;

                }else{

                   return true;
                }


            }
        });

    }

    public void Implant(View v, final Context context){

        final boolean[] swichMovimiento = {true};
        final boolean[] swichRotacion = {false};

        final int[] id = {-1};
        final float[] fingerOneX = {0};
        final float[] fingerOneY = {0};
        final float[] fingerTowX = {0};
        final float[] fingerTowY = {0};

        //Mover variables
        final float[] prevX = {0};
        final float[] prevY = {0};
        final float[] auxRotation = {0};
        final float[] rawX = {0};
        final float[] rawY = {0};
        final float[] rawX2 = {0};
        final float[] rawY2 = {0};

        //rotar variables
        final float[] mCurrAngle = {0};
        final float[] angle_aux = {0};
        final float[] currentAngle_aux = {0};
        final float[] mPrevAngle = {0};
        final float[] xc = {0};
        final float[] yc = {0};
        final long[] eventDownTime = {0};




        v.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int action = event.getActionMasked();

                int location[] = {0,0};
                v.getLocationOnScreen(location);

                //Para conseguir la posicion de los dedos
                int pointerCount = event.getPointerCount();
                for (int i = 0; i < pointerCount; i++) {

                    int pointerIndex = i;
                    id[0] = event.getPointerId(pointerIndex);

                    if (id[0] == 0) {
                        fingerOneX[0] = event.getX(pointerIndex);
                        fingerOneY[0] = event.getY(pointerIndex);

                        rawX[0] = event.getRawX();
                        rawY[0] = event.getRawY();

                        //Eviar touch a otra vista


                        Log.v(TAG, String.valueOf(rawX[0]) + ":" + String.valueOf(rawY[0]));

                    }

                    if (id[0] == 1) {
                        fingerTowX[0] = event.getX(pointerIndex);
                        fingerTowY[0] = event.getY(pointerIndex);

                        //Codigo para obtener RawX & RawY de otros pointer diferentes al 1


                        double angle=Math.toDegrees(Math.atan2(fingerTowY[0], fingerTowX[0]));
                        angle += v.getRotation();

                        final float length= PointF.length(fingerTowX[0], fingerTowY[0]);

                        rawX2[0] =(float)(length*Math.cos(Math.toRadians(angle)))+location[0];
                        rawY2[0] =(float)(length*Math.sin(Math.toRadians(angle)))+location[1];

                    }



                }



                switch (action){
                    case (MotionEvent.ACTION_DOWN):

                        eventDownTime[0] = System.currentTimeMillis();

                        if (swichMovimiento[0] == true){

                            prevX[0] = event.getRawX();
                            //-----------------
                            prevY[0] = event.getRawY();

                        }

                        if (swichRotacion[0] == true){

                            xc[0] =  v.getX() + ((View)v.getParent()).getX() + v.getWidth()/2;
                            yc[0] =   v.getY() + ((View)v.getParent()).getY() +  + v.getHeight()/2;

                            mCurrAngle[0] = (float) Math.toDegrees(Math.atan2(rawY[0] - yc[0], rawX[0] -xc[0]));
                            angle_aux[0] = (float) (mCurrAngle[0]);

                        }

                        break;

                    case (MotionEvent.ACTION_MOVE):

                        if (swichMovimiento[0] == true){
                                float mX = v.getX();
                                float move_auxX = (event.getRawX() - prevX[0]);
                                float movePointX = (mX + move_auxX);
                                objectMoveX(movePointX, 0, v);

                                prevX[0] = (event.getRawX());


                            //------------------------------

                                float mY = v.getY();
                                float move_auxY = (event.getRawY() - prevY[0]);
                                float movePointY = (mY + move_auxY);
                                objectMoveY(movePointY, 0, v);

                                prevY[0] = event.getRawY();

                        }

                        if (swichRotacion[0] == true){

                            //Usado para la rotacion

                            //Formula angulo en un punt teniendo como pivote el otro punto Math.toDegrees(Math.atan2(Y - Y2, X - X2));
                            mCurrAngle[0] = (float) Math.toDegrees(Math.atan2(rawY[0] - yc[0], rawX[0] -xc[0]));
                            currentAngle_aux[0] = (float) (mCurrAngle[0] - angle_aux[0]);
                            v.setRotation((currentAngle_aux[0] + mPrevAngle[0]));

                            Log.v(TAG, String.valueOf(xc[0]) + " " + String.valueOf(yc[0]));

                        }
                        break;

                    case (MotionEvent.ACTION_UP):


                        long eventUpTime = System.currentTimeMillis();

                        if (swichMovimiento[0] == true){

                        }

                        if (swichRotacion[0] == true){

                            //Usado para la rotacion
                            mPrevAngle[0] = currentAngle_aux[0] + mPrevAngle[0];

                        }




                        if (eventUpTime - eventDownTime[0] < 100){

                            if (swichMovimiento[0] == true){

                                swichMovimiento[0] = false;
                                swichRotacion[0] = true;
                            }else{
                                if (swichRotacion[0] == true){

                                    swichMovimiento[0] = false;
                                    swichRotacion[0] = false;
                                }else{
                                    swichMovimiento[0] = true;
                                }
                            }



                        }



                        break;
                }

                return true;
            }
        });

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



}
