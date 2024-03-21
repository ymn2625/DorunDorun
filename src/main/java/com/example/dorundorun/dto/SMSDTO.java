package com.example.dorundorun.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
@NoArgsConstructor
public class SMSDTO {
    private String id;
    private String certificationCode;
    private String tel;
}
