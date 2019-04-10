package com.pan.spring.dao;

import java.util.List;

import com.pan.spring.entity.Interested;

public interface InterestedDao {

	
	public Interested createInterest(Interested in) throws Exception;

	
	public boolean deleteInterest(int id);

	
	public Interested getInterest(int id);


	public List<?> getInterestedJobId(int jobId, int userId);
	
	
	public List<Integer> getAllInterestedJobId(int userId); 
}
