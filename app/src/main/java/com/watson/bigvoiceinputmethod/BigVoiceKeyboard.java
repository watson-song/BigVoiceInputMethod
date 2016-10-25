package com.watson.bigvoiceinputmethod;

import android.content.Context;
import android.inputmethodservice.Keyboard;

/**
 * Created by Watson on 25/10/2016.
 */

public class BigVoiceKeyboard extends Keyboard {
    public BigVoiceKeyboard(Context context, int xmlLayoutResId) {
        super(context, xmlLayoutResId);
    }

    public BigVoiceKeyboard(Context context, int xmlLayoutResId, int modeId, int width, int height) {
        super(context, xmlLayoutResId, modeId, width, height);
    }

    public BigVoiceKeyboard(Context context, int xmlLayoutResId, int modeId) {
        super(context, xmlLayoutResId, modeId);
    }

    public BigVoiceKeyboard(Context context, int layoutTemplateResId, CharSequence characters, int columns, int horizontalPadding) {
        super(context, layoutTemplateResId, characters, columns, horizontalPadding);
    }
}
