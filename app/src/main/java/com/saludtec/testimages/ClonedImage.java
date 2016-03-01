package com.saludtec.testimages;

import android.app.ActionBar;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by saludtec on 25/02/16.
 */
public class ClonedImage extends FrameLayout{

    private Canvas canvas;
    private Bitmap mutableBitmap;
    private ImageView iv_delete = new ImageView(getContext());
    private ImageView clonedImage = new ImageView(getContext());
    private Paint paint;
    MovementsTouchListener mMovementsTouchListener = new MovementsTouchListener();

    public ClonedImage(Context context, int sizeX,int sizeY, int puntoInicioX, int puntoInicioY, Bitmap bitmap, Path path) {
        super(context);

        LayoutParams iv_delete_params =
                new LayoutParams(
                        convertDpToPixel(30, getContext()),
                        convertDpToPixel(30, getContext())
                );
        iv_delete_params.gravity = Gravity.TOP | Gravity.RIGHT;
        this.iv_delete.setImageResource(R.drawable.remove);



        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT
                , FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER);

        this.setLayoutParams(params);
        //this.getLayoutParams().width = sizeX;
        //this.getLayoutParams().height = sizeY;

        mutableBitmap = Bitmap.createBitmap(sizeX,sizeY, Bitmap.Config.ARGB_8888);

        int[] pixels = new int[(int) (sizeX * sizeY)];

        bitmap.getPixels(pixels, 0, sizeX, puntoInicioX, puntoInicioY, sizeX, sizeY);
        mutableBitmap.setPixels(pixels, 0, sizeX, 0, 0, sizeX, sizeY);


        mutableBitmap.setHasAlpha(true);
        canvas = new Canvas(mutableBitmap);
        //this.setImageBitmap(mutableBitmap);

        paint = new Paint();
        paint.setColor(Color.TRANSPARENT);
        paint.setStyle(Paint.Style.FILL);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        path.setFillType(Path.FillType.INVERSE_EVEN_ODD);

        canvas.drawPath(path, paint);
        clonedImage.setImageBitmap(mutableBitmap);
        this.clonedImage.setBackgroundColor(Color.WHITE);
        this.clonedImage.getBackground().setAlpha(25);
        this.addView(clonedImage);
        this.addView(iv_delete, iv_delete_params);


        mMovementsTouchListener.RotateResizeMove(clonedImage);

        iv_delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Cerrar View", Toast.LENGTH_LONG).show();
                ((FrameLayout)iv_delete.getParent().getParent()).removeView((FrameLayout)iv_delete.getParent());

            }
        });

    }

    private static int convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return (int)px;
    }


}
