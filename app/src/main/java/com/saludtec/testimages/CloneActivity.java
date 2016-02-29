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


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_clone);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        final ImageView toCloneImage = (ImageView)findViewById(R.id.toCloneImage);
        toCloneImage.setImageResource(R.drawable.landscapes);
        final Bitmap image = ((BitmapDrawable)toCloneImage.getDrawable()).getBitmap();

        image.setHasAlpha(true);

        final Path path= new Path();
        final Path pathMoved = new Path();

        final Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLUE);


        //frameLayout.addView(clonedImage);
        //frameLayout.addView(clonedImage1);




        toCloneImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int id = 0;
                int toCloneImageWidht = toCloneImage.getWidth();
                int toCloneImageHeight = toCloneImage.getHeight();

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

                }for (int i = 0; i < pointerCount; i++) {

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


                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:

                        puntoInicioX = fingerOneX;
                        puntoInicioY = fingerOneY;
                        path.moveTo(fingerOneX, fingerOneY);

                        lowerX = fingerOneX;
                        higherX = fingerOneX;
                        lowerY = fingerOneY;
                        higherY = fingerOneY;

                        imageM = image.copy(Bitmap.Config.ARGB_8888, true);
                        canvas = new Canvas(imageM);


                        break;

                    case MotionEvent.ACTION_MOVE:

                        if((fingerOneY > 0 && fingerOneY < toCloneImageHeight) && (fingerOneX > 0 && fingerOneX < toCloneImageWidht)){

                            PointF pointF = new PointF();
                            pointF.set(fingerOneX, fingerOneY);
                            pathArray.add(pointF);
                            path.lineTo(fingerOneX,fingerOneY);

                            //Encontrar tamaño de la imagen clonada
                            if (fingerOneX > higherX){
                                higherX = fingerOneX;
                            }else{
                                if (fingerOneX < lowerX){
                                    lowerX = fingerOneX;
                                }
                            }

                            if (fingerOneY > higherY){
                                higherY = fingerOneY;
                            }else{
                                if (fingerOneY < lowerY){
                                    lowerY = fingerOneY;
                                }
                            }

                        }

                        canvas.drawPath(path,paint);
                        toCloneImage.setImageBitmap(imageM);


                        break;

                    case MotionEvent.ACTION_UP:

                        toCloneImage.setImageBitmap(image);


                        final Bitmap imageresized = Bitmap.createScaledBitmap(image, toCloneImage.getWidth(), toCloneImage.getHeight(), false);

                        path.lineTo(puntoInicioX, puntoInicioY);

                        sizeX = (int) (higherX - lowerX);
                        sizeY = (int) (higherY - lowerY);


                        float lowerXPixelRatio = lowerX/toCloneImageWidht;
                        float lowerYPixelRatio = lowerY/toCloneImageHeight;


                        int leftXPixel = (int) (imageresized.getWidth() * lowerXPixelRatio);
                        int topYPixel = (int) (imageresized.getHeight() * lowerYPixelRatio);

                        int bitMapWidth = (int) ((sizeX / toCloneImageWidht) * imageresized.getWidth());
                        int bitMapHeight = (int) ((sizeY / toCloneImageHeight) * imageresized.getHeight());



                        pathMoved.moveTo(puntoInicioX - lowerX, puntoInicioY -  lowerY);

                        for(int n=0; n<pathArray.size(); n++){
                            pathMoved.lineTo((pathArray.get(n).x - lowerX), (pathArray.get(n).y - lowerY));
                        }

                        ClonedImage clonedImage = new ClonedImage(getApplicationContext(), bitMapWidth, bitMapHeight
                                , leftXPixel, topYPixel, imageresized, pathMoved);

                        FrameLayout frameLayout = (FrameLayout)findViewById(R.id.CloneContent);
                        frameLayout.addView(clonedImage);
                        path.reset();
                        pathMoved.reset();
                        pathArray = new ArrayList<PointF>();
                        break;
                }


                Log.d(TAG, String.valueOf(fingerOneX) + "    " + String.valueOf(fingerOneY));


                return true;
            }
        });


    }

    protected void onStart() {
        super.onStart();




    }



}
