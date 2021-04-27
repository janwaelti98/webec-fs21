package webeng.contactlist.model;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryIT {

    private final UserRepository repo;

    UserRepositoryIT(@Autowired UserRepository repo) throws IOException {
        this.repo = repo;
    }

    @Test
    void insertAndGet() {
        var admin = new User("admin", "2093840293845", Set.of("ADMIN"));
        repo.save(admin);

        var users = repo.findAll();
        assertEquals(1, users.size());
        var dbAdmin = users.get(0);
        assertEquals("admin", dbAdmin.getUsername());
    }

    @Test
    void findByUsername() {
        var admin = new User("admin", "2093840293845", Set.of("ADMIN"));
        repo.save(admin);

        var dbAdmin = repo.findByUsername("admin");
        assertTrue(dbAdmin.isPresent());
        assertEquals("admin", dbAdmin.get().getUsername());
    }

    @Test
    void findByUsernameNonExisting() {
        var dbAdmin = repo.findByUsername("admin");
        assertEquals(Optional.empty(), dbAdmin);
    }
}
