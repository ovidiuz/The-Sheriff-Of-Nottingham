package main.Asset;

import java.util.HashMap;
import java.util.Map;

public final class SackOfAssets {

    private Map<Asset, Integer> assetMap = new HashMap<>();
    private Asset declaredAsset;

    private int bribe;
    private int numberOfAssets;

    public Map<Asset, Integer> getAssetMap() {
        return assetMap;
    }

    public void setAssetMap(final Map<Asset, Integer> newMap) {
        this.assetMap = newMap;
    }

    public Asset getDeclaredAsset() {
        return declaredAsset;
    }

    public void setDeclaredAsset(final Asset newAsset) {
        this.declaredAsset = newAsset;
    }

    public int getBribe() {
        return bribe;
    }

    public void setBribe(final int newBribe) {
        this.bribe = newBribe;
    }

    public int getNumberOfAssets() {
        return numberOfAssets;
    }

    public void setNumberOfAssets(final int newNumber) {
        this.numberOfAssets = newNumber;
    }
}

