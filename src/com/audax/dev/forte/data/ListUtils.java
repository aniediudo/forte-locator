package com.audax.dev.forte.data;

import java.util.Collection;

public class ListUtils {
	public  interface Matcher<It> {
		boolean matches(It p0);
	}
	public static <I> I getFirst(Collection<I> source, Matcher<I> matcher) {
		for (I i : source) {
			if (matcher.matches(i)) {
				return i;
			}
		}
		return null;
	}
}
