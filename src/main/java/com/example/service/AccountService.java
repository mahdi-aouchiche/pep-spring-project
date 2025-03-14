package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {

    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    /** Register a new user account
     * @param newAccount
     * @return the new registred account
    */
    public Account registerUserAccount(Account newAccount) {
        String username = newAccount.getUsername();
        String password = newAccount.getPassword();

        if( ! username.isBlank() || password.length() > 4) {
            return accountRepository.save(new Account(username, password));
        }

        return null;
    }


    /** Login a user account
     * @param account
     * @return the account if the login is successful, null otherwise
    */
    public Account login(Account account) {

        Account foundAccount = accountRepository.findAccountByUsername(account.getUsername());
        
        if((foundAccount != null) && (foundAccount.getPassword().equals(account.getPassword()))) {
            return foundAccount;
        }

        return null;
    }


    /** Find an account by accountId
     * @param accountId
     * @return account if the account id exists, null otherwise
    */
    public Account getAccountById(Integer accountId) {
        return (Account)accountRepository.findById(accountId).orElse(null);
    }


    /** Find an account by username
     * @param username
     * @return account if the username exists, null otherwise
    */
    public Account getAccountByUsername(String username) {
        return (Account)accountRepository.findAccountByUsername(username);
    }
    
}
