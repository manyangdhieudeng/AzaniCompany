package com.azani.company.model;

public class SeniorSchool extends Institution {
    public SeniorSchool(String name, ContactPerson contactPerson){
        super(name, contactPerson);
    }
    @Override
    public String getCategory(){
        return "Senior School";
    }
}
