package com.rick;

import rx.Observable;
import rx.schedulers.Schedulers;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestSync {

	@Test
	public void testJust() {
		String value = "";
		Observable.just("Hola Mundo Just")
				.subscribe(s -> {
					System.out.println(s);
				});
		
		assertTrue(true);
	}
	
	
	@Test
	public void testJustSchedulers() {
		Iterable<String> value = Observable.just("Hola Mundo Just Schedulers")
			.subscribeOn(Schedulers.computation())
			.toBlocking()
			.toIterable();
		assertEquals("Hola Mundo Just Schedulers",value.iterator().next());
	}
	
}
