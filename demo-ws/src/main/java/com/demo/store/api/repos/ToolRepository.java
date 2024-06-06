package com.demo.store.api.repos;

import com.demo.store.api.entity.Tool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToolRepository extends JpaRepository<Tool, String> {


     Tool findByToolCode(String toolCode);


}
