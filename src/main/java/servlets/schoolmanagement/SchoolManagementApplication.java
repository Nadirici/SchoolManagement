package servlets.schoolmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"servlets.schoolmanagement.controllers", "servlets.schoolmanagement.services","servlets.schoolmanagement.repository","servlets.schoolmanagement.DAO" })

public class SchoolManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchoolManagementApplication.class, args);
		System.out.println("Hello World!");
	}

}
