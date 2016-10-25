package com.watson.bigvoiceinputmethod;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;

/**
 * Created by Watson on 25/10/2016.
 */

public class BigVoiceKeyboardView extends KeyboardView {
    // these three constants are manually mirrored in the
    // BigVoiceInputMethod class
    private final int SHIFT_OFF = 0;
    private final int SHIFT_ON = 1;
    private final int SHIFT_CAPS = 2;
    private Resources res = null;
    private Drawable capsIcon = null;
    private Drawable shiftIcon = null;
    private Keyboard.Key shiftKey = null;

    public BigVoiceKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPreviewEnabled(false);
        res = context.getResources();
    }

    public BigVoiceKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setPreviewEnabled(false);
        res = context.getResources();
    }

    public void setShifted(int state) {
        if (state == SHIFT_OFF) {
            shiftKey.icon = shiftIcon;
            super.setShifted(false);
        }
        else if (state == SHIFT_ON) {
            shiftKey.icon = shiftIcon;
            super.setShifted(true);
        }
        else {
            shiftKey.icon = capsIcon;
            super.setShifted(true);
        }
    }
    @Override public void setKeyboard(Keyboard keyboard) {
        super.setKeyboard(keyboard);
        capsIcon = res.getDrawable(R.drawable.sym_keyboard_shift_locked);
        shiftKey = getKeyboard().getKeys().get(getKeyboard().getShiftKeyIndex());
        shiftIcon = shiftKey.icon;
    }
    @Override protected boolean onLongPress(Keyboard.Key key) {
        return super.onLongPress(key);
    }

}
