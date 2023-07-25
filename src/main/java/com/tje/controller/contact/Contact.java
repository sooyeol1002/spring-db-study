package com.tje.controller.contact;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Controller;

@Data
@Builder
@AllArgsConstructor
public class Contact {
    private Integer id;
    private String name;
    private String phone;
    private String email;
}
