package ru.imatveev.library.domain.service;

import java.io.File;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface FileStorage {
    void save(byte[] bytes, String fileName, UUID id);

    Optional<File> find(UUID id);

    Collection<File> findAll();

    void delete(UUID id);

    void deleteAll();
}
