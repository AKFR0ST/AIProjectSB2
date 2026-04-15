package com.sb2.controller;

import com.sb2.dto.CreateTaskResponse;
import com.sb2.dto.Item;
import com.sb2.entity.CpTask;
import com.sb2.entity.TaskStatus;
import com.sb2.service.CpTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cp/tasks")
@RequiredArgsConstructor
public class CpTaskController {

    private final CpTaskService service;

    @PostMapping
    public ResponseEntity<CreateTaskResponse> createTask(@RequestParam String fileName) {
        CpTask task = service.createTask(fileName);
        service.processTask(task.getId());

        CreateTaskResponse response =
                new CreateTaskResponse(task.getId(), TaskStatus.CREATED.name());

        return ResponseEntity.accepted().body(response);
    }

    @GetMapping("/{id}")
    public CpTask getTask(@PathVariable UUID id) {
        return service.getTask(id);
    }

    @GetMapping("/{id}/top")
    public List<Item> getTopSuppliers(@PathVariable UUID id) {
        return service.getTopSuppliers(id);
    }
}
