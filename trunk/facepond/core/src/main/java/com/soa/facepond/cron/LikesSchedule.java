package com.soa.facepond.cron;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.soa.facepond.service.FacepondService;

@Component
public class LikesSchedule {
	private final Log log = LogFactory.getLog(LikesSchedule.class);
	
	@Autowired
	private FacepondService facepondService;
	
	public LikesSchedule() throws Exception {
		log.error("calling LikesSchedule");
		System.out.println("calling LikesSchedule");
		init();
	}
	
	public void init(){
		try {
			SchedulerFactory sf = new StdSchedulerFactory();
			Scheduler sched = sf.getScheduler();
			sched.start();
			JobDetail jd = new JobDetail("myjob", sched.DEFAULT_GROUP,
					LikesJob.class);

			//jd.getJobDataMap().put("facepondService", facepondService);
			SimpleTrigger st = new SimpleTrigger("mytrigger", sched.DEFAULT_GROUP,
					new Date(), null, SimpleTrigger.REPEAT_INDEFINITELY,
					60L * 1000L);
			sched.scheduleJob(jd, st);
		}catch(Exception e){
			log.error(e);
		}
	}

	public static void main(String args[]) {
		try {
			new LikesSchedule();
		} catch (Exception e) {
		}
	}
}