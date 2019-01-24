package org.hackatbrown.hacknotes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.hackatbrown.hacknotes.data.NotesDatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NotesListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);

        RecyclerView notesList = findViewById(R.id.notesRecyclerView);
        // The LayoutManager tells the RecyclerView how to position all the individual items
        notesList.setLayoutManager(new LinearLayoutManager(this));

        // RecyclerView needs an Adapter to control _how_ each item is displayed
        NotesAdapter adapter = new NotesAdapter();
        notesList.setAdapter(adapter);

        NotesDatabase.getInstance(this).all().observe(this, adapter::submitList);
    }

    public void onNewNote(View button) {
        Toast.makeText(getApplicationContext(), "Creating note!", Toast.LENGTH_SHORT).show();

        // First, create the note using our NotesDatabase helper. We provide the rest of the code
        // as a callback because Android apps aren't allowed to do possibly-slow things like database
        // access where it could block the UI. The helper creates a note and then calls back to
        // our code when it's done.
        NotesDatabase.getInstance(this).create(note -> {
            // Then, use an Intent to ask Android to switch to EditNoteActivity
            Intent intent = new Intent(this, EditNoteActivity.class);
            // Pass the note's ID into the intent so EditNoteActivity can look it up
            intent.putExtra("org.hackatbrown.hacknotes.NOTE_ID", note.getId());
            // And make the switch!
            startActivity(intent);
        });
    }
}
