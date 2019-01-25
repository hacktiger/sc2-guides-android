package com.sc2guide.sc2_guides_android.view.users;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sc2guide.sc2_guides_android.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyGuidesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyGuidesFragment extends Fragment {

    public MyGuidesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MyGuidesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyGuidesFragment newInstance() {
        MyGuidesFragment fragment = new MyGuidesFragment();
        Bundle args = new Bundle();
        //
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_guides, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
