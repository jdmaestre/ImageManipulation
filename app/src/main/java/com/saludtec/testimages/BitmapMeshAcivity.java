package com.saludtec.testimages;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class BitmapMeshAcivity extends AppCompatActivity {

    private static final int WIDTH = 20;
    private static final int HEIGHT = 20;
    private static final int COUNT = (WIDTH + 1) * (HEIGHT + 1);

    private final float[] mVerts = new float[COUNT * 2];
    private final float[] mOrig = new float[COUNT * 2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitmap_mesh_acivity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);









        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();



            }
        });







    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //Here you can get the size!

        final ImageView mImageView = (ImageView)findViewById(R.id.imageMesh);
        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.landscapes);
        final Bitmap bitmapMutable = bitmap.copy(Bitmap.Config.ARGB_8888,true);
        final Canvas canvas = new Canvas(bitmapMutable);
        mImageView.setImageBitmap(bitmapMutable);

        float w = bitmapMutable.getWidth();
        float h = bitmapMutable.getHeight();
        // construct our mesh
        int index = 0;
        for (int y = 0; y <= HEIGHT; y++) {
            float fy = h * y / HEIGHT;
            for (int x = 0; x <= WIDTH; x++) {
                float fx = w * x / WIDTH;
                setXY(mVerts, index, fx, fy);
                setXY(mOrig, index, fx, fy);
                index += 1;
            }
        }

        final Matrix mMatrix = new Matrix();
        final Matrix mInverse = new Matrix();

        mMatrix.setTranslate(10, 10);
        mMatrix.invert(mInverse);


        float width = bitmapMutable.getWidth()/21;
        float height = bitmapMutable.getHeight()/21;
        float x=0;
        float y;
        int n = 0;
        int i =0;
        final float[] ver = new float[441*2];
        int p = 0;


        for (n=0; n<21;n++){
            x = x + width;
            y = 0;
            for (i= 0 ; i<21; i++){
                y = y + height;
                ver[p] = x;
                p = p+1;
                ver[p] = y;
                p=p+1;
            }
        }

        final int[] mLastWarpX = {-9999}; // don't match a touch coordinate
        final int[] mLastWarpY = new int[1];

        mImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                canvas.concat(mMatrix);
                canvas.drawBitmapMesh(bitmapMutable, WIDTH, HEIGHT, mVerts, 0, null, 0,
                        null);

                float[] pt = { event.getX(), event.getY() };
                mInverse.mapPoints(pt);

                int x = (int) pt[0];
                int y = (int) pt[1];
                if (mLastWarpX[0] != x || mLastWarpY[0] != y) {
                    mLastWarpX[0] = x;
                    mLastWarpY[0] = y;
                    warp(pt[0], pt[1]);
                    v.requestLayout();
                }


                return true;
            }
        });



    }

    private static void setXY (float[] array, int index, float x, float y) {
        array[index * 2 + 0] = x;
        array[index * 2 + 1] = y;
    }

    private void warp(float cx, float cy) {
        final float K = 10000;
        float[] src = mOrig;
        float[] dst = mVerts;
        for (int i = 0; i < COUNT * 2; i += 2) {
            float x = src[i + 0];
            float y = src[i + 1];
            float dx = cx - x;
            float dy = cy - y;
            float dd = dx * dx + dy * dy;
            float d = (float) Math.sqrt(dd);
            float pull = K / (dd + 0.000001f);

            pull /= (d + 0.000001f);
            // android.util.Log.d("skia", "index " + i + " dist=" + d +
            // " pull=" + pull);

            if (pull >= 1) {
                dst[i + 0] = cx;
                dst[i + 1] = cy;
            } else {
                dst[i + 0] = x + dx * pull;
                dst[i + 1] = y + dy * pull;
            }
        }

    }


}
