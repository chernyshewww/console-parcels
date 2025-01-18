package com.hofftech.deliverysystem;

import com.hofftech.deliverysystem.telegram.BotFactory;
import com.hofftech.deliverysystem.telegram.BotInitializer;
import com.hofftech.deliverysystem.util.ConfigLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}