package org.hackatbrown.hacknotes.data;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import androidx.lifecycle.LiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = { Note.class }, version = 1, exportSchema = false)
@TypeConverters({ Converters.class })
public abstract class NotesDatabase extends RoomDatabase {
    private static NotesDatabase INSTANCE;

    private final Executor executor = new ThreadPoolExecutor(1, 4, 5, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

    abstract NotesDao notesDao();

    public static NotesDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (NotesDatabase.class) {
                // Re-check in case it were set while we synchronized
                if (INSTANCE == null) {
                    INSTANCE = Room
                            .databaseBuilder(context.getApplicationContext(), NotesDatabase.class, "hacknotes")
                            .build();
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

    public void create(Consumer<Note> callback) {
        Note note = new Note();
        note.setLastUpdated(Instant.now());
        // Use an AsyncTask so create can be called on the UI thread
        new InsertNoteTask(notesDao(), callback).execute(note);
    }

    public void setTitle(long noteId, String title) {
        executor.execute(() -> {
            Note note = notesDao().getNoteSync(noteId);
            note.setLastUpdated(Instant.now());
            note.setTitle(title);
            Log.i("NotesDatabase", String.format("Changing title of note %d to %s", noteId, title));
            notesDao().saveNote(note);
        });
    }

    public void setContent(long noteId, String content) {
        executor.execute(() -> {
            Note note = notesDao().getNoteSync(noteId);
            note.setLastUpdated(Instant.now());
            note.setContent(content);
            Log.i("NotesDatabase", String.format("Changing content of note %d to %s", noteId, content));
            notesDao().saveNote(note);
        });
    }

    public void save(Note note) {
        note.setLastUpdated(Instant.now());
        // Use an AsyncTask so save can be called on the UI thread
        new SaveNoteTask(notesDao()).execute(note);
    }

    public void delete(Note note) {
        // Use an AsyncTask so delete can be called on the UI thread
        new DeleteNoteTask(notesDao()).execute(note);
    }

    // Task to insert a note, producing the same note but with its ID set
    private static class InsertNoteTask extends AsyncTask<Note, Void, Note> {
        private final NotesDao dao;
        private final Consumer<Note> callback;

        private InsertNoteTask(NotesDao dao, Consumer<Note> callback) {
            this.dao = dao;
            this.callback = callback;
        }

        @Override
        protected Note doInBackground(Note... notes) {
            if (notes.length != 1) throw new AssertionError();
            Note note = notes[0];

            Log.i("NotesDatabase", String.format("Inserting note %s", note));
            note.setId(dao.insertNote(note));
            return note;
        }

        @Override
        protected void onPostExecute(Note note) {
            callback.accept(note);
        }
    }

    private static class SaveNoteTask extends AsyncTask<Note, Void, Void> {
        private final NotesDao dao;

        private SaveNoteTask(NotesDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            for (Note note : notes) {
                Log.i("NotesDatabase", String.format("Saving note %s", note));
                dao.saveNote(note);
            }

            return null;
        }
    }

    private static class DeleteNoteTask extends AsyncTask<Note, Void, Void> {
        private final NotesDao dao;

        private DeleteNoteTask(NotesDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            for (Note note : notes) {
                Log.i("NotesDatabase", String.format("Deleting note %s", note));
                dao.deleteNote(note);
            }
            return null;
        }
    }
}
