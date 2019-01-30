package com.sc2guide.sc2_guides_android.view.guides;

import android.animation.Animator;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sc2guide.sc2_guides_android.R;
import com.sc2guide.sc2_guides_android.adapter.GuideDetailBodyItemAdapter;
import com.sc2guide.sc2_guides_android.controller.FirebaseController;
import com.sc2guide.sc2_guides_android.data.model.Guide;
import com.sc2guide.sc2_guides_android.data.model.GuideBodyItem;
import com.sc2guide.sc2_guides_android.repo.SavedGuidesRepository;
import com.sc2guide.sc2_guides_android.view.MainActivity;

import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GuideDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GuideDetailFragment extends Fragment {
    private static final String FRAG_NAME = "FRAGMENT_NAME";
    private static final String GUIDE_OBJ = "GUIDE_OBJECT";

    private FloatingActionButton fab;
    private boolean isFabOpen = false;
    private boolean isFabAnimating = false;
    private FloatingActionButton fab_save;
    private FloatingActionButton fab_edit;
    private FloatingActionButton fab_delete;

    private RecyclerView bodyItemsContainer;
    private TextView title;
    private TextView authorName;
    private TextView date;

    private GuideDetailBodyItemAdapter guideDetailBodyItemAdapter;
    private SavedGuidesRepository savedGuidesRepository;

    private String fragName;
    private Guide guide;


    public GuideDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param fragName Parameter 1.
     * @param guide    Parameter 2.
     * @return A new instance of fragment AllDetailFragment.
     */
    public static GuideDetailFragment newInstance(String fragName, Guide guide) {
        GuideDetailFragment fragment = new GuideDetailFragment();
        Bundle args = new Bundle();
        // all the params
        args.putString(FRAG_NAME, fragName);
        args.putSerializable(GUIDE_OBJ, guide);
        //
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fragName = getArguments().getString(FRAG_NAME);
            guide = (Guide) getArguments().getSerializable(GUIDE_OBJ);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_guide_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //
        mapVariables();
        setUpAdapter();
        loadGuideDetails();

        FloatingActionButton fab = ((MainActivity) getActivity()).getFab();
        fab.hide();
    }

    private void setUpAdapter() {
        guideDetailBodyItemAdapter = new GuideDetailBodyItemAdapter();

        bodyItemsContainer.setHasFixedSize(false);
        bodyItemsContainer.setAdapter(guideDetailBodyItemAdapter);
        bodyItemsContainer.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void mapVariables() {
        fab = getView().findViewById(R.id.guide_detail_fab);
        fab_save = getView().findViewById(R.id.guide_detail_fab_save);
        fab_edit = getView().findViewById(R.id.guide_detail_fab_edit);
        fab_delete = getView().findViewById(R.id.guide_detail_fab_delete);

        fab.setOnClickListener(v -> {
            if (!isFabOpen) {
                showFabMenu();
            } else {
                closeFabMenu();
            }
        });
        savedGuidesRepository = new SavedGuidesRepository(getContext()); // TODO:migrate

        // set on click after animation finishes
        fab_save.setOnClickListener(v -> {
            // TODO: implement save db to local/ online db
            SavedGuidesRepository.CheckGuideExistAsyncTask task = new SavedGuidesRepository.CheckGuideExistAsyncTask(new SavedGuidesRepository.CheckGuideExistAsyncTask.CallBackReceiver2() {

                @Override
                public void receiveData(String s) {
                    if(s == null) {
                        Toast.makeText(getActivity(), "not saved", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Already saved", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            task.execute(guide.getId());


        });

        if (!((MainActivity) Objects.requireNonNull(getActivity())).getUserId().equals(guide.getAuthorId())) {
            fab_delete.setColorFilter(Color.GRAY);
            fab_edit.setColorFilter(Color.GRAY);
        } else {
            fab_edit.setOnClickListener(v -> {
                // TODO: implement
                // !important : this is only for testing purposes
                // clicking edit will clear all room db entries
                savedGuidesRepository.nukeTable();
            });

            fab_delete.setOnClickListener(v -> {
                // prompt dialog box to delete guide first
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); // TODO: make separate frag in the future
                builder.setMessage(R.string.dialog_delete_mess);
                // Add the buttons
                builder.setPositiveButton("CONFIRM", (dialog, id) -> {
                    // User clicked OK button
                    onDeleteConfirm();
                });
                builder.setNegativeButton("CANCEL", (dialog, id) -> {
                    // User cancelled the dialog
                    dialog.dismiss();
                });

                // Set other dialog properties
                // Create the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();
            });
        }

        bodyItemsContainer = getView().findViewById(R.id.guide_detail_body);
        title = getView().findViewById(R.id.guide_detail_title);
        authorName = getView().findViewById(R.id.guide_detail_author);
        date = getView().findViewById(R.id.guide_detail_date);
    }

    private void onDeleteConfirm() {
        FirebaseController con = new FirebaseController();
        con.deleteGuide(guide, o -> {
            savedGuidesRepository.deleteGuide(guide); // Delete the guide in offline db
            if (getFragmentManager() != null) {
                getFragmentManager().popBackStack();
            }
            Toast.makeText(getActivity(), "Guide deleted" , Toast.LENGTH_SHORT).show();
        }, e -> {
            Toast.makeText(getActivity(), "Cant delete: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        });
    }

    private void showFabMenu() {
        if (isFabAnimating) {
            return;
        }
        isFabOpen = true;
        fab.animate().rotationBy(45).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isFabAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isFabAnimating = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                isFabAnimating = false;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        fab_save.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        fab_edit.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
        fab_delete.animate().translationY(-getResources().getDimension(R.dimen.standard_155));

    }

    private void closeFabMenu() {
        if (isFabAnimating) {
            return;
        }
        isFabOpen = false;
        fab.animate().rotationBy(-45).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isFabAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isFabAnimating = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                isFabAnimating = false;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        fab_save.animate().translationY(0);
        fab_edit.animate().translationY(0);
        fab_delete.animate().translationY(0);
        // may need to hide it too

    }


    private void loadGuideDetails() {
        title.setText(guide.getTitle());
        loadGuideBodyItems();
        authorName.append(guide.getAuthorName());
        date.append(guide.getDate());
    }

    private void loadGuideBodyItems() {
        List<GuideBodyItem> guideBodyItemList = guide.getGuideBodyItems();
        guideDetailBodyItemAdapter.setGuideBodyItems(guideBodyItemList);
    }

    @Override
    public void onResume() {
        super.onResume();
        fab.show();
        fab_edit.show();
        fab_save.show();
        fab_delete.show();
    }

    @Override
    public void onStop() {
        super.onStop();
        fab.hide();
        fab_edit.hide();
        fab_save.hide();
        fab_delete.hide();
    }
}
