package com.labs.stimaupdate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements
        LoginFragment.OnLoginFormActivityListener,
        RegistrationFragment.RegisterFormListener {

    public static PrefConfig prefConfig;
    public static ApiInterface apiInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final String LOG_TAG = MainActivity.class.getSimpleName();
        prefConfig = new PrefConfig(this);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        if(findViewById(R.id.fragment_container) !=null){
            if(savedInstanceState !=null){
                return;
            }
            else if(prefConfig.readingLoginStatus()){
              getSupportFragmentManager()
                      .beginTransaction()
                      .add(R.id.fragment_container,new HomeFragment())
                      .commit();
            }
            else{
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragment_container,new LoginFragment())
                        .commit();
            }
        }
    }

    @Override
    public void performRegister() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new RegistrationFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void performLogin(String firstName, String lastName, String email, int phoneNumber) {
        prefConfig.writeFirstName(firstName);
        prefConfig.writeLastName(lastName);
        prefConfig.writeEmail(email);
        prefConfig.writePhoneNumber(phoneNumber);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new HomeFragment() )
                .commit();
    }

    @Override
    public void BackToLogin() {
    getSupportFragmentManager()
    .beginTransaction()
    .replace(R.id.fragment_container, new LoginFragment())
    .commit();
    }
}
