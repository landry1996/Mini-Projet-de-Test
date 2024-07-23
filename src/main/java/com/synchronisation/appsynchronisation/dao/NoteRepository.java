package com.synchronisation.appsynchronisation.dao;

import com.synchronisation.appsynchronisation.models.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findBySynchronised(boolean synchronised);
    Optional<Note> findNoteByTitle(String title);
}
