package br.com.alexandremauro.todolist.user;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface IUserRepository  extends JpaRepository<userModel,UUID>{
    
    userModel findByUsername(String username);
}
