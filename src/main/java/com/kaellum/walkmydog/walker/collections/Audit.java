package com.kaellum.walkmydog.walker.collections;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.domain.Persistable;


import lombok.Data;

/**
 * @author Raphael Cremasco
 *
 */
@Data
public abstract class Audit implements Persistable<String> {

	@CreatedBy    
	private String createdBy;

	@CreatedDate
    private LocalDateTime createdDate;
    
    @LastModifiedBy
    private String lastModifiedBy;
    
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;
    
    @Version
    public Integer version;
    
}
