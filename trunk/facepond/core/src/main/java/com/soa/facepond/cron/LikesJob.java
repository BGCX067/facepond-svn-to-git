package com.soa.facepond.cron;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.soa.facepond.service.FacepondService;

public class LikesJob implements Job {
	
	private final Log log = LogFactory.getLog(LikesJob.class);
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.debug("Hello World Quartz Scheduler: " + new Date());
		System.out.println("Hello World Quartz Scheduler: " + new Date());
		//FacepondService facepondService = (FacepondService) context.get("facepondService");
		//facepondService.updateUserDeals();
		System.out.println("done updating: " + new Date());
		//List<User> users = facepondService
		
	}
}