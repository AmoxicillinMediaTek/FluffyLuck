package me.exeos.clickgui.setting;

import me.exeos.module.Module;

import java.util.ArrayList;

public final class Setting {
    private final String name;
    private final Module parent;
    private final String mode;
    private String sval;
    private final ArrayList<String> options;
    private boolean bval;
    private double dval;
    private final double min;
    private final double max;
    private final boolean onlyint;

    public Setting(String name, Module parent, String sval, ArrayList<String> options) {
        this.name = name;
        this.parent = parent;
        this.sval = sval;
        this.options = options;
        this.mode = "Combo";
        this.min = 0;
        this.max = 0;
        this.onlyint = false;
    }

    public Setting(String name, Module parent, boolean bval) {
        this.name = name;
        this.parent = parent;
        this.bval = bval;
        this.mode = "Check";
        this.options = new ArrayList<>();
        this.min = 0;
        this.max = 0;
        this.onlyint = false;
    }

    public Setting(String name, Module parent, double dval, double min, double max, boolean onlyint) {
        this.name = name;
        this.parent = parent;
        this.dval = dval;
        this.min = min;
        this.max = max;
        this.onlyint = onlyint;
        this.mode = "Slider";
        this.options = new ArrayList<>();
    }

    public String getName() { return name; }
    public Module getParentMod() { return parent; }
    public String getValString() { return sval; }
    public void setValString(String value) { sval = value; }
    public ArrayList<String> getOptions() { return options; }
    public boolean getValBoolean() { return bval; }
    public void setValBoolean(boolean value) { bval = value; }
    public double getValDouble() { return onlyint ? Math.rint(dval) : dval; }
    public void setValDouble(double value) { dval = Math.max(min, Math.min(max, onlyint ? Math.rint(value) : value)); }
    public double getMin() { return min; }
    public double getMax() { return max; }
    public boolean isCombo() { return mode.equals("Combo"); }
    public boolean isCheck() { return mode.equals("Check"); }
    public boolean isSlider() { return mode.equals("Slider"); }
    public boolean onlyInt() { return onlyint; }
}