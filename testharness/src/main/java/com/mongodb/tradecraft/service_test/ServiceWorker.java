package com.mongodb.tradecraft.service_test;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceWorker implements Runnable {

	Logger logger;
	HttpClient client;
	int threadId;
	int nCalls = 1000;
	String url  = null;
	ServiceWorker(int threadid,String url, int ncalls) {
		logger = LoggerFactory.getLogger(ServiceWorker.class);
		client = HttpClientBuilder.create().build();
		this.threadId = threadid;
		this.url = url;
		this.nCalls = ncalls;
		
	}

	public void run() {
		
		
		PrintWriter writer;
		try {
			writer = new PrintWriter("responses_"+threadId+".txt", "UTF-8");
		
		for (int c = 0; c < nCalls; c++) {
			HttpPut put = new HttpPut(url);
			// add header
			put.setHeader("User-Agent", "ServiceTest");

			String rval = sendData(put);
			
			writer.println(rval);
			writer.flush();;
		}
		writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	String sendData(HttpPut put) {
		String rval = "";
		HttpResponse response;
		try {
			response = client.execute(put);
			rval = EntityUtils.toString(response.getEntity(), "UTF-8");
		
			response.getEntity().getContent().close();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rval;
	}

}
