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
	private static Map<Integer, Integer> idMgmtNoLock = new HashMap<Integer, Integer>();
	private static Map<Integer, Integer> idMgmtWithLock = new HashMap<Integer, Integer>();
	
	public ConcurrentTest(){
		
	}
	
	public static Integer nextId(Integer k) throws InterruptedException {		
		AtomicInteger v = null;
		Integer result = null;
		v = idMgmt.get(k);

		if (v == null) {
			v = new AtomicInteger(0);
			AtomicInteger tmp = idMgmt.putIfAbsent(k, v);
			if(tmp != null){
				v = tmp;
			}
		}

		result = v.incrementAndGet();
		//System.out.println(k.toString() + " " + result.toString());
		return result;
	}
	
	public static Integer nextIdNoLock(Integer k) throws InterruptedException {		
		Integer v = null;
		Integer result = null;
		v = idMgmtNoLock.get(k);

		if (v == null) {
			v = new Integer(0);
			idMgmtNoLock.put(k, v);
		}

		result = v.intValue();
		result++;
		idMgmtNoLock.put(k, result);
		//System.out.println(k.toString() + " " + result.toString());
		return result;
	}
	
	public static Integer nextIdWithLock(Integer k) throws InterruptedException {		
		Integer v = null;
		Integer result = null;
		
		
		synchronized(ConcurrentTest.class){
			v = idMgmtWithLock.get(k);

			if (v == null) {
				v = new Integer(0);
				idMgmtWithLock.put(k, v);
			}

			result = v.intValue();
			result++;
			idMgmtWithLock.put(k, result);
			//System.out.println(k.toString() + " " + result.toString());
		}
		//System.out.println(k.toString() + " " + result.toString());
		return result;
	}
	
	public void testConcurrent(){
		long start = System.currentTimeMillis();
		int threadNum = 23;
		int times = 1000000;
		int maxSite = 10;
		ExecutorService executor = Executors.newFixedThreadPool(threadNum);
		for(int i = 0; i < times; i++){
			
			Random r = new Random();
			Integer src = r.nextInt(maxSite);
			Integer dest = r.nextInt(maxSite);
			Runnable worker = new ThreadConcurrent(src, dest);
			executor.execute(worker);
		}
		executor.shutdown();
        while (!executor.isTerminated()) {
        }
		long end = System.currentTimeMillis();
	    System.out.println("Finished all threads " + (end - start) + " ms");
	    
	    int all = 0;
	    for(AtomicInteger a : idMgmt.values()){
	    	all += a.intValue();
	    }
	    System.out.println("all is " + all);
	}
	
	public void testNoLock(){
		long start = System.currentTimeMillis();
		int threadNum = 23;
		int times = 10000;
		int maxSite = 10;
		ExecutorService executor = Executors.newFixedThreadPool(threadNum);
		for(int i = 0; i < times; i++){
			Random r = new Random();
			Integer src = r.nextInt(maxSite);
			Integer dest = r.nextInt(maxSite);
			Runnable worker = new ThreadNoLock(src, dest);
			executor.execute(worker);
		}
		executor.shutdown();
        while (!executor.isTerminated()) {
        }
		long end = System.currentTimeMillis();
	    System.out.println("Finished all threads " + (end - start) + " ms");
	    
	    int all = 0;
	    for(Integer a : idMgmtNoLock.values()){
	    	all += a;
	    }
	    System.out.println("all is " + all);
	}
	
	public void testWithLock(){
		long start = System.currentTimeMillis();
		int threadNum = 23;
		int times = 1000000;
		int maxSite = 10;
		ExecutorService executor = Executors.newFixedThreadPool(threadNum);
		for(int i = 0; i < times; i++){
			Random r = new Random();
			Integer src = r.nextInt(maxSite);
			Integer dest = r.nextInt(maxSite);
			Runnable worker = new ThreadWithLock(src, dest);
			executor.execute(worker);
		}
		executor.shutdown();
        while (!executor.isTerminated()) {
        }
		long end = System.currentTimeMillis();
	    System.out.println("Finished all threads " + (end - start) + " ms");
	    
	    int all = 0;
	    for(Integer a : idMgmtWithLock.values()){
	    	all += a;
	    }
	    System.out.println("all is " + all);
	}
	
	public class ThreadConcurrent implements Runnable{
		Integer dest;
		Integer src;
		public ThreadConcurrent(Integer src, Integer dst){
			//System.out.println("thread " + name);
			this.src = src;
			this.dest = dst;
		}
		@Override
		public void run(){
			int id;
			try {
				id = ConcurrentTest.nextId(this.src);
				//System.out.println("src: " + src + " dst: " + this.dest + " id: " + id);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public class ThreadNoLock implements Runnable{
		Integer dest;
		Integer src;
		public ThreadNoLock(Integer src, Integer dst){
			//System.out.println("thread " + name);
			this.src = src;
			this.dest = dst;
		}
		@Override
		public void run(){
			int id;
			try {
				id = ConcurrentTest.nextIdNoLock(this.src);
				//System.out.println("src: " + src + " dst: " + this.dest + " id: " + id);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public class ThreadWithLock implements Runnable{
		Integer dest;
		Integer src;
		public ThreadWithLock(Integer src, Integer dst){
			//System.out.println("thread " + name);
			this.src = src;
			this.dest = dst;
		}
		@Override
		public void run(){
			int id;
			try {
				id = ConcurrentTest.nextIdWithLock(this.src);
				//System.out.println("src: " + src + " dst: " + this.dest + " id: " + id);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String args[]){
		ConcurrentTest t = new ConcurrentTest();
		t.testConcurrent();
	}
}
