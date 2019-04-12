package com.trialweek;

class CoordinatesDimension {
    private int startCoordinate;
    private int endCoordinate;

    CoordinatesDimension(int startCoordinate, int endCoordinate) {
        this.startCoordinate = startCoordinate;
        this.endCoordinate = endCoordinate;
    }

    public int getStartCoordinate() {
        return startCoordinate;
    }

    public void setStartCoordinate(int startCoordinate) {
        this.startCoordinate = startCoordinate;
    }

    public int getEndCoordinate() {
        return endCoordinate;
    }

    public void setEndCoordinate(int endCoordinate) {
        this.endCoordinate = endCoordinate;
    }
}
