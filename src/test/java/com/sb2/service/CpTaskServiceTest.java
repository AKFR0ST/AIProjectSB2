package com.sb2.service;

import com.sb2.dto.Item;
import com.sb2.entity.CpTask;
import com.sb2.entity.ExtractedData;
import com.sb2.entity.TaskStatus;
import com.sb2.exception.TaskNotFoundException;
import com.sb2.repository.CpTaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CpTaskServiceTest {

    @Mock
    private CpParserMock parser;

    @Mock
    private CpTaskRepository repository;

    @InjectMocks
    private CpTaskService service;

    @Test
    void create_task_success() {
        CpTask task = new CpTask();
        task.setId(UUID.randomUUID());
        task.setFileName("test.pdf");
        task.setStatus(TaskStatus.CREATED);

        when(repository.save(any(CpTask.class)))
                .thenReturn(task);

        CpTask result = service.createTask("test.pdf");

        assertNotNull(result);
        assertEquals("test.pdf", result.getFileName());
        assertEquals(TaskStatus.CREATED, result.getStatus());

        verify(repository, times(1)).save(any(CpTask.class));
    }

    @Test
    void create_task_fail() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.createTask("")
        );

        assertEquals("fileName must not be empty", ex.getMessage());
    }

    @Test
    void should_return_task_when_exists() {
        UUID id = UUID.randomUUID();

        CpTask task = new CpTask();
        task.setId(id);

        when(repository.findById(id))
                .thenReturn(Optional.of(task));

        CpTask result = service.getTask(id);

        assertEquals(id, result.getId());
    }

    @Test
    void should_throw_exception_when_task_not_found() {
        UUID id = UUID.randomUUID();

        when(repository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class,
                () -> service.getTask(id));
    }

    @Test
    void should_process_task_successfully() {
        UUID id = UUID.randomUUID();

        CpTask task = new CpTask();
        task.setId(id);
        task.setFileName("laptop.pdf");
        task.setStatus(TaskStatus.CREATED);

        when(parser.parse(anyString()))
                .thenReturn(new ExtractedData(List.of()));

        when(repository.findById(id))
                .thenReturn(Optional.of(task));

        when(repository.save(any()))
                .thenReturn(task);

        service.processTask(id);

        assertEquals(TaskStatus.DONE, task.getStatus());
        assertNotNull(task.getExtractedData());

        verify(repository, atLeastOnce()).save(any());
    }

    @Test
    void get_top_suppliers() {

        UUID id = UUID.randomUUID();

        CpTask task = new CpTask();
        task.setId(id);

        ExtractedData data = new ExtractedData();
        data.setItems(List.of(
                new Item("A", BigDecimal.valueOf(100), "S1"),
                new Item("B", BigDecimal.valueOf(50), "S2"),
                new Item("C", BigDecimal.valueOf(150), "S3"),
                new Item("D", BigDecimal.valueOf(70), "S4"),
                new Item("E", BigDecimal.valueOf(30), "S5"),
                new Item("F", BigDecimal.valueOf(90), "S6")
        ));

        task.setExtractedData(data);

        when(repository.findById(id)).thenReturn(Optional.of(task));

        List<Item> result = service.getTopSuppliers(id);

        assertEquals(5, result.size());

        assertEquals(BigDecimal.valueOf(30), result.get(0).getPrice());
        assertEquals(BigDecimal.valueOf(50), result.get(1).getPrice());
    }
}