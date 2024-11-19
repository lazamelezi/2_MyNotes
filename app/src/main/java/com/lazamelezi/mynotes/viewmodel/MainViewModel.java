package com.lazamelezi.mynotes.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lazamelezi.mynotes.database.AppRepository;
import com.lazamelezi.mynotes.database.NoteEntity;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    public LiveData<List<NoteEntity>> mNotes;
    private AppRepository mAppRepository;

    public MainViewModel(@NonNull Application application) {
        super(application);

        mAppRepository = AppRepository.getInstance(application.getApplicationContext());
        mNotes = mAppRepository.mNotes;

    }

    public void addSampleData() {
        mAppRepository.addSampleDate();
    }

    public void deleteAllNotes() {
        mAppRepository.deleteAllNotes();
    }

    public void deleteNote(int index) {
        mAppRepository.deleteNote(mNotes.getValue().get(index));
    }

}
