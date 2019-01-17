package org.hackatbrown.hacknotes.data;

import android.content.Context;

import java.time.Instant;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = { Note.class }, version = 1, exportSchema = false)
@TypeConverters({ Converters.class })
public abstract class NotesDatabase extends RoomDatabase {
    private static NotesDatabase INSTANCE;

    abstract NotesDao notesDao();

    public static NotesDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (NotesDatabase.class) {
                // Re-check in case it were set while we synchronized
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), NotesDatabase.class, "hacknotes").build();
                }
            }
        }

        return INSTANCE;
    }

    public LiveData<List<Note>> all() {
        return notesDao().getAll();
    }

    public LiveData<Note> note(long id) {
        return notesDao().getNote(id);
    }

    public Note create() {
        Note note = new Note();
        note.setLastUpdated(Instant.now());
        note.setId(notesDao().insertNote(note));
        return note;
    }

    public void save(Note note) {
        note.setLastUpdated(Instant.now());
        notesDao().saveNote(note);
    }

    public void delete(Note note) {
        notesDao().deleteNote(note);
    }
}
