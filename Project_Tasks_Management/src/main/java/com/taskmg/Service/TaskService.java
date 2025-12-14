package com.taskmg.Service;

import com.taskmg.dto.TaskDTO;
import com.taskmg.model.Project;
import com.taskmg.model.Task;
import com.taskmg.repository.ProjectRepository;
import com.taskmg.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }

    public TaskDTO createTask(Long projectId, TaskDTO taskDTO) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setDueDate(taskDTO.getDueDate());
        task.setStatus(false); 
        task.setProject(project);

        Task savedTask = taskRepository.save(task);
        return mapToDTO(savedTask);
    }

    // --- UPDATED METHOD FOR PAGINATION/SEARCH ---
    public Page<TaskDTO> getTasksByProject(Long projectId, String search, Boolean status, int page, int size) {
        // 1. Create Pageable (Sort by DueDate ascending so upcoming tasks are first)
        Pageable pageable = PageRequest.of(page, size, Sort.by("dueDate").ascending());

        // 2. Call the custom repository method
        Page<Task> tasks = taskRepository.searchTasks(projectId, search, status, pageable);

        // 3. Convert Page<Entity> to Page<DTO>
        return tasks.map(this::mapToDTO);
    }

    public TaskDTO updateTask(Long taskId, TaskDTO taskDTO) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setDueDate(taskDTO.getDueDate());
        
        // Note: We usually don't update status here if we have a specific 'complete' endpoint, 
        // but if you want to allow it:
        // task.setStatus(taskDTO.isStatus());

        Task updatedTask = taskRepository.save(task);
        return mapToDTO(updatedTask);
    }

    public Task markTaskAsCompleted(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        task.setStatus(true);
        return taskRepository.save(task);
    }
    
    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }

    // Helper method to map Entity -> DTO
    private TaskDTO mapToDTO(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.isStatus());
        dto.setDueDate(task.getDueDate());
        dto.setProjectId(task.getProject().getId());
        return dto;
    }
}