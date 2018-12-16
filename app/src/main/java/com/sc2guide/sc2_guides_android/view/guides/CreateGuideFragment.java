package com.sc2guide.sc2_guides_android.view.guides;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sc2guide.sc2_guides_android.MainActivity;
import com.sc2guide.sc2_guides_android.R;
import com.sc2guide.sc2_guides_android.data.model.Guide;
import com.sc2guide.sc2_guides_android.data.model.UserModel;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateGuideFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateGuideFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

// TODO: !important : Split the firebase stuff to somewhere else
public class CreateGuideFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    private Spinner spinner;
    private Spinner spinner_op;
    private ProgressBar progressBar;

    private EditText guideTitle;
    private EditText guideBody;
    private Button guideCreateBtn;
    private String my_race;
    private String op_race;

    private OnFragmentInteractionListener mListener;

    private FirebaseDatabase firebaseDatabase;

    public CreateGuideFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CreateGuideFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateGuideFragment newInstance() {
        CreateGuideFragment fragment = new CreateGuideFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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

        guideCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleCreateGuide();
            }
        });
    }

    private void handleCreateGuide() {
        // Manage what happens if user click confirm to create guide
        String guideDatabaseReference = "guides";

        guideCreateBtn.setBackgroundColor(Color.GRAY);
        progressBar.setVisibility(View.VISIBLE);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference mDatabase = firebaseDatabase.getReference(guideDatabaseReference);
        Activity main = getActivity();

        String userEmail = ((MainActivity) main).getUserEmail();
        String uid = ((MainActivity) main).getUserId();

        Guide guide = new Guide(guideTitle.getText().toString(), guideBody.getText().toString(), my_race, op_race ,uid, "Author");

        mDatabase.push().setValue(guide).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getActivity(), "Guide Created", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });

        guideCreateBtn.setBackgroundColor(Color.GREEN);
        progressBar.setVisibility(View.INVISIBLE);
    }



    private void setUpMapVariable () {
        guideTitle = getView().findViewById(R.id.create_guide_title);
        guideBody = getView().findViewById(R.id.create_guide_body);
        guideCreateBtn = getView().findViewById(R.id.create_guide_button);
        progressBar = getView().findViewById(R.id.create_guide_progress);
    }

    private void setUpSpinner() {
        spinner = (Spinner) getView().findViewById(R.id.create_guide_spinner_my_race);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.race_option, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
