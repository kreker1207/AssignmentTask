package com.example.assignment.service;

import com.example.assignment.exception.EntityNotFoundByIdException;
import com.example.assignment.exception.TooYoungException;
import com.example.assignment.model.User;
import com.example.assignment.model.UserDTO;
import com.example.assignment.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final String ILLEGAL_AGE = "You are too young";
    private static final String NOT_FOUND_BY_ID = "User was not found by id";
    private static final String INVALID_DATE = "Invalid date input";

    public static int YOUNG;
    @Value("${age}")
    public void setProperty(int value){
        YOUNG = value;
    }
    private final UserRepository userRepository;
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public User getUserById(Long id){
        return userRepository.findById(id).orElseThrow(()->{
            throw new EntityNotFoundByIdException(NOT_FOUND_BY_ID);
        });
    }
    public User addUser(User userSaveRequest) {
        isOld(userSaveRequest.getBirthDate());
        User userSave = new User()
                .setId(userSaveRequest.getId()==null?0:userSaveRequest.getId())
                .setFirstName(userSaveRequest.getFirstName())
                .setLastName(userSaveRequest.getLastName())
                .setEmail(userSaveRequest.getEmail())
                .setBirthDate(userSaveRequest.getBirthDate())
                .setAddress(userSaveRequest.getAddress())
                .setPhone(userSaveRequest.getPhone());
        return userRepository.save(userSave);
    }
    public User deleteUserById(Long id){
        User user = getUserById(id);
        userRepository.deleteById(id);
        return user;
    }

    public List<User> getUsersByBirthDate(LocalDate birthDateFrom,LocalDate birthDateTo) {
        if (birthDateFrom.isAfter(birthDateTo)) {
            throw new IllegalArgumentException(INVALID_DATE);
        }

        return userRepository.findAll().stream()
                .filter(user -> user.getBirthDate().isAfter(birthDateFrom) && user.getBirthDate().isBefore(birthDateTo))
                .collect(Collectors.toList());}

    public User updateSomeField(Long id, UserDTO userUpdate) {
        User oldUser = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundByIdException(NOT_FOUND_BY_ID));
        User configUser = updateFields(oldUser, userUpdate, oldUser.getId());
        isOld(configUser.getBirthDate());
        return userRepository.save(configUser);
    }

    public User updateAllFields(Long id, UserDTO userUpdate) {
        User oldUser = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundByIdException(NOT_FOUND_BY_ID));
        User configUser = updateFields(new User().setBirthDate(oldUser.getBirthDate()), userUpdate, oldUser.getId());
        isOld(configUser.getBirthDate());
        return userRepository.save(configUser);
    }

    private User updateFields(User oldUser, UserDTO newUser, Long id) {
        return new User().setId(id)
                .setEmail(newUser.getEmail() == null ? oldUser.getEmail() : newUser.getEmail())
                .setFirstName(newUser.getFirstName() == null ? oldUser.getFirstName() : newUser.getFirstName())
                .setLastName(newUser.getLastName() == null ? oldUser.getLastName() : newUser.getLastName())
                .setBirthDate(newUser.getBirthDate() == null ? oldUser.getBirthDate() : newUser.getBirthDate())
                .setAddress(newUser.getAddress() == null ? oldUser.getAddress() : newUser.getAddress())
                .setPhone(newUser.getPhone() == null ? oldUser.getPhone() : newUser.getPhone());
    }
    private void isOld(LocalDate birthDate){
        long age = LocalDate.from(birthDate).until(LocalDate.now(), ChronoUnit.YEARS);
        if(age < YOUNG) throw new TooYoungException(ILLEGAL_AGE);
    }
}
