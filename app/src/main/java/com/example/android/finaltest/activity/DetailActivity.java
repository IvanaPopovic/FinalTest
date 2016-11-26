package com.example.android.finaltest.activity;

import android.content.SharedPreferences;
import android.graphics.Movie;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.android.finaltest.db.DatabaseHelper;
import com.example.android.finaltest.db.model.Data;
import com.example.android.finaltest.db.model.Note;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.List;
import com.example.android.finaltest.R;

/**
 * Created by android on 26.11.16..
 */
public class DetailActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private SharedPreferences prefs;
    private Note a;

    private EditText name;
    private EditText desc;
    private EditText date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if(toolbar != null) {
            setSupportActionBar(toolbar);
        }

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int key = getIntent().getExtras().getInt(MainListActivity.NOTE_KEY);

        try {
            a = getDatabaseHelper().getNoteDao().queryForId(key);

            name = (EditText) findViewById(R.id.note_name);
            desc = (EditText) findViewById(R.id.note_description);
            date = (EditText) findViewById(R.id.note_date);

            name.setText(a.getmName());
            desc.setText(a.getmDescription());
            date.setText(a.getmDate());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        final ListView listView = (ListView) findViewById(R.id.note_data_list);

        try {
            List<Data> list = getDatabaseHelper().getDataDao().queryBuilder()
                    .where()
                    .eq(Data.FIELD_NAME_NOTE, a.getmId())
                    .query();

            ListAdapter adapter = new ArrayAdapter<>(this, R.layout.list_item, list);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Data m = (Data) listView.getItemAtPosition(position);
                    Toast.makeText(DetailActivity.this, m.getmName() + " " + m.getmDescription(), Toast.LENGTH_SHORT).show();

                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void refresh() {
        ListView listview = (ListView) findViewById(R.id.note_data_list);

        if (listview != null){
            ArrayAdapter<Data> adapter = (ArrayAdapter<Data>) listview.getAdapter();

            if(adapter!= null)
            {
                try {
                    adapter.clear();
                    List<Data> list = getDatabaseHelper().getDataDao().queryBuilder()
                            .where()
                            .eq(Data.FIELD_NAME_NOTE, a.getmId())
                            .query();

                    adapter.addAll(list);

                    adapter.notifyDataSetChanged();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    //jos koda ovde - DRUGE METODE

    //Metoda koja komunicira sa bazom podataka
    public DatabaseHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // nakon rada sa bazo podataka potrebno je obavezno
        //osloboditi resurse!
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
}}
