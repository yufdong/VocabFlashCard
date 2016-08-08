package com.yufandong.vocabflashcard.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.yufandong.vocabflashcard.R;
import com.yufandong.vocabflashcard.data.DatabaseManager;
import com.yufandong.vocabflashcard.dialog.EditFlashcardDialogFragment;
import com.yufandong.vocabflashcard.exception.DatabaseUpdateException;
import com.yufandong.vocabflashcard.listadapter.EditFlashcardsListAdapter;
import com.yufandong.vocabflashcard.model.VocabSet;
import com.yufandong.vocabflashcard.model.Flashcard;

/**
 * Activity that let's the user select a flashcard to edit.
 */
public class EditFlashcardsActivity extends AppCompatActivity implements EditFlashcardDialogFragment.DialogCallback {

    public static final String SET_ID_KEY = "set_id_key";

    private ListView editList;
    private EditFlashcardsListAdapter listAdapter;
    private VocabSet vocabSet;
    private DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_flashcards);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseManager = new DatabaseManager(this);
        vocabSet = fetchVocabSet();
        setupListView();
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

    private VocabSet fetchVocabSet() {
        Intent intent = getIntent();
        long setId = intent.getLongExtra(SET_ID_KEY, 0L);

        return databaseManager.getVocabSet(setId);
    }

    private void setupListView() {
        editList = (ListView) findViewById(R.id.edit_list);
        listAdapter = new EditFlashcardsListAdapter(this, vocabSet);
        editList.setAdapter(listAdapter);
    }

    /**
     * Callback from the dialog to update the edited flashcard in the database and in the UI.
     */
    @Override
    public void updateFlashcard(Flashcard newFlashcard) {
        try {
            databaseManager.updateFlashCard(newFlashcard);
        } catch (DatabaseUpdateException e) {
            Log.e(this.getClass().getSimpleName(), String.format("Exception occurred while trying to update the new " +
                    "flashcard: [%s, %s] in the database.", newFlashcard.getFront(), newFlashcard.getBack()), e);
            Toast.makeText(this, "ERROR: Cannot update flashcard in Database", Toast.LENGTH_SHORT).show();
        }

        for (Flashcard flashcard : vocabSet.getList()) {
            if (flashcard.getId() == newFlashcard.getId()) {
                flashcard.setFront(newFlashcard.getFront());
                flashcard.setBack(newFlashcard.getBack());
                listAdapter.notifyDataSetChanged();
                break;
            }
        }
    }
}
