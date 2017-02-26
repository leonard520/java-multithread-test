package com.eg;

import java.util.Random;

public class ThreadRun implements Runnable{
	public static ConcurrentHashMapTest test = new ConcurrentHashMapTest();
	public int max = 10;
	
	public void run() {
		Random r = new Random();
		test.join(r.nextInt(max));
	}
	
	public static void main(String args[]){
		long start = System.currentTimeMillis();
		Thread[] t = new Thread[10];
		for(int i = 0; i < 10; i++){
			t[i] = new Thread(new ThreadRun());
			t[i].start();
		}
		for(int i = 0; i < 10; i++){
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
