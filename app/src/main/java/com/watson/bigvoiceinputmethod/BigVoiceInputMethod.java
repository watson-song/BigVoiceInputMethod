package com.watson.bigvoiceinputmethod;

import android.content.Intent;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;

import java.lang.ref.Reference;

/**
 * Created by Watson on 25/10/2016.
 */

public class BigVoiceInputMethod extends InputMethodService implements KeyboardView.OnKeyboardActionListener {

    // these three constants are manually mirrored in the BigVoiceKeyboardView class
    private final int SHIFT_OFF = 0;
    private final int SHIFT_ON = 1;
    private final int SHIFT_CAPS = 2;

    private boolean autoPunctuate = true;
    private boolean autoCapitalize = true;
    private Vibrator vib = null;
    private boolean shifted = false;
    private boolean capsLocked = false;
    private boolean showReference = false;
    private boolean swipeClose = false;

    BigVoiceKeyboard mKeyboard;
    BigVoiceKeyboardView mKeyboardView;
    private StringBuilder text = new StringBuilder(0);

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onInitializeInterface() {
        mKeyboard = new BigVoiceKeyboard(this, R.layout.bigvoice);
    }

    @Override
    public View onCreateInputView() {
        mKeyboardView = (BigVoiceKeyboardView) getLayoutInflater().inflate(R.layout.input, null);
        mKeyboardView.setOnKeyboardActionListener(this);
        mKeyboardView.setKeyboard(mKeyboard);
        return mKeyboardView;
    }

    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        switch (primaryCode) {
            case 1:
                text.append(".");
                break;
            case 2:
                text.append("-");
                break;
            case Keyboard.KEYCODE_SHIFT:
                toggleShift();
                break;
            case 32:
                handleSpace();
                break;
            case Keyboard.KEYCODE_DELETE:
                handleBackspace();
                break;
            default:
                break;
        }
    }

    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {
        if (swipeClose) {
            handleClose();
        }
    }

    @Override
    public void swipeUp() {
        if (showReference) {
            handleClose();
            Intent i = new Intent(this, Reference.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
    }

    /*
     * Commits the passed text to the text box and reset the
     * cursor position. Used to commit a predetermined string of
     * text instead of something the user is composing.
     * @param s the text to commit
     */
    private void commitText(String s) {
        InputConnection ic = getCurrentInputConnection();
        ic.commitText(s, /* new cursor pos */1);
        text.setLength(0);
    }
    /*
     * Handle the closing of this IME.
     */
    private void handleClose() {
        requestHideSelf(0);
        mKeyboardView.closing();
    }
    /*
     * Handle the backspace action.
     */
    private void handleBackspace() {
        if (text.length() == 0) {
            // delete the letter to the left of the cursor position
            getCurrentInputConnection().sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
        }
        text.setLength(0);
        if (!capsLocked && autoCapitalize) {
            initShift(getCurrentInputConnection().getTextBeforeCursor(1, /* flags */ 0).length() != 1);
        }
    }
    private void handleSpace() {
        if (text.length() == 0) {
            final InputConnection ic = getCurrentInputConnection();
            final String previousCharacter = ic.getTextBeforeCursor(1, 0).toString();
            if (autoPunctuate && previousCharacter.equalsIgnoreCase(" ") && ic.deleteSurroundingText(1, 0)) {
                commitText(". ");
                if (autoCapitalize && !capsLocked) {
                    shifted = true;
                    capsLocked = false;
                    mKeyboardView.setShifted(SHIFT_ON);
                    return;
                }
            }
            else {
                commitText(" ");
            }
        }
        else {
            commitText(text.toString());
        }
        if (shifted && !capsLocked) {
            shifted = false;
            capsLocked = false;
            mKeyboardView.setShifted(SHIFT_OFF);
        }
    }
    private void toggleShift() {
        if (capsLocked) {
            shifted = false;
            capsLocked = false;
            mKeyboardView.setShifted(SHIFT_OFF);
            return;
        }
        if (shifted) {
            shifted = true;
            capsLocked = true;
            mKeyboardView.setShifted(SHIFT_CAPS);
            return;
        }
        shifted = true;
        mKeyboardView.setShifted(SHIFT_ON);
    }
    private void initShift(boolean shifted) {
        this.shifted = shifted;
        capsLocked = false;
        if (null != mKeyboardView) {
            mKeyboardView.setShifted((shifted) ? SHIFT_ON : SHIFT_OFF);
        }
    }
}
