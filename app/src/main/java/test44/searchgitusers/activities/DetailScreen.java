package test44.searchgitusers.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;
import test44.searchgitusers.R;
import test44.searchgitusers.adapters.FollowersAdapter;
import test44.searchgitusers.application.MainApplication;
import test44.searchgitusers.helpers.AppConstants;
import test44.searchgitusers.helpers.Loading;
import test44.searchgitusers.helpers.NetworkUtils;
import test44.searchgitusers.interfaces.iResponseHandler;
import test44.searchgitusers.modals.FollowersModal;
import test44.searchgitusers.pojo.GetUserResponse;
import test44.searchgitusers.retrofit.ErrorResponse;
import test44.searchgitusers.retrofit.RestCaller;

public class DetailScreen extends AppCompatActivity {

    @BindView(R.id.text_user_name)
    TextView userName;

    @BindView(R.id.text_user_email)
    TextView userEmail;

    @BindView(R.id.text_no_follower)
    TextView textNoFollowers;

    @BindView(R.id.user_image)
    ImageView userImage;

    @BindView(R.id.rv_followers)
    RecyclerView recyclerView;

    private static final int RC_GET_USER_FOLLOWERS = 1;
    private GetUserResponse userResponse;
    private ArrayList<FollowersModal> followersModalArrayList;
    private FollowersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        userResponse = new Gson().fromJson(this.getIntent().getStringExtra(AppConstants.EXTRA_KEY_POSSIBLE_ROUTES),
                new TypeToken<GetUserResponse>() {
                }.getType());

        setUserData(userResponse);
    }

    private void setUserData(GetUserResponse userData) {
        if (userData != null) {

            userName.setText(userData.getLogin());

            if (userData.getEmail() == null) {
                userEmail.setText("Public email not found");
            } else userEmail.setText(userData.getEmail());

            Picasso.with(this)
                    .load(userData.getAvatarUrl())
                    .placeholder(R.drawable.placeholder)
                    .into(userImage);

            if (NetworkUtils.isNetworkAvailable(this)) {
                Loading.show(this, false, "Please wait! Fetching user's followers.");
                getUserFollowers(userName.getText().toString());
            } else {
                showDialog(getResources().getString(R.string.check_internet_connection));
            }
        }

    }

    private void getUserFollowers(String userName) {
        new RestCaller(new iResponseHandler() {
            @Override
            public void onSuccess(Call call, Response response, int reqCode) {
                Loading.cancel();
                List<GetUserResponse> objResponse = (List<GetUserResponse>) response.body();
                if (objResponse.size() > 0) {
                    textNoFollowers.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    followersModalArrayList = new ArrayList<>();
                    for (int i = 0; i < objResponse.size(); i++) {
                        FollowersModal modal = new FollowersModal(objResponse.get(i).getLogin(), objResponse.get(i).getAvatarUrl());
                        followersModalArrayList.add(modal);
                    }

                    adapter = new FollowersAdapter(followersModalArrayList, DetailScreen.this);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(DetailScreen.this);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(adapter);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    textNoFollowers.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Call call, ErrorResponse response, int reqCode) {
                Loading.cancel();
                showDialog(response.getMessage());

            }

            @Override
            public void onApiCrash(Call call, Throwable t, int reqCode) {
                Loading.cancel();
                Toast.makeText(DetailScreen.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();


            }
        }, MainApplication.getDefaultRestClient().getFollowers(userName), RC_GET_USER_FOLLOWERS);
    }

    private void showDialog(String message) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(message);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

}
