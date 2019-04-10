/**
 * 
 */
package com.pan.spring.dao;

import com.pan.spring.entity.JobApplication;


public interface JobApplicationDao {

	
	boolean cancel(int jobAppId);


	JobApplication apply(int jobseekerId, int jobId, boolean resumeFlag, String resumePath);

	
	JobApplication getJobApplication(int jobAppId);

	
	JobApplication modifyJobApplicationStatus(int jobAppId, int state);
	
	
	JobApplication updateApplication(JobApplication ja);

}
