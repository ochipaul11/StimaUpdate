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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.List;

public class FieldAdminLoginFragment extends Fragment {
    Button btnFieldAdminLogin;
    EditText etFieldAdminEmail, getEtFieldAdminPassword;
    OnFieldAdminLoginListener onFieldAdminLoginListener;
    ProgressDialog progressDialog;
    Toolbar fieldAdminLoginToolbar;

    public FieldAdminLoginFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        onFieldAdminLoginListener = (OnFieldAdminLoginListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_field_admin_login, container, false);

        etFieldAdminEmail = view.findViewById(R.id.etFieldAdminEmailLogin);
        getEtFieldAdminPassword = view.findViewById(R.id.etFieldAdminPasswordLogin);
        btnFieldAdminLogin = view.findViewById(R.id.btnFieldAdminLogin);
        fieldAdminLoginToolbar = view.findViewById(R.id.fieldAdminLoginToolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(fieldAdminLoginToolbar);
        fieldAdminLoginToolbar.setTitle("Field Admin Login");
        fieldAdminLoginToolbar.setNavigationIcon(R.drawable.ic_navigate_before_white_24dp);
        fieldAdminLoginToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFieldAdminLoginListener.backFromAdminLoginToLoginFrag();
            }
        });

        btnFieldAdminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkDataEntered(etFieldAdminEmail, getEtFieldAdminPassword)) {
                    userLogin();
                }
            }
        });


        return view;
    }

    private void userLogin() {
        final String email = etFieldAdminEmail.getText().toString();
        final String userPassword = getEtFieldAdminPassword.getText().toString();

        progressDialog = ProgressDialog.show(getContext(), "Logging in...", null, true, true);

        Backendless.UserService.login(email, userPassword, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser response) {
                String email = response.getEmail();
                String fname = (String) response.getProperty("fname");
                String lname = (String) response.getProperty("lname");
                String phonenumber = (String) response.getProperty("phonenumber");
                String fieldAdminId = response.getObjectId();

                MainActivity.prefConfig.displayToast("Logged in Successfully!");
                MainActivity.backendlessUser = response;
                onFieldAdminLoginListener.performFieldAdminLogin(fname, lname, email, phonenumber, fieldAdminId);

                Backendless.UserService.getUserRoles(new AsyncCallback<List<String>>() {
                    @Override
                    public void handleResponse(List<String> response) {
                        int i = 0;
                        while (i < response.size()) {
                            Log.d("FIELDADMINLOGIN: getting userrole***************** ", response.get(i));
                            i++;
                        }
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Log.d("FIELDaDMINlOGIN: Error", fault.toString());
                    }
                });

                progressDialog.dismiss();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                progressDialog.dismiss();
                MainActivity.prefConfig.displayToast(fault.getMessage());
            }
        }, true);
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
            etFieldAdminEmail.setError("Enter valid email!");
            dataStatus = false;

        } else if (!isPassword(checkPassword)) {
            getEtFieldAdminPassword.setError("Password length should be atleast 8 characters!");
            dataStatus = false;

        }

        return dataStatus;
    }

    public interface OnFieldAdminLoginListener {
        void performFieldAdminLogin(String firstName, String lastName, String email, String phoneNumber, String consumerId);

        void backFromAdminLoginToLoginFrag();
    }
}