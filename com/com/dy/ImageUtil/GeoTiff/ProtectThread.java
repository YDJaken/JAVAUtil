package com.dy.ImageUtil.GeoTiff;

// 守护线程
public class ProtectThread extends Thread {
	private final Thread[] pool;
	private final Compare fatherThread;
	private static final int waitTime = 6 * 1000;

	ProtectThread(Thread[] pool, Compare fatherThread) {
		this.pool = pool;
		this.fatherThread = fatherThread;
	}

	private boolean stopFlag = false;

	public void setFlag(boolean input) {
		this.stopFlag = input;
	}

	private int test() {
		int ret = 0;
		for (int i = 0; i < pool.length; i++) {
			if (pool[i] == null)
				continue;
			if (((SubCompareThread) pool[i]).stillRun()) {
				ret++;
			}
		}
		return ret;
	}

	@Override
	public synchronized void run() {
		super.run();
		for (int i = 0; i < pool.length; i++) {
			if (pool[i] == null)
				continue;
			pool[i].start();
		}
		
		while (!stopFlag) {

			int t = test();

			if (t == 0) {
				setFlag(true);
			}

			if (fatherThread.threadCount != t) {
				System.out.println("发现线程意外停止,记录运行线程数:" + fatherThread.threadCount + "实际运行线程数:" + t + "请检查日志。");
			}
			
			try {
				sleep(waitTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
				setFlag(true);
				fatherThread.reProtect();
			}
		}

	}
}
