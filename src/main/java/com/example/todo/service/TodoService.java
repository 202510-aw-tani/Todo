package com.example.todo.service;

import com.example.todo.entity.Todo;
import com.example.todo.exception.TodoNotFoundException;
import com.example.todo.exception.TodoNotFoundForEditException;
import com.example.todo.form.TodoForm;
import com.example.todo.repository.TodoRepository;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.dao.OptimisticLockingFailureException;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Transactional
    public Todo create(TodoForm form) {
        Todo todo = toEntity(form);
        return todoRepository.save(todo);
    }

    @Transactional(readOnly = true)
    public List<Todo> findAllOrderByCreatedAtDesc() {
        return todoRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    @Transactional(readOnly = true)
    public TodoForm findFormById(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundForEditException(id));
        return toForm(todo);
    }

    @Transactional
    public void update(Long id, TodoForm form) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundForEditException(id));
        if (!Objects.equals(form.getVersion(), todo.getVersion())) {
            throw new OptimisticLockingFailureException("Version mismatch");
        }
        todo.setTitle(form.getTitle());
        todo.setDescription(form.getDescription());
        if (form.getPriority() != null) {
            todo.setPriority(form.getPriority());
        }
        // Save is optional for managed entity, but explicit for clarity.
        todoRepository.save(todo);
    }

    @Transactional
    public void deleteById(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException(id));
        todoRepository.delete(todo);
    }

    private Todo toEntity(TodoForm form) {
        Todo todo = new Todo();
        todo.setTitle(form.getTitle());
        todo.setDescription(form.getDescription());
        if (form.getPriority() != null) {
            todo.setPriority(form.getPriority());
        }
        return todo;
    }

    private TodoForm toForm(Todo todo) {
        TodoForm form = new TodoForm();
        form.setTitle(todo.getTitle());
        form.setDescription(todo.getDescription());
        form.setPriority(todo.getPriority());
        form.setVersion(todo.getVersion());
        return form;
    }
}
