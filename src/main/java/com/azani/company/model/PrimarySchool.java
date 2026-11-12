package com.azani.company.model;

public class PrimarySchool extends Institution {
    public PrimarySchool(String name, ContactPerson contactPerson){
        super(name, contactPerson);
    }
    @Override 
    public String getCategory(){
        return "Primary School";
    }
    
}
