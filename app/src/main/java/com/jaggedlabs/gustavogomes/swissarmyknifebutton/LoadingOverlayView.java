package com.jaggedlabs.gustavogomes.swissarmyknifebutton;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;

import org.w3c.dom.Attr;

import java.text.AttributedCharacterIterator;
import java.util.Date;

/**
 * Created by gustavogomes on 13/06/2017.
 */

public class LoadingOverlayView extends View implements Runnable {

    private static final int loadingAnimLength = 40;
    private static final int loadingAlphaStep = 10;

    private boolean isLoading;
    private Date currentLoadingIterationDate;

    private int[] loadingColors = {
            this.adjustAlphaValue(this.originalColor, 1f),
            this.adjustAlphaValue(this.originalColor, 0.7f),
            this.adjustAlphaValue(this.originalColor, 0.4f)
    };

    private static float LOADING_CIRCLE_RADIUS = 10;
    private static float LOADING_CIRCLE_PADDING = 10;
    private static float loadingCircleRadiusScalingFactor = 0.3f;

    private int originalColor;

    public LoadingOverlayView(Context context) {
        super(context);

        this.initialize(context, null);
    }

    public LoadingOverlayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        this.initialize(context, attrs);
    }

    public LoadingOverlayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.initialize(context, attrs);
    }

    private void initialize(Context context, @Nullable AttributeSet attrs)
    {
        this.originalColor = Color.WHITE;

        this.loadingColors[0] = this.adjustAlphaValue(this.originalColor, .33f);
        this.loadingColors[1] = this.adjustAlphaValue(this.originalColor, .22f);
        this.loadingColors[2] = this.adjustAlphaValue(this.originalColor, .11f);

        this.currentLoadingIterationDate = new Date();
    }

    public void toggleLoading(boolean turnOn)
    {
        this.isLoading = turnOn;

        this.invalidate();
    }

    @Override
    public void run() {
        this.invalidate();
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        if (this.isLoading)
        {
            this.drawLoadingOverlay(canvas);
            Date auxDate = new Date();

            if (this.currentLoadingIterationDate.getTime() + LoadingOverlayView.loadingAnimLength <= auxDate.getTime()) {
                this.postDelayed(this, LoadingOverlayView.loadingAnimLength);
                this.currentLoadingIterationDate = auxDate;
            }
        }
        else // Reset loading Colours.
        {
            this.loadingColors[0] = this.adjustAlphaValue(this.originalColor, .33f);
            this.loadingColors[1] = this.adjustAlphaValue(this.originalColor, .22f);
            this.loadingColors[2] = this.adjustAlphaValue(this.originalColor, .11f);
        }
    }

    private void drawLoadingOverlay(Canvas canvas)
    {
        int paddingTop = this.getPaddingTop();
        float loadingHeight = (this.getHeight() + this.pxToDp(paddingTop) / 2) / 2 - (LoadingOverlayView.LOADING_CIRCLE_RADIUS / 2) / 2;

        Paint loadingCirclesPaint = new Paint();
        loadingCirclesPaint.setColor(this.originalColor);
        loadingCirclesPaint.setAntiAlias(true);
        loadingCirclesPaint.setStyle(Paint.Style.FILL);

        float circleRadius = this.pxToDp(this.getScaledRadius(canvas));
        float circleDiameter =  circleRadius * 2;

        float[] circleHorizontalPosition = {
                (float) (((this.getWidth()) / 2) - (1.5 * circleDiameter)),
                ((this.getWidth()) / 2),
                (float) (((this.getWidth()) / 2) + (1.5 * circleDiameter))
        };

        loadingCirclesPaint.setColor(this.loadingColors[0]);
        canvas.drawCircle(circleHorizontalPosition[0], loadingHeight, circleRadius, loadingCirclesPaint);

        loadingCirclesPaint.setColor(this.loadingColors[1]);
        canvas.drawCircle(circleHorizontalPosition[1], loadingHeight, circleRadius, loadingCirclesPaint);

        loadingCirclesPaint.setColor(this.loadingColors[2]);
        canvas.drawCircle(circleHorizontalPosition[2], loadingHeight, circleRadius, loadingCirclesPaint);

        this.updateLoadingColors();
    }

    private void updateLoadingColors()
    {
        this.loadingColors[0] = this.getLoadingColor(this.loadingColors[0]);
        this.loadingColors[1] = this.getLoadingColor(this.loadingColors[1]);
        this.loadingColors[2] = this.getLoadingColor(this.loadingColors[2]);
    }

    private int getLoadingColor(int currentColor)
    {
        int currentAlpha = Color.alpha(currentColor);
        int currentRed = Color.red(currentColor);
        int currentGreen = Color.green(currentColor);
        int currentBlue = Color.blue(currentColor);

        return Color.argb(currentAlpha + LoadingOverlayView.loadingAlphaStep, currentRed, currentGreen, currentBlue);
    }

    private int adjustAlphaValue(int currentColor, float alphaFactor)
    {
        // HACK: if the current Alpha is 0 then we need to set it to
        float currentAlpha = 255 * alphaFactor;

        int alpha = Math.round(currentAlpha);
        int red = Color.red(currentColor);
        int green = Color.green(currentColor);
        int blue = Color.blue(currentColor);

        return Color.argb(alpha, red, green, blue);
    }

    private int getScaledRadius(Canvas canvas)
    {
        return (int) ((canvas.getWidth() > canvas.getHeight() ?
                canvas.getHeight() : canvas.getWidth()) * LoadingOverlayView.loadingCircleRadiusScalingFactor);
    }

    public int pxToDp(int px)
    {
        return (int) (px / this.getScreenDensity());
    }

    private float getScreenDensity()
    {
        return getResources().getDisplayMetrics().density;
    }
}
