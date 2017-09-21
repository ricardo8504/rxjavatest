package com.rick;



import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.*;

import java.lang.reflect.Array;
import java.util.Arrays;

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
	public void simpleAsyncAPICalls() throws InterruptedException {
        
        Flowable.create((FlowableEmitter<String> s) -> {
                try {
                String result = makeCallString("http://sisglocotizacionesapi.herokuapp.com/sisglo/sinapsis/ordenes/status");
                s.onNext(result);
                } catch (Exception e) {
                        s.onError(e);
                }
                s.onComplete();
        }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.newThread()).subscribe(System.out::println);

       Thread.currentThread().sleep(10000);
     
	}

	private String makeCallString(String URI) {
	        RestTemplate restTemplate = new RestTemplate();
	        String result = restTemplate.getForObject(URI, String.class);
	        return result;
	}
	
	
		
	
	public static void main(String args[]) throws InterruptedException {
		new TestSync().flowableAsyncBasic();
		Thread.currentThread().sleep(10000);
	}
	
}
