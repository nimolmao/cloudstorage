package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.service.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.security.SecureRandom;
import java.util.Base64;

@Controller
@RequestMapping("/credential")
public class CredentialController {

    private final UserService userService;
    private final CredentialService credentialService;
    private final EncryptionService encryptionService;

    public CredentialController(UserService userService, CredentialService credentialService, EncryptionService encryptionService) {
        this.userService = userService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
    }

    @PostMapping("/save")
    public String saveCredential(Principal principal, @ModelAttribute Credential credential, Model model) {
        //String key = "test1234quad5678";
        User user = userService.getUser(principal.getName());

        Credential existingCredential = credentialService.getCredential(credential.getCredentialId());

        if (existingCredential != null) {
            System.out.println("existed credential: " + existingCredential);
            existingCredential.setUrl(credential.getUrl());
            existingCredential.setUsername(credential.getUsername());
            existingCredential.setPassword(credential.getPassword());

            boolean updated = credentialService.updateCredential(existingCredential);

            if (updated) {
                model.addAttribute("updatedCredentialSuccess", true);
            } else {
                model.addAttribute("updatedCredentialError", "There was an error updating the credential. Please try again.");
            }
        } else {
            System.out.println("user: " + user);
            if (user != null) {
                SecureRandom random = new SecureRandom();
                byte[] key = new byte[16];
                random.nextBytes(key);
                String encodedKey = Base64.getEncoder().encodeToString(key);
                credential.setUserId(user.getUserId());
                credential.setKey(encodedKey);
                String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);
                credential.setPassword(encryptedPassword);
                int rowsAdded = credentialService.insertCredential(credential);

                if (rowsAdded < 0) {
                    model.addAttribute("errorMessage", "There was an error adding a credential. Please try again.");
                } else {
                    model.addAttribute("successMessage", "Credential saved. Click <a href=\"/#nav-credentials\">here</a> to continue.");
                }
            }
        }

        return "redirect:/result";
    }

    @GetMapping("/delete/{id}")
    public String deleteCredential(@PathVariable("id") Integer id, Model model) {
        boolean deleted = credentialService.deleteCredential(id);

        if (deleted) {
            model.addAttribute("deletedCredentialSuccess", true);
        } else {
            model.addAttribute("deletedCredentialError", "There was an error deleting the credential. Please try again.");
        }

        return "redirect:/credential/credentials";
    }

    @GetMapping("/credentials")
    public String getAllCredentials(Model model) {
        model.addAttribute("credentials", credentialService.getAllCredentials());

        return "home";
    }

    @GetMapping("/{id}")
    public String getCredentialById(@PathVariable("id") Integer id, Model model) {
        Credential credential = credentialService.getCredential(id);

        if (credential != null) {
            String decryptedPassword = encryptionService.decryptValue(credential.getPassword(), credential.getKey());
            credential.setPassword(decryptedPassword);
            model.addAttribute("getCredential", credential);
        }

        return "home";
    }
}