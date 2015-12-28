package com.soa.facepond.api;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

import org.junit.Test;

public class CalendarTest extends TestCase {

	@Test
	public void testYear(){
		 
		Calendar date = Calendar.getInstance();  
	    date.setTime(new Date());  
	    Format f = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  
	    System.out.println(f.format(date.getTime()));  
	    date.add(Calendar.YEAR,-1);  
	    System.out.println(f.format(date.getTime()));  
	    System.out.println(date.getTime().toString());
		
	}
}
