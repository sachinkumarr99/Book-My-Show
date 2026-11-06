package com.Sk.BookMyShow.Exceptions;

public class ShowException extends RuntimeException{
    public ShowException(){
        super("Error Occured in operation related to Shows");
    }
    public ShowException(String msg){
        super(msg);
    }
}
