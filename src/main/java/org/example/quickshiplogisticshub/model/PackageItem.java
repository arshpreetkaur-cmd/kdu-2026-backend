package org.example.quickshiplogisticshub.model;

public class PackageItem {
    private String id;
    private String destination;
    private Double weight;
    private String status;
    private String deliveryType;

    public PackageItem() {}

    public PackageItem(String id, String destination, Double weight, String status, String deliveryType) {
        this.id = id;
        this.destination = destination;
        this.weight = weight;
        this.status = status;
        this.deliveryType = deliveryType;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDeliveryType() { return deliveryType; }
    public void setDeliveryType(String deliveryType) { this.deliveryType = deliveryType; }

}
