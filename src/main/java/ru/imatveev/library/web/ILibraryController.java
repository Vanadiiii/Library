package ru.imatveev.library.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.imatveev.library.domain.entity.MetaInfo;

import java.io.IOException;
import java.util.Collection;
import java.util.UUID;

@Tag(name = "Library Controller", description = "allow save (and get after that) any files")
public interface ILibraryController {
    @Operation(summary = "allow to get all files metaInfo")
    @GetMapping("/hello")
    ResponseEntity<Collection<MetaInfo>> findAllMetaInfo();

    @Operation(summary = "allow to get metaInfo by Id")
    @GetMapping("/hello/{id}")
    ResponseEntity<MetaInfo> getMetaInfo(@PathVariable("id") UUID id);

    @Operation(summary = "get file from server")
    @GetMapping("/download/{id}")
    ResponseEntity<InputStreamResource> download(@PathVariable("id") UUID id) throws IOException;

    @Operation(summary = "upload file to server")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Object> upload(@RequestParam("file") MultipartFile file);

    @Operation(summary = "allow to change file's metaInfo", description = "this method don't change file's Id!")
    @PutMapping("hello/{id}")
    ResponseEntity<Object> changeMetaInfo(@PathVariable("id") UUID id, @RequestBody MetaInfo metaInfo);

    @Operation(summary = "Only for admins", hidden = true)
    @DeleteMapping("hello/{id}")
    ResponseEntity<Object> remove(@PathVariable("id") UUID id);

    @Operation(summary = "Only for admins", hidden = true)
    @DeleteMapping("hello/removeAll")
    ResponseEntity<Object> removeAll();

    @Operation(summary = "just for get exception) Only for developer", hidden = true)
    @GetMapping("/oops")
    ResponseEntity<Void> oops();
}
