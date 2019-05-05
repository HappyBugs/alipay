package com.likuncheng;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.codingapi.txlcn.tm.config.EnableTransactionManagerServer;


@SpringBootApplication
@EnableTransactionManagerServer
public class TxLcnManagerApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(TxLcnManagerApplication.class, args);
	}

}
