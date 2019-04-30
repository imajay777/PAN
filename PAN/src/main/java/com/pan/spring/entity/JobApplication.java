/**
 * 
 */
package com.pan.spring.entity;
import java.sql.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


@Entity
@Table(name = "jobapplication")
public class JobApplication {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "appId", unique = true, nullable = false)
	private int appId;
	
	@ManyToOne
	@JoinColumn(name="jobId")
	private JobPosting jobposting;
	
	@ManyToOne
	@JoinColumn(name="jobSeekerId")
	@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="jobSeekerId")
	private JobSeeker jobSeeker;
	
	@Column(name="resume")
	private boolean resume;
	
	@Column(name="resumePath")
	private String resumePath;
	
	@Column(name="state")
	private int state;
	
	
	
	public int getAppId() {
		return appId;
	}

	
	public void setAppId(int appId) {
		this.appId = appId;
	}

	
	public JobPosting getJobPosting() {
		return jobposting;
	}

	
	public void setJobPosting(JobPosting jobPosting) {
		this.jobposting = jobPosting;
	}

	
	@JsonProperty
	public JobSeeker getJobSeeker() {
		return jobSeeker;
	}

	
	public void setJobSeeker(JobSeeker jobSeeker) {
		this.jobSeeker = jobSeeker;
	}

	
	public boolean isResume() {
		return resume;
	}

	
	public void setResume(boolean resume) {
		this.resume = resume;
	}

	
	public String getResumePath() {
		return resumePath;
	}

	
	public void setResumePath(String resumePath) {
		this.resumePath = resumePath;
	}

	
	public int getState() {
		return state;
	}

	
	public void setState(int state) {
		this.state = state;
	}

	
}
