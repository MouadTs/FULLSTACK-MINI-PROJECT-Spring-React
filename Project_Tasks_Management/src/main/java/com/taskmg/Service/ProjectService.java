package com.taskmg.Service;


import com.taskmg.dto.ProjectDTO;
import com.taskmg.model.Project;
import com.taskmg.model.Task;
import com.taskmg.model.User;
import com.taskmg.repository.ProjectRepository;
import com.taskmg.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    //  Get Logged-in User 
    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // --- CREATE ---
    public ProjectDTO createProject(ProjectDTO projectDTO) {
        User user = getCurrentUser();
        
        Project project = new Project();
        project.setTitle(projectDTO.getTitle());
        project.setDescription(projectDTO.getDescription());
        project.setUser(user);
        
        Project savedProject = projectRepository.save(project);
        return mapToDTO(savedProject); // ✅ CORRECT: Returns DTO
    }

    // --- READ ALL (For Current User) ---
    public List<ProjectDTO> getAllProjectsForUser() {
        User user = getCurrentUser();
        // Uses the custom method in ProjectRepository: findByUserId
        List<Project> projects = projectRepository.findByUserId(user.getId());
        
        return projects.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // --- READ ONE ---
    public ProjectDTO getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        // Security Check: Make sure the project belongs to the user
        User currentUser = getCurrentUser();
        if (!project.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not authorized to view this project");
        }

        return mapToDTO(project);
    }

    // --- UPDATE ---
    public ProjectDTO updateProject(Long id, ProjectDTO projectDTO) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        // Security Check
        User currentUser = getCurrentUser();
        if (!project.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not authorized to update this project");
        }

        // Update fields
        project.setTitle(projectDTO.getTitle());
        project.setDescription(projectDTO.getDescription());
        
        Project updatedProject = projectRepository.save(project);
        return mapToDTO(updatedProject);
    }

    // --- DELETE ---
    public void deleteProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        // Security Check
        User currentUser = getCurrentUser();
        if (!project.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not authorized to delete this project");
        }

        projectRepository.delete(project);
    }

    // Map Entity to DTO + Calculate Progress
    private ProjectDTO mapToDTO(Project project) {
        ProjectDTO dto = new ProjectDTO();
        dto.setId(project.getId());
        dto.setTitle(project.getTitle());
        dto.setDescription(project.getDescription());
        dto.setCreatedAt(project.getCreatedAt());

        List<Task> tasks = project.getTasks();
        if (tasks != null && !tasks.isEmpty()) {
            int total = tasks.size();
            int completed = (int) tasks.stream().filter(Task::isStatus).count();
            
            dto.setTotalTasks(total);
            dto.setCompletedTasks(completed);
            // Avoid division by zero
            dto.setProgressPercentage(total > 0 ? ((double) completed / total) * 100 : 0);
        } else {
            dto.setTotalTasks(0);
            dto.setCompletedTasks(0);
            dto.setProgressPercentage(0.0);
        }
        return dto;
    }
}