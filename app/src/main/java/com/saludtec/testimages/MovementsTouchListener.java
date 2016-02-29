package com.saludtec.testimages;

import android.animation.ObjectAnimator;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

/**
 * Created by saludtec on 25/02/16.
 */
public class MovementsTouchListener {

    public void RotateResizeMove(final ImageView imageView){

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

        imageView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

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

                            auxRotation[0] = imageView.getRotation();
                            break;

                        case  MotionEvent.ACTION_MOVE:

                            if (moveEneable[0] == true) {
                                float mX = imageView.getX();
                                float move_auxX = (event.getRawX() - prevX[0]);
                                float currentX = (fingerOneX[0] - prevX[0]);
                                float movePointX = (mX + move_auxX);
                                objectMoveX(movePointX, 0, imageView);

                                prevX[0] = (event.getRawX());


                            }

                            //------------------------------
                            if (moveEneable[0] == true) {
                                float mY = imageView.getY();
                                float move_auxY = (event.getRawY() - prevY[0]);
                                float currentX = (fingerOneY[0] - prevY[0]);
                                float movePointY = (mY + move_auxY);
                                objectMoveY(movePointY, 0, imageView);
                                prevY[0] = event.getRawY();

                            }

                            break;

                        case MotionEvent.ACTION_UP:
                            break;
                    }

                }

                return true;
            }
        });

    }

    private void objectMoveX(float translationX, long duration, ImageView imageView){

        ObjectAnimator move = ObjectAnimator.ofFloat(imageView, "x", translationX);
        move.setDuration(duration);
        move.start();
    }

    private void objectMoveY(float translationY, long duration, ImageView imageView){

        ObjectAnimator move = ObjectAnimator.ofFloat(imageView, "y", translationY);
        move.setDuration(duration);
        move.start();
    }

}
