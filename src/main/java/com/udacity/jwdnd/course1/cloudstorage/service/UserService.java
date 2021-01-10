package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * A service that interacts with the UserMapper.
 */
@Service
public class UserService {

    private final UserMapper userMapper;
    private final HashService hashService;

    public UserService(UserMapper userMapper, HashService hashService) {
        this.userMapper = userMapper;
        this.hashService = hashService;
    }

    /**
     * Verifies if a username already exists in the database.
     * @param username
     * @return
     */
    public boolean isUsernameAvailable(String username) {
        return userMapper.getUser(username) == null;
    }

    /**
     * Inserts a new user into the database.
     * @param user
     * @return
     */
    public int createUser(User user) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String hashedPassword = hashService.getHashedValue(user.getPassword(), encodedSalt);
        return userMapper.insert(new User(null, user.getUsername(), encodedSalt, hashedPassword, user.getFirstName(), user.getLastName()));
    }

    /**
     * Gets a user from the database.
     * @param username
     * @return
     */
    public User getUser(String username) {
        return userMapper.getUser(username);
    }

    /**
     * Updates a user in the database.
     * @param user
     * @return boolean
     */
    public boolean updateUser(User user) {
        try {
            userMapper.updateUser(user);
            return true;
        } catch (Exception ex){
            return false;
        }
    }

    /**
     * Deletes a user from the database.
     * @param username
     * * @return boolean
     */
    public boolean deleteUser(String username) {
        try {
            userMapper.deleteUser(username);
            return true;
        } catch (Exception ex) {
            return false;
        }

    }
}
