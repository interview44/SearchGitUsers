package test44.searchgitusers.interfaces;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import test44.searchgitusers.pojo.GetUserResponse;

public interface RestApis {

    @GET("users/{user}")
    Call<GetUserResponse> getUsers(@Path("user") String userName);

    @GET("users/{user}/followers")
    Call<List<GetUserResponse>> getFollowers(
            @Path("user") String user
    );

}
