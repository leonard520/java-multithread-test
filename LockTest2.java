package com.eg;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class LockTest2 {
	public Map<Integer, List<String>> tables = new HashMap<Integer, List<String>>();
	public Object[] locks = new Object[lockNum];
	public int getTimes() {
		return times;
	}
	public void setTimes(int times) {
		this.times = times;
	}
	public int times = 1000000;
	public static final int threadNum = 2;
	public static final int lockNum = 2;
	
	public LockTest2(){
		for(int i = 0; i < lockNum; i ++){
			locks[i] = new Object();	
		}
	}

	public void join(Integer s) {
		List<String> l;
		Object lock = locks[s % 2];
		synchronized (lock) {
			l = tables.get(s);
			if (l == null) {
				System.out.println(s + " is null");
				l = new LinkedList<String>();
		    	tables.put(s, l);
			} else {
		    	System.out.println(s + " is not null");
		    }
			for (int i = 0; i < times; i++) {
				//System.out.println("add " + s + " " + i);
				l.add(new String(i + ""));
			}
		    tables.put(s, l);
			System.out.println(s + " length " + l.size());
		}
	}
}
