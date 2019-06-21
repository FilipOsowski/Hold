package com.example.rc211.prototype2;

public class Force {
    private float x;
    private float y;
    private double expirationTime;

    public Force(float x, float y, float expirationTime) {
        this.x = x;
        this.y = y;
        this.expirationTime = expirationTime;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public double getExpirationTime() {
        return expirationTime;
    }
}
