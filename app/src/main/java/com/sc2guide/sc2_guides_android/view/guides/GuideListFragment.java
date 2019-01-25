package com.sc2guide.sc2_guides_android.view.guides;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.sc2guide.sc2_guides_android.viewmodel.AllGuideViewModel;
import com.sc2guide.sc2_guides_android.viewmodel.RaceGuideViewModel;

import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link GuideListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GuideListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PRIMARY_COLOR = "PRIMARY_COLOR";
    private static final String ARG_GRADIENT = "GRADIENT";
    private static final String ARG_TITLE = "TITLE";
    private static final String ARG_SUBTITLE = "SUBTITLE";
    private static final String ARG_RACE = "RACE_NAME";

    private int primary_color;
    private int gradient;
    private String title;
    private String subtitle;
    private String race;
    private boolean isLoading = true;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RaceGuideViewModel raceViewModel;
    private AllGuideViewModel allGuideViewModel;
    private GuideAdapter adapter;


    //TODO: !important : dont load all..
    public GuideListFragment() {
        // Required empty public constructor
    }

    /**
     * @param primaryColor primary color of the race
     * @param gradient     the color gradient in the drawer header
     * @param title        title of the guide
     * @param subtitle     subtitle of guide
     * @param race         1 of 3 ("Zerg","Protoss","Terran")
     * @param fragName     name of the fragment
     * @return fragment
     */
    public static GuideListFragment newInstance(int primaryColor, int gradient, String title, String subtitle,
                                                String race, String fragName) {
        GuideListFragment fragment = new GuideListFragment();
        Bundle args = new Bundle();
        // put things here
        args.putInt(ARG_PRIMARY_COLOR, primaryColor);
        args.putInt(ARG_GRADIENT, gradient);
        args.putString(ARG_TITLE, title);
        args.putString("FRAGMENT_NAME", fragName);
        args.putString(ARG_SUBTITLE, subtitle);
        args.putString(ARG_RACE, race);
        //
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            primary_color = getArguments().getInt("PRIMARY_COLOR");
            gradient = getArguments().getInt("GRADIENT");
            title = getArguments().getString("TITLE");
            subtitle = getArguments().getString("SUBTITLE");
            race = getArguments().getString("RACE_NAME");
        }
    }

    /**
     * @param guide the list of all the guides
     * @effects: display all the guides
     * make progress bar invisible after
     */
    private void updateUI(List<Guide> guide) {
        adapter.setGuides(guide);
        ((MainActivity) Objects.requireNonNull(getActivity())).getProgressBar().setVisibility(View.INVISIBLE);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_guide_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // change UI for fragment
        ((MainActivity) Objects.requireNonNull(getActivity())).getProgressBar().setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).changeUIColors(primary_color, gradient);
        // handle what happens when user pull to refresh
        mSwipeRefreshLayout = Objects.requireNonNull(getView()).findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         *
         * set refreshing = true and load all the guides again
         */
        mSwipeRefreshLayout.post(() -> { // load first time
            mSwipeRefreshLayout.setRefreshing(true);
            // Fetching data from server
            loadGuides(false);
            isLoading = false;
            mSwipeRefreshLayout.setRefreshing(false);

        });
        //
        setUpAdapter(); // set up adapter to load guides in the recycler view
        setUpRecyclerView(); // set up recycler view
    }

    /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true); // display refreshing spinning icon
        isLoading = true;
        // Fetching data from firebase server
        loadGuides(true); // TODO: change back to loadGuide() later
        mSwipeRefreshLayout.setRefreshing(false);
        isLoading = false;
    }

    private void loadMoreGuides() {
        // TODO: does not work the first time/ only after the second
        if (race.equals("All") /** when all guides is selected => race = 'All' */) {
            allGuideViewModel = ViewModelProviders.of(this).get(AllGuideViewModel.class);
            allGuideViewModel.getMoreGuides().observe(this, guide -> {
                updateUI(guide);
            });
        } else {
            // load view model for race view model
            raceViewModel = ViewModelProviders.of(this).get(RaceGuideViewModel.class);
            raceViewModel.getMoreRaceGuides(race).observe(this, guide -> {
                updateUI(guide);
            });
        }
    }

    private void loadGuides(boolean forceUpdate) {
        // get data from view model
        if (race.equals("All") /** when all guides is selected => race = 'All' */) {
            // load view model for all guides view model
            allGuideViewModel = ViewModelProviders.of(this).get(AllGuideViewModel.class);
            allGuideViewModel.getAllGuides(forceUpdate).observe(this, guide -> {
                updateUI(guide);
            });
        } else {
            // load view model for race view model
            raceViewModel = ViewModelProviders.of(this).get(RaceGuideViewModel.class);
            raceViewModel.getRaceGuides(race, forceUpdate).observe(this, guide -> {
                updateUI(guide);
            });
        }
    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = getView().findViewById(R.id.all_guides_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);   // set guide adapter to view
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // TODO: load more is a little bit laggy
                int itemCount = adapter.getItemCount();
                int visibleThreshold = 100;
                int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (itemCount <= (lastVisibleItem + visibleThreshold) && !isLoading) {
                    ((MainActivity)getActivity()).getProgressBar().setVisibility(View.VISIBLE);
                    loadMoreGuides();
                    ((MainActivity)getActivity()).getProgressBar().setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    public GuideAdapter getAdapter() {
        return adapter;
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


    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setActionBarInfo(title, subtitle);
        ((MainActivity) getActivity()).getFab().show();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((MainActivity) getActivity()).getFab().hide();
        ((MainActivity) getActivity()).getProgressBar().setVisibility(View.INVISIBLE);
    }
}
