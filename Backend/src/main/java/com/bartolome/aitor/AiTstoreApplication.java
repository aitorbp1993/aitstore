package com.bartolome.aitor;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableScheduling
public class AiTstoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiTstoreApplication.class, args);
    }

    @PostConstruct
    public void generarHashAdmin() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        String hash = encoder.encode("admin123");
        System.out.println("🧪 Hash correcto para 'admin123': " + hash);
    }

}
