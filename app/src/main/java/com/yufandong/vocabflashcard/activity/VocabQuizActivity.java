package com.yufandong.vocabflashcard.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yufandong.vocabflashcard.R;
import com.yufandong.vocabflashcard.model.VocabSet;
import com.yufandong.vocabflashcard.model.Flashcard;

import java.util.List;

/**
 * Activity that let's the user input answers for flashcards to "quiz" themselves. A message will be displayed to
 * notify the user if the answer is correct or not.
 */
public class VocabQuizActivity extends AppCompatActivity {

    private static final String CORRECT_MSG = "Correct!";
    private static final String WRONG_MSG = "Incorrect!";

    private List<Flashcard> flashcardList;
    private TextView testWord;
    private TextView evalText;
    private EditText editText;
    private int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocab_quiz);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        VocabSet vocabSet = (VocabSet) intent.getSerializableExtra("vocabSet");
        flashcardList = vocabSet.getList();

        currentIndex = 0;

        setupTextViews();
    }

    private void setupTextViews() {
        testWord = (TextView) findViewById(R.id.testWord);
        evalText = (TextView) findViewById(R.id.evalText);
        editText = (EditText) findViewById(R.id.editText);

        testWord.setText(flashcardList.get(0).getFront());

        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    evaluateAndSetMessage();
                    return true;
                }
                return false;
            }
        });
    }

    private void evaluateAndSetMessage() {
        if (evaluateWord(editText.getText().toString(), flashcardList.get(currentIndex).getBack())) {
            evalText.setText(CORRECT_MSG);
            evalText.setTextColor(getResources().getColor(R.color.correctWord));
        }
        else {
            evalText.setText(WRONG_MSG);
            evalText.setTextColor(getResources().getColor(R.color.incorrectWord));
        }
    }

    private boolean evaluateWord(String input, String answer) {
        String regex = ".*(\\(|\\)| |~|\\.).*";
        String filteredInput = input.replaceAll(regex, "");
        String filteredAnswer = answer.replaceAll(regex, "");
        return filteredInput.equalsIgnoreCase(filteredAnswer);
    }

    /**
     * onclick method for the Give Up button
     */
    public void giveUp(View view) {
        evalText.setText(flashcardList.get(currentIndex).getBack());
        evalText.setTextColor(Color.GRAY);
    }

    /**
     * onclick method for the Next button
     */
    public void nextFlashcard(View view) {
        currentIndex++;
        if (currentIndex >= flashcardList.size()) {
            this.finish();
            return;
        }
        evalText.setText("");
        testWord.setText(flashcardList.get(currentIndex).getFront());
        editText.setText("");
    }
}
