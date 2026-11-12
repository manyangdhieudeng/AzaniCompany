package com.azani.company.model;

public class College extends Institution{
    public College(String name, ContactPerson contactPerson){
        super(name,contactPerson);
    }
    @Override 
    public String getCategory(){
        return "College";
    }
} 
