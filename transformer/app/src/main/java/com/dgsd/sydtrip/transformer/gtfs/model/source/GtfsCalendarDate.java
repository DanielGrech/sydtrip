package com.dgsd.sydtrip.transformer.gtfs.model.source;

public class GtfsCalendarDate extends BaseGtfsModel {

    private String serviceId;

    private String date;

    private String exceptionType;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(String exceptionType) {
        this.exceptionType = exceptionType;
    }

    @Override
    public String toString() {
        return "CalendarDate{" +
                "serviceId='" + serviceId + '\'' +
                ", date='" + date + '\'' +
                ", exceptionType='" + exceptionType + '\'' +
                '}';
    }
}
