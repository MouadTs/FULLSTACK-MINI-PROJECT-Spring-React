package com.taskmg.Controller;


import com.taskmg.Service.ProjectService;
import com.taskmg.dto.ProjectDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "*") // Allow React to access this
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    // Create a Project
    @PostMapping
    public ResponseEntity<ProjectDTO> createProject(@RequestBody ProjectDTO projectDTO) {
        return ResponseEntity.ok(projectService.createProject(projectDTO));
    }

    // 2. Get All Projects (for logged-in user)  Returns Progress as well
    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjectsForUser());
    }

    // 3. Get One Project
    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    // 4. Update Project
    @PutMapping("/{id}")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable Long id, @RequestBody ProjectDTO projectDTO) {
        return ResponseEntity.ok(projectService.updateProject(id, projectDTO));
    }

    // 5. Delete Project
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok("Project deleted successfully");
    }

    // 6. DEDICATED PROGRESS ENDPOINT
    // URL: GET http://localhost:8080/api/projects/1/progress
    @GetMapping("/{projectId}/progress")
    public ResponseEntity<java.util.Map<String, Object>> getProjectProgress(@PathVariable Long projectId) {
        ProjectDTO project = projectService.getProjectById(projectId);

        // Create a custom response with ONLY the progress data
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        response.put("projectId", project.getId());
        response.put("projectTitle", project.getTitle());
        response.put("totalTasks", project.getTotalTasks());
        response.put("completedTasks", project.getCompletedTasks());
        response.put("progressPercentage", project.getProgressPercentage());

        return ResponseEntity.ok(response);
    }
}