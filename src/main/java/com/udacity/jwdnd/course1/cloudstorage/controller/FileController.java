package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.FileService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;

@Controller
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;
    private final UserService userService;

    public FileController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable("filename") String filename, Model model) {
        File file = fileService.getFile(filename);

        if (file != null) {
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(new ByteArrayResource(file.getFileData()));
        } else {
            return ResponseEntity.badRequest().build();
        }


    }

    @PostMapping()
    public String uploadFile(Principal principal, @RequestParam("fileUpload") MultipartFile fileUpload, Model model) throws IOException {
        String existingFile = fileService.getFilename(fileUpload.getOriginalFilename());

        if (existingFile != null) {
            model.addAttribute("fileExisted", existingFile + " is already uploaded.");
        } else {
            if (!fileUpload.getOriginalFilename().equals("")) {
                User user = userService.getUser(principal.getName());
                File file = new File(null, fileUpload.getOriginalFilename(), fileUpload.getContentType(), fileUpload.getSize(), user.getUserId(), fileUpload.getBytes());

                int rowsAdded = fileService.uploadFile(file);

                if (rowsAdded < 0) {
                    model.addAttribute("uploadingError", "There was an error uploading the file. Please try again.");
                } else {
                    model.addAttribute("uploadingSuccess", true);
                }
            }
        }

        model.addAttribute("filenames", fileService.getFilenames());


        //return "redirect:/file/files";
        return "home";
    }

    @GetMapping("/files")
    public String getFilenames(Model model) {
        model.addAttribute("filenames", fileService.getFilenames());
        return "home";
    }

    @GetMapping("/delete/{filename}")
    public String removeFile(@PathVariable("filename") String filename, Model model) {
        boolean deleted = fileService.deleteFile(filename);

        if (deleted) {
            model.addAttribute("deletenoteSuccess", true);
        } else {
            model.addAttribute("deletenoteError", "There was an error deleting the note. Please try again.");
        }

        return "redirect:/file/files";
    }
}