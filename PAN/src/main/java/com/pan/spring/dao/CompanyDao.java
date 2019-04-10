package com.pan.spring.dao;

import java.util.List;

import com.pan.spring.entity.Company;


public interface CompanyDao {
	
	
	
	public List<String> PasswordLookUp(String emailid);
	

	public Company createCompany(Company com) throws Exception;

	
	public Company updateCompany(Company js);

	
	public Company getCompany(int id);
	
	
	public void verify(Company c);
	
	
	public List<?> getJobsByCompany(int companyId);

	
	public List<Integer> getCompanyIdFromEmail(String emailid);
	
	

}
