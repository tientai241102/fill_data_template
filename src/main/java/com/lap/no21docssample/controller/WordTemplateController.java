package com.lap.no21docssample.controller;

import com.lap.no21docssample.service.FillTemplateFactory;
import com.lap.no21docssample.service.FillTemplateSelector;
import com.lap.no21docssample.service.detail.FillTemplateDemoService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
class WordTemplateController {

    @Autowired
    private FillTemplateSelector fillTemplateSelector;

    @GetMapping("/")
    public String home() {
        return "index";
    }

//    @PostMapping("/upload")
//    public void uploadAndFill(@RequestParam("url") String url, HttpServletResponse response) throws IOException {
//        FillTemplateFactory fillTemplateService = fillTemplateSelector.getTemplateByType("demo");
//
//        byte[] filledDoc = fillTemplateService.fillDocxTemplate(url);
//        response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
//        response.setHeader("Content-Disposition", "attachment; filename=filled_template.docx");
//        response.getOutputStream().write(filledDoc);
//    }

    @PostMapping("/upload")
    public void uploadAndFill(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws IOException {
        FillTemplateFactory fillTemplateService = fillTemplateSelector.getTemplateByType("demo");

        byte[] filledDoc = fillTemplateService.fillDocxTemplateFile(file);
        response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        response.setHeader("Content-Disposition", "attachment; filename=filled_template.docx");
        response.getOutputStream().write(filledDoc);
    }
}