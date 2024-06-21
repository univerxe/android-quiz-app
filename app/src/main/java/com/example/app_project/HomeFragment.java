package com.example.app_project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HomeFragment extends Fragment {

    private TextView textViewWord, textViewWordOfTheDay;
    private CardView cardView;
    private Button buttonPrevious, buttonNext;
    private List<Vocab> vocabList;
    private List<Vocab> dictionaryList;
    private int currentIndex = 0;
    private boolean isShowingKorean = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        textViewWord = view.findViewById(R.id.textViewWord);
        textViewWordOfTheDay = view.findViewById(R.id.textViewWordOfTheDay);
        cardView = view.findViewById(R.id.cardView);
        buttonPrevious = view.findViewById(R.id.buttonPrevious);
        buttonNext = view.findViewById(R.id.buttonNext);

        try {
            loadVocabulary();
            loadDictionary();
            showRandomWordOfTheDay();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        updateWord();

        cardView.setOnClickListener(v -> {
            if (isShowingKorean) {
                textViewWord.setText(vocabList.get(currentIndex).getEnglish());
                isShowingKorean = false;
            } else {
                textViewWord.setText(vocabList.get(currentIndex).getKorean());
                isShowingKorean = true;
            }
        });

        buttonPrevious.setOnClickListener(v -> {
            if (currentIndex > 0) {
                currentIndex--;
                updateWord();
            }
        });

        buttonNext.setOnClickListener(v -> {
            if (currentIndex < vocabList.size() - 1) {
                currentIndex++;
                updateWord();
            }
        });

        return view;
    }

    private void updateWord() {
        isShowingKorean = true;
        textViewWord.setText(vocabList.get(currentIndex).getKorean());
    }

    private void loadVocabulary() throws IOException, JSONException {
        vocabList = new ArrayList<>();
        InputStream is = getContext().getAssets().open("vocabulary.json");
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        String json = new String(buffer, StandardCharsets.UTF_8);
        JSONObject jsonObject = new JSONObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray("words");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            String korean = obj.getString("korean");
            String english = obj.getString("english");
            vocabList.add(new Vocab(korean, english));
        }
    }

    private void loadDictionary() throws IOException, JSONException {
        dictionaryList = new ArrayList<>();
        InputStream is = getContext().getAssets().open("dictionary.json");
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        String json = new String(buffer, StandardCharsets.UTF_8);
        JSONObject jsonObject = new JSONObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray("words");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            String korean = obj.getString("korean");
            String english = obj.getString("english");
            dictionaryList.add(new Vocab(korean, english));
        }
    }

    private void showRandomWordOfTheDay() {
        if (dictionaryList != null && !dictionaryList.isEmpty()) {
            Random random = new Random();
            int randomIndex = random.nextInt(dictionaryList.size());
            Vocab wordOfTheDay = dictionaryList.get(randomIndex);
            String wordOfTheDayText = "Word of the Day: " + wordOfTheDay.getKorean() + " - " + wordOfTheDay.getEnglish();
            textViewWordOfTheDay.setText(wordOfTheDayText);
        }
    }
}
