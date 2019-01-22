package com.sc2guide.sc2_guides_android.view.guides;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.sc2guide.sc2_guides_android.R;
import com.sc2guide.sc2_guides_android.adapter.CreateGuideBodyItemAdapter;
import com.sc2guide.sc2_guides_android.adapter.helper.SimpleItemTouchHelperCallback;
import com.sc2guide.sc2_guides_android.controller.FirebaseController;
import com.sc2guide.sc2_guides_android.data.model.Guide;
import com.sc2guide.sc2_guides_android.view.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateGuideFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateGuideFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
// TODO: !important : update new features:
// TODO: clicking to create guide too fast before the guide list load leads to the progress bar not cancelled
/**
 * 1. add timing (with time/ drone count)/ note(* yellow box)/ normal description (normal text)
 * 2. drag to change position
 */
// TODO: maybe to activity instead ?
public class CreateGuideFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private Spinner spinner;
    private Spinner spinner_op;
    private ProgressBar progressBar;

    private EditText guideTitle;
    private Button guideCreateBtn;
    private Button addNoteBtn;
    private Button addDescBtn;
    private RecyclerView recyclerView;
    // helper
    private ItemTouchHelper mItemTouchHelper;
    private CreateGuideBodyItemAdapter createGuideBodyItemAdapter;
    // Strings
    private String myRace;
    private String opRace;
    // controller
    private FirebaseController mFirebaseController;

    public CreateGuideFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CreateGuideFragment.
     */
    public static CreateGuideFragment newInstance() {
        CreateGuideFragment fragment = new CreateGuideFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        mFirebaseController = new FirebaseController();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_guide, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Set up spinner
        setUpSpinner();
        setUpMapVariable();
        setUpHideKeyBoard(); // behind map variable
        setUpButtons();

        /**
         * set up itemTouchHelper for the list of notes/desc/timing
         */
        createGuideBodyItemAdapter = new CreateGuideBodyItemAdapter();
        //
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(createGuideBodyItemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(createGuideBodyItemAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void setUpButtons() {
        // set up the buttons
        addNoteBtn.setOnClickListener(v -> {
            addNote();
        });
        addDescBtn.setOnClickListener(v -> {
            addDesc();
        });
        guideCreateBtn.setOnClickListener(v -> {
            createGuide();
        });
    }

    private void addNote() {
        // TODO: change color of item
        LinearLayout mLayout = Objects.requireNonNull(getView()).findViewById(R.id.create_guide_add_note_layout);
        // TODO: rename linearlayout + change layout to prettier
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setWeightSum(100f);
        EditText mEditTxt = new EditText(getActivity());
        mEditTxt.setHint("Add Note here...");
        mEditTxt.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus) { hideKeyBoard(); }
        });
        mEditTxt.setSingleLine(false);
        mEditTxt.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        Button confirm = new Button(getActivity());
        confirm.setText(getResources().getString(R.string.confirm_button));
        confirm.setOnClickListener(v -> {
            String body = mEditTxt.getText().toString();

            if (body.length() == 0) {
                Toast.makeText(getActivity(), "Fill the void pls :( ", Toast.LENGTH_SHORT).show();
                return;
            }
            String type = getResources().getString(R.string.guide_body_item_type_note);
            createGuideBodyItemAdapter.addItem(type, body);

            mLayout.removeView(linearLayout);
        });
        linearLayout.addView(mEditTxt);
        linearLayout.addView(confirm);
        // add the linear layout
        mLayout.addView(linearLayout);
    }

    private void addDesc() {
        LinearLayout mLayout = Objects.requireNonNull(getView()).findViewById(R.id.create_guide_add_note_layout);
        // TODO: rename linearlayout + change layout to prettier
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setWeightSum(100f);
        EditText mEditTxt = new EditText(getActivity());
        mEditTxt.setHint("Add description here...");
        mEditTxt.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus) { hideKeyBoard(); }
        });
        mEditTxt.setSingleLine(false);
        mEditTxt.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        Button confirm = new Button(getActivity());
        confirm.setText("Confirm");
        confirm.setOnClickListener(v -> {
            String body = mEditTxt.getText().toString();

            if (body.length() == 0) {
                Toast.makeText(getActivity(), "Fill the void pls :( ", Toast.LENGTH_SHORT).show();
                return;
            }
            String type = getResources().getString(R.string.guide_body_item_type_desc);
            createGuideBodyItemAdapter.addItem(type, body);
            //
            mLayout.removeView(linearLayout);
        });
        linearLayout.addView(mEditTxt);
        linearLayout.addView(confirm);
        // add the linear layout
        mLayout.addView(linearLayout);
    }

    /**
     * @effects: main method to handle adding the guide to firebase database
     */
    private void createGuide() {
        // Manage what happens if user click confirm to create guide
        updateProgressBarAndBtn(Color.GRAY, true);
        // author information
        // TODO: bundle to user information rather than this
        String userEmail = ((MainActivity) Objects.requireNonNull(getActivity())).getUserEmail();
        String uid = ((MainActivity) getActivity()).getUserId();
        // init the guide to the model
        Guide guide;
        // get current date
        String strCurrentDate = getCurrentDate();
        //
        try {
            guide = new Guide("0", guideTitle.getText().toString(), myRace, opRace ,
                    uid, userEmail, createGuideBodyItemAdapter.getItems(), strCurrentDate);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Some fields are empty", Toast.LENGTH_SHORT).show();
            // if not successful then change back the UI and return from the method
            updateProgressBarAndBtn(Color.GREEN, false);
            return;
        }
        // insert the guide to firebase database
        mFirebaseController.insertGuide(guide, task -> {
            if(task.isSuccessful()){
                Toast.makeText(getActivity(), "Guide created", Toast.LENGTH_SHORT).show();
                try {
                    GuideListFragment frag = GuideListFragment.getInstance();
                    frag.getAdapter().notifyItemInserted(frag.getAdapter().getItemCount());

                } catch (Exception e) {
                    System.err.print("CreateGuideFragment.createGuide().mFirebaseController.notifyItemInserted");
                    e.printStackTrace();
                }
            } else {
                updateProgressBarAndBtn(Color.GREEN, false);
                Toast.makeText(getActivity(), "Error! not created", Toast.LENGTH_SHORT).show();
            }
        });
        updateProgressBarAndBtn(Color.GREEN, false);
    }

    private String getCurrentDate() {
        Date date = new Date();
        Date newDate = new Date(date.getTime() + (604800000L * 2) + (24 * 60 * 60));
        SimpleDateFormat dt = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
        return dt.format(newDate);


    }

    /**
     * @effects: handle UI change for progress bar and button colr
     *  used in {@code: this.handleCreateGuide()}
     * @param btnColor
     * @param isVisible
     */
    private void updateProgressBarAndBtn (int btnColor, boolean isVisible) {
        guideCreateBtn.setBackgroundColor(btnColor);
        if (isVisible) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }


    /**
     * @effects: map variables to layout components
     *  used in {@code: this.onViewCreated}
     */
    private void setUpMapVariable () {
        //
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0,10,0,10);
        //
        guideTitle = Objects.requireNonNull(getView()).findViewById(R.id.create_guide_title);
        recyclerView = getView().findViewById(R.id.create_guides_recyler_view);
        addNoteBtn = getView().findViewById(R.id.create_guide_add_note);
        addDescBtn = getView().findViewById(R.id.create_guide_add_desc);
        guideCreateBtn = getView().findViewById(R.id.create_guide_button);
        progressBar = getView().findViewById(R.id.create_guide_progress);

    }

    /**
     * @effects: set up options for the 2 spinners
     *  used in {@code: this.onViewCreated}
     */
    private void setUpSpinner() {
        // spinner for my race
        spinner = (Spinner) Objects.requireNonNull(getView()).findViewById(R.id.create_guide_spinner_my_race);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getActivity()),
                R.array.race_option, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        // Spinner for the opponent race
        spinner_op = (Spinner) getView().findViewById(R.id.create_guide_spinner_enemy_race);
        ArrayAdapter<CharSequence> adapter_op = ArrayAdapter.createFromResource(getActivity(),
                R.array.race_option, android.R.layout.simple_spinner_item);
        adapter_op.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_op.setAdapter(adapter_op);
        spinner_op.setOnItemSelectedListener(this);

    }

    /**
     * @effects: hide key board on user press outside of the text input fields
     */
    private void setUpHideKeyBoard() {
        guideTitle.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) hideKeyBoard();
            }
        });
    }

    private void hideKeyBoard() {
        ((MainActivity) getActivity()).hideKeyboard(getView());
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) Objects.requireNonNull(getActivity())).getFab().hide();
        ((MainActivity) getActivity()).setActionBarInfo("Create Guide", "Please not a cheese, I beg you");
    }

    @Override
    public void onStop() {
        super.onStop();
        ((MainActivity) Objects.requireNonNull(getActivity())).getFab().show();

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == spinner.getId()) {
            myRace = parent.getItemAtPosition(position).toString();
            Toast.makeText(getActivity(), "PICKED :" + myRace, Toast.LENGTH_SHORT).show();
        }
        if(parent.getId() == spinner_op.getId()) {
            opRace = parent.getItemAtPosition(position).toString();
            Toast.makeText(getActivity(), "PICKED :" + opRace, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent){
        Toast.makeText(getActivity(), "Please choose an option", Toast.LENGTH_SHORT).show();
    }
}
