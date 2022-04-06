package com.fintech.transactions.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Schema for a typical user transaction.
 * Fields country, city, device and browser are inferred fields
 */
public class Transaction {

    @NotBlank
    private String userId;

    @NotBlank
    private String ipAddress;

    private String city;

    private String country;

    private String originCountry;

    private String device;

    private String browser;

    //TODO to handle floating point numbers , use BigDecimal
    @NotNull
    private Integer amount;

    @NotBlank
    private String type;

    private Integer dailyLimit;

    private Integer spentBeforeThis;

    private String userAgent;

    public Transaction(String user_id, String ip_address, String userAgent, Integer amount, String type) {
        this.userId = user_id;
        this.ipAddress = ip_address;
        this.userAgent = userAgent;
        this.amount = amount;
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getDailyLimit() {
        return dailyLimit;
    }

    public void setDailyLimit(Integer dailyLimit) {
        this.dailyLimit = dailyLimit;
    }

    public Integer getSpentBeforeThis() {
        return spentBeforeThis;
    }

    public void setSpentBeforeThis(Integer spentBeforeThis) {
        this.spentBeforeThis = spentBeforeThis;
    }

    public String getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "userId='" + userId + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", originCountry='" + originCountry + '\'' +
                ", device='" + device + '\'' +
                ", browser='" + browser + '\'' +
                ", amount=" + amount +
                ", type='" + type + '\'' +
                ", dailyLimit=" + dailyLimit +
                ", spentBeforeThis=" + spentBeforeThis +
                ", userAgent='" + userAgent + '\'' +
                '}';
    }

    public boolean isEmpty()
    {
        if(isNullOrEmpty(this.userId) || isNullOrEmpty(this.ipAddress) || isNullOrEmpty(this.type) || this.amount == null)
            return true;
        return false;
    }

    private boolean isNullOrEmpty(String s)
    {
        return (s == null || s.isEmpty());
    }
}
