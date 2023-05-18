package com.example.braguia.ui.Fragments;

import static android.content.Context.TELEPHONY_SERVICE;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.braguia.R;
import com.example.braguia.model.app.Partner;
import com.example.braguia.ui.viewAdapters.PartnersRecyclerViewAdapter;
import com.example.braguia.viewmodel.AppInfoViewModel;

import java.io.IOException;
import java.util.List;

public class PartnersListFragment extends Fragment {


    private static final String ARG_COLUMN_COUNT = "column-count";

    private int mColumnCount = 1;

    private AppInfoViewModel appInfoViewModel;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PartnersListFragment() {
    }

    public static PartnersListFragment newInstance(int columnCount) {
        PartnersListFragment fragment = new PartnersListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_partners_list, container, false);

        appInfoViewModel = new ViewModelProvider(requireActivity()).get(AppInfoViewModel.class);
        try {
            appInfoViewModel.getAppInfo().observe(getViewLifecycleOwner(), appInfo -> {
                List<Partner> partners = appInfo.getPartners();
                Log.e("Partners List","partners size:" + partners.size());
                loadRecyclerView(view, partners);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return view;
    }

    private void loadRecyclerView(View view, List<Partner> partners){
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            PartnersRecyclerViewAdapter adapter = new PartnersRecyclerViewAdapter(partners);
            recyclerView.setAdapter(adapter);

            if(isTelephonyEnabled()){
                adapter.setOnPhoneClickListener(this::callPhoneNumber);
            }
        }
    }

    private boolean isTelephonyEnabled(){
        TelephonyManager tm = (TelephonyManager) getContext().getSystemService(TELEPHONY_SERVICE);
        return tm != null && tm.getSimState()==TelephonyManager.SIM_STATE_READY;
    }
    private void callPhoneNumber(String phoneNumber) { //TODO maybe adicionar um backtrace a partir da main activity para tornar o fragmento mais fléxivel
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        getContext().startActivity(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

}
