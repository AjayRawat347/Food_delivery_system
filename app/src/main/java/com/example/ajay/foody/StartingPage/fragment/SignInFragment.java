package com.example.ajay.foody.StartingPage.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ajay.foody.HomePage.HomePageActivity;
import com.example.ajay.foody.R;
import com.example.ajay.foody.StartingPage.SignInActivity;
import com.example.ajay.foody.controller.SPManipulation;
import com.example.ajay.foody.controller.VolleyController;
import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class SignInFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener{

    // Declare all views name;
    View view;
//    Button btn_signIn;
    EditText mobile, password;
    TextView toSignUp;

    LoginButton mLoginButton;
    SPManipulation mSPManipulation;
    CallbackManager callbackManager;
    private GoogleApiClient mGoogleApiClient;
    private SignInButton btnSignIn;
    private static final int RC_SIGN_IN = 007;
    Button btn_signIn, mFbButtonSignIn;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sign_in, container, false);
//        fbGoogleSignIn();
        init();
        return view;
    }





    private void init(){
        // Init all views
        mobile = (EditText) view.findViewById(R.id.sign_in_username);
        password = (EditText) view.findViewById(R.id.sign_in_pwd);


        toSignUp = (TextView) view.findViewById(R.id.to_sign_up);
        toSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set Tap Switch

            }
        });
        btn_signIn = (Button) view.findViewById(R.id.sign_in_btn);
        btn_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLoginInfo();
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }



    // Set Default URL and Json
    final private String Login_URL = "http://rjtmobile.com/ansari/fos/fosapp/fos_login.php";
    final private String TAG = "SIGNIN";
    private String buildUrl(){
        return Login_URL + "?user_phone=" + mobile.getText().toString() + "&user_password=" + password.getText().toString();
    }
    private void checkLoginInfo() {
        SignInActivity.showPDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, buildUrl(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Response --> " + response);
                        if (response.toString().contains("success")){
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                Log.e("JSON", jsonArray.toString());
                                JSONObject userInfo = jsonArray.getJSONObject(0);
                                Log.e("JSON", userInfo.toString());
                                SPManipulation.getInstance(getContext()).setName(userInfo.getString("UserName"));
                                SPManipulation.getInstance(getContext()).setEmail(userInfo.getString("UserEmail"));
                                SPManipulation.getInstance(getContext()).setAddress(userInfo.getString("UserAddress"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(getContext(), "Login successful!", Toast.LENGTH_SHORT).show();
                            SPManipulation.getInstance(getContext()).setMobile(mobile.getText().toString());
                            SPManipulation.getInstance(getContext()).setPwd(password.getText().toString());
                            Intent homePageIntent = new Intent(getActivity(), HomePageActivity.class);
                            startActivity(homePageIntent);
                        }
                        else {
                            Toast.makeText(getContext(), "Wrong password/mobile. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                        SignInActivity.disPDialog();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.getMessage(), error);
                        SignInActivity.disPDialog();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                final Map<String, String> params = new HashMap<String, String>();
                params.put("user_phone",mobile.getText().toString());
                params.put("user_password",password.getText().toString());
                Log.e("POST",params.toString());
                return params;
            }
        };
        VolleyController.getInstance().addToRequestQueue(stringRequest, TAG);
    }
}
