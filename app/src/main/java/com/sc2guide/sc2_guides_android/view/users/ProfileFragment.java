package com.sc2guide.sc2_guides_android.view.users;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sc2guide.sc2_guides_android.R;
import com.sc2guide.sc2_guides_android.adapter.GuideAdapter;
import com.sc2guide.sc2_guides_android.data.model.Guide;
import com.sc2guide.sc2_guides_android.view.MainActivity;
import com.sc2guide.sc2_guides_android.view.guides.GuideDetailFragment;
import com.sc2guide.sc2_guides_android.viewmodel.ProfileViewModel;

import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;

    private GuideAdapter adapter;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
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
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadMyGuides();
        setUpAdapter();
        setUpRecyclerView();
    }

    private void loadMyGuides() {
        ((MainActivity) Objects.requireNonNull(getActivity())).getProgressBar().setVisibility(View.VISIBLE);

        mViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        mViewModel.setAuthorId(((MainActivity)getActivity()).getUserId());
        mViewModel.getGuides(false).observe(this, guides-> {
            // update UI
            updateRecycler(guides);
        });
    }

    private void updateRecycler(List<Guide> guides) {
        adapter.setGuides(guides);
        ((MainActivity) Objects.requireNonNull(getActivity())).getProgressBar().setVisibility(View.INVISIBLE);

    }

    private void setUpAdapter() {
        adapter = new GuideAdapter(/** First param **/new GuideAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Guide guide) {
                GuideDetailFragment guideDetail = GuideDetailFragment.newInstance("GUIDE DETAIL", guide);
                try {
                    ((MainActivity) getActivity()).navigateToFragment(guideDetail, "GUIDE_DETAIL_FRAG");
                } catch (NullPointerException e) {
                    Log.d("Null Pointer Exception", "GuideListFragment.setUpAdapter.OnItemClick.mainActivity.navigateToFragment");
                    e.printStackTrace();
                }
            }
        },/** Second param **/new GuideAdapter.OnItemLongClickListener() {
            @Override
            public void OnItemLongClick(Guide guide) {
                Toast.makeText(getActivity(), "Long clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = getView().findViewById(R.id.profile_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);   // set guide adapter to view
    }

}
