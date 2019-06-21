package com.example.rc211.prototype2;

import java.util.ArrayList;

public class CompositeForce {
    private ArrayList<Force> forces = new ArrayList<>();
    private int currentTime = 0;

    public void addForce(Force force) {
        forces.add(force);
    }

    public float getX() {
        float xForce = 0;
        for (Force force : forces) {
            xForce += force.getX();
        }

        return xForce;
    }

    public float getY() {
        float yForce = 0;
        for (Force force : forces) {
            yForce += force.getY();
        }

        return yForce;
    }

    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
        removeExpiredForces();
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public void removeExpiredForces() {
        int index = 0;
        while (index < forces.size()) {
            if (forces.get(index).getExpirationTime() <= currentTime) {
                forces.remove(index);
            }
            else {
                index += 1;
            }
        }
    }
}
