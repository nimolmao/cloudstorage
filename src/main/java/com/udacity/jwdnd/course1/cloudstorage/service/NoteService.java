package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * A service that interacts with the NoteMapper.
 */
@Service
public class NoteService {

    private final NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    /**
     * Gets a note from the database.
     * @param noteId
     * @return
     */
    public Note getNote(Integer noteId) {
        return noteMapper.getNote(noteId);
    }

    /**
     * Updates a note in the database.
     * @param note
     * @return boolean
     */
    public boolean updateNote(Note note) {
        try {
            noteMapper.updateNote(note);
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    /**
     * Inserts a note into the database.
     * @param note
     * @return
     */
    public Integer insertNote(Note note) {
        return noteMapper.insertNote(note);
    }

    /**
     * Deletes a note from the database.
     * @param noteId
     * @return boolean
     */
    public boolean deleteNote(Integer noteId) {
        try {
            noteMapper.deleteNote(noteId);
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    /**
     * Gets all notes.
     * @return List of notes.
     */
    public List<Note> getAllNotes() {
        return noteMapper.getAllNotes();
    }
}