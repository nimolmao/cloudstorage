package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * A service that interacts with the FileMapper.
 */
@Service
public class FileService {

    private final FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    /**
     * Gets a file from the database.
     * @param filename
     * @return File
     */
    public File getFile(String filename) {
        return fileMapper.getFile(filename);
    }

    /**
     * Inserts a new file into the database.
     * @param file
     * @return rows added
     */
    public Integer uploadFile(File file)
    {
        return fileMapper.insertFile(file);
    }

    /**
     * Updates a file in the database.
     * @param file
     */
    public void updateFile(File file) {
        fileMapper.updateFile(file);
    }

    /**
     * Deletes a file from the database.
     * @param filename
     * @return boolean
     */
    public boolean deleteFile(String filename)
    {
        try {
            fileMapper.deleteFile(filename);
            return true;
        } catch (Exception ex) {
            return false;
        }

    }

    /**
     * Gets all filenames.
     * @return List of file
     */
    public List<String> getFilenames() {
        return fileMapper.getFilenames();
    }

    /**
     * Get a filename
     * @param filename
     * @return filename
     */
    public String getFilename(String filename) {
        return fileMapper.getFilename(filename);
    }
}