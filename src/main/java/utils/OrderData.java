package utils;

public class OrderData {
    private final String name;
    private final String lastName;
    private final String address;
    private final String metroStation;
    private final String phone;
    private final String deliveryDate;
    private final String rentalPeriod;
    private final String color;
    private final String comment;

    public OrderData(String name, String lastName, String address, String metroStation,
                     String phone, String deliveryDate, String rentalPeriod, String color, String comment) {
        this.name = name;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.deliveryDate = deliveryDate;
        this.rentalPeriod = rentalPeriod;
        this.color = color;
        this.comment = comment;
    }

    // Геттеры
    public String getName() {
        return name;
    }
    public String getLastName() {
        return lastName;
    }
    public String getAddress() {
        return address;
    }
    public String getMetroStation() {
        return metroStation;
    }
    public String getPhone() {
        return phone;
    }
    public String getDeliveryDate() {
        return deliveryDate;
    }
    public String getRentalPeriod() {
        return rentalPeriod;
    }
    public String getColor() {
        return color;
    }
    public String getComment() {
        return comment;
    }
}
