package com.systemdesign.lld.fooddelivery.springboot.domain;

import java.util.Objects;

public class Restaurant {
    private final String id;
    private String name;
    private Location location;
    private final Menu menu;
    private boolean active;

    public Restaurant(String id, String name, Location location) {
        this.id = Objects.requireNonNull(id, "Restaurant ID cannot be null");
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.location = Objects.requireNonNull(location, "Location cannot be null");
        this.menu = new Menu();
        this.active = true;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name);
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = Objects.requireNonNull(location);
    }

    public Menu getMenu() {
        return menu;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
