package com.zyx.vhr.service;

import com.zyx.vhr.mapper.NationMapper;
import com.zyx.vhr.model.Nation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Zhang Yuxiao
 * @Date 2021/5/4 19:01
 */
@Service
public class NationService {

    @Autowired
    NationMapper nationMapper;
    public List<Nation> getAllNations() {
        return nationMapper.getAllNations();
    }
}
