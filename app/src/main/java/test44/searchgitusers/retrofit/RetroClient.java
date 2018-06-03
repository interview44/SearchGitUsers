package test44.searchgitusers.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okhttp3.Route;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import test44.searchgitusers.interfaces.RestApis;
import test44.searchgitusers.helpers.AppConstants;

/**
 * Created by Taimur on 21/05/16.
 */
public class RetroClient {
    private Retrofit defaultNetworkClient = null;
    private static RetroClient object;
    private RestApis service;

    private RetroClient() {

        Gson gson = new GsonBuilder().create();
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.authenticator(new Authenticator() {
            @Override
            public Request authenticate(Route route, okhttp3.Response response) throws IOException {
                String credential = Credentials.basic("Interview44", "sinterview44");
                return response.request().newBuilder().header("Authorization", credential).build();
            }
        });
        httpClient.retryOnConnectionFailure(true);
        httpClient.connectTimeout(60, TimeUnit.SECONDS);
        httpClient.readTimeout(60, TimeUnit.SECONDS);
        httpClient.writeTimeout(60, TimeUnit.SECONDS);
        httpClient.addInterceptor(loggingInterceptor);

        defaultNetworkClient = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL_GIT_USER_SEARCH)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();


        service = defaultNetworkClient.create(RestApis.class);
    }

    public static RetroClient getInstance() {
        if (object == null) {
            object = new RetroClient();
        }
        return object;
    }


    public RestApis getApiServices() {
        return object.service;
    }

    public Retrofit getDefaultNetworkClient() {
        return object.defaultNetworkClient;
    }

    public static synchronized ErrorResponse parseErrorResponse(Response response) {

        ErrorResponse genericResponse = null;

        try {
            Converter<ResponseBody, ErrorResponse> errorConverter =
                    RetroClient.getInstance().getDefaultNetworkClient().responseBodyConverter(ErrorResponse.class, new Annotation[0]);
            genericResponse = errorConverter.convert(response.errorBody());
            return genericResponse;
        } catch (IOException e) {
            e.printStackTrace();
            return genericResponse;
        }
    }
}
