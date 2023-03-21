package com.example.demo.service;

public interface DummyService {

    void generate(int size);

    void batchExport(Long id);

    void exportBySqlSession(Long id);
}
