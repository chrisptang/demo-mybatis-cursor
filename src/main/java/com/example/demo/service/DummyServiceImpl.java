package com.example.demo.service;

import com.example.demo.dao.DummyMapper;
import com.example.demo.dto.DummyDTO;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class DummyServiceImpl implements DummyService {

    @Autowired
    private DummyMapper dummyMapper;

    private static final Random RANDOM = new Random();

    private final static char[] CHARS = "1qaz2wsx3edc4rfv5tgb6yhn7ujm8ik,9ol.0p;/QWERTYUIOP{ASDFGHJKL:ZXCVBNM<>?".toCharArray();

    private static String randomData(int size) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int idx = 0; idx < size; idx++) {
            stringBuilder.append(CHARS[RANDOM.nextInt(CHARS.length)]);
        }
        return stringBuilder.toString();
    }

    @Override
    public void generate(int size) {
        LinkedList<DummyDTO> buffer = new LinkedList<>();
        for (int idx = 0; idx < size; idx++) {
            DummyDTO dummyDTO = new DummyDTO();
            dummyDTO.setData(randomData(1024));
            buffer.add(dummyDTO);
            if (buffer.size() >= 1000) {
                dummyMapper.insertList(buffer);
                buffer = new LinkedList<>();
            }
        }
        if (buffer.size() >= 1) {
            dummyMapper.insertList(buffer);
        }
    }

    @Override
    @Transactional
    public void batchExport(Long id) {
        try (Cursor<DummyDTO> dummyDTOCursor = dummyMapper.exportList(id)) {
            final LinkedList<DummyDTO> buffer = new LinkedList<>();
            dummyDTOCursor.forEach(data -> {
                buffer.add(data);
                if (buffer.size() >= 1000) {
                    log.warn("current id:{}", buffer.getLast().getId());
                    buffer.clear();
                }
            });
            if (buffer.size() >= 1) {
                log.warn("current id:{}", buffer.getLast().getId());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.warn("DONE");
    }

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Override
    public void exportBySqlSession(Long id) {
        AtomicInteger counter = new AtomicInteger(0);
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            Iterator<DummyDTO> iterator = sqlSession.getMapper(DummyMapper.class)
              .exportList(id)
              .iterator();
            while (iterator.hasNext()) {
                DummyDTO dummyDTO = iterator.next();
                if (counter.incrementAndGet() % 1000 == 0) {
                    log.info("current id:{}", dummyDTO.getId());
                }
            }
            log.info("final index:{}", counter.get());
        }
    }
}
