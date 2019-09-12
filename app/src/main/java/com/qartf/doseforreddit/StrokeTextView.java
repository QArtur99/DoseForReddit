package com.qartf.doseforreddit;


import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;

import androidx.core.content.ContextCompat;

public class StrokeTextView extends androidx.appcompat.widget.AppCompatTextView {

    public StrokeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        final ColorStateList textColor = getTextColors();

        TextPaint paint = this.getPaint();

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeMiter(10);
        this.setTextColor(ContextCompat.getColor(getContext(), R.color.colorBlack));
        paint.setStrokeWidth(10);

        super.onDraw(canvas);
        paint.setStyle(Paint.Style.FILL);

        setTextColor(textColor);
        super.onDraw(canvas);
    }

}
