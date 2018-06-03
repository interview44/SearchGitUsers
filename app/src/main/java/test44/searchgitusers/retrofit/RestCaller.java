package test44.searchgitusers.retrofit;


import android.util.Log;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import test44.searchgitusers.R;
import test44.searchgitusers.application.MainApplication;
import test44.searchgitusers.interfaces.iResponseHandler;


/**
 * Created by Taimur on 21/05/16.
 */
public class RestCaller {

    private int REQ_CODE = 0;
    iResponseHandler handler;
    Call obj;

    public RestCaller(iResponseHandler context, Call caller, final int REQUEST_CODE) throws NumberFormatException {
        if (REQUEST_CODE <= 0) {
            NumberFormatException ex = new NumberFormatException();
            throw ex;
        }
        REQ_CODE = REQUEST_CODE;
        handler = context;
        this.obj = caller;
        obj.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful())
                    handler.onSuccess(call, response, REQ_CODE);
                else {
                    ErrorResponse errorResponse = RetroClient.parseErrorResponse(response);
                    handler.onFailure(call, errorResponse, REQ_CODE);
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                if (t != null) {
                    Log.e("RestCaller", t.getMessage());
                    if (t instanceof SocketTimeoutException) {
                        t = new Throwable(MainApplication.getAppContext().getString(R.string.error_timeout));
                    } else if (t instanceof ConnectException) {
                        t = new Throwable(MainApplication.getAppContext().getString(R.string.text_checkConnection));
                    } else if (t.getLocalizedMessage() != null && t.getLocalizedMessage().contains("MalformedJsonException") || t.getLocalizedMessage().contains("malformed JSON")) {
                        t = new Throwable(MainApplication.getAppContext().getString(R.string.error_invalid_data_posted));
                    } else if (t instanceof SSLHandshakeException || t instanceof SSLException) {
                        t = new Throwable(MainApplication.getAppContext().getString(R.string.error_ssl));
                    } else {
                        t = new Throwable(t.getMessage());
                    }
                    handler.onApiCrash(call, t, REQ_CODE);
                } else {
                    t = new Throwable(MainApplication.getAppContext().getString(R.string.text_something_went_wrong));
                    handler.onApiCrash(call, t, REQ_CODE);
                }
            }
        });
    }
}
