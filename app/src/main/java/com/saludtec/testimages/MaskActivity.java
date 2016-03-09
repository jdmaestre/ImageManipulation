package com.saludtec.testimages;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
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
import android.widget.ImageView;

public class MaskActivity extends AppCompatActivity {

    DrawView drawView = null;
    float puntoInicioX = 0;
    float puntoInicioY = 0;

    Canvas canvas;
    private String TAG = MaskActivity.class.getSimpleName();


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_mask);
        //getSupportActionBar().hide();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);


        final ImageView imageView = (ImageView)findViewById(R.id.maskImage);
        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.square);
        final Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        canvas = new Canvas(mutableBitmap);
        imageView.setImageBitmap(mutableBitmap);

        final Path path = new Path();
        final Paint clear = new Paint();
        clear.setColor(android.graphics.Color.RED);
        clear.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        clear.setStyle(Paint.Style.FILL);
        path.setFillType(Path.FillType.INVERSE_EVEN_ODD);

        final Paint unclear = new Paint();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Clear canvas", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                final Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                canvas.drawBitmap(mutableBitmap, 0, 0, unclear);

            }
        });


        final MovementsTouchListener movementsTouchListener = new MovementsTouchListener();

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                float mFingerPositionX = event.getX();
                float mFingerPositionY = event.getY();

                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        puntoInicioX = mFingerPositionX;
                        puntoInicioY = mFingerPositionY;
                        path.moveTo(puntoInicioX, puntoInicioY);
                        break;

                    case MotionEvent.ACTION_MOVE:
                        path.lineTo(mFingerPositionX,mFingerPositionY);
                        break;

                    case MotionEvent.ACTION_UP:
                        path.lineTo(puntoInicioX, puntoInicioY);
                        canvas.drawPath(path, clear);
                        imageView.setImageBitmap(mutableBitmap);
                        path.reset();
                        //movementsTouchListener.ClonedImage(imageView);
                        break;
                }

                //Log.d(TAG, String.valueOf(mFingerPositionX) + "   " + String.valueOf(mFingerPositionY));


                return true;
            }
        });
    }



}
