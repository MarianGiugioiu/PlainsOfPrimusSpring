package com.awbd.plainsofprimus.DTO;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Options {
    private String name;
    private List<String> orderList;
    private String selectedOrder;

    public Options() {
        this.name = "";
        selectedOrder = "default";
        this.orderList = new ArrayList<>(List.of("default", "ASC", "DESC"));
    }

    public Options(String name, String selectedOrder) {
        this.name = name;
        this.selectedOrder = selectedOrder;
        this.orderList = new ArrayList<>(List.of("default", "ASC", "DESC"));
    }
}
