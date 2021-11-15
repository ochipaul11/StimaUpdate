package com.labs.stimaupdate;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
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

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class LoginFragment extends Fragment {


    TextView btnRegisterLogin;
    ProgressDialog progressDialog;
    EditText etEmail, etUserPassword;
    Button btnLogin, btnReset;
    OnLoginFormActivityListener onLoginFormActivityListener;


    public LoginFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
       // progressDialog = ProgressDialog.show(getContext(), "Loading...", null, true, true);
        etEmail = view.findViewById(R.id.etEmailLogin);
        etUserPassword = view.findViewById(R.id.etPasswordLogin);
        btnLogin = view.findViewById(R.id.btnLogin);

        btnReset = view.findViewById(R.id.btnReset);


        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });
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

    private void resetPassword() {
        if (etEmail.getText().toString().isEmpty()) {
            MainActivity.prefConfig.displayToast("Please enter email address in the email field!");
        } else {
            String email = etEmail.getText().toString();
            progressDialog = ProgressDialog.show(getContext(), "Resetting password...", null, true, true);
            Backendless.UserService.restorePassword(email, new AsyncCallback<Void>() {
                @Override
                public void handleResponse(Void response) {
                    progressDialog.dismiss();
                    MainActivity.prefConfig.displayToast("Reset instructions sent to email address!");
                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    progressDialog.dismiss();
                    MainActivity.prefConfig.displayToast("Error: " + fault.getMessage());
                }
            });
        }
    }

    private void userLogin() {
        final String email = etEmail.getText().toString();
        final String userPassword = etUserPassword.getText().toString();

        progressDialog = ProgressDialog.show(getContext(), "Logging in...", null, true, true);

        Backendless.UserService.login(email, userPassword, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser response) {
                String email = response.getEmail();
                String fname = (String) response.getProperty("fname");
                String lname = (String) response.getProperty("lname");
                String phonenumber = (String) response.getProperty("phonenumber");
                String consumerId = response.getObjectId();
                MainActivity.prefConfig.displayToast("Logged in Successfully!");
                MainActivity.backendlessUser = response;
                onLoginFormActivityListener.performLogin(fname,lname,email,phonenumber, consumerId);
                progressDialog.dismiss();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                progressDialog.dismiss();
                MainActivity.prefConfig.displayToast("Error: " + fault.getMessage());
            }
        },true);
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

        void performLogin(String firstName, String lastName, String email, String phoneNumber, String consumerId);
    }
}
