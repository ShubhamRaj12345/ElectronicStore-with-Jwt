package com.electronic.HandlingException;


public class BadApiRequest extends  RuntimeException {


     public BadApiRequest(String  message){
         super(message);
     }

     public BadApiRequest(){
         super("BadRequest");
     }
}
