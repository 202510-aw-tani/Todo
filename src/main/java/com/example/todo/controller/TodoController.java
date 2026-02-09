package com.example.todo.controller;

import com.example.todo.form.TodoForm;
import com.example.todo.service.TodoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    // ToDo一覧画面を表示します。
    @GetMapping("/todos")
    public String list(Model model) {
        model.addAttribute("todos", todoService.findAllOrderByCreatedAtDesc());
        return "todo/list";
    }

    // ToDo新規作成画面を表示します。
    @GetMapping("/todos/new")
    public String newTodo() {
        return "todo/form";
    }

    @PostMapping("/todos/confirm")
    public String confirm(TodoForm form, Model model) {
        model.addAttribute("title", form.getTitle());
        model.addAttribute("description", form.getDescription());
        model.addAttribute("priority", form.getPriority());
        return "todo/confirm";
    }

    @PostMapping("/todos/complete")
    public String complete(TodoForm form, RedirectAttributes redirectAttributes) {
        todoService.create(form);
        redirectAttributes.addFlashAttribute("message", "登録が完了しました");
        return "redirect:/todos";
    }

    @PostMapping("/todos/{id}/delete")
    public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        todoService.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "ToDoを削除しました");
        return "redirect:/todos";
    }

    @GetMapping("/todos/{id}/edit")
    public String edit(@PathVariable("id") Long id, Model model) {
        model.addAttribute("form", todoService.findFormById(id));
        model.addAttribute("todoId", id);
        return "todo/edit";
    }

    // 指定IDのToDo詳細画面を表示します。
    @GetMapping("/todos/{id}")
    public String detail(@PathVariable("id") Long id) {
        return "todo/detail";
    }
}
