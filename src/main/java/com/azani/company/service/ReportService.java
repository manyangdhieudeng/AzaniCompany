package com.azani.company.service;

import java.util.List;
import java.util.stream.Collectors;

import com.azani.company.dao.BillDAO;
import com.azani.company.dao.BillDAOImpl;
import com.azani.company.dao.InstitutionDAO;
import com.azani.company.dao.InstitutionDAOImpl;
import com.azani.company.dao.PurchaseDAO;
import com.azani.company.dao.PurchaseDAOImpl;
import com.azani.company.dao.SiteAssessmentDAO;
import com.azani.company.dao.SiteAssessmentDAOImpl;
import com.azani.company.dao.SubscriptionDAO;
import com.azani.company.dao.SubscriptionDAOImpl;
import com.azani.company.model.Bill;
import com.azani.company.model.Institution;
import com.azani.company.model.InstitutionStatus;
import com.azani.company.model.Purchase;
import com.azani.company.model.SiteAssessment;

public class ReportService {

    private final InstitutionDAO institutionDAO = new InstitutionDAOImpl();
    private final BillDAO billDAO = new BillDAOImpl();
    private final SubscriptionDAO subscriptionDAO = new SubscriptionDAOImpl();
    private final SiteAssessmentDAO siteAssessmentDAO = new SiteAssessmentDAOImpl();
    private final PurchaseDAO purchaseDAO = new PurchaseDAOImpl();
    
    

    // Use Case: Generate Registered Institutions List
    public List<Institution> getRegisteredInstitutions() {
        return institutionDAO.findAll();
    }

    // Use Case: Generate Defaulters List
    public List<Bill> getDefaulters() {
        return billDAO.findDefaulters();
    }

    // Use Case: Generate Disconnection List
    public List<Institution> getDisconnectedInstitutions() {
        return institutionDAO.findAll().stream()
                .filter(i -> i.getStatus() == InstitutionStatus.DISCONNECTED)
                .collect(Collectors.toList());
    }

    // Use Case: Generate Infrastructure Requirements Report
    public List<SiteAssessment> getInfrastructureReport() {
        return siteAssessmentDAO.findAll();
    }

    // Supporting detail: all purchases made by one institution (used within the infrastructure report)
    public List<Purchase> getPurchasesForInstitution(int institutionId) {
        return purchaseDAO.findAll().stream()
                .filter(p -> p.getInstitution().getId() == institutionId)
                .collect(Collectors.toList());
    }
}