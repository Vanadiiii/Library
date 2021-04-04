package ru.imatveev.library.web.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.imatveev.library.domain.LibraryDomainService;
import ru.imatveev.library.domain.entity.FileWrapper;
import ru.imatveev.library.domain.entity.MetaInfo;
import ru.imatveev.library.exception.OopsException;
import ru.imatveev.library.web.ILibraryController;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/app")
@RequiredArgsConstructor
public class LibraryController implements ILibraryController {
    private final LibraryDomainService libraryDomainService;

    @Override
    public ResponseEntity<Collection<MetaInfo>> findAllMetaInfo() {
        return ResponseEntity.ok(libraryDomainService.findAllMeta());
    }

    @Override
    public ResponseEntity<InputStreamResource> download(UUID id) throws IOException {
        FileWrapper fileWrapper = libraryDomainService.find(id);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", fileWrapper.getMetaInfo().getFileName()));
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(fileWrapper.getFile().length())
                .contentType(MediaType.IMAGE_PNG)
                .body(new InputStreamResource(new FileInputStream(fileWrapper.getFile())));
    }

    @Override
    public ResponseEntity<Object> upload(MultipartFile multipartFile) {
        UUID id = UUID.randomUUID();
        String fileName = multipartFile.getOriginalFilename();
        MetaInfo metaInfo = new MetaInfo(id, fileName);

        byte[] bytes;

        try {
            bytes = multipartFile.getBytes();
        } catch (IOException e) {
            log.error("Can't get bytes from downloaded file, cause: " + e.getCause());
            throw new OopsException(e.getMessage(), e);
        }

        libraryDomainService.save(bytes, metaInfo);
        log.info("File {}_{} was successfully saved", id, fileName);

        return ResponseEntity.ok(
                "File was upload successfully with id - " + id
        );
    }

    @Override
    public ResponseEntity<MetaInfo> getMetaInfo(UUID id) {
        return ResponseEntity.ok(libraryDomainService.findMetaInfo(id));
    }

    @Override
    public ResponseEntity<Object> changeMetaInfo(UUID id, MetaInfo metaInfo) {
        metaInfo.setId(id);
        libraryDomainService.changeMetaInfo(metaInfo);
        return new ResponseEntity<>("'Hello' updated successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> remove(UUID id) {
        libraryDomainService.remove(id);
        return new ResponseEntity<>("'Hello' with id - " + id + " was removed successfully", HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<Object> removeAll() {
        libraryDomainService.removeAll();
        return new ResponseEntity<>("All books was removed successfully", HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<Void> oops() {
        throw OopsException.init();
    }
}
