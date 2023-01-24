package com.ovot.fiveStarapp_test1;

public class IncentiveModel
{
    String date;
    String ServiceId ;
    String Servicetype ;
    String CustomerName ;
    String CustomerRating;
    String starIncentive;
    String ApproveStatus ;
    String IncentiveAmt  ;
    String UniformApproval ;
    String PaidStatus ;
    String RejectionReason ;


    public IncentiveModel(String date, String serviceId, String servicetype, String customerName, String customerRating, String starIncentive, String approveStatus, String incentiveAmt, String uniformApproval, String paidStatus, String rejectionReason) {
        this.date = date;
        ServiceId = serviceId;
        Servicetype = servicetype;
        CustomerName = customerName;
        CustomerRating = customerRating;
        this.starIncentive = starIncentive;
        ApproveStatus = approveStatus;
        IncentiveAmt = incentiveAmt;
        UniformApproval = uniformApproval;
        PaidStatus = paidStatus;
        RejectionReason = rejectionReason;
    }

    public String getDate() {
        return date;
    }

    public String getServiceId() {
        return ServiceId;
    }

    public String getServicetype() {
        return Servicetype;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public String getCustomerRating() {
        return CustomerRating;
    }

    public String getStarIncentive() {
        return starIncentive;
    }

    public String getApproveStatus() {
        return ApproveStatus;
    }

    public String getIncentiveAmt() {
        return IncentiveAmt;
    }

    public String getUniformApproval() {
        return UniformApproval;
    }

    public String getPaidStatus() {
        return PaidStatus;
    }

    public String getRejectionReason() {
        return RejectionReason;
    }
}
