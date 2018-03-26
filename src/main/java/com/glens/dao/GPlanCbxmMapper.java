package com.glens.dao;

import com.glens.model.GPlanCbxm;
import com.glens.model.GPlanCbxmWithBLOBs;

import java.util.List;

public interface GPlanCbxmMapper {
    int deleteByPrimaryKey(String prjid);

    int insert(GPlanCbxmWithBLOBs record);

    int insertSelective(GPlanCbxmWithBLOBs record);

    GPlanCbxmWithBLOBs selectByPrimaryKey(String prjid);

    int updateByPrimaryKeySelective(GPlanCbxmWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(GPlanCbxmWithBLOBs record);

    int updateByPrimaryKey(GPlanCbxm record);

    List<GPlanCbxmWithBLOBs> selectByParameter(GPlanCbxmWithBLOBs gPlanCbxmWithBLOBs);
}