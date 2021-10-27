package com.labs.stimaupdate;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {


    TextView btnRegisterLogin;
    ProgressDialog progressDialog;
    EditText etEmail, etUserPassword;
    Button btnLogin;
    OnLoginFormActivityListener onLoginFormActivityListener;

    public LoginFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        etEmail = view.findViewById(R.id.etEmailLogin);
        etUserPassword = view.findViewById(R.id.etPasswordLogin);
        btnLogin = view.findViewById(R.id.btnLogin);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkDataEntered(etEmail, etUserPassword)) {
                    userLogin();
                }
            }
        });
        btnRegisterLogin = view.findViewById(R.id.btnRegister);
        btnRegisterLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLoginFormActivityListener.performRegister();
            }
        });
        return view;
    }

    private void userLogin() {
        final String email = etEmail.getText().toString();
        final String userPassword = etUserPassword.getText().toString();

        Call<User> call = MainActivity.apiInterface.performUserLogin(email, userPassword);
        call.enqueue(new Callback<User>() {


            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (response.body().getResponse().equals("ok")) {
                    MainActivity.prefConfig.writeLoginStatus(true);
                    onLoginFormActivityListener.performLogin(
                            response.body().getFname(),
                            response.body().getLname(),

                            response.body().getEmail(),
                            response.body().getPhonenumber());
                    MainActivity.prefConfig.displayToast(String.valueOf(response.code()));
                } else if (response.body().getResponse().equals("failed")) {
                    MainActivity.prefConfig.displayToast("Wrong Email or Password");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("MainActivity", t.getMessage());
                MainActivity.prefConfig.displayToast(t.getMessage());
            }
        });
    }

    boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    boolean isPassword(EditText text) {
        CharSequence str = text.getText().toString();
        Boolean bolean = true;

        if (str.length() < 8) {
            bolean = false;
        }
        return bolean;
    }

    Boolean checkDataEntered(EditText checkEmail, EditText checkPassword) {

        Boolean dataStatus = true;

        if (!isEmail(checkEmail)) {
            etEmail.setError("Enter valid email!");
            dataStatus = false;

        } else if (!isPassword(checkPassword)) {
            etUserPassword.setError("Password length should be atleast 8 characters!");
            dataStatus = false;

        }

        return dataStatus;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        onLoginFormActivityListener = (OnLoginFormActivityListener) activity;
    }

    public interface OnLoginFormActivityListener {
        void performRegister();

        void performLogin(String firstName, String lastName, String email, int phoneNumber);
    }
}
