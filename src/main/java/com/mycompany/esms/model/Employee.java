package com.mycompany.esms.model;

public class Employee extends Member {

    private String position;
    private boolean active;

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
