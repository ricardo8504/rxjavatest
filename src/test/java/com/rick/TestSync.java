package com.rick;

import rx.Observable;
import rx.schedulers.Schedulers;

import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.*;

import java.lang.reflect.Array;
import java.util.Arrays;

import org.junit.Test;

public class TestSync {

	
	@Test
	public void testCreate() {
		String value = "";
		Observable.create(s ->{
			s.onNext("Hola Mundo Create 1");
			s.onNext("Hola Mundo Create 2");
			s.onCompleted();
		})
				.subscribe(s -> {
					assertThat(Arrays.asList(new String[] {"Hola Mundo Create 1","Hola Mundo Create 2"}),hasItem(s));
				});
		
		assertTrue(true);
	}
	
	
	
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
