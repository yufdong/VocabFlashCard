package com.yufandong.vocabflashcard.activity;

import android.app.Activity;
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

import com.yufandong.vocabflashcard.R;
import com.yufandong.vocabflashcard.data.DatabaseManager;
import com.yufandong.vocabflashcard.exception.DatabaseInsertException;
import com.yufandong.vocabflashcard.listadapter.VocabSetPickerListAdapter;
import com.yufandong.vocabflashcard.model.VocabSet;
import com.yufandong.vocabflashcard.utility.XmlUtility;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Starting activity that displays a list of vocab sets the user can choose to start memorizing or to edit.
 */
public class VocabSetPickerActivity extends AppCompatActivity {

    private Activity activity;
    private List<VocabSet> vocabSetList;
    private ListView listView;
    private DatabaseManager databaseManager;
    private VocabSetPickerListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocab_set_picker);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        activity = this;

        databaseManager = new DatabaseManager(this);
        vocabSetList = databaseManager.getAllVocabSets();
        setupListView();
    }

    private void popSets(int rid) {
        try {
            VocabSet set = XmlUtility.parse(rid, activity);
            if(set != null) {
                databaseManager.addVocabSet(set);
            }
        } catch (IOException | XmlPullParserException e) {
            Log.e(this.getClass().getSimpleName(), "Exception occurred while parsing XML.", e);
        } catch (DatabaseInsertException e) {
            Log.e(this.getClass().getSimpleName(), "Exception occurred while adding words from XML to database.", e);
            Toast.makeText(this, "ERROR: Cannot Insert to Database", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupListView() {
        listView = (ListView) findViewById(R.id.setList);

        listAdapter = new VocabSetPickerListAdapter(this, vocabSetList);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VocabSet selectedSet = vocabSetList.get(position);
                Intent intent = new Intent(activity, MemorizeActivity.class);
                intent.putExtra(MemorizeActivity.SET_ID_KEY, selectedSet.getId());
                startActivity(intent);
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_set_picker, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id) {
            case R.id.action_load_xml:
                Field[] ID_Fields = R.raw.class.getFields();
                int[] resArray = new int[ID_Fields.length];
                for(int i = 0; i < ID_Fields.length; i++) {
                    try {
                        resArray[i] = ID_Fields[i].getInt(null);
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                        Log.e(this.getClass().getSimpleName(), "Exception while trying to access XML files", e);
                    }
                }
                for(int i = 0; i < resArray.length; i++) {
                    if(resArray[i] > 0) {
                        popSets(resArray[i]);
                    }
                }

                vocabSetList = databaseManager.getAllVocabSets();
                setupListView();

                return true;
            case R.id.action_reset:
                databaseManager.resetDatabase();
                vocabSetList = databaseManager.getAllVocabSets();
                setupListView();
                return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
