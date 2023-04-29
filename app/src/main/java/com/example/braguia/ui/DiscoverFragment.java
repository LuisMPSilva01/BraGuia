package com.example.braguia.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.braguia.R;

/*
* Neste momento, este fragmento só imprime o conteudo escrito no centro do ecrã.
* TODO: Podemos permitir pesquisa por nome, sugerir locais (random?) debaixo da searchview, ...
* */
public class DiscoverFragment extends Fragment {

    private SearchView searchView;
    private TextView searchText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ConstraintLayout rootView = (ConstraintLayout) inflater.inflate(R.layout.fragment_discover, container, false);

        searchText = rootView.findViewById(R.id.search_text);

        searchView = rootView.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchText.setText(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return rootView;
    }
}