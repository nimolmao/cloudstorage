package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CredentialService {
    private Logger logger = LoggerFactory.getLogger(CredentialService.class);

    private final CredentialMapper credentialMapper;
    private final EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    /**
     * Gets a credential from the database.
     * @param credentialId
     * @return
     */
    public Credential getCredential(Integer credentialId) {
        return credentialMapper.getCredential(credentialId);
    }

    /**
     * Updates a credential in the database.
     * @param credential
     * @return boolean
     */
    public boolean updateCredential(Credential credential) {
        try {
            credentialMapper.updateCredential(credential);
            System.out.println("success update");
            return true;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return false;
        }
    }

    /**
     * Inserts a credential into the database.
     * @param credential
     * @return credentialId
     */
    public Integer insertCredential(Credential credential) {
        return credentialMapper.insertCredential(credential);
    }

    /**
     * Deletes a credential from the database.
     * @param credentialId
     * @return boolean
     */
    public boolean deleteCredential(Integer credentialId) {
        try {
            credentialMapper.deleteCredential(credentialId);
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    /**
     * Gets all credentials.
     * @return List of credentials.
     */
    public List<Credential> getAllCredentials() {
        return credentialMapper.getAllCredentials();
    }
}
