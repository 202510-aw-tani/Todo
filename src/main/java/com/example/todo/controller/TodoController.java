package com.example.todo.controller;

import com.example.todo.form.TodoForm;
import com.example.todo.service.TodoService;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.dao.OptimisticLockingFailureException;

@Controller
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    // ToDo一覧を表示
    @GetMapping("/todos")
    public String list(Model model) {
        model.addAttribute("todos", todoService.findAllOrderByCreatedAtDesc());
        return "todo/list";
    }

    // ToDo新規作成画面を表示
    @GetMapping("/todos/new")
    public String newTodo(Model model) {
        TodoForm form = new TodoForm();
        form.setPriority(3);
        model.addAttribute("form", form);
        return "todo/form";
    }

    @PostMapping("/todos/confirm")
    public String confirm(@Valid @ModelAttribute("form") TodoForm form, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("form", form);
            return "todo/form";
        }
        model.addAttribute("form", form);
        return "todo/confirm";
    }

    @PostMapping("/todos/complete")
    public String complete(TodoForm form, RedirectAttributes redirectAttributes) {
        todoService.create(form);
        redirectAttributes.addFlashAttribute("message", "ToDoを作成しました。");
        return "redirect:/todos";
    }

    @PostMapping("/todos/{id}/delete")
    public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        todoService.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "ToDoを削除しました。");
        return "redirect:/todos";
    }

    @PostMapping("/todos/{id}/toggle")
    public String toggle(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        todoService.toggleCompleted(id);
        redirectAttributes.addFlashAttribute("message", "完了状態を更新しました。");
        return "redirect:/todos";
    }

    @PostMapping(value = "/todos/{id}/toggle", produces = "application/json")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> toggleAjax(@PathVariable("id") Long id) {
        var todo = todoService.toggleCompleted(id);
        Map<String, Object> body = new HashMap<>();
        body.put("id", todo.getId());
        body.put("completed", todo.getCompleted());
        return ResponseEntity.ok(body);
    }

    @GetMapping("/todos/{id}/edit")
    public String edit(@PathVariable("id") Long id, Model model) {
        model.addAttribute("form", todoService.findFormById(id));
        model.addAttribute("todoId", id);
        return "todo/edit";
    }

    @PostMapping("/todos/{id}/update")
    public String update(
            @PathVariable("id") Long id,
            @Valid @ModelAttribute("form") TodoForm form,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("todoId", id);
            return "todo/edit";
        }
        try {
            todoService.update(id, form);
        } catch (OptimisticLockingFailureException ex) {
            bindingResult.reject("optimisticLock", "他のユーザーにより更新されています。最新の内容で再度お試しください。");
            model.addAttribute("todoId", id);
            return "todo/edit";
        }
        redirectAttributes.addFlashAttribute("message", "ToDoを更新しました。");
        return "redirect:/todos";
    }

    // ToDo詳細を表示
    @GetMapping("/todos/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        model.addAttribute("todo", todoService.findById(id));
        return "todo/detail";
    }
}
