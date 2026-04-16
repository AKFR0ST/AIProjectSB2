package com.sb2.controller;

import com.sb2.dto.CreateTaskResponse;
import com.sb2.dto.ErrorResponse;
import com.sb2.dto.Item;
import com.sb2.dto.TaskResponse;
import com.sb2.entity.CpTask;
import com.sb2.entity.TaskStatus;
import com.sb2.service.CpTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/cp/tasks")
@Tag(name = "CP Tasks", description = "API для работы с задачами обработки КП")
@RequiredArgsConstructor
public class CpTaskController {

    private final CpTaskService service;

    @Operation(summary = "Создать задачу на обработку КП")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Задача принята в обработку",
                    content = @Content(schema = @Schema(implementation = CreateTaskResponse.class))),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос"),
            @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    @PostMapping
    public ResponseEntity<CreateTaskResponse> createTask(@RequestParam String fileName) {
        log.info("Received request to create task: fileName={}", fileName);
        CpTask task = service.createTask(fileName);
        service.processTask(task.getId());

        CreateTaskResponse response =
                new CreateTaskResponse(task.getId(), TaskStatus.CREATED.name());

        return ResponseEntity.accepted().body(response);
    }


    @Operation(summary = "Получить задачу по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Задача найдена",
                    content = @Content(schema = @Schema(implementation = TaskResponse.class))),
            @ApiResponse(responseCode = "404", description = "Задача не найдена",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    @GetMapping("/{taskId}")
    public CpTask getTask(@PathVariable UUID taskId) {
        return service.getTask(taskId);
    }

    @Operation(summary = "Получить ТОП-5 поставщиков")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список поставщиков",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Item.class)))),
            @ApiResponse(responseCode = "404", description = "Задача не найдена",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав"),
            @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    @GetMapping("/{taskId}/top")
    public List<Item> getTopSuppliers(@PathVariable UUID taskId) {
        return service.getTopSuppliers(taskId);
    }
}
