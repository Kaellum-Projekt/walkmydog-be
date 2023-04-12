package com.kaellum.walkmydog.hazelcast.config;

import java.util.Random;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionConfig;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.IndexConfig;
import com.hazelcast.config.IndexType;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MaxSizePolicy;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.jet.config.JetConfig;
import com.hazelcast.map.IMap;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class HazelcastConfiguration {
	
	public Config hazelCastConfig(){
        return new Config()
        		.setJetConfig(getJetConfig())
                .setInstanceName("hazel-instance-kaellum-" + new Random().nextLong())
                .addMapConfig(new MapConfig()
                        .setName("addresses")
                        .setTimeToLiveSeconds(3000)
                        .setEvictionConfig(new EvictionConfig()
                                .setSize(200)
                                .setMaxSizePolicy(MaxSizePolicy.FREE_HEAP_SIZE)
                                .setEvictionPolicy(EvictionPolicy.LRU)
                        )
                )
                .addMapConfig(new MapConfig()
                        .setName("city_boundaries")
                        .setTimeToLiveSeconds(3000)
                        .setEvictionConfig(new EvictionConfig()
                                .setSize(200)
                                .setMaxSizePolicy(MaxSizePolicy.FREE_HEAP_SIZE)
                                .setEvictionPolicy(EvictionPolicy.LRU)
                        )
                );
    }
    
    
    private JetConfig getJetConfig() {
    	return new JetConfig().setEnabled(true);
    }
    
    
    @Bean("hzInstance")
    public HazelcastInstance hzInstance() {
        HazelcastInstance instance;
            instance = Hazelcast.newHazelcastInstance(hazelCastConfig());
            createIndex(instance);
        return instance;
    }
    
    private void createIndex(HazelcastInstance instance) {
        IMap<Object, Object> iMap = instance.getMap("addresses");

        IndexConfig numberIndex = new IndexConfig(IndexType.HASH, "number");
        numberIndex.setName("numberIndex");
        iMap.addIndex(numberIndex);
        
        IndexConfig streetIndex = new IndexConfig(IndexType.HASH, "street");
        streetIndex.setName("streetIndex");
        iMap.addIndex(streetIndex);
        
        IndexConfig cityIndex = new IndexConfig(IndexType.HASH, "city");
        cityIndex.setName("cityIndex");
        iMap.addIndex(cityIndex);
    }
     
}
