package com.eg;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ConcurrentHashMapTest {
	public ConcurrentMap<Integer, List<String>> tables = new ConcurrentHashMap<>();

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public int times = 1000000;

	public void join(Integer s) {
		while (true) {
			List<String> l = tables.get(s);
			if (l == null) {
				System.out.println(s + " is null");
				l = new LinkedList<String>();
				List<String> l2 = tables.putIfAbsent(s, l);
				if(l2 != null){
					continue;
				}
			} else {
				System.out.println(s + " is not null");
			}
			synchronized (l) {
				for (int i = 0; i < times; i++) {
					// System.out.println("add " + s + " " + i);
					l.add(new String(i + ""));
				}
				System.out.println(s + " length " + l.size());
			}
			return;

		}
	}
}
