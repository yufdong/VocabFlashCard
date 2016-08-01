package com.yufandong.vocabflashcard.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.yufandong.vocabflashcard.data.DBUtility;
import com.yufandong.vocabflashcard.listadapter.MemListAdapter;
import com.yufandong.vocabflashcard.R;
import com.yufandong.vocabflashcard.model.VocabSet;
import com.yufandong.vocabflashcard.model.Word;
import com.yufandong.vocabflashcard.model.WordCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MemorizeActivity extends AppCompatActivity {

    public static final String SET_ID_KEY = "key_id_key";

    private DBUtility dbUtility;
    private String setName;
    private VocabSet vocabSet;
    private List<WordCard> wordSet;
    private ListView listView;
    private MemListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memorize);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        long setId = intent.getLongExtra(SET_ID_KEY, 0L);

        dbUtility = new DBUtility(this);
        vocabSet = dbUtility.getVocabSet(setId);

        setName = vocabSet.getName();
        wordSet = new ArrayList<>(vocabSet.getList().size());
        for (Word word : vocabSet.getList()) {
            wordSet.add(new WordCard(word));
        }
//        logList();

        randomizeList(wordSet);
//        logList();

        listView = (ListView) findViewById(R.id.listView);
        listAdapter = new MemListAdapter(this, wordSet);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listAdapter.toggleWord(position);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_memorize, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_text_input:
                Intent i = new Intent(this, TextInputActivity.class);
                i.putExtra("vocabSet", vocabSet);
                startActivity(i);
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    public static <E> void randomizeList(List<E> list) {
        Random random = new Random();

        for (int i = list.size() - 1; i > 0; i--) {
            int rnum = random.nextInt(i + 1);
            E temp = list.get(i);
            list.set(i, list.get(rnum));
            list.set(rnum, temp);
        }
    }

    private void logList() {
        Log.d("MEM_LIST", setName);
        for (WordCard word : wordSet) {
            Log.d("MEM_LIST", word.getWord().getFront() + " : " + word.getWord().getBack());
        }
    }


}
