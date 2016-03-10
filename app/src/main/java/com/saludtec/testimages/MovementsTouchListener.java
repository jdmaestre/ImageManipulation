package com.saludtec.testimages;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
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

    public void CloneAction(final ImageView imageView, final Context context){


        final float[] puntoInicioX = {0};
        final float[] puntoInicioY = {0};

        final float[] fingerOneX = {0};
        final float[] fingerOneY = {0};
        final float[] fingerTowX = {0};
        final float[] fingerTowY = {0};

        final float[] lowerX = {0};
        final float[] lowerY = {0};
        final float[] higherX = {0};
        final float[] higherY = {0};
        final float[] sizeX = {0};
        final float[] sizeY = {0};

        final Bitmap[] imageM = new Bitmap[1];
        final Canvas[] canvas = new Canvas[1];

        final ArrayList<PointF>pathArray = new ArrayList<PointF>();


        imageView.setImageResource(R.drawable.landscapes);
        final Bitmap image = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        image.setHasAlpha(true);

        final Path path= new Path();
        final Path pathMoved = new Path();
        final Path drawPath = new Path();

        final Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(10);

        imageView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int id = 0;
                int toCloneImageWidht = imageView.getWidth();
                int toCloneImageHeight = imageView.getHeight();
                float ratioX = (float)image.getWidth()/(float)imageView.getWidth();
                float ratioY = (float)image.getHeight()/(float)imageView.getHeight();

                int action = event.getActionMasked();
                //Log.v(TAG, actionToString(action));

                int pointerCount = event.getPointerCount();
                for (int i = 0; i < pointerCount; i++) {

                    int pointerIndex = i;
                    id = event.getPointerId(pointerIndex);

                    if (id == 0) {
                        fingerOneX[0] = event.getX(pointerIndex);
                        fingerOneY[0] = event.getY(pointerIndex);


                    }

                    if (id == 1) {
                        fingerTowX[0] = event.getX(pointerIndex);
                        fingerTowY[0] = event.getY(pointerIndex);
                    }

                }for (int i = 0; i < pointerCount; i++) {

                    int pointerIndex = i;
                    id = event.getPointerId(pointerIndex);

                    if (id == 0) {
                        fingerOneX[0] = event.getX(pointerIndex);
                        fingerOneY[0] = event.getY(pointerIndex);


                    }

                    if (id == 1) {
                        fingerTowX[0] = event.getX(pointerIndex);
                        fingerTowY[0] = event.getY(pointerIndex);
                    }

                }


                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:

                        puntoInicioX[0] = fingerOneX[0];
                        puntoInicioY[0] = fingerOneY[0];
                        path.moveTo(fingerOneX[0], fingerOneY[0]);
                        drawPath.moveTo(fingerOneX[0] *ratioX, fingerOneY[0] *ratioY);


                        lowerX[0] = fingerOneX[0];
                        higherX[0] = fingerOneX[0];
                        lowerY[0] = fingerOneY[0];
                        higherY[0] = fingerOneY[0];

                        //imageM = Bitmap.createScaledBitmap(image, toCloneImage.getWidth(), toCloneImage.getHeight(), false);
                        imageM[0] = image.copy(Bitmap.Config.ARGB_8888,true);
                        canvas[0] = new Canvas(imageM[0]);

                        break;

                    case MotionEvent.ACTION_MOVE:

                        if((fingerOneY[0] > 0 && fingerOneY[0] < toCloneImageHeight) && (fingerOneX[0] > 0 && fingerOneX[0] < toCloneImageWidht)){

                            PointF pointF = new PointF();
                            pointF.set(fingerOneX[0], fingerOneY[0]);
                            pathArray.add(pointF);
                            path.lineTo(fingerOneX[0], fingerOneY[0]);
                            drawPath.lineTo(fingerOneX[0] *ratioX, fingerOneY[0] *ratioY);
                            //Log.d(TAG, String.valueOf(fingerOneX*ratioX) + "     " + String.valueOf(fingerOneY*ratioY));

                            //Encontrar tamaño de la imagen clonada
                            if (fingerOneX[0] > higherX[0]){
                                higherX[0] = fingerOneX[0];
                            }else{
                                if (fingerOneX[0] < lowerX[0]){
                                    lowerX[0] = fingerOneX[0];
                                }
                            }

                            if (fingerOneY[0] > higherY[0]){
                                higherY[0] = fingerOneY[0];
                            }else{
                                if (fingerOneY[0] < lowerY[0]){
                                    lowerY[0] = fingerOneY[0];
                                }
                            }

                        }

                        canvas[0].drawPath(drawPath, paint);
                        imageView.setImageBitmap(imageM[0]);


                        break;

                    case MotionEvent.ACTION_UP:

                        try{
                            final Bitmap imageresized = Bitmap.createScaledBitmap(image, imageView.getWidth(),
                                    imageView.getHeight(), false);

                            imageView.setImageBitmap(image);

                            path.lineTo(puntoInicioX[0], puntoInicioY[0]);

                            sizeX[0] = (int) (higherX[0] - lowerX[0]);
                            sizeY[0] = (int) (higherY[0] - lowerY[0]);


                            float lowerXPixelRatio = lowerX[0] /toCloneImageWidht;
                            float lowerYPixelRatio = lowerY[0] /toCloneImageHeight;


                            int leftXPixel = (int) (imageresized.getWidth() * lowerXPixelRatio);
                            int topYPixel = (int) (imageresized.getHeight() * lowerYPixelRatio);

                            int bitMapWidth = (int) ((sizeX[0] / toCloneImageWidht) * imageresized.getWidth());
                            int bitMapHeight = (int) ((sizeY[0] / toCloneImageHeight) * imageresized.getHeight());



                            pathMoved.moveTo(puntoInicioX[0] - lowerX[0], puntoInicioY[0] - lowerY[0]);

                            for(int n=0; n< pathArray.size(); n++){
                                pathMoved.lineTo((pathArray.get(n).x - lowerX[0]), (pathArray.get(n).y - lowerY[0]));
                            }

                            ClonedImage clonedImage = new ClonedImage(context, bitMapWidth, bitMapHeight
                                    , leftXPixel, topYPixel, imageresized, pathMoved);

                            FrameLayout frameLayout = (FrameLayout)imageView.getParent();
                            frameLayout.addView(clonedImage);
                            path.reset();
                            pathMoved.reset();
                            drawPath.reset();;
                            pathArray.clear();
                            break;
                        }catch (Exception e){

                            Log.e(TAG, "Exception caught: Touch event to clone");

                        }

                }


                Log.d(TAG, String.valueOf(fingerOneX[0]) + "    " + String.valueOf(fingerOneY[0]));


                return true;


            }
        });

    }

    public void ZoomAction(final View frameLayout, Context context){


        final float[] scale = {1};
        final float[] scaleLayoutDiagonal = {0};
        final float[] scaleOffset = {0};
        final float[] scale_aux = {0};
        final float[] pixelsOffset = {0};


        final float[] restrictedLeftX = {0};
        final float[] restrictedRightX = {-restrictedLeftX[0]};
        final float[] restrictedTopY = {0};
        final float[] restrictedButtonY = {-restrictedTopY[0]};

        final float[] scalePY = {0};
        final float[] scalePX = {0};

        final float[] layoutX = {0};
        final float[] layoutY = {0};

        final boolean[] getXY = {false};


        final int[] id = {-1};
        final float[] fingerOneX = {0};
        final float[] fingerOneY = {0};
        final float[] fingerTowX = {0};
        final float[] fingerTowY = {0};
        final float[] rawX = {0};
        final float[] rawY = {0};
        final float[] rawX2 = {0};
        final float[] rawY2 = {0};

        final float[] mFingersDist = {0};
        final float[] mPrevFingersDist = {0};


        //Mover variables
        final float[] prevX = {0};
        final float[] prevY = {0};
        final float[] auxRotation = {0};
        final boolean[] moveEneable = {true};


        final float layoutOrgininalWidth = frameLayout.getWidth();
        final float layoutOriginalHeight = frameLayout.getHeight();
        final float layoutDiagonal = (int) Math.sqrt(Math.pow(layoutOrgininalWidth, 2) + Math.pow(layoutOriginalHeight, 2));

       frameLayout.setOnTouchListener(new OnTouchListener() {
           @Override
           public boolean onTouch(View v, MotionEvent event) {

               if (getXY[0] == false){
                   layoutX[0] = frameLayout.getX();
                   layoutY[0] = frameLayout.getY();
                   getXY[0] = true;
               }
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

                   }

                   if (id[0] == 1) {
                       fingerTowX[0] = event.getX(pointerIndex);
                       fingerTowY[0] = event.getY(pointerIndex);

                       //Codigo para obtener RawX & RawY de otros pointers diferentes al 1
                       int location[] = {0,0};
                       v.getLocationOnScreen(location);

                       double angle=Math.toDegrees(Math.atan2(fingerTowY[0], fingerTowX[0]));
                       angle += v.getRotation();

                       final float length= PointF.length(fingerTowX[0], fingerTowY[0]);

                       rawX2[0] =(float)(length*Math.cos(Math.toRadians(angle)))+location[0];
                       rawY2[0] =(float)(length*Math.sin(Math.toRadians(angle)))+location[1];
                   }

               }

               if (event.getPointerCount() > 1){
                   //Eventos multitouch

                   switch (action){
                       case MotionEvent.ACTION_POINTER_DOWN:
                           //Usado para movimiento
                           moveEneable[0] = false;
                           Log.v(TAG, String.valueOf(moveEneable[0]));

                           //Usado para escalar
                           mPrevFingersDist[0] = (float) Math.sqrt(Math.pow(fingerOneX[0] - fingerTowX[0], 2) + Math.pow(fingerOneY[0] - fingerTowY[0], 2));

                           break;

                       case  MotionEvent.ACTION_MOVE:

                           mFingersDist[0] = (float) Math.sqrt(Math.pow(fingerOneX[0] - fingerTowX[0], 2) + Math.pow(fingerOneY[0] - fingerTowY[0], 2));

                           pixelsOffset[0] = mFingersDist[0] - mPrevFingersDist[0];
                           scaleLayoutDiagonal[0] = (layoutDiagonal + (27/10)*(mFingersDist[0] - mPrevFingersDist[0]));
                           scaleOffset[0] = scaleLayoutDiagonal[0] /layoutDiagonal;
                           scale[0] = scaleOffset[0] + scale_aux[0];
                           v.setScaleX(scale[0]);
                           v.setScaleY(scale[0]);


                           break;

                       case MotionEvent.ACTION_POINTER_UP:
                           scale_aux[0] = (scale[0] -1);
                           if (scale[0] < 1){
                               v.setScaleX(1);
                               v.setScaleY(1);
                               scale_aux[0] = 0;
                           }

                           if (v.getX() > restrictedLeftX[0] + layoutX[0]){
                               //v.setX(restrictedLeftX-1);
                               objectMoveX(restrictedLeftX[0], 0, frameLayout);
                           }else{
                               if (v.getX() < restrictedRightX[0] + layoutX[0]){
                                   //v.setX(restrictedRightX + 1);
                                   objectMoveX(restrictedRightX[0], 0, frameLayout);
                               }
                           }



                           if (v.getY() > restrictedTopY[0] + layoutY[0]){
                               //v.setY(restrictedTopY - 1);
                               objectMoveY(restrictedTopY[0] + layoutY[0], 0, frameLayout);
                           }else{
                               if (v.getY() < restrictedButtonY[0] + layoutY[0]){
                                   //v.setY(restrictedButtonY + 1);
                                   objectMoveY(restrictedButtonY[0] + layoutY[0], 0, frameLayout);
                               }

                           }


                           break;
                   }

               }else{
                   //Eventos singletouch

                   switch (action){
                       case MotionEvent.ACTION_DOWN:
                           prevX[0] = event.getRawX();
                           //-----------------
                           prevY[0] = event.getRawY();

                           auxRotation[0] = frameLayout.getRotation();
                           break;

                       case  MotionEvent.ACTION_MOVE:


                           scalePX[0] = (v.getScaleX() - 1)/2;
                           restrictedLeftX[0] = scalePX[0] * layoutOrgininalWidth;
                           restrictedRightX[0] = -restrictedLeftX[0];


                           if (moveEneable[0] == true && (v.getX() <= restrictedLeftX[0]) && (v.getX() >= restrictedRightX[0])) {
                               float mX = frameLayout.getX();
                               float move_auxX = (event.getRawX() - prevX[0]);
                               float currentX = (fingerOneX[0] - prevX[0]);
                               float movePointX = (mX + move_auxX);
                               //objectMoveX(movePointX, 0, frameLayout);
                               v.setX(movePointX);

                               prevX[0] = (event.getRawX());
                               Log.v(TAG, "Movimiento en el layout");


                           }


                           //------------------------------

                           scalePY[0] = (v.getScaleY() - 1)/2;
                           restrictedTopY[0] = scalePY[0] * layoutOriginalHeight;
                           restrictedButtonY[0] = -restrictedTopY[0];

                           if (moveEneable[0] == true && v.getY() <= (restrictedTopY[0] + layoutY[0]) && v.getY() >= (restrictedButtonY[0] + layoutY[0]) ) {
                               float mY = frameLayout.getY();
                               float move_auxY = (event.getRawY() - prevY[0]);
                               float currentX = (fingerOneY[0] - prevY[0]);
                               float movePointY = (mY + move_auxY);
                               //objectMoveY(movePointY, 0, frameLayout);
                               v.setY(movePointY);

                               prevY[0] = event.getRawY();
                               Log.v(TAG, String.valueOf(moveEneable[0]));

                           }

                           break;

                       case MotionEvent.ACTION_UP:

                           if (v.getX() > restrictedLeftX[0] + layoutX[0]){
                               //v.setX(restrictedLeftX-1);
                               objectMoveX(restrictedLeftX[0], 0, frameLayout);
                           }else{
                               if (v.getX() < restrictedRightX[0] + layoutX[0]){
                                   //v.setX(restrictedRightX + 1);
                                   objectMoveX(restrictedRightX[0], 0, frameLayout);
                               }
                           }



                           if (v.getY() > restrictedTopY[0] + layoutY[0]){
                               //v.setY(restrictedTopY - 1);
                               objectMoveY(restrictedTopY[0] + layoutY[0], 0, frameLayout);
                           }else{
                               if (v.getY() < restrictedButtonY[0] + layoutY[0]){
                                   //v.setY(restrictedButtonY + 1);
                                   objectMoveY(restrictedButtonY[0] + layoutY[0], 0, frameLayout);
                               }

                           }
                           moveEneable[0] = true;
                           break;
                   }

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
