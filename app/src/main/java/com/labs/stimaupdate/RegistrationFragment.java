package com.labs.stimaupdate;

import android.app.Activity;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationFragment extends Fragment {
    RegisterFormListener registerFormListener;
    private EditText fname, lname, email, phonenumber, password;
    private Button btnRegisterUser, btnBackRegister;

    public RegistrationFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration, container, false);

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
                registerFormListener.BackToLogin();
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

        Activity activity = (Activity)context;
        registerFormListener = (RegisterFormListener) activity;
    }

    public void registerUser() {
        String fName = fname.getText().toString();
        String lName = lname.getText().toString();
        String userEmail = email.getText().toString();
        int phoneNumber = Integer.parseInt(phonenumber.getText().toString());
        String passWord = password.getText().toString();

        Call<User> call = MainActivity.apiInterface.performRegistration(fName, lName, userEmail, phoneNumber, passWord);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body().getResponse().equals("ok")) {
                    MainActivity.prefConfig.displayToast("Registration Successful");
                    fname.setText("");
                    lname.setText("");
                    email.setText("");
                    phonenumber.setText("");
                    password.setText("");
                } else if (response.body().getResponse().equals("exists")) {
                    MainActivity.prefConfig.displayToast("User already exits...");
                } else if (response.body().getResponse().equals("error")) {
                    MainActivity.prefConfig.displayToast("Something went wrong.Try again later");
                    fname.setText("");
                    lname.setText("");
                    email.setText("");
                    phonenumber.setText("");
                    password.setText("");
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                MainActivity.prefConfig.displayToast(t.getMessage());
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

    public  interface RegisterFormListener{
        void BackToLogin();
    }

}
