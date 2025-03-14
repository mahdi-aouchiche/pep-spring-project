package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    /**
     * Find all messages by the user who posted them
     * @param postedBy
     * @return list of messages or an empty list if no messages are found
    */
    List<Message> findAllMessagesBypostedBy(Integer postedBy);
    
}