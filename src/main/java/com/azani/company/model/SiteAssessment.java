package com.azani.company.model;

public class SiteAssessment {

    private int id;
    private Institution institution;
    private int numberOfUsers;
    private int existingComputers;
    private int existingLanNodes;
    private boolean ready;

    public SiteAssessment(Institution institution, int numberOfUsers, int existingComputers, int existingLanNodes) {
        this.institution = institution;
        this.numberOfUsers = numberOfUsers;
        this.existingComputers = existingComputers;
        this.existingLanNodes = existingLanNodes;
        this.ready = false;
    }

    // Getters
    public int getId() { return id; }
    public Institution getInstitution() { return institution; }
    public int getNumberOfUsers() { return numberOfUsers; }
    public int getExistingComputers() { return existingComputers; }
    public int getExistingLanNodes() { return existingLanNodes; }
    public boolean isReady() { return ready; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setExistingComputers(int existingComputers) { this.existingComputers = existingComputers; }
    public void setExistingLanNodes(int existingLanNodes) { this.existingLanNodes = existingLanNodes; }
    public void setReady(boolean ready) { this.ready = ready; }
}
