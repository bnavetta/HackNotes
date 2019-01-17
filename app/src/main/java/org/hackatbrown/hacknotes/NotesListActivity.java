package org.hackatbrown.hacknotes;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class NotesListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);
    }

    public void onNewNote(View button) {
        Toast.makeText(getApplicationContext(), "Creating note!", Toast.LENGTH_SHORT).show();
    }
}
