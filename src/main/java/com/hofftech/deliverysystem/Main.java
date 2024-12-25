package com.hofftech.deliverysystem;

import com.hofftech.deliverysystem.controller.AppController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        try {
            ApplicationContext context = SpringApplication.run(Main.class, args);

            AppController appController = context.getBean(AppController.class);

            appController.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
