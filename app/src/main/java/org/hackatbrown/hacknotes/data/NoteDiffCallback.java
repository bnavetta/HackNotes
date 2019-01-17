package org.hackatbrown.hacknotes.data;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

public class NoteDiffCallback extends DiffUtil.ItemCallback<Note> {
    @Override
    public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
        return Objects.equals(oldItem.getTitle(), newItem.getTitle()) &&
                Objects.equals(oldItem.getContent(), newItem.getContent());
    }
}
