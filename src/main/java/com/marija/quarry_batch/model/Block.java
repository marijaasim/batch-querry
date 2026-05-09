package com.marija.quarry_batch.model;

import java.util.Objects;

public class Block {

    /*

    qualityClass:
    A - no cracks|minimal cracks|uniform color
    B - visible veins|small cracks|not perfect
    C - many cracks|bad color|deformations

    category:
    1 - volume > 6.0 m³
    2 - volume 3.0 - 6.0 m³
    3 - volume < 3.0 m³

    price:
    1A - 200 e/t
    1B - 160 e/t
    1C - 120 e/t
    2A - 180 e/t
    2B - 140 e/t
    2C - 100 e/t
    3A - 160 e/t
    3B - 120 e/t
    3C - 80 e/t

    */

    private Long id;
    private double length;
    private double width;
    private double height;
    private double mass;
    private String qualityClass;
    private String category;

    public Block() {
    }

    public Block(Long id, double length, double width, double height, double mass, String qualityClass, String category) {
        this.id = id;
        this.length = length;
        this.width = width;
        this.height = height;
        this.mass = mass;
        this.qualityClass = qualityClass;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public double getLength() {
        return length;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getMass() {
        return mass;
    }

    public String getQualityClass() {
        return qualityClass;
    }

    public String getCategory() {
        return category;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public void setQualityClass(String qualityClass) {
        this.qualityClass = qualityClass;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Block block = (Block) o;
        return Objects.equals(id, block.id);
    }

    @Override
    public String toString() {
        return "Block{" +
                "id=" + id +
                '}';
    }
}
