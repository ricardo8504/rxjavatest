package com.rick;



import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;



public class TestSync {

	
	@Test
	public void testCreate() {
		String value = "";
		Observable.create(s ->{
			s.onNext("Hola Mundo Create 1");
			s.onNext("Hola Mundo Create 2");
			
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
	public void flowableAsyncBasic() {
         Flowable.create((FlowableEmitter<Void> s) -> {
                try {
                		System.out.println("Se inicia el async");
                        Thread.sleep(3000);
                        System.out.println("Se finaliza el async");
                } catch (Exception e) {
                	e.printStackTrace();
                }
                s.onComplete();
        }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.newThread()).subscribe();

        System.out.println("Se finaliza el flowableAsyncBasic");
	}	
	
	@Test
	@Ignore
	public void asyncAPICallsTest() throws InterruptedException {
        
        Flowable.create((FlowableEmitter<String> s) -> {
                try {
                	System.out.println("asyncAPICallsTestObserver: "+Thread.currentThread().getName());
                String result = makeCallString("http://sisglocotizacionesapi.herokuapp.com/sisglo/sinapsis/ordenes/status");
                s.onNext(result+" 1");
                Thread.currentThread().sleep(1000);
                s.onNext(result+" 2");
                System.out.println("asyncAPICallsTest: "+s);
                Thread.currentThread().sleep(1000);
                s.onNext(result+" 3");
                Thread.currentThread().sleep(1000);
                s.onNext(result);
                } catch (Exception e) {
                        s.onError(e);
                }
                s.onComplete();
        }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(s->{
        	Thread.currentThread().sleep(5000);
        	System.out.println("asyncAPICallsTest: "+Thread.currentThread().getName());
        	System.out.println("asyncAPICallsTest: "+s);
        });

       Thread.currentThread().sleep(20000);
       System.out.println("asyncAPICallsTest: "+Thread.currentThread().getName());
	}

	private String makeCallString(String URI) {
	        RestTemplate restTemplate = new RestTemplate();
	        String result = restTemplate.getForObject(URI, String.class);
	        return result;
	}
	
	@Test
	public void arrayProcessFlowable() {
		List<String> lista = Arrays.asList("1","2");
		Flowable.fromArray(lista).map(s -> s.stream()
					.map(val -> val+".1")
					.collect(Collectors.toList()))
				.subscribe(s -> {
			System.out.println("Lista:"+ s);
			
		});
		
	}
	@Test
	public void testListObservable() throws InterruptedException {
		List<String> data = Arrays.asList("Hola","Mundo");
		Flowable.fromIterable(data)
			.observeOn(Schedulers.computation())
			.subscribeOn(Schedulers.io())
			.subscribe(System.out::println)
			.wait();
			
	}
	
	@Test
	public void callableFlowable() {
		 Flowable.fromCallable(()-> makeCallString("http://sisglocotizacionesapi.herokuapp.com/sisglo/sinapsis/ordenes/status"))
		 		.observeOn(Schedulers.io())
		 		.subscribe(System.out::println);
		 
		 Flowable.fromCallable(()-> makeCallString("http://sisglocotizacionesapi.herokuapp.com/sisglo/sinapsis/ordenes/status"))
	 		.observeOn(Schedulers.io())
	 		.subscribe(System.out::println);
		 	 
		 		
		
	}
	
		
	
	public static void main(String args[]) throws InterruptedException {
		new TestSync().flowableAsyncBasic();
		
	}
	
}
