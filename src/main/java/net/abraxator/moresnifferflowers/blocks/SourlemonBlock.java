package net.abraxator.moresnifferflowers.blocks;

public class SourlemonBlock extends SaltemoneBlock{
    public SourlemonBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isCorrupted(){
        return true;
    }
}
