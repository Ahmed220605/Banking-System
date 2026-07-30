package com.example.demo;

import com.example.demo.ui.ConsoleMenu;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {
    private final ConsoleMenu console;

    public DemoApplication(ConsoleMenu console){
        this.console = console;
    }

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

    @Override
    public void run(String... args) throws Exception {
        console.start();
    }
}
