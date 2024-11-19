package com.lazamelezi.mynotes.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.lazamelezi.mynotes.database.AppRepository;
import com.lazamelezi.mynotes.database.NoteEntity;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class EditorViewModel extends AndroidViewModel {

    public MutableLiveData<NoteEntity> mLiveNote = new MutableLiveData<>();
    private AppRepository appRepository;
    private Executor executor = Executors.newSingleThreadExecutor();

    public EditorViewModel(Application application) {
        super(application);

        appRepository = AppRepository.getInstance(getApplication());

    }

    public void loadData(final int noteId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                NoteEntity note = appRepository.getNoteById(noteId);
                mLiveNote.postValue(note);
            }
        });
    }

    public void saveNote(String noteText) {

        NoteEntity note = mLiveNote.getValue();

        if (note == null) {
            if (TextUtils.isEmpty(noteText.trim()))
                return;
            note = new NoteEntity(new Date(), noteText.trim());
        } else {
            note.setText(noteText.trim());
        }

        appRepository.insertNote(note);
    }

    public void deleteNote() {
        appRepository.deleteNote(mLiveNote.getValue());
    }

}
