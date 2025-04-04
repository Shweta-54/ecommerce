package com.example.ecommerce;

import java.util.Date;

public class MyOrderItemModel {


    private String productId;
    private String productImage;
    private String productTitle;
    private String orderStatus;


    private String address;
    private String coupenId;
    private String cuttedPrice;
    private Date orderedDate;
    private Date packedDate;
    private Date shippedDate;
    private Date deliverdDte;
    private Date cancelledDate;
    private String discountedPrice;
    private Long freeCoupens;
    private String fullName;
    private String orderId;
    private String paymentMethod;
    private String pincode;
    private String productPrice;
    private Long productQuantity;
    private String userId;
    private String deliverPrice;
    private int rating = 0;
    private boolean cancellationRequested;

    public MyOrderItemModel(String productId, String orderStatus, String address, String coupenId,
                            String cuttedPrice, Date orderedDate, Date packedDate, Date shippedDate, Date deliverdDte,
                            Date cancelledDate, String discountedPrice, Long freeCoupens, String fullName, String orderId,
                            String paymentMethod, String pincode, String productPrice, Long productQuantity, String userId,
                            String productImage,String productTitle,String deliverPrice,boolean cancellationRequested) {
        this.productImage = productImage;
        this.productTitle = productTitle;
        this.productId = productId;
        this.orderStatus = orderStatus;
        this.address = address;
        this.coupenId = coupenId;
        this.cuttedPrice = cuttedPrice;
        this.orderedDate = orderedDate;
        this.packedDate = packedDate;
        this.shippedDate = shippedDate;
        this.deliverdDte = deliverdDte;
        this.cancelledDate = cancelledDate;
        this.discountedPrice = discountedPrice;
        this.freeCoupens = freeCoupens;
        this.fullName = fullName;
        this.orderId = orderId;
        this.paymentMethod = paymentMethod;
        this.pincode = pincode;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.userId = userId;
        this.deliverPrice = deliverPrice;
        this.cancellationRequested = cancellationRequested;

    }

    public boolean isCancellationRequested() {
        return cancellationRequested;
    }

    public void setCancellationRequested(boolean cancellationRequested) {
        this.cancellationRequested = cancellationRequested;
    }

    public String getDeliverPrice() {
        return deliverPrice;
    }

    public void setDeliverPrice(String deliverPrice) {
        this.deliverPrice = deliverPrice;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCoupenId() {
        return coupenId;
    }

    public void setCoupenId(String coupenId) {
        this.coupenId = coupenId;
    }

    public String getCuttedPrice() {
        return cuttedPrice;
    }

    public void setCuttedPrice(String cuttedPrice) {
        this.cuttedPrice = cuttedPrice;
    }

    public Date getOrderedDate() {
        return orderedDate;
    }

    public void setOrderedDate(Date orderedDate) {
        this.orderedDate = orderedDate;
    }

    public Date getPackedDate() {
        return packedDate;
    }

    public void setPackedDate(Date packedDate) {
        this.packedDate = packedDate;
    }

    public Date getShippedDate() {
        return shippedDate;
    }

    public void setShippedDate(Date shippedDate) {
        this.shippedDate = shippedDate;
    }

    public Date getDeliverdDte() {
        return deliverdDte;
    }

    public void setDeliverdDte(Date deliverdDte) {
        this.deliverdDte = deliverdDte;
    }

    public Date getCancelledDate() {
        return cancelledDate;
    }

    public void setCancelledDate(Date cancelledDate) {
        this.cancelledDate = cancelledDate;
    }

    public String getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(String discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public Long getFreeCoupens() {
        return freeCoupens;
    }

    public void setFreeCoupens(Long freeCoupens) {
        this.freeCoupens = freeCoupens;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public Long getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Long productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
