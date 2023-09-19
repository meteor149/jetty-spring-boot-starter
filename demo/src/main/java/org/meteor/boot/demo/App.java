package org.meteor.boot.demo;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"org.meteor.boot.demo", "org.meteor.boot"})
public class App {
    public static void main (String[] args) {
        //SpringApplication.run(App.class, args);

    }
}
