package ru.imatveev.library.domain.service;

import ru.imatveev.library.domain.entity.MetaInfo;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface MetaInfoStorage {
    MetaInfo save(MetaInfo metaInfo);

    Optional<MetaInfo> findById(UUID id);

    Collection<MetaInfo> findAll();

    void deleteById(UUID id);

    void deleteAll();
}
