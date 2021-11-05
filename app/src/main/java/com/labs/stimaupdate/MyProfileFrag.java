package com.labs.stimaupdate;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyProfileFrag extends Fragment {
    MyprofileFragListener myprofileFragListener;

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
    }
}
