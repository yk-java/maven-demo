package com.glens.model;

import com.glens.excel.ExcelColumn;

import java.math.BigDecimal;

public class DistributionNetworkReport {

    @ExcelColumn(value="储备要求(万元)", col=2)
    private BigDecimal reserveRequirements;

    @ExcelColumn(value="资本(万元)", col=3)
    private BigDecimal capital;

    @ExcelColumn(value="成本(万元)", col=4)
    private BigDecimal cost;

    @ExcelColumn(value="合计(万元)", col=5)
    private BigDecimal total;

    @ExcelColumn(value="储备数量(个)", col=6)
    private Integer gridReserveQuantity;

    @ExcelColumn(value="储备金额(万元)", col=7)
    private BigDecimal gridReserveAmount;

    @ExcelColumn(value="储备数量(个)", col=8)
    private Integer nogridReserveQuantity;

    @ExcelColumn(value="储备金额(万元)", col=9)
    private BigDecimal nogridReserveAmount;

    @ExcelColumn(value="上报占年度计划比例(%)", col=10)
    private BigDecimal reportProportion;

    @ExcelColumn(value="评审金额(万元)", col=11)
    private BigDecimal evaluationAmount;

    @ExcelColumn(value="评审金额占提交金额比(%)", col=12)
    private BigDecimal reviewRatio;

    @ExcelColumn(value="评审通过金额 (万元)", col=13)
    private BigDecimal throughAmount;

    @ExcelColumn(value="通过率(%)", col=14)
    private BigDecimal passRate;

    @ExcelColumn(value="储备占年度计划比例(%)", col=15)
    private BigDecimal reserveRatio;

    public BigDecimal getReserveRequirements() {
        return reserveRequirements;
    }

    public void setReserveRequirements(BigDecimal reserveRequirements) {
        this.reserveRequirements = reserveRequirements;
    }

    public BigDecimal getCapital() {
        return capital;
    }

    public void setCapital(BigDecimal capital) {
        this.capital = capital;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Integer getGridReserveQuantity() {
        return gridReserveQuantity;
    }

    public void setGridReserveQuantity(Integer gridReserveQuantity) {
        this.gridReserveQuantity = gridReserveQuantity;
    }

    public BigDecimal getGridReserveAmount() {
        return gridReserveAmount;
    }

    public void setGridReserveAmount(BigDecimal gridReserveAmount) {
        this.gridReserveAmount = gridReserveAmount;
    }

    public Integer getNogridReserveQuantity() {
        return nogridReserveQuantity;
    }

    public void setNogridReserveQuantity(Integer nogridReserveQuantity) {
        this.nogridReserveQuantity = nogridReserveQuantity;
    }

    public BigDecimal getNogridReserveAmount() {
        return nogridReserveAmount;
    }

    public void setNogridReserveAmount(BigDecimal nogridReserveAmount) {
        this.nogridReserveAmount = nogridReserveAmount;
    }

    public BigDecimal getReportProportion() {
        return reportProportion;
    }

    public void setReportProportion(BigDecimal reportProportion) {
        this.reportProportion = reportProportion;
    }

    public BigDecimal getEvaluationAmount() {
        return evaluationAmount;
    }

    public void setEvaluationAmount(BigDecimal evaluationAmount) {
        this.evaluationAmount = evaluationAmount;
    }

    public BigDecimal getReviewRatio() {
        return reviewRatio;
    }

    public void setReviewRatio(BigDecimal reviewRatio) {
        this.reviewRatio = reviewRatio;
    }

    public BigDecimal getThroughAmount() {
        return throughAmount;
    }

    public void setThroughAmount(BigDecimal throughAmount) {
        this.throughAmount = throughAmount;
    }

    public BigDecimal getPassRate() {
        return passRate;
    }

    public void setPassRate(BigDecimal passRate) {
        this.passRate = passRate;
    }

    public BigDecimal getReserveRatio() {
        return reserveRatio;
    }

    public void setReserveRatio(BigDecimal reserveRatio) {
        this.reserveRatio = reserveRatio;
    }
}