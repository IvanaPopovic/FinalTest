package com.example.android.finaltest.db.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by android on 26.11.16..
 */
@DatabaseTable(tableName = Note.TABLE_NAME_USERS)
public class Note {
    public static final String TABLE_NAME_USERS = "note";

    public static final String FIELD_NAME_ID     = "id";
    public static final String TABLE_NOTE_NAME = "name";
    public static final String TABLE_NOTE_DESCRIPTION = "description";
    public static final String TABLE_NOTE_DATE = "date";
    public static final String TABLE_DATA_DATA = "data";



    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int mId;

    @DatabaseField(columnName = TABLE_NOTE_NAME)
    private String mName;

    @DatabaseField(columnName = TABLE_NOTE_DESCRIPTION)
    private String mDescription;

    @DatabaseField(columnName = TABLE_NOTE_DATE)
    private String mDate;

    @ForeignCollectionField(columnName = Note.TABLE_DATA_DATA, eager = true)
    private ForeignCollection<Data> data;


    public Note() {
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public ForeignCollection<Data> getData() {
        return data;
    }

    public void setData(ForeignCollection<Data> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return mName;
    }
}
