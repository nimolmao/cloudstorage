package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.apache.ibatis.annotations.*;

/**
 * A mapper that interacts with the database for CRUD operations relating to USERS table.
 */
@Mapper
public interface UserMapper {
    @Select("SELECT * FROM USERS WHERE username = #{username}")
    User getUser(String username);

    @Insert("INSERT INTO USERS (username, salt, password, firstname, lastname) VALUES(#{username}, #{salt}, #{password}, #{firstName}, #{lastName})")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    Integer insert(User user);

    @Update("UPDATE USER SET username = #{username}, salt = #{salt}, filesize = #{password}, firstname = #{firstName}, lastname = #{lastName} WHERE userid= #{userId}")
    void updateUser(User user);

    @Delete("DELETE FROM USERS WHERE username = #{username}")
    void deleteUser(String username);
}