package ru.imatveev.library.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.stereotype.Service;
import ru.imatveev.library.domain.service.FileStorage;
import ru.imatveev.library.exception.OopsException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FileStorageImpl implements FileStorage {
    private static final Path PATH = Path.of("./tmp");

    @Override
    public void save(byte[] bytes, String fileName, UUID id) {
        File file = new File(PATH.toString() + "/" + id + "_" + fileName);
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(bytes);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            log.error("Can't save file to localStorage, cause: " + e.getCause());
            throw new OopsException(e.getMessage(), e);
        }
    }

    @Override
    public Optional<File> find(UUID id) {
        try {
            return Files.find(
                    PATH,
                    1,
                    (path, __) -> path.toFile().getName().startsWith(id.toString())
            )
                    .findFirst()
                    .map(Path::toFile);
        } catch (IOException e) {
            log.error("Can't find file with id - {}", id);
            throw new OopsException("Can't find file with id - " + id, e);
        }
    }

    @Override
    public Collection<File> findAll() {
        try {
            return Files.find(
                    PATH,
                    1,
                    (__1, __2) -> true
            )
                    .map(Path::toFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.error("Can't find files in local storage");
            throw new OopsException("Can't find files in local storage");
        }
    }

    @Override
    public void delete(UUID id) {
        File file = find(id)
                .orElseThrow(() -> new OopsException("Can't find file with id - " + id));
        try {
            Files.delete(Path.of(file.getPath()));
        } catch (IOException e) {
            log.error("Can't remove file with id - {} from local storage, cause:\n", id, e);
            throw new OopsException("Can't remove file with id " + id + " from local storage", e);
        }
    }

    @Override
    public void deleteAll() {
        try {
            FileUtils.cleanDirectory(PATH.toFile());
        } catch (IOException e) {
            log.error("Can't clean the local storage, cause:\n", e);
            throw new OopsException("Can't clean the local storage, cause:\n", e);
        }
    }
}
