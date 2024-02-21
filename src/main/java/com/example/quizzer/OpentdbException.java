package com.example.quizzer;

public class OpentdbException extends Exception {
    OpentdbException(){
        super();
    }

    OpentdbException(Exception e){
        super(e);
    }
}
