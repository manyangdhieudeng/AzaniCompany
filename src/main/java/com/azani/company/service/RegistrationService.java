package com.azani.company.service;

import com.azani.company.dao.InstitutionDAO;
import com.azani.company.dao.InstitutionDAOImpl;
import com.azani.company.dao.PaymentDAO;
import com.azani.company.dao.PaymentDAOImpl;
import com.azani.company.dao.SiteAssessmentDAO;
import com.azani.company.dao.SiteAssessmentDAOImpl;
import com.azani.company.dao.SubscriptionDAO;
import com.azani.company.dao.SubscriptionDAOImpl;
import com.azani.company.model.BandwidthPlan;
import com.azani.company.model.College;
import com.azani.company.model.ContactPerson;
import com.azani.company.model.Institution;
import com.azani.company.model.InstitutionStatus;
import com.azani.company.model.JuniorSchool;
import com.azani.company.model.Payment;
import com.azani.company.model.PaymentType;
import com.azani.company.model.PrimarySchool;
import com.azani.company.model.SeniorSchool;
import com.azani.company.model.SiteAssessment;
import com.azani.company.model.Subscription;

public class RegistrationService {

    private final InstitutionDAO institutionDAO = new InstitutionDAOImpl();
    private final PaymentDAO paymentDAO = new PaymentDAOImpl();
    private final SiteAssessmentDAO siteAssessmentDAO = new SiteAssessmentDAOImpl();
    private final SubscriptionDAO subscriptionDAO = new SubscriptionDAOImpl();

    private static final double REGISTRATION_FEE = 8500;
    private static final double INSTALLATION_FEE = 10000;

    // Use Case: Register Institution - now also records the desired bandwidth plan
    public Institution registerInstitution(String category, String name, ContactPerson contactPerson, BandwidthPlan desiredPlan) {
        Institution institution = switch (category) {
            case "Primary School" -> new PrimarySchool(name, contactPerson);
            case "Junior School" -> new JuniorSchool(name, contactPerson);
            case "Senior School" -> new SeniorSchool(name, contactPerson);
            case "College" -> new College(name, contactPerson);
            default -> throw new IllegalArgumentException("Unknown category: " + category);
        };

        institutionDAO.save(institution);

        // Create the subscription now, but it stays inactive until installation is paid
        Subscription subscription = new Subscription(institution, desiredPlan);
        subscription.setStatus(InstitutionStatus.REGISTERED_UNPAID);
        subscriptionDAO.save(subscription);

        return institution;
    }

    public void payRegistrationFee(Institution institution) {
        if (institution.getStatus() != InstitutionStatus.REGISTERED_UNPAID) {
            throw new IllegalStateException("Registration fee already paid or not applicable.");
        }

        Payment payment = new Payment(institution, PaymentType.REGISTRATION, REGISTRATION_FEE);
        paymentDAO.save(payment);

        institution.setStatus(InstitutionStatus.REGISTRATION_PAID);
        institutionDAO.updateStatus(institution);
    }

    public SiteAssessment recordSiteAssessment(Institution institution, int numberOfUsers,
                                                int existingComputers, int existingLanNodes, boolean ready) {
        if (institution.getStatus() != InstitutionStatus.REGISTRATION_PAID) {
            throw new IllegalStateException("Institution must have paid registration before a site visit.");
        }

        SiteAssessment assessment = new SiteAssessment(institution, numberOfUsers, existingComputers, existingLanNodes);
        assessment.setReady(ready);
        siteAssessmentDAO.save(assessment);

        if (ready) {
            institution.setStatus(InstitutionStatus.READY);
            institutionDAO.updateStatus(institution);
        }

        return assessment;
    }

    // Use Case: Pay Installation Fee - now also activates the subscription chosen at registration
    public void payInstallationFee(Institution institution) {
        if (institution.getStatus() != InstitutionStatus.READY) {
            throw new IllegalStateException("Site must be assessed and ready before installation.");
        }

        Payment payment = new Payment(institution, PaymentType.INSTALLATION, INSTALLATION_FEE);
        paymentDAO.save(payment);

        institution.setStatus(InstitutionStatus.ACTIVE);
        institutionDAO.updateStatus(institution);

        Subscription subscription = subscriptionDAO.findByInstitutionId(institution.getId());
        if (subscription != null) {
            subscription.setStatus(InstitutionStatus.ACTIVE);
            subscriptionDAO.updateStatus(subscription);
        }
    }
}