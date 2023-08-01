package com.ysy.myapp.contact;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ContactModifyRequest {
    private String name;
    private String phone;
}
