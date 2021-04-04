package ru.imatveev.library.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.imatveev.library.domain.entity.FileWrapper;
import ru.imatveev.library.domain.entity.MetaInfo;
import ru.imatveev.library.domain.service.FileStorage;
import ru.imatveev.library.domain.service.MetaInfoStorage;
import ru.imatveev.library.exception.OopsException;

import java.io.File;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LibraryDomainService {
    private final MetaInfoStorage metaInfoStorage;
    private final FileStorage fileStorage;

    public void save(byte[] bytes, MetaInfo metaInfo) {
        metaInfoStorage.save(metaInfo);
        fileStorage.save(bytes, metaInfo.getFileName(), metaInfo.getId());
    }

    public FileWrapper find(UUID id) {
        File file = fileStorage.find(id)
                .orElseThrow(() -> {
                    String message = "Can't find file by id - " + id;
                    log.error(message);
                    throw new OopsException(message);
                });
        MetaInfo metaInfo = metaInfoStorage.findById(id)
                .orElseThrow(() -> {
                    String message = "Can't find hello's by with id - " + id;
                    log.error(message);
                    throw new OopsException(message);
                });

        log.info("Hello with id {} was successfully found", id);

        return FileWrapper.builder()
                .metaInfo(metaInfo)
                .file(file)
                .build();
    }

    public MetaInfo findMetaInfo(UUID id) {
        return metaInfoStorage.findById(id)
                .orElseThrow(() -> {
                    String message = "Can't find hello's info by id - " + id;
                    log.error(message);
                    throw new OopsException(message);
                });
    }

    public Collection<FileWrapper> findAll() {
        return metaInfoStorage.findAll()
                .stream()
                .collect(Collectors.toMap(
                        helloInfo -> helloInfo,
                        helloInfo -> fileStorage.find(helloInfo.getId())
                ))
                .entrySet()
                .stream()
                .filter(entry -> {
                    if (entry.getValue().isEmpty()) {
                        log.error("Can't find file by id - " + entry.getKey().getId());
                        return false;
                    }
                    return true;
                })
                .map(entry -> FileWrapper.builder()
                        .metaInfo(entry.getKey())
                        .file(entry.getValue().orElseThrow(OopsException::init))
                        .build()
                )
                .collect(Collectors.toList());
    }

    public Collection<MetaInfo> findAllMeta() {
        return metaInfoStorage.findAll();
    }

    public void remove(UUID id) {
        metaInfoStorage.deleteById(id);
        fileStorage.delete(id);
        log.info("Hello with id - {} was successfully removed", id);
    }

    public void removeAll() {
        metaInfoStorage.deleteAll();
        fileStorage.deleteAll();
        log.info("All files and their info was successfully removed");
    }

    public void changeMetaInfo(MetaInfo metaInfo) {
        metaInfoStorage.deleteById(metaInfo.getId());
        metaInfoStorage.save(metaInfo);
        log.info("metaInfo of file {} was successfully changed", metaInfo.getId());
    }
}
