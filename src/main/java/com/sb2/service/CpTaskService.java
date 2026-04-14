package com.sb2.service;

import com.sb2.entity.CpTask;
import com.sb2.entity.TaskStatus;
import com.sb2.repository.CpTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CpTaskService {

    private final CpTaskRepository repository;

    public CpTask createTask(String fileName) {

        LocalDateTime now = LocalDateTime.now();

        return repository.save(
                CpTask.builder()
                        .fileName(fileName)
                        .status(TaskStatus.CREATED)
                        .createdAt(now)
                        .updatedAt(now)
                        .build()
        );
    }

    @Async
    public void processTask(UUID taskId) {
        CpTask task = repository.findById(taskId).orElseThrow();

        try {
            task.setStatus(TaskStatus.PROCESSING);
            repository.save(task);

            // 🔥 Тут будет "парсинг"
            Thread.sleep(3000);

            task.setExtractedData("{\"items\": []}");
            task.setStatus(TaskStatus.DONE);

        } catch (Exception e) {
            task.setStatus(TaskStatus.FAILED);
            task.setError(e.getMessage());
        }

        task.setUpdatedAt(LocalDateTime.now());
        repository.save(task);
    }

    public CpTask getTask(UUID id) {
        return repository.findById(id).orElseThrow();
    }
}
