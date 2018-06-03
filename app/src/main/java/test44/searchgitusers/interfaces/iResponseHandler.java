package test44.searchgitusers.interfaces;

import retrofit2.Call;
import retrofit2.Response;
import test44.searchgitusers.retrofit.ErrorResponse;

/**
 * Created by Taimur on 21/05/16.
 */
public interface iResponseHandler {
    void onSuccess(Call call, Response response, int reqCode);

    void onFailure(Call call, ErrorResponse response, int reqCode);

    void onApiCrash(Call call, Throwable t, int reqCode);
}
