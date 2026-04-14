package com.sb2.controller;

import com.sb2.entity.CpTask;
import com.sb2.service.CpTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/cp/tasks")
@RequiredArgsConstructor
public class CpTaskController {

    private final CpTaskService service;

    @PostMapping
    public CpTask createTask(@RequestParam String fileName) {
        CpTask task = service.createTask(fileName);
        service.processTask(task.getId());
        return task;
    }

    @GetMapping("/{id}")
    public CpTask getTask(@PathVariable UUID id) {
        return service.getTask(id);
    }
}
