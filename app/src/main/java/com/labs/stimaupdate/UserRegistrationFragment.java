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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class UserRegistrationFragment extends Fragment {
    RegisterFormListener registerFormListener;
    ProgressDialog progressDialog;
    private EditText fname, lname, email, phonenumber, password;
    private Button btnRegisterUser, btnBackRegister;

    public UserRegistrationFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_registration, container, false);

        fname = view.findViewById(R.id.etFirstNameRegister);
        lname = view.findViewById(R.id.etLastnameRegister);
        email = view.findViewById(R.id.etEmailRegister);
        phonenumber = view.findViewById(R.id.etPhoneNumber);
        password = view.findViewById(R.id.etPasswordRegister);

        btnBackRegister = view.findViewById(R.id.btnBackRegister);
        btnRegisterUser = view.findViewById(R.id.btnRegisterUser);

        btnBackRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerFormListener.BackToLoginFromRegistrationFrag();
            }
        });

        btnRegisterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkDataEntered(fname, lname, email, password)) {
                    registerUser();
                }
            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        Activity activity = (Activity) context;
        registerFormListener = (RegisterFormListener) activity;
    }

    public void registerUser() {
        String fName = fname.getText().toString();
        String lName = lname.getText().toString();
        String userEmail = email.getText().toString();
        String phoneNumber = phonenumber.getText().toString();
        String passWord = password.getText().toString();

        BackendlessUser user = new BackendlessUser();
        user.setEmail(userEmail);
        user.setPassword(passWord);
        user.setProperty("fname", fName);
        user.setProperty("lname", lName);
        user.setProperty("phonenumber", phoneNumber);

        progressDialog = ProgressDialog.show(getContext(), "Registering User...", null, true, true);

        Backendless.UserService.register(user, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser response) {
                progressDialog.dismiss();
                registerFormListener.BackToLoginFromRegistrationFrag();
                MainActivity.prefConfig.displayToast("User Successfully Registered");
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                progressDialog.dismiss();
                MainActivity.prefConfig.displayToast("Error: " + fault.getMessage());
            }
        });

    }


    Boolean checkDataEntered(EditText checkFirstName, EditText checkLastName, EditText checkEmail, EditText checkPassword) {

        Boolean dataStatus = true;

        if (isEmpty(checkFirstName)) {
            Toast t = Toast.makeText(getContext(), "You must enter first name to register!", Toast.LENGTH_SHORT);
            t.show();
            dataStatus = false;
        } else if (isEmpty(checkLastName)) {

            checkLastName.setError("Last name is required!");
            dataStatus = false;

        } else if (!isEmail(checkEmail)) {
            email.setError("Enter valid email!");
            dataStatus = false;

        } else if (!isPassword(checkPassword)) {
            password.setError("Password length should be atleast 8 characters!");
            dataStatus = false;

        }

        return dataStatus;
    }

    boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    boolean isPassword(EditText text) {
        CharSequence str = text.getText().toString();
        Boolean bolean = true;

        if (str.length() < 8) {
            bolean = false;
        }
        return bolean;
    }

    public interface RegisterFormListener {
        void BackToLoginFromRegistrationFrag();
    }

}
