package com.sb2.service;

import com.sb2.constant.ErrorMessages;
import com.sb2.dto.Item;
import com.sb2.entity.CpTask;
import com.sb2.entity.ExtractedData;
import com.sb2.entity.TaskStatus;
import com.sb2.exception.TaskNotFoundException;
import com.sb2.repository.CpTaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.sb2.constant.LoggerMessages.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CpTaskService {

    private final CpTaskRepository repository;
    private final CpParserMock parserMock;

    public CpTask createTask(String fileName) {
        log.info(CREATING_TASK_FOR_FILE, fileName);
        if (Objects.isNull(fileName) || fileName.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessages.FILE_NAME_MUST_NOT_BE_EMPTY);
        }

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
        log.info(START_PROCESSING_TASK, taskId);
        CpTask task = repository.findById(taskId).orElseThrow();

        try {
            task.setStatus(TaskStatus.PROCESSING);
            repository.save(task);

            // TODO тут стоит расположить интеграцию с сервисом парсинга
            Thread.sleep(3000);
            ExtractedData data = parserMock.parse(task.getFileName());

            task.setExtractedData(data);
            task.setStatus(TaskStatus.DONE);

            log.info(TASK_PROCESSED_SUCCESSFULLY, taskId);

        } catch (Exception e) {
            log.error(ERROR_PROCESSING_TASK, taskId, e.getMessage(), e);
            task.setStatus(TaskStatus.FAILED);
            task.setError(e.getMessage());
        }

        task.setUpdatedAt(LocalDateTime.now());
        repository.save(task);
    }

    public CpTask getTask(UUID id) {
        return repository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
    }

    public List<Item> getTopSuppliers(UUID taskId) {
        CpTask task = repository.findById(taskId).orElseThrow();

        if (task.getExtractedData() == null) {
            throw new IllegalStateException(ErrorMessages.TASK_NOT_PROCESSED_YET);
        }

        return task.getExtractedData().getItems().stream()
                .sorted(Comparator.comparing(Item::getPrice))
                .limit(5)
                .toList();
    }
}
