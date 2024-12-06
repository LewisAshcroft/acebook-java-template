package com.makersacademy.acebook.repository;

import com.makersacademy.acebook.model.Comments;
import com.makersacademy.acebook.model.Like;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comments, Long> {
    List<Comments> findAll();
}