package com.jaggedlabs.gustavogomes.swissarmyknifebutton;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Demo extends AppCompatActivity implements View.OnClickListener{

    private boolean toggleButton = true;
    SwissArmyKnifeButton redCenterButton;
    SwissArmyKnifeButton togglingButton;

    LoadingOverlayView loadingOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        ButterKnife.bind(this, this);

        this.loadingOverlay = ((com.jaggedlabs.gustavogomes.swissarmyknifebutton.LoadingOverlayView)
                this.findViewById(R.id.loading_overlay));

        this.togglingButton = ((com.jaggedlabs.gustavogomes.swissarmyknifebutton.SwissArmyKnifeButton)
                this.findViewById(R.id.new_game_button));
        this.togglingButton.setOnClickListener(this);

        this.redCenterButton = ((com.jaggedlabs.gustavogomes.swissarmyknifebutton.SwissArmyKnifeButton)
                this.findViewById(R.id.amazing_button_2));
        this.redCenterButton.setOnClickListener(this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == this.redCenterButton.getId())
        {
            this.loadingOverlay.toggleLoading(this.toggleButton);
            this.togglingButton.toggleLoading(this.toggleButton);
            this.toggleButton = !toggleButton;
        }
        else if (view.getId() == this.togglingButton.getId())
        {
            this.togglingButton.toggleLoading(true);
            SpannableString spannableString = new SpannableString("Another Spannable Right String");
            spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            this.togglingButton.setAuxText(spannableString);
        }
    }

    @OnClick(R.id.new_game_button)
    public void newGameClicked()
    {
        this.togglingButton.toggleLoading(!toggleButton);
    }
}
