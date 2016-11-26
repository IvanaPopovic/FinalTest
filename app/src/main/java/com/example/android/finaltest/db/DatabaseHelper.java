package com.example.android.finaltest.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.android.finaltest.db.model.Note;
import com.example.android.finaltest.db.model.Data;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;



import java.sql.SQLException;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper{

    private static final String DATABASE_NAME    = "finaltest.db";
    private static final int    DATABASE_VERSION = 1;

    private Dao<Data, Integer> mDataDao = null;
    private Dao<Note, Integer> mNoteDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Data.class);
            TableUtils.createTable(connectionSource, Note.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Data.class, true);
            TableUtils.dropTable(connectionSource, Note.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Dao<Data, Integer> getDataDao() throws SQLException {
        if (mDataDao == null) {
            mDataDao = getDao(Data.class);
        }

        return mDataDao;
    }

    public Dao<Note, Integer> getNoteDao() throws SQLException {
        if (mNoteDao == null) {
            mNoteDao = getDao(Note.class);
        }

        return mNoteDao;
    }

    //obavezno prilikom zatvarnaj rada sa bazom osloboditi resurse
    @Override
    public void close() {
        mDataDao = null;
        mNoteDao = null;

        super.close();
    }
}
