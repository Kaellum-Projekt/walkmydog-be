package com.kaellum.walkmydog.config;


import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

//@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {
 
	@Value("${spring.data.mongodb.uri}")
	String databaseUri;
	@Value("${spring.data.mongodb.database}")
	String database;
    
	
	@Override
    protected String getDatabaseName() {
        return database;
    }
 
    @Override
    public MongoClient mongoClient() {
        ConnectionString connectionString = new ConnectionString(databaseUri + "/" + database);
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .build();
        
        return MongoClients.create(mongoClientSettings);
    }
 
    @Override
    public Collection<String> getMappingBasePackages() {
        return Collections.singleton("com.kaellum.walkmydog");
    }
}
