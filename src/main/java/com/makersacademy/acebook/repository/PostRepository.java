package com.makersacademy.acebook.repository;

import com.makersacademy.acebook.model.Post;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.stream.IntStream;

public interface PostRepository extends CrudRepository<Post, Long> {

    //List<Post> findAllPublicPostsOrderedByCreatedAt();

    @Override
    long count();

    List<Post> findAll();
}
