package com.mongodb.tradecraft.service_test;



import java.util.logging.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;



public class ServiceTest {
	static final String version = "0.0.1";
	static Logger logger;
    
    
	public static void main(String[] args) {
		LogManager.getLogManager().reset();

		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();
		
		logger = LoggerFactory.getLogger(ServiceTest.class);
		logger.info(version);

		int nThreads = 4;
		int nCalls = 1000;
		String url = "http://localhost:8000/sequence/customer";
	
		if(args.length > 0) {
			url = new String(args[0]);
		} else {
			System.out.println("Usage: java -jar Servicetest,jar <url> [threads[ [times to call]");
			return ;
		}
		
		if(args.length > 1) {
			 nThreads = Integer.parseInt(args[1]);
		}
		
		if(args.length > 2) {
			 nCalls = Integer.parseInt(args[2]);
		}
		
		ExecutorService simexec = Executors.newFixedThreadPool(nThreads);
		long start = System.currentTimeMillis();
		for (int i = 0; i < nThreads; i++) {
			simexec.execute(new ServiceWorker(i,url,nCalls));
		}

		simexec.shutdown();

		try {
			simexec.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
			long end = System.currentTimeMillis();
			System.out.println("Time Taken " + (end-start) + " ms");
			simexec.shutdown();
		} catch (InterruptedException e) {
			logger.error(e.getMessage());

		}
	}

}
