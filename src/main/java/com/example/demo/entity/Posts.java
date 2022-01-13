package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Posts {

    @Id @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long postId;
    private String caption;
    private String location;

    @OneToOne
    private User user;

    private long likes;
    private long comments;


}
