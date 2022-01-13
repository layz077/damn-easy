package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="post_details")
public class PostDetails {

    @Id
    @Column(name = "post_id")
    private long postId;
    @Column(name = "commented_by_id")
    private long commentedById;
    @Column(name = "liked_by_id")
    private long likedById;
    @Column(name = "comment")
    private String comment;

}
