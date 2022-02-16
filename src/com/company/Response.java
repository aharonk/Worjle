package com.company;

import java.awt.*;

public enum Response {
    WRONG, LETTER, PLACE;

    public static final Color GRAY = new Color(120, 124, 126),
            YELLOW = new Color(201, 180, 88),
            GREEN = new Color(106, 170, 100);

    public Color getColor() {
        return switch (this) {
            case WRONG -> GRAY; //Color.GRAY
            case LETTER -> YELLOW; // Color.YELLOW
            case PLACE -> GREEN; // Color.GREEN
        };
    }
}
