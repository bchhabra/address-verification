package com.payworks.response.impl;

import com.payworks.response.RestResponse;

public class AddressResponse extends RestResponse {

    private String ErrorCode;
    private String ErrorMessage;
    private String AddressLine1;
    private String AddressLine2;
    private String Number;
    private String PreDir;
    private String Street;
    private String Suffix;
    private String PostDir;
    private String City;
    private String State;
    private String Zip;
    private String Zip4;
    private String Sec;
    private String SecNumber;


    public AddressResponse(String responseBody, int statusCode) {
        super(responseBody, statusCode);
    }

    public String getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(String errorCode) {
        ErrorCode = errorCode;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
    }

    public String getAddressLine1() {
        return AddressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        AddressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return AddressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        AddressLine2 = addressLine2;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getPreDir() {
        return PreDir;
    }

    public void setPreDir(String preDir) {
        PreDir = preDir;
    }

    public String getStreet() {
        return Street;
    }

    public void setStreet(String street) {
        Street = street;
    }

    public String getSuffix() {
        return Suffix;
    }

    public void setSuffix(String suffix) {
        Suffix = suffix;
    }

    public String getPostDir() {
        return PostDir;
    }

    public void setPostDir(String postDir) {
        PostDir = postDir;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getZip() {
        return Zip;
    }

    public void setZip(String zip) {
        Zip = zip;
    }

    public String getZip4() {
        return Zip4;
    }

    public void setZip4(String zip4) {
        Zip4 = zip4;
    }

    public String getSec() {
        return Sec;
    }

    public void setSec(String sec) {
        Sec = sec;
    }

    public String getSecNumber() {
        return SecNumber;
    }

    public void setSecNumber(String secNumber) {
        SecNumber = secNumber;
    }
}
