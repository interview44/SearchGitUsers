package test44.searchgitusers.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Response;
import test44.searchgitusers.R;
import test44.searchgitusers.application.MainApplication;
import test44.searchgitusers.helpers.AppConstants;
import test44.searchgitusers.helpers.Utils;
import test44.searchgitusers.interfaces.iResponseHandler;
import test44.searchgitusers.pojo.GetUserResponse;
import test44.searchgitusers.retrofit.ErrorResponse;
import test44.searchgitusers.retrofit.RestCaller;
import test44.searchgitusers.helpers.Loading;
import test44.searchgitusers.helpers.NetworkUtils;

public class SearchScreen extends AppCompatActivity {

    @BindView(R.id.et_username)
    EditText userName;

    @BindView(R.id.iv_clear_search)
    ImageView clearSearch;

    private static final int RC_GET_USER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        userName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String username = userName.getText().toString();
                    if (username.length() > 0) {
                        if (NetworkUtils.isNetworkAvailable(SearchScreen.this)) {
                            Loading.show(SearchScreen.this, false, "Please wait! Fetching user");
                            Utils.hideKeyboard(SearchScreen.this);
                            searchGitUser(username);
                        } else {
                            showDialog(getResources().getString(R.string.check_internet_connection));
                        }
                    } else {
                        Toast.makeText(SearchScreen.this, "Please enter user name or email to search!", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                return false;
            }
        });

    }

    @OnClick(R.id.iv_clear_search)
    public void setClearSearch() {
        userName.setText("");
    }

    private void searchGitUser(String username) {
        new RestCaller(new iResponseHandler() {
            @Override
            public void onSuccess(Call call, Response response, int reqCode) {
                Loading.cancel();
                GetUserResponse objResponse = (GetUserResponse) response.body();
                if (objResponse != null) {
                    Intent i = new Intent(SearchScreen.this, DetailScreen.class);
                    i.putExtra(AppConstants.EXTRA_KEY_POSSIBLE_ROUTES, new Gson().toJson(objResponse));
                    startActivity(i);
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
                Toast.makeText(SearchScreen.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }, MainApplication.getDefaultRestClient().getUsers(username), RC_GET_USER);
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
