package bello.andrea.imieiposti;

import android.graphics.Color;

public enum Category {

    FOOD ("Food", Color.parseColor("#EF5350")),
    PEOPLE ("Peolpe", Color.parseColor("#5C6BC0")),
    ENTERTAINMENT ("Entertainment", Color.parseColor("#FFEE58")),
    OTHER ("Other", Color.parseColor("#66BB6A"));

    private final String name;
    private final int color;

    Category(String name, int color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }
    public int getColor() {
        return color;
    }

    public String toString(){
        return name;
    }

}
