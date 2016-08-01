package com.yufandong.vocabflashcard.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.yufandong.vocabflashcard.data.DBUtility;
import com.yufandong.vocabflashcard.R;
import com.yufandong.vocabflashcard.listadapter.SetPickerListAdapter;
import com.yufandong.vocabflashcard.model.VocabSet;
import com.yufandong.vocabflashcard.data.XMLUtility;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Starting activity that displays a list of vocab sets the user can choose or edit.
 */
public class SetPickerActivity extends AppCompatActivity {

    private List<VocabSet> setLists;
    private ListView listView;
    private Activity context;
    private DBUtility dbUtility;
    private SetPickerListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_set_picker);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbUtility = new DBUtility(this);

        setLists = dbUtility.getAllVocabSet();

        createListView();

    }

    private void popSets(int rid) {
        try {
            VocabSet set = XMLUtility.parse(rid, context);
            if(set != null) {
                dbUtility.addVocabSet(set);
            }
        } catch (IOException | XmlPullParserException e) {
            Log.e("ERROR", this.getClass().getSimpleName(), e);
        }


//        if(setLists != null) {
//            for (VocabSet set : setLists) {
//                Log.d("VOCAB_LIST", set.name);
//                for (Word word : set.list) {
//                    Log.d("VOCAB_LIST", word.front + " : " + word.back);
//                }
//            }
//        }
//        else {
//            Log.d("VOCAB_LIST", "Set is empty");
//        }
    }

    private void createListView() {
        listView = (ListView) findViewById(R.id.setList);

        listAdapter = new SetPickerListAdapter(this, setLists);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VocabSet selectedSet = setLists.get(position);
                Intent intent = new Intent(context, MemorizeActivity.class);
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
                        Log.e("ERROR", this.getClass().getSimpleName(), e);
                    }
                }
                for(int i = 0; i < resArray.length; i++) {
                    if(resArray[i] > 0) {
                        popSets(resArray[i]);
                    }
                }

                setLists = dbUtility.getAllVocabSet();
                createListView();

                return true;
            case R.id.action_reset:
                dbUtility.resetDatabase();
                setLists = dbUtility.getAllVocabSet();
                createListView();
                return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
