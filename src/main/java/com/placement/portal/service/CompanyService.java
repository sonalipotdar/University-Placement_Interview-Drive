
package com.placement.portal.service;

import com.placement.portal.model.Company;
import com.placement.portal.repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public int addCompany(Company company) {
        return companyRepository.addCompany(company);
    }

    public List<Company> getAllCompanies() {
        return companyRepository.getAllCompanies();
    }

    public Optional<Company> getCompanyById(int companyId) {
        return companyRepository.getCompanyById(companyId);
    }

    public int updateCompany(Company company) {
        return companyRepository.updateCompany(company);
    }

    public int deleteCompany(int companyId) {
        return companyRepository.deleteCompany(companyId);
    }
}
