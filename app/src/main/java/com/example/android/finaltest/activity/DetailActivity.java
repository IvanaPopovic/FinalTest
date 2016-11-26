package com.example.android.finaltest.activity;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Movie;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
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
    public static String NOTIF_TOAST = "notif_toast";
    public static String NOTIF_STATUS = "notif_statis";

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

    private void showStatusMesage(String message){
        NotificationManager mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.ic_launcher);
        mBuilder.setContentTitle("Final test");
        mBuilder.setContentText(message);

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_add);

        mBuilder.setLargeIcon(bm);
        // notificationID allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());
    }

    private void showMessage(String message){
        //provera podesavanja
        boolean toast = prefs.getBoolean(NOTIF_TOAST, false);
        boolean status = prefs.getBoolean(NOTIF_STATUS, false);

        if (toast){
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }

        if (status){
            showStatusMesage(message);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.id_add:
                //OTVORI SE DIALOG UNESU SE INFORMACIJE
                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.add_data);

                Button add = (Button) dialog.findViewById(R.id.add_data_btn);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText name = (EditText) dialog.findViewById(R.id.data_name);
                        EditText desc = (EditText) dialog.findViewById(R.id.data_description);

                        Data m = new Data();
                        m.setmName(name.getText().toString());
                        m.setmDescription(desc.getText().toString());
                        m.setmNote(a);

                        try {
                            getDatabaseHelper().getDataDao().create(m);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        //URADITI REFRESH
                        refresh();

                        showMessage("New data added to note");

                        dialog.dismiss();
                    }
                });

                dialog.show();

                break;
            case R.id.id_edit:
                //POKUPITE INFORMACIJE SA EDIT POLJA
                a.setmName(name.getText().toString());
                a.setmDescription(desc.getText().toString());

                try {
                    getDatabaseHelper().getNoteDao().update(a);

                    showMessage("Note data updated");

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.id_remove:
                try {
                    getDatabaseHelper().getNoteDao().delete(a);

                    showMessage("Note deleted");

                    finish(); //moramo pozvati da bi se vratili na prethodnu aktivnost
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
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
