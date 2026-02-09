package com.example.todo.repository;

import com.example.todo.entity.Todo;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    List<Todo> findByCompleted(Boolean completed);

    List<Todo> findByTitleContaining(String keyword);

    List<Todo> findByDueDateLessThanEqual(LocalDate date);

    List<Todo> findAllByOrderByPriorityAsc();

    // @Query usage example: same as findByTitleContaining + order by priority
    @Query("select t from Todo t where t.title like %:keyword% order by t.priority asc")
    List<Todo> searchByTitleOrderByPriority(@Param("keyword") String keyword);
}
