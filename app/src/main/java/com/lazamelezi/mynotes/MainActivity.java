package com.lazamelezi.mynotes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lazamelezi.mynotes.database.NoteEntity;
import com.lazamelezi.mynotes.ui.NotesAdapter;
import com.lazamelezi.mynotes.utillities.SampleData;
import com.lazamelezi.mynotes.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;

    private List<NoteEntity> notesData = new ArrayList<>();
    private NotesAdapter notesAdapter;
    private MainViewModel mainViewModel;
    private AlertDialog dialog;

    @OnClick(R.id.fab)
    void fabClickHandler() {
        Intent intent = new Intent(this, EditorActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        notesData.addAll(SampleData.getNotes());

        for (NoteEntity note : notesData) {
            Log.i("MyNotesV2Log", note.toString());
        }

        initRecyclerView();
        initViewModel();


    }

    private void setItemTouchHelper(RecyclerView recycler_view) {

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                dialogDelete(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recycler_view);

    }

    private void dialogDelete(@NonNull int position) {
        dialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Delete Note")
                .setMessage("Are you sure you wanna delete this Note?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mainViewModel.deleteNote(position);
                    }
                })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        notesAdapter.cancelDelete(position);

                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        notesAdapter.cancelDelete(position);
                    }
                })
                .show();
    }

    private void initViewModel() {

        final Observer<List<NoteEntity>> notesObserver = new Observer<List<NoteEntity>>() {
            @Override
            public void onChanged(List<NoteEntity> noteEntities) {
                notesData.clear();

                if (noteEntities != null) {
                    notesData.addAll(noteEntities);
                }

                if (notesAdapter == null) {

                    notesAdapter = new NotesAdapter(MainActivity.this, notesData);
                    recycler_view.setAdapter(notesAdapter);

                    notesAdapter.setLongClickListener(new NotesAdapter.LongItemClickListener() {
                        @Override
                        public void onLongItemClick(View view, int position) {
                            dialogDelete(position);
                            Log.i("LongClickLog", String.valueOf(position));

                        }
                    });

                } else {
                    notesAdapter.notifyDataSetChanged();
                }

            }
        };

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.mNotes.observe(this, notesObserver);


    }

    private void initRecyclerView() {

        recycler_view.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycler_view.setLayoutManager(layoutManager);

        DividerItemDecoration divider = new DividerItemDecoration(recycler_view.getContext(), layoutManager.getOrientation());
        recycler_view.addItemDecoration(divider);
        setItemTouchHelper(recycler_view);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_sample_data) {
            addSampleData();
            return true;
        } else if (id == R.id.action_delete_all) {
            deleteAllNotes();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteAllNotes() {
        mainViewModel.deleteAllNotes();
    }

    private void addSampleData() {
        mainViewModel.addSampleData();
    }
}