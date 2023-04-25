package com.example.braguia.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.braguia.R;
import com.example.braguia.model.app.AppInfo;
import com.example.braguia.viewmodel.AppInfoViewModel;

import java.io.IOException;

public class AppInfoFragment extends Fragment {
    private AppInfoViewModel appInfoViewModel;

    public AppInfoFragment(){
    }

    public static AppInfoFragment newInstance() {
        return new AppInfoFragment();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_descricao, container, false);


        appInfoViewModel = new ViewModelProvider(this).get(AppInfoViewModel.class);
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
        TextView textview = (TextView) view.findViewById(R.id.textView2);
        textview.setText(appInfo.getAppDesc());
    }
}
