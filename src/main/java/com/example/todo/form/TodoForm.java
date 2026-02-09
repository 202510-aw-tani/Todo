package com.example.todo.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TodoForm {

    private String title;

    private String description;

    private Integer priority;
}
