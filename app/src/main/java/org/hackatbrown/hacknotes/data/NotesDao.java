package org.hackatbrown.hacknotes.data;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface NotesDao {
    @Query("SELECT * FROM note")
    LiveData<List<Note>> getAll();

    @Query("SELECT * FROM note WHERE id = :noteId")
    LiveData<Note> getNote(long noteId);

    @Insert
    long insertNote(Note note);

    @Update
    void saveNote(Note note);

    @Delete
    void deleteNote(Note note);
}
