package com.example.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * Write endpoints and handlers for the controller using Spring.
 * The endpoints needed can be found in readme.md as well as the test cases.
 * Use the @GET/POST/PUT/DELETE/etc Mapping annotations where applicable
 * as well as the @ResponseBody and @PathVariable annotations.
*/
@RestController
public class SocialMediaController {
    private MessageService messageService;
    private AccountService accountService;

    public SocialMediaController(MessageService messageService, AccountService accountService) {
        this.messageService = messageService;
        this.accountService = accountService;
    }


    /** ## 1
     * Create a new account to register a new user
     * @param newAccount
     * @return The response status 200 if the registration is successful. 
     *         The response body contains a JSON of the created account.
     * @return The response status 409 if the registration is not successful due to a duplicate username.
     * @return The response status 400 if the registration is not successful for some other reason.
    */
    @PostMapping("/register")
    public ResponseEntity<Account> createNewAccount(@RequestBody Account newAccount) {
    
        if( accountService.getAccountByUsername(newAccount.getUsername()) != null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Account createdAccount = accountService.registerUserAccount(newAccount);

        if(createdAccount == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(createdAccount, HttpStatus.OK);
        }
    }


    /** ## 2
     * Process User logins.
     * @param account
     * @return The response status of 200 if the login is successful.
     *         The response body contains a JSON of the user account.
     * @return The response status of 401. (Unauthorized) if the login is not successful.
    */
    @PostMapping("/login")
    public ResponseEntity<Account> userLogin(@RequestBody Account account) {
        Account foundAccount = accountService.login(account);
        if(foundAccount == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(foundAccount, HttpStatus.OK);
        }
    }

    
    /** ## 3
     * Creation a new message.
     * @param newMessage to create
     * @return The response status of 200 if message is created successfully.
     *         The response body contains a JSON of the created message.
     * @return The response status of 400 (Client error) if the message creation is not successful.
    */
    @PostMapping("/messages")
    public ResponseEntity<Message> createNewMessage(@RequestBody Message newMessage) {
        Integer postedBy = newMessage.getPostedBy();
        Account account = accountService.getAccountById(postedBy);

        if(account == null || newMessage.getMessageText().isBlank() || newMessage.getMessageText().length() > 255) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Message createdMessage = messageService.createMessage(newMessage);

        return new ResponseEntity<>(createdMessage, HttpStatus.OK);
    }


    /** ## 4 
     * Retrieve all messages.
     * @return The response of 200, which is the default.
     *         The response body contains a JSON of a List of all messages in the database
    */
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        return new ResponseEntity<>(messageService.getAllMessages(), HttpStatus.OK);
    }


    /** ## 5
     * Get a message by its ID.
     * @param messageId
     * @return A message with the given messageId. If no such message exists, return an empty response body. 
     * @return The response status should always be 200, which is the default.
    */
    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable("messageId") Integer messageId) {
        Message message = messageService.findMessageById(messageId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }


    /** ## 6
     * Delete a message identified by a message ID.
     * @param messageId
     * @return The response status of 200, which is the default.
     * @return The response body contain the number of rows deleted (1) if the message was deleted.
     * @return The reponse body is empty if the message was not found.
    */ 
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessageById(@PathVariable("messageId") Integer messageId) {
        Integer rowsUpdated = messageService.deleteMessageById(messageId);
        if(rowsUpdated == 0) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(rowsUpdated, HttpStatus.OK);
        }
    }
    
    
    /** ## 7
     * Update a message text identified by a message ID.
     * @param messageId
     * @param message with the new messageText
     * @return The response status of 200 if the update is successful.
     *         The response body contains the number of rows updated (1).
     * @return The response status of 400 (Client error) if the update is not successful.
     *         The response body is empty.
    */
    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Integer> updateMessageText(@PathVariable("messageId") Integer messageId, @RequestBody Message message) {
        Message foundMessage = messageService.findMessageById(messageId);
        if(foundMessage == null || message.getMessageText().isBlank() || message.getMessageText().length() > 255) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        foundMessage.setMessageText(message.getMessageText());
        messageService.createMessage(foundMessage);

        return new ResponseEntity<>(1, HttpStatus.OK);
    }

    
    /** ## 8
     * Retrieve all messages written by a particular user using their account id.
     * @param accountId
     * @return The response status of 200, which is the default.
     *         The response body contains a JSON representation of a list of all messages posted by a particular user.
    */
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getAllMessagesWrittenByUser(@PathVariable("accountId") Integer accountId) {
        return new ResponseEntity<>(messageService.getMessagesByAccountId(accountId), HttpStatus.OK);
    }

}
