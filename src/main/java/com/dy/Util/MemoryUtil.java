package com.dy.Util;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

public class MemoryUtil {

	private static final double MB = 1.0 / (1024.0 * 1024.0);
	private static final double GB = MB / 1024.0;

	public static boolean checkMemmory(long needed) {
		Runtime current = Runtime.getRuntime();
		long currentAviliable = current.maxMemory() -  current.totalMemory() + current.freeMemory();
		return currentAviliable > needed;
	}

	public static String logMemoryStatusInMB() {
		return logMemoryStatus(MB, "MB");
	}

	public static String logMemoryStatusInGB() {
		return logMemoryStatus(GB, "GB");
	}

	public static String logMemoryStatus() {
		return logMemoryStatusInMB();
	}

	public static String logMemoryStatus(double mutiplier, String unit) {
		StringBuilder builder = new StringBuilder();
		Runtime current = Runtime.getRuntime();
		double total = current.maxMemory() * mutiplier;
		double phsical = current.totalMemory() * mutiplier;
		double free = current.freeMemory() * mutiplier;
		builder.append("JVM总内存:" + total + unit + System.getProperty("line.separator"));
		builder.append("JVM物理内存:" + phsical + unit + System.getProperty("line.separator"));
		builder.append("JVM空闲内存：" + free + unit + System.getProperty("line.separator"));
		builder.append("JVM使用内存：" + (phsical - free) + unit + System.getProperty("line.separator"));
		MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
		MemoryUsage memoryUsage = memoryMXBean.getNonHeapMemoryUsage();
		builder.append("非堆内存使用：" + (memoryUsage.getUsed() * mutiplier + unit + System.getProperty("line.separator")));
		memoryUsage = memoryMXBean.getHeapMemoryUsage();
		builder.append("堆内存使用情况：" + (memoryUsage.getUsed() * mutiplier + unit));
		return builder.toString();
	}
}
