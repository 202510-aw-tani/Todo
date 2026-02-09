package com.example.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TodoController {

    // ToDo一覧画面を表示します。
    @GetMapping("/todos")
    public String list() {
        return "todo/list";
    }

    // ToDo新規作成画面を表示します。
    @GetMapping("/todos/new")
    public String newTodo() {
        return "todo/new";
    }

    @PostMapping("/todos/confirm")
    public String confirm(
            @RequestParam("title") String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "priority", defaultValue = "3") Integer priority,
            Model model
    ) {
        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("priority", priority);
        return "todo/confirm";
    }

    // 指定IDのToDo詳細画面を表示します。
    @GetMapping("/todos/{id}")
    public String detail(@PathVariable("id") Long id) {
        return "todo/detail";
    }
}
