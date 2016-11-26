package com.example.android.finaltest.db.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
/**
 * Created by android on 26.11.16..
 */
@DatabaseTable(tableName = Data.TABLE_NAME_USERS)
public class Data {

    public static final String TABLE_NAME_USERS = "data";
    public static final String FIELD_NAME_ID     = "id";
    public static final String TABLE_DATA_NAME = "name";
    public static final String TABLE_DATA_DESCRIPTION = "description";
    public static final String FIELD_NAME_NOTE  = "note";

    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int mId;

    @DatabaseField(columnName = TABLE_DATA_NAME)
    private String mName;

    @DatabaseField(columnName = TABLE_DATA_DESCRIPTION)
    private String mDescription;

    @DatabaseField(columnName = FIELD_NAME_NOTE, foreign = true, foreignAutoRefresh = true)
    private Note mNote;

    public Data() {
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public Note getmNote() {
        return mNote;
    }

    public void setmNote(Note mNote) {
        this.mNote = mNote;
    }

    @Override
    public String toString() {
        return mName;
    }
}
