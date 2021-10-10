package com.labs.stimaupdate;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.transferwise.sequencelayout.SequenceStep;

public class HomeFragment extends Fragment {

    HomeFragmentListener homeFragmentListener;
    Button btnLogout;
    SequenceStep step1,step2,step3,step4,step5;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity= (Activity) context;
        homeFragmentListener = (HomeFragmentListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
      /*  btnLogout = view.findViewById(R.id.btnlogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeFragmentListener.LogoutListener();
            }
        });
*/

        return view;
    }

    public interface HomeFragmentListener {
        void LogoutListener();

        void BackFromHomeFragment();
    }
}
