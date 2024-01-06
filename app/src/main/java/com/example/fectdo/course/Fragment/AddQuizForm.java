package com.example.fectdo.course.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fectdo.R;
import com.example.fectdo.Soalan.QuizManager;
import com.example.fectdo.course.Activity.AddCourse;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddQuizForm#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddQuizForm extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnDataPassListener onDataPassListener;
    private QuizManager quizManager;
    LinearLayout layout;
    List<String> correctAnswer;
    List<View> viewList;
    View thisView;

    Button addButton, deleteButton, doneButton;

    public AddQuizForm() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddQuizForm.
     */
    // TODO: Rename and change types and number of parameters
    public static AddQuizForm newInstance(String param1, String param2) {
        AddQuizForm fragment = new AddQuizForm();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        thisView = inflater.inflate(R.layout.fragment_add_quiz_form, container, false);
        quizManager = new QuizManager();
        layout = thisView.findViewById(R.id.layout);
        correctAnswer = new ArrayList<>();
        viewList = new ArrayList<>();
        quizManager = new QuizManager();

        addButton = thisView.findViewById(R.id.addButton);
        addButton.setOnClickListener(this::onClick);
        deleteButton = thisView.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(this::onClick);

        doneButton = thisView.findViewById(R.id.endButton);
        doneButton.setOnClickListener(this::onClick);

        // Inflate the layout for this fragment
        return thisView;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.endButton){
            saveData();
            if (onDataPassListener != null) {
                onDataPassListener.onDataPass(quizManager);
            }
            Toast.makeText(getApplicationContext(), "Question uploaded", Toast.LENGTH_SHORT).show();
            finish();
        }
        else if(v.getId() == R.id.addButton){
            addQuestionForm();
            Toast.makeText(getApplicationContext(), "New question form added", Toast.LENGTH_SHORT).show();
        }
        else if(v.getId() == R.id.deleteButton){
            if(viewList.size() == 0){
                Toast.makeText(getApplicationContext(), "There is no question yet", Toast.LENGTH_SHORT).show();
            }
            else{
                deleteQuestionForm();
                Toast.makeText(getApplicationContext(), "Last question form deleted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addQuestionForm(){
        View view = getLayoutInflater().inflate(R.layout.layout_quiz_form, null);
        int index = viewList.size();
        correctAnswer.add(index, "");

        TextView title = view.findViewById(R.id.tvTitle);
        title.setText("Question " +(index+1));

        TextView choice = view.findViewById(R.id.tvCorrectChoice);
        choice.setText("Please choose you correct answer");
        TextView question = view.findViewById(R.id.etQuestion);

        Button[] choiceButtons = new Button[]{
                view.findViewById(R.id.choice1).findViewById(R.id.choiceButton),
                view.findViewById(R.id.choice2).findViewById(R.id.choiceButton),
                view.findViewById(R.id.choice3).findViewById(R.id.choiceButton),
                view.findViewById(R.id.choice4).findViewById(R.id.choiceButton)
        };
        EditText[] choices = new EditText[]{
                view.findViewById(R.id.choice1).findViewById(R.id.editText),
                view.findViewById(R.id.choice2).findViewById(R.id.editText),
                view.findViewById(R.id.choice3).findViewById(R.id.editText),
                view.findViewById(R.id.choice4).findViewById(R.id.editText)
        };
        setupChoiceButton(index, choiceButtons, choices, choice);

        viewList.add(view);
        layout.addView(view);
    }

    private void setupChoiceButton(int index, Button[] choiceButtons, EditText[] choices, TextView choice){
        for (int i = 0; i < choiceButtons.length; i++) {
            final int clickedBtnIndex = i;
            choiceButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int j = 0; j < choiceButtons.length; j++) {
                        choiceButtons[j].setActivated(j == clickedBtnIndex);
                    }

                    correctAnswer.set(index, choices[clickedBtnIndex].getText().toString());
                    String message = "Choices: " +choices[clickedBtnIndex].getText().toString();
                    choice.setText(message);
                }
            });
        }
    }

    private void deleteQuestionForm(){
        correctAnswer.remove(viewList.size()-1);
        layout.removeView(viewList.remove(viewList.size()-1));
    }

    private void saveData(){
        View currentView;
        EditText choices;
        String[] choicesAns;
        TextView question, choice;
        for(int i=0 ; i<viewList.size() ; i++){
            currentView = viewList.get(i);
            question = currentView.findViewById(R.id.etQuestion);
            choice = currentView.findViewById(R.id.tvCorrectChoice);
            choicesAns = new String[4];

            //choice A
            choices = currentView.findViewById(R.id.choice1).findViewById(R.id.editText);
            choicesAns[0] = choices.getText().toString();

            //choice B
            choices = currentView.findViewById(R.id.choice2).findViewById(R.id.editText);
            choicesAns[1] = choices.getText().toString();

            //choice C
            choices = currentView.findViewById(R.id.choice3).findViewById(R.id.editText);
            choicesAns[2] = choices.getText().toString();

            //choice D
            choices = currentView.findViewById(R.id.choice4).findViewById(R.id.editText);
            choicesAns[3] = choices.getText().toString();

            quizManager.addQuestion(i,
                    question.getText().toString(),
                    choice.getText().toString(),
                    choicesAns);
        }
    }

    private AddCourse getApplicationContext(){
        Context context = getActivity();
        if(context != null){
            if(context instanceof AddCourse){
                return (AddCourse) context;
            }
        }
        return null;
    }

    public void finish(){
        // Inside the fragment
        getApplicationContext().enableActivity();
        FragmentTransaction transaction = requireFragmentManager().beginTransaction();
        transaction.remove(this);
        transaction.commit();
    }

    public QuizManager getQuizManager() {
        return quizManager;
    }

    public interface OnDataPassListener{
        void onDataPass(QuizManager quizManager);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            onDataPassListener = (OnDataPassListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnDataPassListener");
        }
    }
}