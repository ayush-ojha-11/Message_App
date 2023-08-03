package com.as.mymessage.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class UtilityFunctions {
    public static Bitmap generateCircleBitmap(char letter, int color) {
        int size = 100; // Replace with your desired size in pixels
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint);

        paint.setColor(Color.WHITE);
        paint.setTextSize(40); // Replace with your desired text size
        paint.setTextAlign(Paint.Align.CENTER);

        float xPos = canvas.getWidth() / 2f;
        float yPos = (canvas.getHeight() / 2f) - ((paint.descent() + paint.ascent()) / 2f);
        canvas.drawText(String.valueOf(letter).toUpperCase(), xPos, yPos, paint);

        return bitmap;
    }

}
