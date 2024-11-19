package com.lazamelezi.mynotes;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.lazamelezi.mynotes.database.AppDatabase;
import com.lazamelezi.mynotes.database.NoteDao;
import com.lazamelezi.mynotes.database.NoteEntity;
import com.lazamelezi.mynotes.utillities.SampleData;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {

    public static final String TAG = "Junit";
    private AppDatabase mAppDatabase;
    private NoteDao noteDao;


    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        mAppDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        noteDao = mAppDatabase.noteDao();
        Log.i(TAG, "createDb");
    }

    @After
    public void closeDb() {
        mAppDatabase.close();
        Log.i(TAG, "closeDb");
    }

    @Test
    public void createAndRetrieveNotes() {
        noteDao.insertAll(SampleData.getNotes());
        int count = noteDao.getCount();
        Log.i(TAG, "createAndRetrieveNotes: count = " + count);
        assertEquals(SampleData.getNotes().size(), count);
    }

    @Test
    public void compareStrings() {

        noteDao.insertAll(SampleData.getNotes());
        NoteEntity original = SampleData.getNotes().get(0);
        NoteEntity fromDb = noteDao.getNoteById(1);
        assertEquals(original.getText(), fromDb.getText());
        assertEquals(1, fromDb.getId());

    }

}
