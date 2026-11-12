package com.azani.company.model;

public class JuniorSchool extends Institution {
    public JuniorSchool(String name, ContactPerson contactPerson){
        super(name, contactPerson);
    }
    @Override
    public String getCategory(){
        return "Junior School";
    }
}
