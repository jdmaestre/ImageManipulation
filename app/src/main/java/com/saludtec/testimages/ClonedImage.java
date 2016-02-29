package com.saludtec.testimages;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by saludtec on 25/02/16.
 */
public class ClonedImage extends ImageView{

    private Canvas canvas;
    private Bitmap mutableBitmap;
    private Paint paint;
    MovementsTouchListener mMovementsTouchListener = new MovementsTouchListener();

    public ClonedImage(Context context, float sizeX,float sizeY, Bitmap bitmap, Path path) {
        super(context);



        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT
                , LinearLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER);

        this.setLayoutParams(params);


        mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888,true);
        mutableBitmap.setHasAlpha(true);
        canvas = new Canvas(mutableBitmap);
        //this.setImageBitmap(mutableBitmap);

        paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.FILL);
        //paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        //path.setFillType(Path.FillType.INVERSE_EVEN_ODD);

        canvas.drawPath(path, paint);
        this.setImageBitmap(mutableBitmap);
        this.setBackgroundColor(Color.TRANSPARENT);
        //this.getLayoutParams().width = (int) sizeX;
        //this.getLayoutParams().height = (int) sizeY;


        mMovementsTouchListener.RotateResizeMove(this);
    }


}
