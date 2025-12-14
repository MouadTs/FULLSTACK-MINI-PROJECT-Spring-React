package com.taskmg.Controller;

import com.taskmg.dto.TaskDTO;
import com.taskmg.Service.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
// @CrossOrigin(origins = "*")  <-- REMOVED: Handled in SecurityConfig
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/projects/{projectId}/tasks")
    public ResponseEntity<?> createTask(@PathVariable Long projectId, @RequestBody TaskDTO taskDTO) {
        return ResponseEntity.ok(taskService.createTask(projectId, taskDTO));
    }

    // --- UPDATED ENDPOINT ---
    @GetMapping("/projects/{projectId}/tasks")
    public ResponseEntity<Page<TaskDTO>> getTasksByProject(
            @PathVariable Long projectId,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(taskService.getTasksByProject(projectId, search, status, page, size));
    }

    @PutMapping("/tasks/{taskId}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long taskId, @RequestBody TaskDTO taskDTO) {
        return ResponseEntity.ok(taskService.updateTask(taskId, taskDTO));
    }

    @PatchMapping("/tasks/{taskId}/complete")
    public ResponseEntity<?> markTaskComplete(@PathVariable Long taskId) {
        return ResponseEntity.ok(taskService.markTaskAsCompleted(taskId));
    }

    @DeleteMapping("/tasks/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.ok("Task deleted successfully");
    }
}