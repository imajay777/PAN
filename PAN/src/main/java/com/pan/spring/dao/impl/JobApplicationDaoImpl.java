package com.pan.spring.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pan.spring.dao.JobApplicationDao;
import com.pan.spring.entity.JobApplication;
import com.pan.spring.entity.JobPosting;
import com.pan.spring.entity.JobSeeker;

@Service
@Transactional
public class JobApplicationDaoImpl implements JobApplicationDao{
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public JobApplication apply(int jobseekerId, int jobId, boolean resumeFlag, String resumePath) {
		JobApplication ja = new JobApplication();
		try {
			JobSeeker js = entityManager.find(JobSeeker.class, jobseekerId);
			JobPosting jp = entityManager.find(JobPosting.class, jobId);
			ja.setJobPosting(jp);
			ja.setJobSeeker(js);
			ja.setResume(resumeFlag);
			if (!resumePath.equals(null)) {
				ja.setResumePath(resumePath);
			}
			ja.setState(0);
			entityManager.persist(ja);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ja;
	}

	@Override
	public boolean cancel(int jobAppId) {
		JobApplication ja = getJobApplication(jobAppId);
		if (ja != null) {
			entityManager.remove(ja);
		}
		return false;
	}

	public JobApplication getJobApplication(int jobAppId) {
		JobApplication ja = null;
		try {
			ja = entityManager.find(JobApplication.class, jobAppId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ja;
	}
	public List<?> getseeker(int jobId) {
		
		Query one=entityManager.createQuery("SELECT JobSeeker.firstName, JobSeeker.workEx,JobSeeker.highestEducation,JobSeeker.skills	 FROM JobApplication ja WHERE ja.JobPosting.jobId = :jobId");
		one.setParameter("jobId", jobId);
		List<?> querylist1 = one.getResultList();
		
		
		
		/*Query query = entityManager.createQuery("SELECT firstname,workEx,highestEducation,skills FROM jobseeker WHERE ");
		query.setParameter("jobId", jobId);
		List<?> querylist = query.getResultList();*/
		return querylist1;
	}

	
	@Override
	public JobApplication modifyJobApplicationStatus(int jobAppId, int state) {
		JobApplication ja = null;
		ja = getJobApplication(jobAppId);
		try {
			if (ja != null) {
				ja.setState(state);
				entityManager.merge(ja);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ja;
	}

	@Override
	public JobApplication updateApplication(JobApplication ja) {
		entityManager.merge(ja);
		return null;
	}
}
