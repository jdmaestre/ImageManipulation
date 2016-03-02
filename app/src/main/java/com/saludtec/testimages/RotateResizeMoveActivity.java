package com.saludtec.testimages;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class RotateResizeMoveActivity extends AppCompatActivity {

    private String TAG = RotateResizeMoveActivity.class.getSimpleName();
    float angle_aux = 0;
    float currentAngle_aux = 0;
    float auxRotation = 0;

    float zoomAuxX = 0;
    float zoomAuxY = 0;

    float prevX = 0;
    float currentX = 0;
    float prevY = 0;
    float currentY = 0;

    int pivX= 0;
    int pivY=0;

    float fingerOneX = -1;
    float fingerOneY = -1;
    float fingerTowX = -1;
    float fingerTowY = -1;
    float rawX = 0;
    float rawY = 0;
    float rawX2 = 0;
    float rawY2 = 0;
    float prevXmove = -1;

    boolean moveEneable = true;

    private int mHeingh = 0;

    private float mLastScaleFactor = 0;
    private float mLastScaleFactorSmall = 0;
    private float mFingersDist = 0;
    private float mPrevFingersDist = 0;
    private static final float MIN_SCALE = 0.5f;
    private static final float MAX_SCALE = 4f;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = new Intent(this, ZoomActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        final ImageView imB = (ImageView) findViewById(R.id.imageView_back);
        final ImageView imF = (ImageView) findViewById(R.id.imageView_front);
        final Matrix matrix = new Matrix();
        final int[] fromdegrees = {0};
        final int[] todegrees = {90};

        final double[] mCurrAngle = {0};
        final double[] mPrevAngle = {0};

        final float[] fromX = {1};
        final float[] toX = {1};
        final float[] fromY = {1};
        final float[] toY = {1};

        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.chelsea);
        double x = bitmap.getHeight();
        final float imageWidth = (float) x;
        double y = bitmap.getWidth();
        final float imageHeight = (float) y;
        double z = Math.sqrt ((x*x) + (y*y));
        final float ivSize = (float) z;
        int lado = (int) Math.round(z);





        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                fromX[0] = toX[0];
                toX[0] = (float) (toX[0] + 0.1);
                fromY[0] = toY[0];
                toY[0] = (float) (toY[0] + 0.1);
                //rotate(0, 90, imF);

                double x = imF.getHeight();
                double y = imF.getWidth();
                double z = Math.sqrt((x * x) + (y * y));
                int lado = (int) Math.round(z);

                imF.getLayoutParams().height = lado + 1;
                imF.getLayoutParams().width = lado + 1;


            }
        });

        imF.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                final int index = event.getActionIndex();
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
                        fingerTowX = event.getX(pointerIndex);
                        fingerTowY = event.getY(pointerIndex);

                        //Codigo para obtener RawX & RawY de otros pointer diferentes al 1
                        int location[] = {0,0};
                        v.getLocationOnScreen(location);

                        double angle=Math.toDegrees(Math.atan2(fingerTowY, fingerTowX));
                        angle += v.getRotation();

                        final float length= PointF.length(fingerTowX, fingerTowY);

                        rawX2=(float)(length*Math.cos(Math.toRadians(angle)))+location[0];
                        rawY2=(float)(length*Math.sin(Math.toRadians(angle)))+location[1];
                    }

                }

                //Si hay dos dedos en pantalla
                if (event.getPointerCount() > 1) {


                    switch (action) {
                        case MotionEvent.ACTION_POINTER_DOWN:

                            //Usado para la rotacion
                            mCurrAngle[0] = Math.toDegrees(Math.atan2(rawY - rawY2, rawX - rawX2));
                            angle_aux = (float) (mCurrAngle[0]);

                            //Usado para el zoom
                            zoomAuxX = fingerTowX;
                            zoomAuxY = fingerTowY;

                            //Usado para scale
                            mPrevFingersDist = (float) Math.sqrt(Math.pow(rawX - rawX2, 2) + Math.pow(rawY- rawY2, 2));
                            mHeingh = imF.getHeight();

                            //Usado para el movimiento
                            moveEneable = false;
                            break;

                        case MotionEvent.ACTION_MOVE:

                            //Usado para la rotacion

                            //Formula angulo en un punt teniendo como pivote el otro punto Math.toDegrees(Math.atan2(Y - Y2, X - X2));
                            mCurrAngle[0] = Math.toDegrees(Math.atan2(rawY - rawY2, rawX - rawX2));
                            currentAngle_aux = (float) (mCurrAngle[0] - angle_aux);
                            v.setRotation((float) (currentAngle_aux + mPrevAngle[0]));

                            //Usado para Scale
                            //Se usa la distancia entre los dedos para aumentar o dismminuir el tamaño
                            //Formula distancia entre dos puntos Math.sqrt(Math.pow(X - 2, 2) + Math.pow(Y- 2, 2));
                            mFingersDist = (float) Math.sqrt(Math.pow(rawX - rawX2, 2) + Math.pow(rawY- rawY2, 2));

                            if (mFingersDist > mPrevFingersDist) {
                                imF.getLayoutParams().height = (int) (mHeingh + (Math.abs(mPrevFingersDist - mFingersDist)));
                            } else {
                                imF.getLayoutParams().height = (int) (mHeingh - (Math.abs(mPrevFingersDist - mFingersDist)));
                            }
                            imF.requestLayout();
                            break;

                        case MotionEvent.ACTION_POINTER_UP:

                            //Usado para la rotacion
                            mPrevAngle[0] = currentAngle_aux + mPrevAngle[0];
                            break;

                    }

                }
                //Si hay un dedo en pantalla
                else {


                    float mX = 0;
                    float move_auxX = 0;
                    float movePointX = 0;
                    //--------------------
                    float mY = 0;
                    float move_auxY = 0;
                    float movePointY = 0;

                    Log.v(TAG, actionToString(action));


                    switch (action) {

                        case MotionEvent.ACTION_DOWN:

                            prevX = rawX;
                            //-----------------
                            prevY = rawY;
                            break;

                        case MotionEvent.ACTION_MOVE:


                            if (moveEneable == true) {
                                mX = imF.getX();
                                move_auxX = (event.getRawX() - prevX);
                                currentX = (fingerOneX - prevX);
                                movePointX = (mX + move_auxX);
                                objectMoveX(movePointX, 0, imF);
                                prevX = (event.getRawX());


                            }

                            //------------------------------
                            if (moveEneable == true) {
                                mY = imF.getY();
                                move_auxY = (event.getRawY() - prevY);
                                currentX = (fingerOneY - prevY);
                                movePointY = (mY + move_auxY);
                                objectMoveY(movePointY, 0, imF);
                                prevY = event.getRawY();

                            }

                            break;

                        case MotionEvent.ACTION_UP:
                            moveEneable = true;
                            break;
                    }


                }

                return true;
            }
        });


        Picasso.with(getApplicationContext())
                .load(R.drawable.landscapes)
                .centerCrop().fit()
                .into(imB);


        Picasso.with(getApplicationContext())
                .load(R.drawable.square).centerCrop()
                .resize(500, 500)
                .into(imF);



    }

    @Override
    public void onResume() {
        super.onResume();




    }


        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void rotate(float fromdegrees, float todegrees, ImageView iv) {
        final RotateAnimation rotateAnim = new RotateAnimation(fromdegrees, todegrees,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        rotateAnim.setDuration(2011);
        rotateAnim.setFillAfter(true);
        iv.startAnimation(rotateAnim);
    }

    private void animate(double fromDegrees, double toDegrees, int pivX, int pivY, long durationMillis,  ImageView iv) {
        final RotateAnimation rotate = new RotateAnimation((float) fromDegrees, (float) toDegrees,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);


        rotate.setDuration(durationMillis);
        rotate.setFillEnabled(false);
        rotate.setFillAfter(true);
        rotate.reset();
        iv.startAnimation(rotate);


    }

    private void objectRotate(float fromDegrees, float toDegrees, int pivX, int pivY, long duration,ImageView iv){

        ObjectAnimator rot = ObjectAnimator.ofFloat(iv, "rotation", fromDegrees, toDegrees);
        rot.setDuration(duration);
        rot.start();

    }

    private void animationMoveX(float fromX,float toX,float fromY, float toY, long duration, ImageView imageView){

        TranslateAnimation translateAnimation = new TranslateAnimation(fromX, toX, fromY, toY);
        translateAnimation.setDuration(duration);
        imageView.startAnimation(translateAnimation);
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
