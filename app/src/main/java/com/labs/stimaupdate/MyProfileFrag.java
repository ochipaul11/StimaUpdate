package com.labs.stimaupdate;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyProfileFrag extends Fragment {
    MyprofileFragListener myprofileFragListener;
    TextView tvProfileFirstName, tvProfileLastName, tvProfileEmail, tvProfilePhoneNumber, tvProfilePassword;
    Button btnProfileSaveChanges, btnProfileCancel;
    ProgressDialog progressDialog;
    String password;

    public MyProfileFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frament_my_profile, container, false);
        Toolbar toolbarMyProfile = view.findViewById(R.id.toolbarMyProfile);
        toolbarMyProfile.setTitle("My Profile");
        toolbarMyProfile.setNavigationIcon(R.drawable.ic_navigate_before_white_24dp);
        toolbarMyProfile.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myprofileFragListener.backFromMyProfileToDashboard();
            }
        });

        tvProfileFirstName = view.findViewById(R.id.tvProfileFirstName);
        tvProfileLastName = view.findViewById(R.id.tvProfileLastName);
        tvProfileEmail = view.findViewById(R.id.tvProfileEmail);
        tvProfilePassword = view.findViewById(R.id.tvProfilePassword);
        tvProfilePhoneNumber = view.findViewById(R.id.tvProfilePhoneNumber);
        tvProfileFirstName.setText(MainActivity.prefConfig.readFirstName());
        btnProfileCancel = view.findViewById(R.id.btnProfileCancel);
        btnProfileSaveChanges = view.findViewById(R.id.btnProfileSaveChanges);

        tvProfileFirstName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Edit Your First Name");
// Set up the input
                final EditText input = new EditText(getContext());
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                // input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                input.setInputType(InputType.TYPE_CLASS_TEXT);

                builder.setView(input);

// Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tvProfileFirstName.setText(input.getText().toString());
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
        tvProfileLastName.setText(MainActivity.prefConfig.readLastName());
        tvProfileLastName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Edit Your Last Name");
// Set up the input
                final EditText input = new EditText(getContext());
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                // input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                input.setInputType(InputType.TYPE_CLASS_TEXT);

                builder.setView(input);

// Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tvProfileLastName.setText(input.getText().toString());
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
        tvProfilePhoneNumber.setText(MainActivity.prefConfig.readPhoneNumber());
        tvProfilePhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Edit Your Phone Number");
// Set up the input
                final EditText input = new EditText(getContext());
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                // input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                input.setInputType(InputType.TYPE_CLASS_PHONE);

                builder.setView(input);

// Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tvProfilePhoneNumber.setText(input.getText().toString());
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
        tvProfileEmail.setText(MainActivity.prefConfig.readEmail());
        tvProfileEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.prefConfig.displayToast("Your cannot edit your Email!");
            }
        });

        tvProfilePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(getContext());
                dialog.setMessage("Are you sure you want change your Password!");
                dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        progressDialog = ProgressDialog.show(getContext(), "Resetting password...", null, true, true);
                        Backendless.UserService.restorePassword(MainActivity.prefConfig.readEmail(), new AsyncCallback<Void>() {
                            @Override
                            public void handleResponse(Void response) {
                                progressDialog.dismiss();
                                MainActivity.prefConfig.displayToast("Reset instructions sent to email address!");
                                myprofileFragListener.resetPassword();
                                progressDialog.dismiss();
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                MainActivity.prefConfig.displayToast("Error: " + fault.getMessage());
                                progressDialog.dismiss();
                            }
                        });
                    }
                });
                dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.show();
            }
        });

        btnProfileSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Enter password to confirm profile changes");
// Set up the input
                final EditText input = new EditText(getContext());
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(input);

// Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        password = input.getText().toString();

                        dialog.cancel();
                        progressDialog = ProgressDialog.show(getContext(), "Updating profile...", null, true, true);
                        Backendless.UserService.login(MainActivity.prefConfig.readEmail(), password, new AsyncCallback<BackendlessUser>() {
                            @Override
                            public void handleResponse(BackendlessUser response) {
                                response.setProperty("fname", tvProfileFirstName.getText());
                                response.setProperty("lname", tvProfileLastName.getText());
                                response.setProperty("phonenumber", tvProfilePhoneNumber.getText());

                                Backendless.UserService.update(response, new AsyncCallback<BackendlessUser>() {
                                    @Override
                                    public void handleResponse(BackendlessUser response) {
                                        progressDialog.dismiss();
                                        MainActivity.prefConfig.displayToast("Profile Successfully Updated!");
                                    }

                                    @Override
                                    public void handleFault(BackendlessFault fault) {
                                        progressDialog.dismiss();
                                        MainActivity.prefConfig.displayToast("Error: " + fault.getMessage());
                                    }
                                });
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                progressDialog.dismiss();
                                MainActivity.prefConfig.displayToast("Error: " + fault.getMessage());
                            }
                        });

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        myprofileFragListener = (MyprofileFragListener) activity;
    }

    public interface MyprofileFragListener {
        void backFromMyProfileToDashboard();

        void updateMyProfile(String firstName, String lastName, String email, String phoneNumber, String consumerId);

        void resetPassword();
    }
}
