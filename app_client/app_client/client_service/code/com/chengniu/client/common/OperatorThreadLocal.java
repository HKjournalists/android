package com.chengniu.client.common;

public class OperatorThreadLocal {
	private static final ThreadLocal<Operator> OPERATOR_THREADLOCAL = new ThreadLocal<Operator>();

	public static void clean() {
		OPERATOR_THREADLOCAL.remove();
	}

	public static Operator get() {
		return OPERATOR_THREADLOCAL.get();
	}

	public static void set(Operator vo) {
		OPERATOR_THREADLOCAL.set(vo);
	}
}
