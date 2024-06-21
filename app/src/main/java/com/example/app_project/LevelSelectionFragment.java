package com.example.app_project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class LevelSelectionFragment extends Fragment {

    private CardView cardViewEasy;
    private CardView cardViewIntermediate;
    private CardView cardViewAdvanced;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_level_selection, container, false);

        cardViewEasy = view.findViewById(R.id.cardViewEasy);
        cardViewIntermediate = view.findViewById(R.id.cardViewIntermediate);
        cardViewAdvanced = view.findViewById(R.id.cardViewAdvanced);

        cardViewEasy.setOnClickListener(v -> {
            hideCards();
            startQuiz("easy");
        });

        cardViewIntermediate.setOnClickListener(v -> {
            hideCards();
            startQuiz("intermediate");
        });

        cardViewAdvanced.setOnClickListener(v -> {
            hideCards();
            startQuiz("advanced");
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        showCards();
    }

    private void hideCards() {
        cardViewEasy.setVisibility(View.GONE);
        cardViewIntermediate.setVisibility(View.GONE);
        cardViewAdvanced.setVisibility(View.GONE);
    }

    private void showCards() {
        cardViewEasy.setVisibility(View.VISIBLE);
        cardViewIntermediate.setVisibility(View.VISIBLE);
        cardViewAdvanced.setVisibility(View.VISIBLE);
    }

    private void startQuiz(String level) {
        Bundle bundle = new Bundle();
        bundle.putString("level", level);

        QuizFragment quizFragment = new QuizFragment();
        quizFragment.setArguments(bundle);

        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, quizFragment)
                .addToBackStack(null)
                .commit();
    }
}
