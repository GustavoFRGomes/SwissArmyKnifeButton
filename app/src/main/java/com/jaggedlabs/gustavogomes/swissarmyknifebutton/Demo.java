package com.jaggedlabs.gustavogomes.swissarmyknifebutton;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Demo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        final com.jaggedlabs.gustavogomes.swissarmyknifebutton.SwissArmyKnifeButton button = ((com.jaggedlabs.gustavogomes.swissarmyknifebutton.SwissArmyKnifeButton)this.findViewById(R.id.amazing_button));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.toggleLoading(true);
                SpannableString spannableString = new SpannableString("Another Spannable Right String");
                spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                button.setAuxText(spannableString);
            }
        });

        final com.jaggedlabs.gustavogomes.swissarmyknifebutton.SwissArmyKnifeButton centerButton = ((com.jaggedlabs.gustavogomes.swissarmyknifebutton.SwissArmyKnifeButton)this.findViewById(R.id.amazing_button_2));
        centerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setAuxText(Demo.this.getString(R.string.bold_in_strings).toString());
                button.toggleLoading(button.isClickable());
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
