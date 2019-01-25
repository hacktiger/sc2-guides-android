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
import com.sc2guide.sc2_guides_android.viewmodel.SavedGuidesViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SavedGuidesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SavedGuidesFragment extends Fragment {

    private SavedGuidesViewModel mViewModel;

    private GuideAdapter adapter;

    public SavedGuidesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SavedGuidesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SavedGuidesFragment newInstance() {
        SavedGuidesFragment fragment = new SavedGuidesFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved_guides, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadSavedGuides();
        setUpAdapter();
        setUpRecyclerView();
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
        RecyclerView recyclerView = getView().findViewById(R.id.saved_guides_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);   // set guide adapter to view
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void loadSavedGuides() {
        mViewModel = ViewModelProviders.of(this).get(SavedGuidesViewModel.class);
        mViewModel.getGuides(getContext()).observe(this, guides-> {
            // update UI
            for(Guide item : guides) {
                Log.d("ZZLL", item.getTitle());
            }
            //
            updateRecycler(guides);
        });
    }

    private void updateRecycler(List<Guide> guides) {
        adapter.setGuides(guides);
        // TODO: add progress bar or smt
    }
}
