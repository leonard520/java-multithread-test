package com.eg;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/*
 * synchronized a method. bad performance.
 */
public class SyncTest {
	public Map<Integer, List<String>> tables = new HashMap<Integer, List<String>>();
	public int getTimes() {
		return times;
	}
	public void setTimes(int times) {
		this.times = times;
	}
	public int times = 1000000;
	public synchronized void join(Integer s) {
		System.out.println("visit " + s);
	    List<String> l = tables.get(s);
	    if(l == null){
	    	System.out.println(s + " is null");
	    	l = new LinkedList<String>();
	    	tables.put(s, l);
	    } else {
	    	System.out.println(s + " is not null");
	    }
	    for(int i = 0; i < times; i++){
	    	//System.out.println("add " + s + " " + i);
	    	l.add(new String(i + ""));
	    }
		System.out.println(s + " length " + l.size());
	}
	
	
}
