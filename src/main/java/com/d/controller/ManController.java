package com.d.controller;

import com.d.mapper.ManMapper;
import com.d.model.Man;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class ManController {
    @Resource
    ManMapper manMapper;

    @GetMapping("/man/{id}")
    public Man get(@PathVariable("id") Long id) {
        return manMapper.getById(id);
    }
}
