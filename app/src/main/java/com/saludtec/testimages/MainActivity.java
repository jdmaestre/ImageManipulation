package com.saludtec.testimages;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
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
    float prevXmove = -1;

    boolean moveEneable = true;

    private int mHeingh = 0;

    private float mLastScaleFactor = 0;
    private float mLastScaleFactorSmall = 0;
    private float mTouchY;
    private static final float MIN_SCALE = 0.5f;
    private static final float MAX_SCALE = 4f;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = new Intent(this, MTControllerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //startActivity(intent);

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


                //fromdegrees[0] = todegrees[0];
                //todegrees[0] = todegrees[0] - 90;

                fromX[0] = toX[0];
                toX[0] = (float) (toX[0] + 0.1);
                fromY[0] = toY[0];
                toY[0] = (float) (toY[0] + 0.1);
                rotate(0, 90, imF);

                double x = imF.getHeight();
                double y = imF.getWidth();
                double z = Math.sqrt ((x*x) + (y*y));
                int lado = (int) Math.round(z);

                imF.getLayoutParams().height = lado + 1;
                imF.getLayoutParams().width = lado +1;



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


                    }

                    if (id == 1) {
                        fingerTowX = event.getX(pointerIndex);
                        fingerTowY = event.getY(pointerIndex);
                    }

                }

                //Si hay dos dedos en pantalla
                if (event.getPointerCount() > 1) {

                    //Log.d(TAG, String.valueOf(id));
                    //Log.d(TAG, actionToString(action));
                    final int xc = imF.getWidth() / 2;
                    final int yc = imF.getHeight() / 2;


                    switch (action) {
                        case MotionEvent.ACTION_POINTER_DOWN:

                            //Usado para la rotacion
                            mCurrAngle[0] = Math.toDegrees(Math.atan2(fingerTowX - xc, yc - fingerTowY));
                            angle_aux = (float) (mCurrAngle[0]);

                            pivX = (int) (xc);
                            pivY = (int) (yc);

                            //Usado para el zoom
                            zoomAuxX = fingerTowX;
                            zoomAuxY = fingerTowY;

                            //Usado para scale
                            mLastScaleFactor = 0;
                            mTouchY = fingerTowY;
                            mHeingh = imF.getHeight();

                            //Usado para el movimiento
                            moveEneable = false;
                            break;

                        case MotionEvent.ACTION_MOVE:

                            //Usado para la rotacion


                            mCurrAngle[0] = Math.toDegrees(Math.atan2(fingerTowX - xc, yc - fingerTowY));
                            currentAngle_aux = (float) (mCurrAngle[0] - angle_aux);
                            //Matrix matrix = new Matrix();

                            //matrix.setRotate((float) (currentAngle_aux + mPrevAngle[0]), (xc), (yc)); //rotate it
                            //matrix.setTranslate((ivSize/2-imageWidth/2),ivSize/2 -imageHeight/2);

                            //Log.v(TAG,String.valueOf(currentAngle_aux)+mPrevAngle[0]);


                            //imF.setImageMatrix(matrix);



                            //animate(currentAngle_aux, currentAngle_aux, pivX, pivY, 0, imF);
                            objectRotate(currentAngle_aux, (float) (currentAngle_aux + mPrevAngle[0]), pivX, pivY, 0, imF);



                            //Usado para Scale

                            if (fingerTowY > mTouchY) {
                                imF.getLayoutParams().height = (int) (mHeingh + (Math.abs(fingerTowY-mTouchY)));
                            } else {
                                imF.getLayoutParams().height = (int) (mHeingh - (Math.abs(fingerTowY-mTouchY)));
                            }
                            imF.requestLayout();
                            break;

                        case MotionEvent.ACTION_POINTER_UP:

                            //Usado para la rotacion
                            //animate(0, 0, 0, 0, 0, imF);
                            //objectRotate(currentAngle_aux, (float) (currentAngle_aux + mPrevAngle[0]), pivX, pivY, 0, imF);
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

                            prevX = event.getRawX();
                            //-----------------
                            prevY = event.getRawY();

                            auxRotation = imF.getRotation();

                            Log.d(TAG, "rawX: " +String.valueOf(prevX) );


                            break;

                        case MotionEvent.ACTION_MOVE:


                            if ( moveEneable == true) {
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

                            //Log.d(TAG, "mX: " +String.valueOf(mX) + " mY: " + String.valueOf(mY)
                             //       + " F1X: " + String.valueOf(fingerOneX) );
                            Log.d(TAG, "rawX: " +String.valueOf(prevX) );

                            //animationMoveX(movePointX,movePointX,movePointY,movePointY,0,imF);
                            //imF.setX(movePointX);
                            //imF.setY(movePointY);


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






        //imF.getLayoutParams().height = lado +1;
        //imF.getLayoutParams().width = lado + 1;
        //imF.setImageBitmap(bitmap);
        //imF.setScaleType(ImageView.ScaleType.MATRIX);



        //RectF drawableRect = new RectF(0, 0,ivSize , ivSize );
        //RectF viewRect = new RectF(0, 0, imF.getWidth(), imF.getHeight());
        //matrix.setRectToRect(drawableRect, viewRect, Matrix.ScaleToFit.CENTER);
        //matrix.setTranslate((ivSize/2-imageWidth/2),ivSize/2 -imageHeight/2);
       // imF.setImageMatrix(matrix);













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
