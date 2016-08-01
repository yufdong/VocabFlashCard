package com.yufandong.vocabflashcard.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.yufandong.vocabflashcard.R;
import com.yufandong.vocabflashcard.data.DBUtility;
import com.yufandong.vocabflashcard.dialog.EditWordDialogFragment;
import com.yufandong.vocabflashcard.listadapter.EditListAdapter;
import com.yufandong.vocabflashcard.model.VocabSet;
import com.yufandong.vocabflashcard.model.Word;

public class SetEditActivity extends AppCompatActivity implements EditWordDialogFragment.DialogCallback {

    public static final String SET_ID_KEY = "set_id_key";
    private ListView editList;
    private EditListAdapter listAdapter;
    private VocabSet vocabSet;
    private DBUtility dbUtility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        long setId = intent.getLongExtra(SET_ID_KEY, 0L);

        dbUtility = new DBUtility(this);
        vocabSet = dbUtility.getVocabSet(setId);

        editList = (ListView) findViewById(R.id.edit_list);
        listAdapter = new EditListAdapter(this, vocabSet);
        editList.setAdapter(listAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_set_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_save:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateWord(Word newWord) {
        dbUtility.updateWord(newWord);

        for (Word word : vocabSet.getList()) {
            if (word.getId() == newWord.getId()) {
                word.setFront(newWord.getFront());
                word.setBack(newWord.getBack());
                listAdapter.notifyDataSetChanged();
                break;
            }
        }
    }
}
