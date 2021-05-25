package webeng.contactlist;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Component;
import webeng.contactlist.model.User;
import webeng.contactlist.model.UserRepository;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Optional;
import java.util.Set;

import static java.util.Collections.emptySet;

@Component
public class InitialUsersAdder {

    private final UserRepository userRepo;
    private final Optional<String> adminPassword;
    private final Optional<String> userPassword;

    public InitialUsersAdder(UserRepository userRepo,
                             @Value("${contact-list.admin-password:#{null}}") Optional<String> adminPassword,
                             @Value("${contact-list.user-password:#{null}}") Optional<String> userPassword) {
        this.userRepo = userRepo;
        this.adminPassword = adminPassword;
        this.userPassword = userPassword;
    }

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (userRepo.findAll().isEmpty()) {
            var encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

            var adminPw = adminPassword.orElse(generatePassword());
            var admin = new User("admin", encoder.encode(adminPw), Set.of("ADMIN"));
            userRepo.save(admin);
            System.out.println("Password for 'admin': " + adminPw);

            var userPw = userPassword.orElse(generatePassword());
            var user = new User("user", encoder.encode(userPw), emptySet());
            userRepo.save(user);
            System.out.println("Password for 'user': " + userPw);
        }
    }

    private String generatePassword() {
        var random = new SecureRandom();
        return new BigInteger(128, random).toString(32); // better than nextInt()...
    }
}
