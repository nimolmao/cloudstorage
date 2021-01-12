package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String addNote(Principal principal, @ModelAttribute Note note, RedirectAttributes redirectAttributes, Model model) {
        User user = userService.getUser(principal.getName());

        Note existingNote = noteService.getNote(note.getNoteId());

        if (existingNote != null) {
            existingNote.setNoteTitle(note.getNoteTitle());
            existingNote.setNoteDescription(note.getNoteDescription());

            boolean updated = noteService.updateNote(existingNote);

            if (updated) {
                redirectAttributes.addFlashAttribute("updateError", true);
            } else {
                redirectAttributes.addFlashAttribute("updateSuccess", true);
            }
        } else {
            if (user != null) {
                note.setUserId(user.getUserId());
                int rowsAdded = noteService.insertNote(note);

                if (rowsAdded < 0) {
                    redirectAttributes.addFlashAttribute("saveError", true);
                } else {
                    redirectAttributes.addFlashAttribute("saveSuccess", true);
                }
            }

        }

        return "redirect:/result";
    }

    @GetMapping("/delete/{id}")
    public String deleteNote(@PathVariable("id") Integer id,RedirectAttributes redirectAttributes , Model model) {
        boolean deleted = noteService.deleteNote(id);

        if (deleted) {
            redirectAttributes.addFlashAttribute("deleteSuccess", true);
        } else {
            redirectAttributes.addFlashAttribute("deleteError", true);
        }

        return "redirect:/result";
    }

    @GetMapping("/notes")
    public String getAllNotes(Model model) {
        model.addAttribute("notes", noteService.getAllNotes());
        return "home";
    }
}