package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> { 
    
    /**
     * Find an account by username
     * @param username
     * @return account if the username exists, null otherwise
    */
    Account findAccountByUsername(String username);
    
}