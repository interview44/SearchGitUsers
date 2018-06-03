package test44.searchgitusers.helpers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;


public class Loading {

    private static ProgressDialog pDialog;

    public static ProgressDialog show(Activity context, boolean cancelable, String message) {
        if (pDialog == null) {
            pDialog = new ProgressDialog(context);
            try {
                pDialog.setCancelable(cancelable);
                pDialog.setMessage(message);
                pDialog.show();
            } catch (Exception e) {
                Log.e("LoadingShowException", e.getMessage());
            }
        }
        return pDialog;
    }

    public static void cancel() {
        try {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
                pDialog.cancel();
                pDialog = null;

            }
        } catch (Exception e) {
            Log.e("LoadingCancelException", e.getMessage());
        }
    }

    public static boolean isVisible() {
        if (pDialog != null && pDialog.isShowing())
            return true;
        else return false;
    }


}
