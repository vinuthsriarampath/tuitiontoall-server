/*
 * Copyright (c) 2026 vinuth sri arampath
 *
 * This code is the intellectual property of vinuth sri arampath and is protected under copyright law.
 * Unauthorized copying, modification, distribution, or use of this code, in whole or in part,
 * without prior written permission is strictly prohibited.
 *
 * Portions of this code may be generated with AI and modified by vinuth sri arampath
 * All rights reserved.
 *
 *
 */

package edu.vinu.infastructure.service.file_storage;

import edu.vinu.common.exception.custom.InvalidInputException;
import edu.vinu.common.exception.custom.NotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Provides file system operations such as file storage, retrieval,
 * streaming, metadata inspection, and directory management.
 *
 * <p>This service acts as the application's central abstraction for
 * interacting with the underlying file system and should be used
 * instead of directly invoking {@link java.nio.file.Files} in
 * business services.</p>
 */
public interface FileService {

    /**
     * Saves a multipart file to the specified directory.
     *
     * <p>If the target directory does not exist, it will be created automatically.
     * The behavior when the target file already exists depends on the supplied
     * {@link StandardCopyOption}.</p>
     *
     * @param file the uploaded file to save
     * @param directory the target directory
     * @param fileName the name to assign to the saved file
     * @param option the copy option to apply (e.g. REPLACE_EXISTING)
     *
     * @throws RuntimeException if the file cannot be saved
     */
    void saveFile(MultipartFile file, Path directory, String fileName, StandardCopyOption option);

    /**
     * Saves a multipart file and returns the path of the saved file.
     *
     * <p>If the target directory does not exist, it will be created automatically.</p>
     *
     * @param file the uploaded file to save
     * @param directory the target directory
     * @param fileName the name to assign to the saved file
     * @param option the copy option to apply
     *
     * @return the path of the saved file
     *
     * @throws RuntimeException if the file cannot be saved
     */
    Path saveAndReturnPath(MultipartFile file, Path directory, String fileName, StandardCopyOption option);

    /**
     * Retrieves a file from the specified directory.
     *
     * @param directory the directory containing the file
     * @param fileName the file name
     *
     * @return the requested file
     *
     * @throws NotFoundException if the file does not exist or is not a regular file
     */
    File getFile(Path directory, String fileName);

    /**
     * Loads a file as a Spring {@link Resource}.
     *
     * <p>This method is useful when returning files through HTTP responses
     * or when streaming content.</p>
     *
     * @param directory the directory containing the file
     * @param fileName the file name
     *
     * @return the file as a resource
     *
     * @throws NotFoundException if the file does not exist
     */
    Resource getResource(Path directory, String fileName);

    /**
     * Reads the entire contents of a file into memory.
     *
     * <p>Use with caution for large files as the entire file will be
     * loaded into a byte array.</p>
     *
     * @param filePath the file path
     *
     * @return the file contents as a byte array
     *
     * @throws RuntimeException if the file cannot be read
     */
    byte[] getBytes(Path filePath);

    /**
     * Creates a {@link ResourceRegion} for HTTP range requests.
     *
     * <p>This method is intended for media streaming and partial content
     * delivery. If no range header is provided, an initial region is
     * generated using the specified chunk size.</p>
     *
     * @param resource the resource being streamed
     * @param rangeHeader the HTTP Range header value
     * @param fileSize the total file size in bytes
     * @param chunkSize the maximum region size in bytes
     *
     * @return a resource region representing the requested range
     *
     * @throws RuntimeException if the range cannot be calculated
     */
    ResourceRegion getRegion(Resource resource, String rangeHeader, long fileSize, long chunkSize);

    /**
     * Builds a partial content response for streaming resources.
     *
     * <p>The response is returned with HTTP status {@code 206 Partial Content}
     * and includes the appropriate range-related headers.</p>
     *
     * @param resource the underlying resource
     * @param region the requested resource region
     * @param mediaType the resource content type
     * @param fileName the file name
     * @param fileSize the total file size
     *
     * @return a partial content response entity
     */
    ResponseEntity<ResourceRegion> buildPartialResponse(
            Resource resource,
            ResourceRegion region,
            MediaType mediaType,
            String fileName,
            long fileSize
    );

    /**
     * Deletes a file if it exists.
     *
     * @param filePath the path of the file to delete
     *
     * @throws RuntimeException if the deletion operation fails
     */
    void delete(Path filePath);

    /**
     * Deletes a directory and all of its contents recursively.
     *
     * @param directory the directory to delete
     *
     * @throws RuntimeException if any deletion operation fails
     */
    void deleteDirectory(Path directory);

    /**
     * Deletes all files matching the specified glob pattern
     * within a directory.
     *
     * <p>Example patterns:</p>
     * <ul>
     *     <li>{@code *.jpg}</li>
     *     <li>{@code *.part}</li>
     * </ul>
     *
     * @param directory the target directory
     * @param pattern the glob pattern
     *
     * @throws RuntimeException if the operation fails
     */
    void deleteMatching(Path directory, String pattern);

    /**
     * Returns the size of a file in bytes.
     *
     * @param filePath the file path
     *
     * @return the file size in bytes
     *
     * @throws RuntimeException if the size cannot be determined
     */
    long size(Path filePath);

    /**
     * Checks whether a file or directory exists.
     *
     * @param filePath the path to check
     *
     * @return {@code true} if the path exists; otherwise {@code false}
     */
    boolean exists(Path filePath);

    /**
     * Attempts to determine the MIME type of a file.
     *
     * <p>If the content type cannot be determined,
     * {@code application/octet-stream} should be returned.</p>
     *
     * @param filePath the file path
     *
     * @return the detected MIME type
     */
    String detectContentType(Path filePath);

    /**
     * Extracts the extension from a file name.
     *
     * <p>The returned value includes the leading dot.</p>
     *
     * <pre>
     * document.pdf -> .pdf
     * image.png    -> .png
     * </pre>
     *
     * @param fileName the original file name
     *
     * @return the file extension including the leading dot
     *
     * @throws InvalidInputException if the file name is null or has no extension
     */
    String extractExtension(String fileName);

    /**
     * Creates a directory and any missing parent directories.
     *
     * <p>If the directory already exists, no action is performed.</p>
     *
     * @param path the directory path
     *
     * @return the created or existing directory path
     *
     * @throws RuntimeException if the directory cannot be created
     */
    Path createDirectory(Path path);

    /**
     * Resolves a file name against a directory path.
     *
     * @param directory the base directory
     * @param fileName the file name
     *
     * @return the resolved path
     */
    Path resolve(Path directory, String fileName);
}
