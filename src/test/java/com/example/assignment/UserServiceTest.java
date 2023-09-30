package com.example.assignment;

import com.example.assignment.exception.EntityNotFoundByIdException;
import com.example.assignment.model.User;
import com.example.assignment.repository.UserRepository;
import com.example.assignment.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = AssignmentApplication.class)
@ExtendWith(SpringExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;

    @Test
    void getUsers(){
        var userList = getUserList();
        Mockito.when(userRepository.findAll()).thenReturn(userList);
        var expectedList = userService.getAllUsers();
        assertThat(expectedList).isEqualTo(userList);
    }
    @Test
    void getUserByIdSuccess(){
        var sourceUser = getDefaultUser();
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(sourceUser));
        var expectedUser = userService.getUserById(1L);
        assertThat(expectedUser).isEqualTo(sourceUser);
    }
    @Test
    void getUserByIdFail(){
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundByIdException.class,()-> userService.getUserById(1L));

    }
    @Test
    void deleteUserByIdSuccess(){
        var user = getDefaultUser();
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        userService.deleteUserById(1L);
        Mockito.verify(userRepository).deleteById(1L);
    }
    @Test
    void deleteUserByIdFail(){
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());
        String expectedMessage = "User was not found by id";
        String actualMessage = Assertions.assertThrows(EntityNotFoundByIdException.class,()->userService.deleteUserById(1L)).getMessage();
        assertThat(expectedMessage).isEqualTo(actualMessage);

    }
    @Test
    void addUserSuccess(){
        var user = getDefaultUser();
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        User savedUser = userService.addUser(user);
        Mockito.verify(userRepository).save(user);
        assertThat(user).isEqualTo(savedUser);
    }
    @Test
    void addUserUnderageFail(){
        var userSaveRequest = getDefaultUser().setBirthDate(LocalDate.now().minusYears(5));
        Assertions.assertThrows(IllegalArgumentException.class,()->userService.addUser(userSaveRequest));
    }


    @Test
    void updateSomeFieldUserSuccess() {
        long userId = 1L;
        var userUpdate = getDefaultUser().setFirstName("Anton").setLastName("Chaika");
        var oldUser = getDefaultUser();
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(oldUser));
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(userUpdate);
        User updatedUser = userService.updateSomeField(userId, userUpdate.toUserDto());

        assertThat(updatedUser).isEqualTo(userUpdate);
        assertThat(updatedUser).isNotEqualTo(oldUser);
        Mockito.verify(userRepository).save(updatedUser);
    }

    @Test
    void updateSomeFieldUnderageFail() {
        long userId = 1L;
        var userUpdate = getDefaultUser().setBirthDate(LocalDate.now().minusYears(5));
        var oldUser = getDefaultUser();
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(oldUser));

       Assertions.assertThrows(IllegalArgumentException.class, () -> userService.updateSomeField(userId, userUpdate.toUserDto()));
    }


    @Test
    void updateAllUserSuccess() {
        long userId = 1L;
        var userUpdate = new User().setId(1L).setFirstName("Kiril").
                setLastName("Sirij").setEmail("ksirij@gmail.com").
                setBirthDate(LocalDate.of(2003,8,4)).setPhone("+380666666666"). setAddress("Kharkiv");
        var oldUser = getDefaultUser();
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(oldUser));
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(userUpdate);
        User updatedUser = userService.updateAllFields(userId, userUpdate.toUserDto());

        assertThat(updatedUser).isEqualTo(userUpdate);
        assertThat(updatedUser).isNotEqualTo(oldUser);
        Mockito.verify(userRepository).save(updatedUser);
    }

    @Test
    void updateAllFail(){
        var userUpdate = getDefaultUser();
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());
        String expectedMessage = "User was not found by id";
        String actualMessage = Assertions.assertThrows(EntityNotFoundByIdException.class,()->userService.updateAllFields(1L,userUpdate.toUserDto())).getMessage();
        assertThat(expectedMessage).isEqualTo(actualMessage);
    }

    @Test
    void getByDateSuccess(){
        LocalDate birthDateFrom = LocalDate.of(2002, 6, 1);
        LocalDate birthDateTo = LocalDate.of(2005, 12, 31);
        List<User> userList = Arrays.asList(
                new User().setId(1L).setFirstName("User1").setBirthDate(LocalDate.of(2002, 3, 15)),
                new User().setId(2L).setFirstName("User2").setBirthDate(LocalDate.of(2003, 7, 25)),
                new User().setId(3L).setFirstName("User3").setBirthDate(LocalDate.of(2004, 11, 10))
        );
        List<User> expectedUserList = Arrays.asList(
                new User().setId(2L).setFirstName("User2").setBirthDate(LocalDate.of(2003, 7, 25)),
                new User().setId(3L).setFirstName("User3").setBirthDate(LocalDate.of(2004, 11, 10))
        );
        Mockito.when(userRepository.findAll()).thenReturn(userList);
        List<User> result = userService.getUsersByBirthDate(birthDateFrom, birthDateTo);
        assertThat(result).isEqualTo(expectedUserList);
    }
    @Test
    void  getByDateFail(){
        LocalDate birthDateFrom = LocalDate.of(2005, 1, 1);
        LocalDate birthDateTo = LocalDate.of(2000, 12, 31);

        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.getUsersByBirthDate(birthDateFrom, birthDateTo));
    }

    private List<User> getUserList(){
        return Arrays.asList(
                new User().setId(1L).setFirstName("Ivan").
                        setLastName("Baranetskyi").setEmail("baranetskyi@gmail.com").
                        setBirthDate(LocalDate.of(2003,7,12)).setPhone("+380956954604")
                        .setAddress("Odesa"),
                new User().setId(2L).setFirstName("Kiril").
                        setLastName("Sirij").setEmail("ksirij@gmail.com").
                        setBirthDate(LocalDate.of(2003,8,4)).setPhone("+380666666666"). setAddress("Kharkiv")

        );
    }
    private User getDefaultUser(){
        return new User().setId(1L).setFirstName("Ivan").
                setLastName("Baranetskyi").setEmail("baranetskyi@gmail.com").
                setBirthDate(LocalDate.of(2003,7,12)).setPhone("+380956954604")
                .setAddress("Odesa");
    }

}
