package com.mongodb.tradecraft.sequence_server;

import static spark.Spark.after;
import static spark.Spark.put;

import java.util.logging.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

public class SequenceService {
	static final String version = "0.0.1";
	static Logger logger;

	public static void main(String[] args) {
		LogManager.getLogManager().reset();

		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();

		logger = LoggerFactory.getLogger(SequenceService.class);
		logger.info(version);

			SequenceServer ws = new SequenceServer();

			put("/sequence/:name", (req, res) -> ws.sequence(req, res));
			after((req, res) -> {
				res.type("application/json");
			});
			return;
	}

}
