package com.taskmg.repository;

	import com.taskmg.model.Task;

import java.util.List;

import org.springframework.data.domain.Page;import org.springframework.data.domain.Pageable;import org.springframework.data.jpa.repository.JpaRepository;import org.springframework.data.jpa.repository.Query;import org.springframework.data.repository.query.Param;

	public interface TaskRepository extends JpaRepository<Task, Long> {
	    
		List<Task> findByProjectId(Long projectId);
	    // The query handles: Project ID (Mandatory) + Search (Optional) + Status (Optional)
	    @Query("SELECT t FROM Task t WHERE t.project.id = :projectId " +
	           "AND (:search IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', :search, '%'))) " +
	           "AND (:status IS NULL OR t.status = :status)")
	    Page<Task> searchTasks(@Param("projectId") Long projectId,
	                           @Param("search") String search,
	                           @Param("status") Boolean status,
	                           Pageable pageable);
	}