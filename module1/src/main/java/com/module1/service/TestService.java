package com.module1.service;

import com.module1.dao.ITestDao;
import com.module1.domain.TestDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @Author: zhouj
 * @Date: 2019/7/31 17:48
 */
@Service
public class TestService {
    @Autowired
    private ITestDao dao;

    public void insert(TestDO testDO) {
        dao.insertSelective(testDO);
    }

    public List<TestDO> get() {
        return dao.selectAll();
    }
}
