package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }


    /**
     * Create a new message
     * @param message
     * @return the newly created message
    */
    public Message createMessage(Message message) {
        return messageRepository.save(message);
    }


    /** Get all messages written by user using their account id
     * @param accountId
     * @return a list of messages written by the account id, empty list if no messages are found
     */
    public List<Message> getMessagesByAccountId(Integer accountId) {
        return messageRepository.findAllMessagesBypostedBy(accountId);  
    }


    /** Delete a message by its id
     * @param messageId
     * @return 1 if the message was deleted, 0 if the message was not found
    */
    public Integer deleteMessageById(Integer messageId) {
        Message message =(Message)messageRepository.findById(messageId).orElse(null);
        if(message == null) {
            return 0;
        }
        
        // delete the message
        messageRepository.deleteById(messageId);
        return 1;
    }


    /** Find a message by its id
     * @param messageId
     * @return the message with the given id, or null if no such message exists
    */
    public Message findMessageById(Integer messageId) {
        return (Message)messageRepository.findById(messageId).orElse(null);
    }


    /** Retreive all messages available in the database
     * @return a list of all messages in the database
    */
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

}