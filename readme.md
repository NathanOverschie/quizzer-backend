# Quizzer - Trivia Web Application

Quizzer is a simple web application that offers a set of multiple-choice questions fetched from the Open Trivia Database API. The user can select answers and check if they are correct. The backend is built in Java using Spring Boot, and the frontend is a React-based user interface for easy interaction.

## Features

- Fetches 5 multiple-choice questions from the Open Trivia Database API.
- Provides a backend REST API to serve questions and check answers.
- Prevents users from inspecting the correct answers by hiding them in the backend.
- Offers a simple and intuitive user interface built with React.

## Getting Started

Follow these instructions to get a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

- Java Development Kit (JDK) installed on your machine (version 21).

### Running

1. Download the file at `/target/Quizzer.jar`

2. Run this jar file

   ```bash
   java -jar Quizzer.jar
   ```

3. Access the application in your web browser at `http://localhost:8080`.

## APIs

The backend exposes the following REST endpoints:

- `GET /questions`: Fetches 5 questions and possible answers.
- `POST /checkanswers`: Checks if provided answer is correct.

## Technologies Used

- Java
- Spring Boot
- Maven
- React
