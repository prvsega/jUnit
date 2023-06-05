package com.example.demo.junit;

import com.example.demo.extensions.GlobalExtension;
import com.example.demo.junit.dto.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(GlobalExtension.class)
class SomeServicesTest {

    private UserService userService;
    private final static User ADMIN = User.of(1, "admin", "12345");
    private final static User MANAGER = User.of(2, "manager", "man");



    @BeforeEach
    void prepare() {
        userService = new UserService();
        userService.add(ADMIN);
        userService.add(MANAGER);
    }

    @Test
    void usersEmptyIfNoUserAdded() {

        var users = userService.getAll();
        users.remove(ADMIN);
        users.remove(MANAGER);
        assertTrue(users.isEmpty());
        assertFalse(false);
    }

    @Test
    void usersSizeIfUsersAdded() {
        var users = userService.getAll();

        assertThat(users).hasSize(2);

    }


    @Test
    @Tag("login")
    void loginSuccessIfUserExists() {
        Optional<User> users = userService.login(ADMIN.getUsername(), ADMIN.getPassword());

        assertThat(users).isPresent();
        users.ifPresent(user -> {
            assertThat(user).isEqualTo(ADMIN);
        });
//        assertTrue(users.isPresent());
//        users.ifPresent(user -> assertEquals(ADMIN, user));
    }

    @Test
    @Tag("login")
    void loginFailIfUserDoesNotExist() {

        Optional<User> users = userService.login(ADMIN.getUsername() + "22", ADMIN.getPassword());
        assertTrue(users.isEmpty());

    }

    @Test
    @Tag("login")
    void loginFailIfPasswordIsNotCorrect() {

        Optional<User> users = userService.login(ADMIN.getUsername(), ADMIN.getPassword() + "123123");
        assertTrue(users.isEmpty());

    }

    @Test
    @Tag("login")
        void throwExceptionIfUsernameOrPasswordIsNull() {

        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> userService.login(null, "123")),
                () -> assertThrows(IllegalArgumentException.class, () -> userService.login("admin", null))
        );


    }

    @ParameterizedTest
    @MethodSource("getArgForLoginTest")
        void loginFailIfUsernameIsNullEmptyWrong(String username, String password, Optional<User> user){

        var maybeUser = userService.login(username, password);

        assertThat(maybeUser).isEqualTo(user);

    }
    public static Stream<Arguments> getArgForLoginTest() {

        return Stream.of(
                Arguments.of("admin", "12345", Optional.of(ADMIN)),
                Arguments.of("manager", "man", Optional.of(MANAGER)),
                Arguments.of("admin", "0000", Optional.empty()),
                Arguments.of("dddd", "man", Optional.empty())
        );


    }



    @AfterEach
    void deleteDataFromDatabase() {
    }


}
