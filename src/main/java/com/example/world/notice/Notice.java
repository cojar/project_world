package com.example.world.notice;


import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Size(max = 50)
    private String subject;

    private LocalDate createDate;

    private LocalDate modifyDate;

    private String thumbnailImg;

    @Column(columnDefinition = "text")
    private String content;

    @Column
    private int viewCount;


}
