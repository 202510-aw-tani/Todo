package com.example.todo.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TodoForm {

    @NotBlank
    @Size(max = 50)
    private String author;

    @NotBlank
    @Size(max = 100)
    private String title;

    @Size(max = 500)
    private String detail;

    @Min(1)
    @Max(5)
    private Integer priority;

    private Long version;
}
