// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.disertatie.mylibrary.domain;

import com.disertatie.mylibrary.domain.Author;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

privileged aspect Author_Roo_Jpa_Entity {
    
    declare @type: Author: @Entity;
    
    declare @type: Author: @Table(name = "author");
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer Author.id;
    
    public Integer Author.getId() {
        return this.id;
    }
    
    public void Author.setId(Integer id) {
        this.id = id;
    }
    
}