package com.selecthome.base;

public class HouseMessage {
    public static final String TOPIC = "house_index";

    private Long houseId;
    private Type type;

    public HouseMessage() {
    }

    public HouseMessage(Long houseId, Type type) {
        this.houseId = houseId;
        this.type = type;
    }

    public Long getHouseId() {
        return houseId;
    }

    public void setHouseId(Long houseId) {
        this.houseId = houseId;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type {
        INDEX, DELETE
    }
}
