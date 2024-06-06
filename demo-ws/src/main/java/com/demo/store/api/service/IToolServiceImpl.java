package com.demo.store.api.service;

import com.demo.store.api.entity.Tool;
import com.demo.store.api.repos.ToolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IToolServiceImpl implements IToolService {

    @Autowired
    private ToolRepository toolRepository;

    @Override
    public Tool getToolByToolCode(String toolCode) {

        return toolRepository.findByToolCode(toolCode);
    }

}
