package com.example.assignment.repository;

import com.example.assignment.exception.EntityNotFoundByIdException;
import com.example.assignment.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

// Imitation of real repository by using List
@Repository
public class UserRepository{
    private final List<User> users = new ArrayList<>();
    private long nextId = 1;

    public User save(User user){
        if(user.getId() == 0){
            user.setId(nextId++);
            users.add(user);
            return user;
        }
        else {
            for(int i = 0;i<users.size();i++){
                User existsUser = users.get(i);
                if(Objects.equals(existsUser.getId(), user.getId())){
                    users.set(i,user);
                    return user;
                }
            }
        }
        throw new EntityNotFoundByIdException("User with ID " + user.getId() + " not found.");
    }
    public Optional<User> findById(long id){
        return users.stream().filter(user -> user.getId() == id).findFirst();
    }

    public List<User> findAll(){
        return users;
    }
    public void deleteById(long id){
        users.removeIf(user -> user.getId() == id);
    }
    public void deleteAll(){
        users.clear();
    }

}
