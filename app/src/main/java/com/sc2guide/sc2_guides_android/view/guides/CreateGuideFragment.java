package com.sc2guide.sc2_guides_android.view.guides;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.sc2guide.sc2_guides_android.MainActivity;
import com.sc2guide.sc2_guides_android.R;
import com.sc2guide.sc2_guides_android.controller.FirebaseController;
import com.sc2guide.sc2_guides_android.data.model.Guide;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateGuideFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateGuideFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class CreateGuideFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private Spinner spinner;
    private Spinner spinner_op;
    private ProgressBar progressBar;

    private EditText guideTitle;
    private EditText guideBody;
    private Button guideCreateBtn;
    private String my_race;
    private String op_race;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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

        guideCreateBtn.setOnClickListener(v -> {
            try {
                handleCreateGuide();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void handleCreateGuide() throws Exception {
        // Manage what happens if user click confirm to create guide
        updateUI(Color.GRAY, true);
        //
        String userEmail = ((MainActivity) getActivity()).getUserEmail();
        String uid = ((MainActivity) getActivity()).getUserId();
        // TODO: change to user name instead of email later
        Guide guide = new Guide(guideTitle.getText().toString(), guideBody.getText().toString(), my_race, op_race ,uid, userEmail);
        mFirebaseController.insertGuide(guide, task -> {
            if(task.isSuccessful()){
                Toast.makeText(getActivity(), "Guide created", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Error! not created", Toast.LENGTH_SHORT).show();
            }
        });

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
        guideTitle = getView().findViewById(R.id.create_guide_title);
        guideBody = getView().findViewById(R.id.create_guide_body);
        guideCreateBtn = getView().findViewById(R.id.create_guide_button);
        progressBar = getView().findViewById(R.id.create_guide_progress);
    }

    /**
     * @effects: set up options for the 2 spinners
     *  used in {@code: this.onViewCreated}
     */
    private void setUpSpinner() {
        // spinner for my race
        spinner = (Spinner) getView().findViewById(R.id.create_guide_spinner_my_race);
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

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
            my_race = parent.getItemAtPosition(position).toString();
            Toast.makeText(getActivity(), "PICKED :" + my_race, Toast.LENGTH_SHORT).show();
        }
        if(parent.getId() == spinner_op.getId()) {
            op_race = parent.getItemAtPosition(position).toString();
            Toast.makeText(getActivity(), "PICKED :" + op_race, Toast.LENGTH_SHORT).show();
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
