package com.d.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Man {
    @Id
    private Long id;

    private String name;

    private Integer age;

    private Integer sex;

    private String idNo;

    private LocalDateTime created;

    private LocalDateTime updated;

    private Integer isDel;
}
