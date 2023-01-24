package com.ovot.fiveStarapp_test1;

public class MyJobModel {

    Long  serviceId;
    int status_id, freeze, registerId, customerId, pinCode, providerId, technicianId;
    String statusCode, stage, complaintype_code,customer_name, phone1, phone2, address1,address2, city
    ,state,product_name, producttype_code, model_code, company_name, provider_phone1, circle_head, call_date,
            description, appointment_date, rescheduled_date, complete_date;


    public MyJobModel(Long serviceId, int status_id, int freeze, int registerId, int customerId, int pinCode, int providerId, int technicianId, String statusCode, String stage, String complaintype_code, String customer_name, String phone1, String phone2, String address1, String address2, String city, String state, String product_name, String producttype_code, String model_code, String company_name, String provider_phone1, String circle_head, String call_date, String description, String appointment_date, String rescheduled_date, String complete_date) {
        this.serviceId = serviceId;
        this.status_id = status_id;
        this.freeze = freeze;
        this.registerId = registerId;
        this.customerId = customerId;
        this.pinCode = pinCode;
        this.providerId = providerId;
        this.technicianId = technicianId;
        this.statusCode = statusCode;
        this.stage = stage;
        this.complaintype_code = complaintype_code;
        this.customer_name = customer_name;
        this.phone1 = phone1;
        this.phone2 = phone2;
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.state = state;
        this.product_name = product_name;
        this.producttype_code = producttype_code;
        this.model_code = model_code;
        this.company_name = company_name;
        this.provider_phone1 = provider_phone1;
        this.circle_head = circle_head;
        this.call_date = call_date;
        this.description = description;
        this.appointment_date = appointment_date;
        this.rescheduled_date = rescheduled_date;
        this.complete_date = complete_date;
    }



    public Long getServiceId() {
        return serviceId;
    }

    public int getStatus_id() {
        return status_id;
    }

    public int getFreeze() {
        return freeze;
    }

    public int getRegisterId() {
        return registerId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getPinCode() {
        return pinCode;
    }

    public int getProviderId() {
        return providerId;
    }

    public int getTechnicianId() {
        return technicianId;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getStage() {
        return stage;
    }

    public String getComplaintype_code() {
        return complaintype_code;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public String getPhone1() {
        return phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public String getAddress1() {
        return address1;
    }

    public String getAddress2() {
        return address2;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getProductType_code() {
        return producttype_code;
    }

    public String getModel_code() {
        return model_code;
    }

    public String getCompany_name() {
        return company_name;
    }

    public String getProvider_phone1() {
        return provider_phone1;
    }

    public String getCircle_head() {
        return circle_head;
    }

    public String getCall_date() {
        return call_date;
    }

    public String getDescription() {
        return description;
    }

    public String getAppointment_date() {
        return appointment_date;
    }

    public String getRescheduled_date() {
        return rescheduled_date;
    }

    public String getComplete_date() {
        return complete_date;
    }




}
