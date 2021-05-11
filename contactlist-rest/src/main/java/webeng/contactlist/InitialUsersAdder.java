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

@Component
public class InitialUsersAdder {

    private final UserRepository userRepo;
    private final Optional<String> adminPassword;

    public InitialUsersAdder(UserRepository userRepo,
                             @Value("${contact-list.admin-password:#{null}}") Optional<String> adminPassword) {
        this.userRepo = userRepo;
        this.adminPassword = adminPassword;
    }

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (userRepo.findAll().isEmpty()) {
            var encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            var password = adminPassword.orElse(generatePassword());
            var encoded = encoder.encode(password);
            var admin = new User("admin", encoded, Set.of("ADMIN"));
            userRepo.save(admin);
            System.out.println(password); // maybe better than to log, will not show up in log files
        }
    }

    private String generatePassword() {
        var random = new SecureRandom();
        return new BigInteger(128, random).toString(32); // better than nextInt()...
    }
}
