package com.yufandong.vocabflashcard.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.yufandong.vocabflashcard.R;
import com.yufandong.vocabflashcard.data.DatabaseManager;
import com.yufandong.vocabflashcard.listadapter.MemorizeListAdapter;
import com.yufandong.vocabflashcard.model.VocabSet;
import com.yufandong.vocabflashcard.utility.ListUtility;

/**
 * Activity that displays a list of words on "flashcards". The user can set the default side that is up and they can
 * tap on the word to reveal the other side.
 */
public class MemorizeActivity extends AppCompatActivity {

    public static final String SET_ID_KEY = "key_id_key";

    private VocabSet vocabSet;
    private ListView listView;
    private MemorizeListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_memorize);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        vocabSet = fetchVocabSet();
        ListUtility.randomizeList(vocabSet.getList());

        setupListView();
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
                Intent i = new Intent(this, VocabQuizActivity.class);
                i.putExtra("vocabSet", vocabSet);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private VocabSet fetchVocabSet() {
        Long setId = getIntent().getLongExtra(SET_ID_KEY, 0L);
        DatabaseManager databaseManager = new DatabaseManager(this);
        return databaseManager.getVocabSet(setId);
    }

    private void setupListView() {
        listView = (ListView) findViewById(R.id.listView);
        listAdapter = new MemorizeListAdapter(this, vocabSet.getList());
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listAdapter.toggleFlashcard(position);
            }
        });
    }
}
