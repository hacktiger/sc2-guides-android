package com.sc2guide.sc2_guides_android.view.guides;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
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
import android.widget.TextView;
import android.widget.Toast;

import com.sc2guide.sc2_guides_android.MainActivity;
import com.sc2guide.sc2_guides_android.R;
import com.sc2guide.sc2_guides_android.controller.FirebaseController;
import com.sc2guide.sc2_guides_android.data.model.Guide;

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

public class CreateGuideFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private Spinner spinner;
    private Spinner spinner_op;
    private ProgressBar progressBar;

    private EditText guideTitle;
    private EditText guideBody;
    private Button guideCreateBtn;
    private Button addTimingBtn;
    private Button addNoteBtn;
    private Button addDescBtn;
    private LinearLayout linearLayout;
    private LinearLayout.LayoutParams layoutParams;

    private String myRace;
    private String opRace;

    private OnFragmentInteractionListener mListener;

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

        // set up the buttons
        addTimingBtn.setOnClickListener(v -> {
            addTiming();
        });
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

    // TODO: 1. add tag to keep track of the edit texts
    // TODO: 2. add a way to edit?/save?/delete the newly added stuffs
    // TODO: 3. find a way to save to database
    private void addTiming() {
        // edit text for worker count
        EditText timingEditTxt = new EditText(getActivity());
        timingEditTxt.setHint("Worker/Time");  //TODO: add number only
    }

    private void addNote() {
        //TODO: too long message makes the button disappear
        // working on this
        LinearLayout newBoxLayout = new LinearLayout(getActivity());
        newBoxLayout.setOrientation(LinearLayout.HORIZONTAL);
        newBoxLayout.setWeightSum(100f);
        // TODO: set edit text width to max
        EditText bodyEditTxt = new EditText(getActivity());
        bodyEditTxt.setHint("Add Note here");
        bodyEditTxt.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) hideKeyBoard();
        }); // hide keyboard when click outside of the edit text
        bodyEditTxt.setSingleLine(false); // set to multi lines
        bodyEditTxt.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        // set the weight for the edit text in the linear layout
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight = 96f;
        bodyEditTxt.setLayoutParams(params);
        // things for the confirm button
        Button confirm = new Button(getActivity());
        confirm.setText("Confirm");
        confirm.setOnClickListener(v -> {
            onConfirmAddNote(newBoxLayout, bodyEditTxt, confirm, params); // My custom method
        });
        // add the edit text and button to the linear layout
        newBoxLayout.addView(bodyEditTxt);
        newBoxLayout.addView(confirm);
        // add the new item (a new note - newBoxLayout) to the main linear layout
        linearLayout.addView(newBoxLayout, layoutParams);
    }

    private void onConfirmAddNote(LinearLayout newBoxLayout, EditText bodyEditTxt, Button confirm, LinearLayout.LayoutParams params) {
        // clicking the add button to add the notice
        String text = bodyEditTxt.getText().toString();
        // remove the edit text
        // TODO: change text view styling
        TextView k = new TextView(getActivity());
        k.setText(text);
        newBoxLayout.removeView(bodyEditTxt);
        newBoxLayout.removeView(confirm);
        // confirm button -> edit button with new functions
        confirm.setText("edit");
        confirm.setOnClickListener(view -> {
            // do something to edit
        });
        newBoxLayout.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.note_container));
        // set layout
        k.setLayoutParams(params);
        // TODO: change layout styling
        newBoxLayout.addView(k);
        newBoxLayout.addView(confirm);
    }

    private void addDesc() {

    }

    private void createGuide() {
        // Manage what happens if user click confirm to create guide
        Log.d("ZZLLL","STARTED");
        updateUI(Color.GRAY, true);
        // author information
        String userEmail = ((MainActivity) Objects.requireNonNull(getActivity())).getUserEmail();
        String uid = ((MainActivity) getActivity()).getUserId();
        // TODO: change to user name instead of email later
        // init the guide to the model
        Guide guide;
        try {
            guide = new Guide(guideTitle.getText().toString(), guideBody.getText().toString(), myRace, opRace ,uid, userEmail);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("ZZLLL", e.getMessage());
            Toast.makeText(getActivity(), "Please fill the guide", Toast.LENGTH_SHORT).show();
            // if not successful then change back the UI and return from the method
            updateUI(Color.GREEN, false);
            return;
        }
        // insert the guide to firebase database
        mFirebaseController.insertGuide(guide, task -> {
            if(task.isSuccessful()){
                Toast.makeText(getActivity(), "Guide created", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Error! not created", Toast.LENGTH_SHORT).show();
            }
        });
        Log.d("ZZLLL","OK");
        updateUI(Color.GREEN, false);
    }

    /**
     * @effects: handle UI change for progress bar and button colr
     *  used in {@code: this.handleCreateGuide()}
     * @param btnColor
     * @param isVisible
     */
    private void updateUI (int btnColor, boolean isVisible) {
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
        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0,10,0,10);
        //
        guideTitle = Objects.requireNonNull(getView()).findViewById(R.id.create_guide_title);
        guideBody = getView().findViewById(R.id.create_guide_body);
        linearLayout = getView().findViewById(R.id.create_guides_linear_layout);
        addTimingBtn = getView().findViewById(R.id.create_guide_add_timing);
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
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
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

//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

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

        guideBody.setOnFocusChangeListener(new View.OnFocusChangeListener(){
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
        ((MainActivity) getActivity()).getFab().hide();
        ((MainActivity) getActivity()).setActionBarInfo("Create Guide", "Please not a cheese, I beg you");
    }

    @Override
    public void onStop() {
        super.onStop();
        ((MainActivity) getActivity()).getFab().show();

    }

    //    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
