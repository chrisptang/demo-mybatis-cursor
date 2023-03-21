package com.example.demo.controller;

import com.example.demo.service.DummyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dummy")
public class DummyController {

    @Autowired
    private DummyService dummyService;

    @GetMapping("/generate")
    public ResponseEntity<String> generate(
      @RequestParam(defaultValue = "10000", required = false) Integer size) {
        dummyService.generate(size);
        return ResponseEntity.ok("done");
    }

    @GetMapping("/export")
    public ResponseEntity<String> export(
      @RequestParam(required = false, defaultValue = "1000") Long id) {
        dummyService.batchExport(id);
        return ResponseEntity.ok("done");
    }

    @GetMapping("/export/session")
    public ResponseEntity<String> exportBySession(
      @RequestParam(required = false, defaultValue = "1000") Long id) {
        dummyService.exportBySqlSession(id);
        return ResponseEntity.ok("done");
    }
}
