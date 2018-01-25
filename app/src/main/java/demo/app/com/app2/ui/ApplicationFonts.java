package demo.app.com.app2.ui;

import android.app.Application;
import android.graphics.Typeface;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by root on 11/1/18.
 */

public class ApplicationFonts extends Application {

    private Typeface regularFont;
    private Typeface robotFont;
    private Typeface LatoLightFont;
    private Typeface LatoRegularFont;

    public void setRegularWebFontTextView(TextView textView) {
        if (textView != null) {

            textView.setTypeface(getRegularWebFont());
        }
    }

    public void setRegularWebFontEditText(EditText editText) {
        if (editText != null) {

            editText.setTypeface(getRegularWebFont());
        }
    }

    public void setRobotTextView(TextView textView) {
        if (textView != null) {

            textView.setTypeface(getRobotoFont());
        }
    }

    public void setRobotEditText(EditText editText) {
        if (editText != null) {

            editText.setTypeface(getRobotoFont());
        }
    }

    public void setLatoLightTextView(TextView textView) {
        if (textView != null) {

            textView.setTypeface(getLatoLightFont());
        }
    }

    public void setLatoLightEditText(EditText editText) {
        if (editText != null) {

            editText.setTypeface(getLatoLightFont());
        }
    }

    public void setLatoRegularTextView(TextView textView) {
        if (textView != null) {

            textView.setTypeface(getLatoRegularFont());
        }
    }

    public void setLatoRegularEditText(EditText editText) {
        if (editText != null) {

            editText.setTypeface(getLatoRegularFont());
        }
    }

    private Typeface getRegularWebFont() {
        if(regularFont == null) {
            regularFont = Typeface.createFromAsset(getAssets(),"fonts/opensans_regular_webfont.ttf");
        }
        return this.regularFont;
    }

    private Typeface getRobotoFont() {
        if(robotFont == null) {
            robotFont = Typeface.createFromAsset(getAssets(),"fonts/roboto_regular_webfont.ttf");
        }
        return this.robotFont;
    }

    private Typeface getLatoLightFont() {
        if(LatoLightFont == null) {
            LatoLightFont = Typeface.createFromAsset(getAssets(),"fonts/Lato-Light.ttf");
        }
        return this.LatoLightFont;
    }

    private Typeface getLatoRegularFont() {
        if(LatoRegularFont == null) {
            LatoRegularFont = Typeface.createFromAsset(getAssets(),"fonts/Lato-Regular.ttf");
        }
        return this.LatoRegularFont;
    }
}
