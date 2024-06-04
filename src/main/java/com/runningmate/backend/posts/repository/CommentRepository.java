package com.runningmate.backend.posts.repository;

import com.runningmate.backend.posts.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
