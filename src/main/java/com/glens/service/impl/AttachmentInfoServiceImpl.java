package com.glens.service.impl;

import com.glens.dao.AttachmentInfoMapper;
import com.glens.model.AttachmentInfo;
import com.glens.service.AttachmentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by yk on 2018/1/21.
 * @author yk
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AttachmentInfoServiceImpl implements AttachmentInfoService {
    @Autowired
    private AttachmentInfoMapper attachmentInfoMapper;
    @Override
    public int insertSelective(AttachmentInfo record) {
        return attachmentInfoMapper.insertSelective(record);
    }

    @Override
    public List<AttachmentInfo> selectLists(AttachmentInfo attachmentInfo) {
        return attachmentInfoMapper.selectLists(attachmentInfo);
    }

    @Override
    public AttachmentInfo selectByPrimaryKey(Integer attachId) {

        return attachmentInfoMapper.selectByPrimaryKey(attachId);
    }
}
