package com.jaggedlabs.gustavogomes.swissarmyknifebutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.text.Spannable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by gustavogomes on 01/06/2017.
 */

public class SwissArmyKnifeButton extends android.support.v7.widget.AppCompatButton
        implements Runnable {

    private static final int alphaStepInt = 20;
    private static final int alphaAnimLength = 200;
    private static final int loadingAnimLength = 40;
    private static final int loadingAlphaStep = 10;

    // FOOD 4 THOUGHT: Make the same behaviour for italic <i> tags, but also precedence problem...
    private static final String BOLD_START_TAG = "<b>";
    private static final String BOLD_END_TAG = "</b>";

    private static float LOADING_CIRCLE_RADIUS = 10;
    private static float LOADING_CIRCLE_PADDING = 10;

    private boolean hasSingleText;
    private CharSequence auxText;
    private String text = this.getText().toString();

    private boolean performLoadingCalled = false;
    private boolean idleState = true;
    private boolean dismissLoadingCalled = false;
    private Date currentLoadingIterationDate;
    private Date auxDate;

    // Loading Overlay
    private boolean isLoading = false;
    private int[] loadingColors = {
            this.adjustAlphaValue(this.getCurrentTextColor(), 1f),
            this.adjustAlphaValue(this.getCurrentTextColor(), 0.7f),
            this.adjustAlphaValue(this.getCurrentTextColor(), 0.4f)
    };

    private int originalColor;

    public SwissArmyKnifeButton(Context context) {
        super(context);
        this.initialize(context, null, 0);
    }

    public SwissArmyKnifeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initialize(context, attrs, 0);
    }

    public SwissArmyKnifeButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initialize(context, attrs, defStyleAttr);
    }

    public void initialize(Context context, AttributeSet attrs, int defStyleAttr) {
        super.setAllCaps(false);
        // Make the logic for the custom attrs here.

        this.setWillNotDraw(true);
        if (attrs != null) {
            TypedArray customAttrs = context.getTheme()
                    .obtainStyledAttributes(attrs, R.styleable.SwissArmyKnifeButton, defStyleAttr, defStyleAttr);

            this.text = this.getText().toString();

            int possbileAuxTextId = customAttrs.getResourceId(R.styleable.SwissArmyKnifeButton_auxText, -1);
            if (possbileAuxTextId != -1) {
                this.auxText = this.getContext().getText(possbileAuxTextId);
            }
            else {
                this.auxText = customAttrs.getString(R.styleable.SwissArmyKnifeButton_auxText);
            }

            this.hasSingleText = this.auxText == null;
            this.originalColor = this.getCurrentTextColor();

            this.loadingColors[0] = this.adjustAlphaValue(this.originalColor, .33f);
            this.loadingColors[1] = this.adjustAlphaValue(this.originalColor, .22f);
            this.loadingColors[2] = this.adjustAlphaValue(this.originalColor, .11f);

            this.currentLoadingIterationDate = new Date();

            this.setButtonText();
            this.invalidate();
        }
    }

    public void setAllCaps(boolean allCaps) {
        String auxString = this.getText().toString();
        auxString = (allCaps ? auxString.toUpperCase() : auxString);
        this.setText(auxString);
    }

    private void setButtonText() {
        if (this.hasSingleText) {
            this.setText(this.text);
        } else {
            super.setAllCaps(false);

            this.setGravity(Gravity.START);
            this.setText(this.text);
        }
    }

    @Override
    public void setTextColor(int textColor)
    {
        super.setTextColor(textColor);
    }

    public void setLoadingColor(int loadingColor)
    {
        this.originalColor = loadingColor;
        this.invalidate();
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

    private int getCurrentAlpha(int currentColor)
    {
        return Color.alpha(currentColor);
    }

    public void toggleLoading(boolean on)
    {
        if (on)
        {
            this.performLoading();
        }
        else
        {
            this.disableLoading();
        }
    }

    private void performLoading()
    {
        // Method to fade the text out and show a loading animation
        if (this.isClickable())
        {
            this.setClickable(false);
        }

        this.setLoadingState(true);
        this.invalidate();
    }


    private void disableLoading()
    {
        if (!this.isClickable())
        {
            this.setClickable(true);
        }

        this.setLoadingState(false);
        this.invalidate();
    }

    public void setAuxText(String newAuxText)
    {
        this.auxText = newAuxText;
        this.invalidate(); // Force to redraw this.
    }

    public void setAuxText(CharSequence charSequence)
    {
        this.auxText = charSequence;
        this.invalidate();
    }

    public void setAuxText(int stringId)
    {
        this.auxText = this.getContext().getString(stringId);
        this.invalidate();
    }

    @Override
    public void run() {
        Log.d("SWISS_BUTTON", new java.util.Date().toString());
//        if (!this.idleState)
//        {
//            this.invalidate();
//        }
//
//        Date timeFrame = new Date();
//
//        if (this.isLoading
//                && this.lastLoadingTimeFrame.hasTimeFramePassed(timeFrame)
//                && this.getCurrentAlpha(this.getCurrentTextColor()) == 0)
//        {
//            this.lastLoadingTimeFrame.updateTimeFrame(timeFrame);
            this.invalidate();
//        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (!this.idleState) {
            if (this.performLoadingCalled) {
                this.performFadeOut();
                this.isLoading = true;
            } else if (this.dismissLoadingCalled) {
                this.performFadeIn();
                this.isLoading = false;
            }
            this.postDelayed(this, SwissArmyKnifeButton.alphaAnimLength);
        }

        if (this.isLoading && this.getCurrentAlpha(this.getCurrentTextColor()) == 0)
        {
//            if (this.lastLoadingTimeFrame == null || this.lastLoadingTimeFrame != null && !this.lastLoadingTimeFrame.getHasExecuted())
//                this.lastLoadingTimeFrame = new LoadingTimeFrame(new Date(), SwissArmyKnifeButton.alphaAnimLength);

            this.drawLoadingOverlay(canvas);
            this.auxDate = new Date();

            if (this.currentLoadingIterationDate.getTime() + SwissArmyKnifeButton.loadingAnimLength <= auxDate.getTime()) {
                this.postDelayed(this, SwissArmyKnifeButton.loadingAnimLength);
                this.currentLoadingIterationDate = auxDate;
            }
        }
        else // Reset loading Colours.
        {
            this.loadingColors[0] = this.adjustAlphaValue(this.originalColor, .33f);
            this.loadingColors[1] = this.adjustAlphaValue(this.originalColor, .22f);
            this.loadingColors[2] = this.adjustAlphaValue(this.originalColor, .11f);

//            this.lastLoadingTimeFrame = null;
        }

        if (!this.hasSingleText) {
            if (this.auxText instanceof Spannable) {
                this.drawLeftText(canvas);
            } else {
                if (this.auxText.toString().contains(SwissArmyKnifeButton.BOLD_START_TAG))
                {
                    this.drawCompositeText(canvas);
                }
                else {
                    this.drawLeftText(canvas);
                }
            }
        }

        super.onDraw(canvas);
    }

    private void drawLoadingOverlay(Canvas canvas)
    {
        int paddingTop = this.getPaddingTop();
        float loadingHeight = (this.getY() + this.getHeight() + paddingTop / 2) / 2 - (SwissArmyKnifeButton.LOADING_CIRCLE_RADIUS / 2) / 2;

        Paint loadingCirclesPaint = new Paint();
        loadingCirclesPaint.setColor(this.originalColor);
        loadingCirclesPaint.setAntiAlias(true);
        loadingCirclesPaint.setStyle(Paint.Style.FILL);

        float circleDiameter = this.getDpFromPixels(SwissArmyKnifeButton.LOADING_CIRCLE_RADIUS);

        float[] circleHorizontalPosition = {
                (float) (((this.getX() + this.getWidth()) / 2) - (1.5 * circleDiameter) - this.getLoadingCircleRadius()),
                ((this.getX() + this.getWidth()) / 2),
                (float) (((this.getX() + this.getWidth()) / 2) + (1.5 * circleDiameter) + this.getLoadingCircleRadius())
        };


        loadingCirclesPaint.setColor(this.loadingColors[0]);
        canvas.drawCircle(circleHorizontalPosition[0], loadingHeight, this.getLoadingCircleRadius(), loadingCirclesPaint);

        loadingCirclesPaint.setColor(this.loadingColors[1]);
        canvas.drawCircle(circleHorizontalPosition[1], loadingHeight, this.getLoadingCircleRadius(), loadingCirclesPaint);

        loadingCirclesPaint.setColor(this.loadingColors[2]);
        canvas.drawCircle(circleHorizontalPosition[2], loadingHeight, this.getLoadingCircleRadius(), loadingCirclesPaint);

        this.updateLoadingColors();
    }

    private void drawLeftText(Canvas canvas)
    {
        int paddingTop = this.getPaddingTop();
        int paddingEnd = this.getViewPaddingEnd();

        Paint textPaint = this.getTextPaint();

        canvas.drawText(this.auxText, 0, this.auxText.length(),  (this.getX() + this.getWidth() - paddingEnd), (this.getY() + this.getHeight() + paddingTop / 2) / 2, textPaint);
    }

    private void drawCompositeText(Canvas canvas)
    {
        Paint textPaint = this.getTextPaint();
        Paint boldTextPaint = this.getTextPaint(true);

        if (this.auxText.toString().contains(SwissArmyKnifeButton.BOLD_START_TAG)
                && this.auxText.toString().contains(SwissArmyKnifeButton.BOLD_END_TAG))
        {
            // It has at least one <b> and </b>
            // Know if the <b> comes before the </b>
            ArrayList<Integer> boldStartTags = this.getBoldSubstringPositions(this.auxText.toString(), SwissArmyKnifeButton.BOLD_START_TAG);
            ArrayList<Integer> boldEndTags = this.getBoldSubstringPositions(this.auxText.toString(), SwissArmyKnifeButton.BOLD_END_TAG);

            ArrayList<Pair<Integer, Integer>> boldTags = this.getTagPairs(boldStartTags, boldEndTags);

            ArrayList<BoldText> splittedStringWithBolds = this.splitStringsForBoldSeparation(this.auxText.toString(), boldTags);
            float horizontalOffset = 0.0f;
            Paint currentPaint;
            String currentBoldString;

            for (int i=splittedStringWithBolds.size() - 1; i >= 0; i--)
            {
                currentBoldString = splittedStringWithBolds.get(i).text;
                currentPaint = (splittedStringWithBolds.get(i).isBold ? boldTextPaint : textPaint);
                this.drawPreceedingText(canvas, horizontalOffset, currentPaint, currentBoldString);
                horizontalOffset += this.getTextMeasurement(
                        currentPaint,
                        currentBoldString);
            }
        }
    }

    private void drawPreceedingText(Canvas canvas, float startOffset, Paint textPaint, String stringToDraw)
    {
        int paddingEnd = this.getViewPaddingEnd();
        int paddingTop = this.getPaddingTop();

        canvas.drawText(stringToDraw, 0, stringToDraw.length(),
                (this.getX() + this.getWidth() - paddingEnd - startOffset),
                (this.getY() + this.getHeight() + paddingTop / 2) / 2,
                textPaint);
    }

    private ArrayList<BoldText> splitStringsForBoldSeparation(String originalString, ArrayList<Pair<Integer, Integer>> boldTags)
    {
        //FIXME: Instead of submitting the originalString we are using the auxText within the object. Maybe choose one approach and stick with it.
        ArrayList<BoldText> splitedString = new ArrayList<>();
        String currentBoldString;

        final int boldEndTagLength = SwissArmyKnifeButton.BOLD_END_TAG.length();

        for (int i=0; i < boldTags.size(); i++)
        {

            if (i == 0 && boldTags.get(i).first != 0
                    || boldTags.get(i-1).second + boldEndTagLength != boldTags.get(i).first)
            {
                if (i == 0)
                {
                    currentBoldString = this.auxText.toString().substring(0, boldTags.get(i).first);
                }
                else
                {
                    currentBoldString = this.auxText.toString().substring(boldTags.get(i-1).second + boldEndTagLength, boldTags.get(i).first);
                }
                splitedString.add(new BoldText(currentBoldString, false));
            }

            currentBoldString = this.auxText.toString().substring(boldTags.get(i).first + boldEndTagLength,
                    boldTags.get(i).second);
            splitedString.add(new BoldText(currentBoldString, true));


            if (i == boldTags.size() - 1 && boldTags.get(i).second + SwissArmyKnifeButton.BOLD_END_TAG.length() < this.auxText.length())
            {
                currentBoldString = this.auxText.toString().substring(boldTags.get(i).second + boldEndTagLength,
                        this.auxText.length() - 1);
                splitedString.add(new BoldText(currentBoldString, false));
            }
        }

        return splitedString;
    }

    private float getTextMeasurement(Paint textPaint, String string)
    {
        return textPaint.measureText(string);
    }

    private ArrayList<Pair<Integer, Integer>> getTagPairs(ArrayList<Integer> beginTags, ArrayList<Integer> endTags)
    {
        ArrayList<Integer> beginTagPositionsCopy = new ArrayList<>(beginTags);
        ArrayList<Integer> endTagPositionsCopy = new ArrayList<>(endTags);

        ArrayList<Pair<Integer, Integer>> listOfPairingTags = new ArrayList<>();

        Pair<Integer, Integer> tempPair;
        int endTagPosition;
        // First pair them with the next end tag
        for (int i=0; i < beginTags.size(); i++)
        {
            if (endTagPositionsCopy.size() == 0)
            {
                break;
            }
            else
            {
                for (int j=0; j < endTagPositionsCopy.size(); j++)
                {
                    endTagPosition = endTagPositionsCopy.get(j);
                    if (beginTagPositionsCopy.get(i) < endTagPosition)
                    {
                        tempPair = new Pair<>(beginTagPositionsCopy.get(i), endTagPosition);
                        listOfPairingTags.add(tempPair);
                        endTagPositionsCopy.remove(j);
                        break;
                    }
                }

            }
        }

        return listOfPairingTags;
    }

    // Helper function to get the positions of a matching string.
    private ArrayList<Integer> getBoldSubstringPositions(String potentiallyBoldString, String matchingString)
    {
        int lastIndex = 0;
        ArrayList<Integer> listOfMatchingPositions = new ArrayList<>();

        while(lastIndex != -1){

            lastIndex = potentiallyBoldString.indexOf(matchingString,lastIndex);

            if(lastIndex != -1){
                listOfMatchingPositions.add(lastIndex);
                lastIndex += matchingString.length();
            }
        }

        return listOfMatchingPositions;
    }

    private void updateLoadingColors()
    {
        this.loadingColors[0] = this.getLoadingColor(this.loadingColors[0]);
        this.loadingColors[1] = this.getLoadingColor(this.loadingColors[1]);
        this.loadingColors[2] = this.getLoadingColor(this.loadingColors[2]);
    }

    private int getLoadingColor(int currentColor)
    {
//        int currentAlpha = this.getCurrentAlpha(currentColor);

//        return this.adjustAlphaValue(this.getCurrentTextColor(), this.calculateAlphaPercentage(currentAlpha + this.loadingAlphaStep));

        int currentAlpha = Color.alpha(currentColor);
        int currentRed = Color.red(currentColor);
        int currentGreen = Color.green(currentColor);
        int currentBlue = Color.blue(currentColor);

        return Color.argb(currentAlpha + SwissArmyKnifeButton.loadingAlphaStep, currentRed, currentGreen, currentBlue);
    }

    private Paint getTextPaint()
    {
        return this.getTextPaint(false);
    }

    private Paint getTextPaint(boolean applyBold)
    {
        Paint textPaint = new Paint();
        textPaint.setColor(this.getCurrentTextColor());
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.RIGHT);
        textPaint.setTextSize(this.getTextSize());
        textPaint.setTypeface(!applyBold ? this.getTypeface() : Typeface.create(this.getTypeface(), Typeface.BOLD));
        textPaint.setAntiAlias(true);

        return textPaint;
    }

    private boolean isLayoutRTL()
    {
        return ViewCompat.getLayoutDirection(this.getRootView()) == ViewCompat.LAYOUT_DIRECTION_RTL;
    }

    private int getViewPaddingEnd()
    {

        return (this.isLayoutRTL() ? this.getPaddingLeft() : this.getPaddingRight());
    }

    private void performFadeOut()
    {
        if (this.getCurrentAlpha(this.getCurrentTextColor()) <= 20) {
            // Make alpha at full, so text will be hidden
            this.setTextColor(this.adjustAlphaValue(this.getCurrentTextColor(), 0f));
            this.setStateIdle();
        } else {
            int alpha = this.getCurrentAlpha(this.getCurrentTextColor());
            alpha -= SwissArmyKnifeButton.alphaStepInt; // alpha at 0% is 255
            this.setTextColor(this.adjustAlphaValue(this.getCurrentTextColor(), this.calculateAlphaPercentage(alpha)));
        }
    }

    private void performFadeIn()
    {
        if (this.getCurrentAlpha(this.getCurrentTextColor()) >= 240) {
            // Make alpha at 0, so text will be fully visible
            this.setTextColor(this.adjustAlphaValue(this.getCurrentTextColor(), 1f));
            this.setStateIdle();
        } else {
            int alpha = this.getCurrentAlpha(this.getCurrentTextColor());
            alpha += SwissArmyKnifeButton.alphaStepInt; // alpha at 0% is 255
            this.setTextColor(this.adjustAlphaValue(this.getCurrentTextColor(), this.calculateAlphaPercentage(alpha)));
        }
    }

//    private float calculateAlphaPercentageFromColor(int currentColorInt) { return (((float) Color.alpha(currentColorInt)) * 100 / 255) / 100; }
    private float calculateAlphaPercentage(int alpha)
    {
        return (((float)alpha) * 100 / 255) / 100;
    }

    private void setLoadingState(boolean wasPerformLoadingCalled)
    {
        this.isLoading = true;
        this.performLoadingCalled = wasPerformLoadingCalled;
        this.idleState = false;
        this.dismissLoadingCalled = !wasPerformLoadingCalled;
    }

    private void setStateIdle()
    {
        this.performLoadingCalled = false;
        this.dismissLoadingCalled = false;
        this.idleState = true;
        this.isLoading = false;
    }

    private float getDpFromPixels(float pixels)
    {
        float screenDensity = this.getScreenDensity();
        return pixels * screenDensity;
    }

    private float getLoadingCircleRadius()
    {
        return this.getDpFromPixels(SwissArmyKnifeButton.LOADING_CIRCLE_RADIUS);
    }

    private float getScreenDensity()
    {
        return getResources().getDisplayMetrics().density;
    }

    private class BoldText
    {
        public String text;
        public boolean isBold;

        public BoldText (String text, boolean isBold)
        {
            this.text = text;
            this.isBold = isBold;
        }
    }
}
