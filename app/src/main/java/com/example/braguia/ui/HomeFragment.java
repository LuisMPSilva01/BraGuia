package com.example.braguia.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.braguia.R;
import com.example.braguia.model.app.AppInfo;
import com.example.braguia.viewmodel.AppInfoViewModel;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private AppInfoViewModel appInfoViewModel;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        appInfoViewModel = new ViewModelProvider(requireActivity()).get(AppInfoViewModel.class);
        try {
            Context context = view.getContext();
            appInfoViewModel.getAppInfo().observe(getViewLifecycleOwner(), x -> {
                loadView(view, x);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return view;
    }

    private void loadView(View view, AppInfo appInfo){
        TextView title = view.findViewById(R.id.textView2);
        title.setText(appInfo.getAppDesc());
        TextView intro = view.findViewById(R.id.appIntro);
        intro.setText(appInfo.getAppLandingPageText());
    }
}