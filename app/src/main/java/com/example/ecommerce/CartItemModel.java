package com.example.ecommerce;

import java.util.ArrayList;
import java.util.List;

public class CartItemModel {

    public static final int CART_ITEM = 0;
    public static final int TOTAL_AMOUNT = 1;

    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

        /////// cart item
        private String productID;
        private String productImage;
        private String productTitle;
        private Long freeCoupens;
        private String productPrice;
        private String cuttedPrice;
        private Long productQuentity;
        private Long maxQuentity;
        private Long stockQuentity;
        private Long offersApplied;
        private Long coupensApplied;
        private boolean inStock;
        private List<String> qtyIDs;
        private boolean qtyError;

    public CartItemModel(int type, String productID,String productImage, String productTitle, Long freeCoupens, String productPrice, String cuttedPrice, Long productQuentity, Long offersApplied, Long coupensApplied,boolean inStock,Long maxQuentity,Long stockQuentity) {
        this.type = type;
        this.productID = productID;
        this.productImage = productImage;
        this.productTitle = productTitle;
        this.freeCoupens = freeCoupens;
        this.productPrice = productPrice;
        this.cuttedPrice = cuttedPrice;
        this.productQuentity = productQuentity;
        this.offersApplied = offersApplied;
        this.coupensApplied = coupensApplied;
        this.inStock = inStock;
        this.maxQuentity = maxQuentity;
        this.stockQuentity = stockQuentity;
        qtyIDs = new ArrayList<>();
        qtyError = false;
    }

    public boolean isQtyError() {
        return qtyError;
    }

    public void setQtyError(boolean qtyError) {
        this.qtyError = qtyError;
    }

    public Long getStockQuentity() {
        return stockQuentity;
    }

    public void setStockQuentity(Long stockQuentity) {
        this.stockQuentity = stockQuentity;
    }

    public List<String> getQtyIDs() {
        return qtyIDs;
    }

    public void setQtyIDs(List<String> qtyIDs) {
        this.qtyIDs = qtyIDs;
    }

    public Long getMaxQuentity() {
        return maxQuentity;
    }

    public void setMaxQuentity(Long maxQuentity) {
        this.maxQuentity = maxQuentity;
    }

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
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

    public Long getFreeCoupens() {
        return freeCoupens;
    }

    public void setFreeCoupens(Long freeCoupens) {
        this.freeCoupens = freeCoupens;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getCuttedPrice() {
        return cuttedPrice;
    }

    public void setCuttedPrice(String cuttedPrice) {
        this.cuttedPrice = cuttedPrice;
    }

    public Long getProductQuentity() {
        return productQuentity;
    }

    public void setProductQuentity(Long productQuentity) {
        this.productQuentity = productQuentity;
    }

    public Long getOffersApplied() {
        return offersApplied;
    }

    public void setOffersApplied(Long offersApplied) {
        this.offersApplied = offersApplied;
    }

    public Long getCoupensApplied() {
        return coupensApplied;
    }

    public void setCoupensApplied(Long coupensApplied) {
        this.coupensApplied = coupensApplied;
    }
    /////// cart item

    //////cart total

    public CartItemModel(int type) {
        this.type = type;
    }

    //////cart total
}
