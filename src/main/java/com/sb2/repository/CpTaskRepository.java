package com.sb2.repository;

import com.sb2.entity.CpTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CpTaskRepository extends JpaRepository<CpTask, UUID> {
}
