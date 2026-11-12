package com.azani.company.model;

import java.time.LocalDate;

public abstract class Institution {
    private int id;
    private String name;
    private ContactPerson contactPerson;
    private InstitutionStatus status;
    private LocalDate registrationDate;

    public Institution(String name, ContactPerson contactPerson){
        this.name = name;
        this.contactPerson = contactPerson;
        this.status = InstitutionStatus.REGISTERED_UNPAID;
        this.registrationDate = LocalDate.now();
    }

    public abstract String getCategory();

    //getters
    public int getId(){return id;}
    public String getName(){return name;}
    public ContactPerson getContactPerson(){return contactPerson;}
    public InstitutionStatus getStatus(){return status;}
    public LocalDate getRegistrationDate(){return registrationDate;}

    //setters
    public void setId(int id) { this.id = id; }
public void setStatus(InstitutionStatus status) { this.status = status; }

@Override
public String toString() {
    return name + " (" + getCategory() + ")";
}
}
