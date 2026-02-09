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
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.dao.OptimisticLockingFailureException;

@Controller
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    // ToDo\u4E00\u89A7\u3092\u8868\u793A
    @GetMapping("/todos")
    public String list(Model model) {
        model.addAttribute("todos", todoService.findAllOrderByCreatedAtDesc());
        return "todo/list";
    }

    // ToDo\u65B0\u898F\u4F5C\u6210\u753B\u9762\u3092\u8868\u793A
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
        redirectAttributes.addFlashAttribute("message", "ToDo\u3092\u4F5C\u6210\u3057\u307E\u3057\u305F\u3002");
        return "redirect:/todos";
    }

    @PostMapping("/todos/{id}/delete")
    public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        todoService.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "ToDo\u3092\u524A\u9664\u3057\u307E\u3057\u305F\u3002");
        return "redirect:/todos";
    }

    @PostMapping("/todos/{id}/toggle")
    public String toggle(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        todoService.toggleCompleted(id);
        redirectAttributes.addFlashAttribute("message", "\u5B8C\u4E86\u72B6\u614B\u3092\u66F4\u65B0\u3057\u307E\u3057\u305F\u3002");
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
            @Valid TodoForm form,
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
            bindingResult.reject("optimisticLock", "\u4ED6\u306E\u30E6\u30FC\u30B6\u30FC\u306B\u3088\u308A\u66F4\u65B0\u3055\u308C\u3066\u3044\u307E\u3059\u3002\u6700\u65B0\u306E\u5185\u5BB9\u3067\u518D\u5EA6\u304A\u8A66\u3057\u304F\u3060\u3055\u3044\u3002");
            model.addAttribute("todoId", id);
            return "todo/edit";
        }
        redirectAttributes.addFlashAttribute("message", "ToDo\u3092\u66F4\u65B0\u3057\u307E\u3057\u305F\u3002");
        return "redirect:/todos";
    }

    // ToDo\u8A73\u7D30\u3092\u8868\u793A
    @GetMapping("/todos/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        model.addAttribute("todo", todoService.findById(id));
        return "todo/detail";
    }
}