package com.example.android.finaltest.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.android.finaltest.db.DatabaseHelper;
import com.example.android.finaltest.R;
import com.example.android.finaltest.db.model.Note;
import com.example.android.finaltest.dialog.AboutDialog;
import com.example.android.finaltest.preferences.Preferences;
import com.j256.ormlite.android.apptools.OpenHelperManager;


import java.sql.SQLException;
import java.util.List;

/**
 * Created by android on 26.11.16..
 */
public class MainListActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private SharedPreferences prefs;

    public static String NOTE_KEY = "ACTOR_KEY";
    public static String NOTIF_TOAST = "notif_toast";
    public static String NOTIF_STATUS = "notif_statis";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_list_activity_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if(toolbar != null) {
            setSupportActionBar(toolbar);
        }

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        final ListView listView = (ListView) findViewById(R.id.note_list);

        try {
            List<Note> list = getDatabaseHelper().getNoteDao().queryForAll();

            ListAdapter adapter = new ArrayAdapter<>(MainListActivity.this, R.layout.list_item, list);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Note p = (Note) listView.getItemAtPosition(position);

                    Intent intent = new Intent(MainListActivity.this, DetailActivity.class);
                    intent.putExtra(NOTE_KEY, p.getmId());
                    startActivity(intent);
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
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

    }
}
