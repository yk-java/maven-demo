package com.glens.dao;

import com.glens.model.AttachmentInfo;

import java.util.List;

public interface AttachmentInfoMapper {
    int deleteByPrimaryKey(Integer attachId);

    int insert(AttachmentInfo record);

    int insertSelective(AttachmentInfo record);

    AttachmentInfo selectByPrimaryKey(Integer attachId);

    int updateByPrimaryKeySelective(AttachmentInfo record);

    int updateByPrimaryKey(AttachmentInfo record);

    List<AttachmentInfo> selectLists(AttachmentInfo attachmentInfo);
}