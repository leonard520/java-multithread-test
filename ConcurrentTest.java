package com.test.atomic;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrentTest {
	private static Map<Integer, AtomicInteger> idMgmt = new ConcurrentHashMap<Integer, AtomicInteger>();
	private static Map<Integer, AtomicInteger> idMgmtNoLock = new HashMap<Integer, AtomicInteger>();
	public static Integer nextId(Integer k) throws InterruptedException {		
		AtomicInteger v = null;
		Integer result = null;
		v = idMgmt.get(k);

		if (v == null) {
			v = new AtomicInteger(0);
			idMgmt.put(k, v);
		}

		result = v.incrementAndGet();
		System.out.println(k.toString() + " " + result.toString());
		return result;
	}
	
	public void testConcurrent(){
		long start = System.currentTimeMillis();
		int threadNum = 16;
		int times = 100;
		int maxSite = 10;
		ExecutorService executor = Executors.newFixedThreadPool(threadNum);
		for(int i = 0; i < times; i++){
			
			Random r = new Random();
			Integer src = r.nextInt(maxSite);
			Integer dest = r.nextInt(maxSite);
			Runnable worker = new ThreadForIdAllocate(src, dest);
			executor.execute(worker);
		}
		executor.shutdown();
		long end = System.currentTimeMillis();
	    System.out.println("Finished all threads " + (end - start) + " ms");
	}
	
	public class ThreadForIdAllocate implements Runnable{
		Integer dest;
		Integer src;
		public ThreadForIdAllocate(Integer src, Integer dst){
			//System.out.println("thread " + name);
			this.src = src;
			this.dest = dst;
		}
		@Override
		public void run(){
			int id;
			try {
				id = ConcurrentTest.nextId(this.src);
				System.out.println("src: " + src + " dst: " + this.dest + " id: " + id);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String args[]){
		ConcurrentTest t = new ConcurrentTest();
		t.test();
	}
}
