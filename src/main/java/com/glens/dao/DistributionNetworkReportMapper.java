package com.glens.dao;

import com.glens.model.DistributionNetworkReport;

public interface DistributionNetworkReportMapper {
    int insert(DistributionNetworkReport record);

    int insertSelective(DistributionNetworkReport record);
}