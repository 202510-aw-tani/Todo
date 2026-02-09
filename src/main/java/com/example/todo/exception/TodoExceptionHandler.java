package com.example.todo.exception;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
@Controller
public class TodoExceptionHandler {

    @ExceptionHandler(TodoNotFoundException.class)
    public String handleTodoNotFound(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "指定したToDoが見つかりません");
        return "redirect:/todos";
    }
}
