package com.example.quizzer;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class QuizzerApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(QuizzerApplication.class)
				.web(WebApplicationType.SERVLET)
				.run(args);
	}
}
