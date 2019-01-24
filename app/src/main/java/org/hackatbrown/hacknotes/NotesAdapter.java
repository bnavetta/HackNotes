package org.hackatbrown.hacknotes;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.hackatbrown.hacknotes.data.Note;
import org.hackatbrown.hacknotes.data.NoteDiffCallback;

import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

/**
 * NotesAdapter is a RecyclerView adapter for displaying a list of notes.
 */
public class NotesAdapter extends ListAdapter<Note, NotesAdapter.NoteViewHolder> {
    public NotesAdapter() {
        // NoteDiffCallback tells ListAdapter (a kind of Adapter) how to decide if an entry in the
        // list has changed and needs to be redisplayed.
        super(new NoteDiffCallback());
    }

    // RecyclerView will call this as needed to create ViewHolders for additional items.
    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // LayoutInflater is the class responsible for turning a layout, like notes_list_item.xml
        // into a View we can use
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_list_item, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        // This method is called every time an entry in the list changes, so we can update the UI
        // with its new value.
        holder.bind(getItem(position));
    }

    // The ViewHolder controls how the views for individual items behave and stores
    // RecyclerView-specific state.
    public class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final Button title;
        private long noteId;

        public NoteViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.noteTitle); // Hold on to the title so we can set its text
            title.setOnClickListener(this); // Unlike before, we're explicitly setting the click listener
        }

        public void bind(Note note) {
            title.setText(note.getTitle());
            this.noteId = note.getId(); // Save the note ID to use in the onClick callback
        }

        // This is called whenever the note's button is clicked.
        @Override
        public void onClick(View v) {
            // Just like in NotesListActivity, we use an Intent to start EditNoteActivity
            Intent intent = new Intent(v.getContext(), EditNoteActivity.class);
            intent.putExtra("org.hackatbrown.hacknotes.NOTE_ID", noteId);
            v.getContext().startActivity(intent);
        }
    }
}
