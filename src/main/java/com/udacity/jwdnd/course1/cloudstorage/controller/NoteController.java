package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/note")
public class NoteController {

    private final NoteService noteService;
    private final UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @PostMapping("/save")
    public String addNote(Principal principal, @ModelAttribute Note note, Model model) {
        User user = userService.getUser(principal.getName());

        Note existingNote = noteService.getNote(note.getNoteId());

        if (existingNote != null) {
            existingNote.setNoteTitle(note.getNoteTitle());
            existingNote.setNoteDescription(note.getNoteDescription());

            boolean updated = noteService.updateNote(existingNote);

            if (updated) {
                model.addAttribute("updatedNoteSuccess", true);
            } else {
                model.addAttribute("updatedNoteError", "There was an error updating the note. Please try again.");
            }
        } else {
            if (user != null) {
                note.setUserId(user.getUserId());
                int rowsAdded = noteService.insertNote(note);

                if (rowsAdded < 0) {
                    model.addAttribute("addedNoteError", "There was an error adding a note. Please try again.");
                } else {
                    model.addAttribute("addedNoteSuccess", true);
                }
            }

        }

        return "redirect:/note/notes";
    }

    @GetMapping("/delete/{id}")
    public String deleteNote(@PathVariable("id") Integer id , Model model) {
        boolean deleted = noteService.deleteNote(id);

        if (deleted) {
            model.addAttribute("deletedNoteSuccess", true);
        } else {
            model.addAttribute("deletedNoteError", "There was an error deleting the note. Please try again.");
        }

        return "redirect:/note/notes";
    }

    @GetMapping("/notes")
    public String getAllNotes(Model model) {
        model.addAttribute("notes", noteService.getAllNotes());
        return "home";
    }
}