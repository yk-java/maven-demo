package com.glens.service;

import com.glens.model.AttachmentInfo;

import java.util.List;

/**
 * Created by yk on 2018/1/21.
 * @author yk
 */
public interface AttachmentInfoService {
    /**
     * 插入附件信息
     * @param record bean
     * @return int
     */
    int insertSelective(AttachmentInfo record);

    /**
     * 查询所有附件可按条件查询
     * @param attachmentInfo 附件对象
     * @return list
     */
    List<AttachmentInfo> selectLists(AttachmentInfo attachmentInfo);

    /**
     * 根据主键查询
     * @param attachId id
     * @return AttachmentInfo
     */
    AttachmentInfo selectByPrimaryKey(Integer attachId);
}
