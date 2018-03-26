package com.glens.service;

import com.glens.model.GPlanCbxmWithBLOBs;

import java.util.List;

/**
 * Title:
 * Description:
 *
 * @author yk.
 * @version Version 1.0
 * Date: 2018/2/26 14:21
 */
public interface GPlanCbxmService {

    List<GPlanCbxmWithBLOBs> selectByParameter(GPlanCbxmWithBLOBs gPlanCbxmWithBLOBs);

}
