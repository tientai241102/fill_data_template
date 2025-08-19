package com.lap.no21docssample.service;

import com.lap.no21docssample.service.detail.FillTemplateDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FillTemplateSelector {
    @Autowired
    private FillTemplateDemoService fillTemplateDemoService;

    public FillTemplateFactory getTemplateByType(String type) {
        switch (type) {
            case "demo":
                return fillTemplateDemoService;
            // Add more cases for other template types as needed
            default:
                throw new IllegalArgumentException("Unknown template type: " + type);
        }
    }
}
