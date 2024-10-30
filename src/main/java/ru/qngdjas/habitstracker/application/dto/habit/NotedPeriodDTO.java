package ru.qngdjas.habitstracker.application.dto.habit;

public class NotedPeriodDTO {

    private String beginDate;
    private String endDate;

    public NotedPeriodDTO(String beginDate, String endDate) {
        setBeginDate(beginDate);
        setEndDate(endDate);
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
