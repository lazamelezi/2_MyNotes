package com.lazamelezi.mynotes.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lazamelezi.mynotes.EditorActivity;
import com.lazamelezi.mynotes.R;
import com.lazamelezi.mynotes.database.NoteEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.lazamelezi.mynotes.utillities.Constants.NOTE_ID_KEY;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    private final List<NoteEntity> mNotes;
    private final Context context;
    private LongItemClickListener mClickListener;

    public NotesAdapter(Context context, List<NoteEntity> mNotes) {
        this.mNotes = mNotes;
        this.context = context;
    }

    public void setLongClickListener(LongItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public NotesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.note_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final NoteEntity note = mNotes.get(position);
        holder.note_text.setText(note.getText());

        holder.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditorActivity.class);
                intent.putExtra(NOTE_ID_KEY, note.getId());
                context.startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mClickListener != null)
                    mClickListener.onLongItemClick(v, position);
                return false;
            }
        });

    }

    public void cancelDelete(int position) {
        notifyItemChanged(position);
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    public interface LongItemClickListener {
        void onLongItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.note_text)
        TextView note_text;

        @BindView(R.id.fab)
        FloatingActionButton fab;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

    }
}
