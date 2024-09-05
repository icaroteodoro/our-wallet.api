package api.ourwallet;


import api.ourwallet.domains.User;
import api.ourwallet.services.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserTest {
    @Autowired
    private UserService userService;

    @Test
    @Order(1)
    void createUser() {
        User user = new User();
        user.setName("Testando 123");
        user.setEmail("teste@teste.com");
        user.setPassword("Teste123@");
        User newUser = this.userService.createUser(user);
        Assertions.assertNotNull(newUser);
    }

    @Test
    @Order(2)
    void emailAlreadyRegistered() {
        User user = new User();
        user.setName("Testando 123");
        user.setEmail("teste@teste.com");
        user.setPassword("Teste123@");
        Exception exception = Assertions.assertThrows(
                RuntimeException.class,
                () -> {
                    this.userService.createUser(user);
                }
        );
        Assertions.assertEquals("Email already registered", exception.getMessage());
    }

    @Test
    @Order(3)
    void findUserByEmail() {
        User user = this.userService.findUserByEmail("teste@teste.com");
        Assertions.assertNotNull(user);
    }

    @Test
    @Order(4)
    void findUserByEmailDoesNotExists() {
        Exception exception = Assertions.assertThrows(
                RuntimeException.class,
                () -> {
                    this.userService.findUserByEmail("teste123@test.com");
                }
        );
        Assertions.assertEquals("User not found", exception.getMessage());
    }


    @Test
    @Order(5)
    //AINDA NÃO CONSEGUI RODAR ESSE TESTE, MAS DEIXEI AQUI PRA DELETAR O USUARIO CRIADO NO PRIMEIRO TESTE
    void deleteUser() {
        this.userService.deleteUserByEmail("teste@teste.com");
    }
}
