package com.pan.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.pan.spring.dao.CompanyDao;
import com.pan.spring.dao.JobPostingDao;
import com.pan.spring.entity.Company;
import com.pan.spring.entity.JobPosting;

@Controller
@RequestMapping("/JobPosting")
public class JobPostingController {
	@Autowired
	JobPostingDao jobDao;

	@Autowired
	CompanyDao companyDao;

	/**
	 * @param cid
	 * @param model
	 * @return homepage view
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String showHomePage(@RequestParam("cid") String cid, Model model) {
		System.out.println(cid);
		
		Company company = companyDao.getCompany(Integer.parseInt(cid));
		model.addAttribute("cid", cid);
		model.addAttribute("company", company);
		return "postjob";
	}

	/**
	 * @param title
	 * @param description
	 * @param responsibilities
	 * @param location
	 * @param salary
	 * @param cid
	 * @param model
	 * @return JobPosting
	 */
	@RequestMapping(method = RequestMethod.POST)
	public String createJobPosting(@RequestParam("title") String title, @RequestParam("description") String description,
			@RequestParam("responsibilities") String responsibilities, @RequestParam("location") String location,
			@RequestParam("salary") String salary, @RequestParam("cid") String cid, Model model) {
		JobPosting j = new JobPosting();
		j.setTitle(title);
		j.setDescription(description);
		j.setResponsibilities(responsibilities);
		j.setLocation(location);
		j.setSalary(salary);
		j.setKeywords(title + " " + description + " " + responsibilities + " " + location);

		try {
			

			JobPosting p1 = jobDao.createJobPosting(j, Integer.parseInt(cid));
			

			model.addAttribute("job", p1);
			Company company = companyDao.getCompany(Integer.parseInt(cid));
			model.addAttribute("company", company);
			return "jobprofile";

		} catch (Exception e) {
			
			return "error";
		}

	}
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public String deleteJobPosting(@PathVariable("id") int id, Model model) {

		if (jobDao.deleteJobPosting(id)) {
			String message = "Job Posting with JobID " + id + " is deleted successfully";
			model.addAttribute("message", message);
			return "message";
		} else {
			return "error";
		}
	}

	@RequestMapping(value = "/update/{id}",method = RequestMethod.GET)
	public String showUpdatePage(@PathVariable("id") int id, @RequestParam("cid") String cid, Model model) {
		System.out.println(cid);
		System.out.println(id);
		
		Company company = companyDao.getCompany(Integer.parseInt(cid));
		JobPosting jp = jobDao.getJobPosting(id);
		model.addAttribute("job", jp);
		model.addAttribute("company", company);
		return "updatejob";
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
	public String updateJobPosting(@PathVariable("id") int id, @RequestParam("state") String state,
			@RequestParam("title") String title, @RequestParam("description") String description,
			@RequestParam("responsibilities") String responsibilities, @RequestParam("location") String location,
			@RequestParam("salary") String salary, @RequestParam("cid") String cid, Model model) {
		// TODO routing
		JobPosting job = jobDao.getJobPosting(id);
		if (job != null) {
			job.setjobId(id);
			job.setDescription(description);
			job.setState(Integer.parseInt(state));
			job.setTitle(title);
			job.setLocation(location);
			job.setResponsibilities(responsibilities);
			JobPosting p1 = jobDao.updateJobPosting(job);

			model.addAttribute("job", p1);
			Company company = companyDao.getCompany(Integer.parseInt(cid));
			model.addAttribute("company", company);
			return "jobprofile";
		}
		return "error";

	}
	
	
	@RequestMapping(value = "/modifyjobstate", method = RequestMethod.POST)
	public String modifyJobState(@RequestParam("jobId") String jobId, @RequestParam("state") String state) {
		JobPosting jp = jobDao.getJobPosting(Integer.parseInt(jobId));
		jp.setState(Integer.parseInt(state));
		jp = jobDao.updateJobPosting(jp);
		if(jp==null){
			return "Error";
		}
		return "modified";
	}

	
}
