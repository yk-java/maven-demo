package com.glens.service.impl;

import com.glens.dao.GPlanCbxmMapper;
import com.glens.model.GPlanCbxmWithBLOBs;
import com.glens.service.GPlanCbxmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Title:
 * Description:
 *
 * @author yk.
 * @version Version 1.0
 * Date: 2018/2/26 14:22
 */
@Service
@Transactional
public class GPlanCbxmServiceImpl implements GPlanCbxmService {

    @Resource
    private GPlanCbxmMapper gPlanCbxmMapper;
    @Override
    public List<GPlanCbxmWithBLOBs> selectByParameter(GPlanCbxmWithBLOBs gPlanCbxmWithBLOBs) {
        return gPlanCbxmMapper.selectByParameter(gPlanCbxmWithBLOBs);
    }
}
