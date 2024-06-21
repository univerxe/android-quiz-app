package com.example.app_project;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class QuizFragment extends Fragment {

    private String level;
    private int currentQuestionIndex = 0;
    private int score = 0;

    private TextView questionTextView;
    private RadioGroup optionsRadioGroup;
    private Button nextButton;

    private List<Question> questions;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);

        questionTextView = view.findViewById(R.id.text_question);
        optionsRadioGroup = view.findViewById(R.id.radio_group_options);
        nextButton = view.findViewById(R.id.button_next);

        if (getArguments() != null) {
            level = getArguments().getString("level");
        }

        try {
            questions = loadQuestionsFromJson(getContext(), level);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        if (questions != null) {
            displayQuestion();
        }

        nextButton.setOnClickListener(v -> {
            checkAnswer();
            currentQuestionIndex++;
            if (currentQuestionIndex < questions.size()) {
                displayQuestion();
            } else {
                showResult();
            }
        });

        return view;
    }

    private List<Question> loadQuestionsFromJson(Context context, String level) throws JSONException, IOException {
        List<Question> questionList = new ArrayList<>();
        InputStream is = context.getAssets().open("questions.json");
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        String json = new String(buffer, StandardCharsets.UTF_8);
        JSONObject jsonObject = new JSONObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray(level);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            String questionText = obj.getString("question");
            JSONArray optionsArray = obj.getJSONArray("options");
            String[] options = new String[optionsArray.length()];
            for (int j = 0; j < optionsArray.length(); j++) {
                options[j] = optionsArray.getString(j);
            }
            int correctAnswer = obj.getInt("correctAnswer");
            questionList.add(new Question(questionText, options, correctAnswer));
        }

        return questionList;
    }

    private void displayQuestion() {
        Question question = questions.get(currentQuestionIndex);
        questionTextView.setText(question.getQuestion());
        optionsRadioGroup.removeAllViews();
        for (int i = 0; i < question.getOptions().length; i++) {
            RadioButton radioButton = new RadioButton(getContext());
            radioButton.setText(question.getOptions()[i]);
            radioButton.setId(i);
            optionsRadioGroup.addView(radioButton);
        }
    }

    private void checkAnswer() {
        int selectedId = optionsRadioGroup.getCheckedRadioButtonId();
        if (selectedId == questions.get(currentQuestionIndex).getCorrectAnswer()) {
            score++;
        }
    }

    private void showResult() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Quiz Completed");
        builder.setMessage("Your score: " + score + "/" + questions.size());
        builder.setPositiveButton("Retry", (dialog, which) -> {
            // Reset the quiz
            currentQuestionIndex = 0;
            score = 0;
            displayQuestion();
        });
        builder.setNegativeButton("Go Back", (dialog, which) -> {
            // Send result to LevelSelectionFragment to show buttons
            Bundle result = new Bundle();
            result.putString("action", "showButtons");
            getParentFragmentManager().setFragmentResult("requestKey", result);
            // Dismiss the dialog and go back to the level selection screen
            getParentFragmentManager().popBackStack();
        });
        builder.setCancelable(false);
        builder.show();
    }
}
