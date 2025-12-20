package net.abraxator.moresnifferflowers.capability;

public interface NeoCapability<T> {
    void setFrom(T capNew);
    T getDefault();
    default void setDefault(){
        setFrom(getDefault());
    };
}
