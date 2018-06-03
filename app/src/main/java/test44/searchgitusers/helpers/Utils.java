package test44.searchgitusers.helpers;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class Utils {
    public static void hideKeyboard(Activity mContext) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(mContext.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
        }
    }
}
