package com.Sk.BookMyShow.Exceptions;

public class InvalidCurrencyException extends RuntimeException{
    
    public  InvalidCurrencyException(){
        super("Invalid Currency ");
    }

    public InvalidCurrencyException(String msg){
        super(msg);
    }
}
