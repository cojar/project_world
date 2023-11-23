package com.example.world.admin;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminSearchForm {

//    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime start;

//    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime end;


}
