package com.eg;

import java.util.Random;

public class ThreadRun implements Runnable{
	public static ConcurrentHashMapTest test = new ConcurrentHashMapTest();
	public int max = 1000;
	
	public void run() {
		Random r = new Random();
		test.join(r.nextInt(max));
	}
	
	public static void main(String args[]){
		int threadNum = 40;
		long start = System.currentTimeMillis();
		Thread[] t = new Thread[threadNum];
		for(int i = 0; i < threadNum; i++){
			t[i] = new Thread(new ThreadRun());
			t[i].start();
		}
		for(int i = 0; i < threadNum; i++){
			try {
				t[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		long end = System.currentTimeMillis();
		System.out.println(end - start);
	}
}
