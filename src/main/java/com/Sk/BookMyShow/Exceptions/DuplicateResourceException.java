package com.Sk.BookMyShow.Exceptions;

public class DuplicateResourceException extends RuntimeException{
    public DuplicateResourceException(){
        super("Resource Already Exists");
    }
    public DuplicateResourceException(String msg){
        super(msg);
    }
}
