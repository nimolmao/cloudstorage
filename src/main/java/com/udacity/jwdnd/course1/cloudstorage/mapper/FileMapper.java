package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * A mapper that interacts with the database for CRUD operations relating to FILES table.
 */
@Mapper
public interface FileMapper {
    @Select("SELECT * FROM FILES WHERE filename = #{filename}")
    File getFile(String filename);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) VALUES(#{filename}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    Integer insertFile(File file);

    @Update("UPDATE FILES SET filename = #{filename}, contenttype = #{contentType}, filesize = #{fileSize}, userid = #{userId}, filedata = #{fileData}  WHERE fileid = #{fileId}")
    void updateFile(File file);

    @Delete("DELETE FROM FILES WHERE filename = #{filename}")
    void deleteFile(String filename);

    @Select("SELECT filename FROM FILES")
    List<String> getFilenames();

    @Select("SELECT filename FROM FILES WHERE filename = #{filename}")
    String getFilename (String filename);
}