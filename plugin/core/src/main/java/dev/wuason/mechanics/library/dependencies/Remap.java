package dev.wuason.mechanics.library.dependencies;

public class Remap {
    private final String old;
    private final String new_;

    private Remap(String old, String new_) {
        this.old = old.replace(':', '/');
        this.new_ = new_.replace(':', '/');
    }

    public String getOld() {
        return old;
    }

    public String getNew() {
        return new_;
    }

    public static Remap of(String old, String new_) {
        return new Remap(old, new_);
    }
}
