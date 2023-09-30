package com.example.assignment.controller;

import com.example.assignment.model.User;
import com.example.assignment.model.UserDTO;
import com.example.assignment.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }

    @GetMapping("/date")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getUserByBirth( @RequestParam("birthDateFrom") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate birthDateFrom,
                                      @RequestParam("birthDateTo") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate birthDateTo){
        return userService.getUsersByBirthDate(birthDateFrom,birthDateTo);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@Valid @RequestBody User userSaveRequest){
        return userService.addUser(userSaveRequest);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User deleteUser(@PathVariable Long id){
        return userService.deleteUserById(id);
    }

    @PutMapping("/some/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User updateSomeField(@PathVariable Long id, @RequestBody UserDTO userUpdate){
        return userService.updateSomeField(id,userUpdate);
    }
    @PutMapping("/all/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User updateAllFields(@PathVariable Long id, @RequestBody UserDTO userUpdate){
        return userService.updateAllFields(id,userUpdate);
    }

}
