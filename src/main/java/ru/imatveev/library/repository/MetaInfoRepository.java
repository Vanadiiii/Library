package ru.imatveev.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.imatveev.library.domain.entity.MetaInfo;
import ru.imatveev.library.domain.service.MetaInfoStorage;

import java.util.UUID;

public interface MetaInfoRepository extends MetaInfoStorage, JpaRepository<MetaInfo, UUID> {
}
