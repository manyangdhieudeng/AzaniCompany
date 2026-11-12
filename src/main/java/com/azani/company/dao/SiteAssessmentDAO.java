package com.azani.company.dao;

import java.util.List;

import com.azani.company.model.SiteAssessment;

public interface SiteAssessmentDAO {
    void save(SiteAssessment siteAssessment);
    SiteAssessment findById(int id);
    List<SiteAssessment> findAll();
}
