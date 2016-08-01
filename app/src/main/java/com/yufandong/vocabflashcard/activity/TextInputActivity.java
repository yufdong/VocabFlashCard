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
import com.yufandong.vocabflashcard.model.Word;
import com.yufandong.vocabflashcard.model.WordCard;

import java.util.ArrayList;
import java.util.List;

public class TextInputActivity extends AppCompatActivity {

    final String CORRECT_MSG = "Correct!";
    final String WRONG_MSG = "Incorrect!";

    String setName;
    List<WordCard> wordSet;
    VocabSet vocabSet;
    TextView testWord;
    TextView evalText;
    EditText editText;

    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_input);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        vocabSet = (VocabSet) intent.getSerializableExtra("vocabSet");
        setName = vocabSet.getName();
        wordSet = new ArrayList<>(vocabSet.getList().size());
        for (Word word : vocabSet.getList()) {
            wordSet.add(new WordCard(word));
        }

        MemorizeActivity.randomizeList(wordSet);
        index = 0;

        testWord = (TextView) findViewById(R.id.testWord);
        evalText = (TextView) findViewById(R.id.evalText);
        editText = (EditText) findViewById(R.id.editText);

        testWord.setText(wordSet.get(0).getWord().getFront());

        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN) {
                    if(keyCode == KeyEvent.KEYCODE_ENTER) {
                        if(evalWord()) {
                            evalText.setText(CORRECT_MSG);
                            evalText.setTextColor(Color.rgb(0, 204, 0));
                        }
                        else {
                            evalText.setText(WRONG_MSG);
                            evalText.setTextColor(Color.RED);
                        };
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private boolean evalWord() {
        String regex = ".*(\\(|\\)| |~|\\.).*";
        String eval = editText.getText().toString().replaceAll(regex, "");
        String answer = wordSet.get(index).getWord().getBack().replaceAll(regex, "");

        if(eval.equalsIgnoreCase(answer)) {
            return true;
        }
        return false;
    }

    public void giveUp(View view) {
        evalText.setText(wordSet.get(index).getWord().getBack());
        evalText.setTextColor(Color.GRAY);
    }

    public void nextWord(View view) {
        index++;
        if(index >= wordSet.size()) {
            this.finish();
            return;
        }
        evalText.setText("");
        testWord.setText(wordSet.get(index).getWord().getFront());
        editText.setText("");
    }
}
