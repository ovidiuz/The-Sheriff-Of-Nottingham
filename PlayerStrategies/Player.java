package main.PlayerStrategies;

import main.Asset.Asset;
import main.Asset.SackOfAssets;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

public abstract class Player {

    static final int MAX_COINS = 50;
    static final int MAX_FREQ = -10000;

    private int coins = MAX_COINS;
    private int earnedMoney = 0;
    private int bonus = 0;
    private Map<Asset, Integer> assetMap = new HashMap<>();
    private Map<Asset, Integer> merchantStand = new HashMap<>();

    /**
     *  Getter.
     *  returneaza merchantStand.
     */
    public Map<Asset, Integer> getMerchantStand() {
        return merchantStand;
    }

    /**
     * numara cate bunuri avem in mana.
     */
    public int countAssets() {

        int count = 0;

        for (Map.Entry<Asset, Integer> entry: assetMap.entrySet()) {
            count += entry.getValue();
        }

        return count;
    }

    /**
     * @param isLegal --> specifica ce tip de bunuri cautam
     *  returneaza bunul cu frecventa cea mai mare de tipul cautat
     *  sau null daca nu exista astfel de bunuri
     */
    public Asset getMaxFreqAsset(final boolean isLegal) {

        Asset maxAsset = null;
        int maxFreq = MAX_FREQ;

        for (Map.Entry<Asset, Integer> entry: assetMap.entrySet()) {
            if (entry.getKey().isLegal() == isLegal) {
                if (maxFreq < entry.getValue()) {
                    maxAsset = entry.getKey();
                    maxFreq = entry.getValue();
                } else
                    if (maxFreq == entry.getValue()
                            && maxAsset.getProfit() < entry.getKey().getProfit()) {
                        maxAsset = entry.getKey();
                        maxFreq = entry.getValue();
                    }
            }
        }

        return maxAsset;

    }

    /**
     * @param isLegal --> specifica ce tip de bunuri cautam
     *  returneaza bunul cu profitul cel mai mare de tipul cautat
     *  sau null daca nu exista astfel de bunuri
     */
    public Asset getMaxProfitAsset(final boolean isLegal) {

        Asset maxAsset = null;
        int maxProfit = MAX_FREQ;

        for (Map.Entry<Asset, Integer> entry: assetMap.entrySet()) {
            if (entry.getKey().isLegal() == isLegal
                    && maxProfit < entry.getKey().getProfit()) {
                maxAsset = entry.getKey();
                maxProfit = entry.getKey().getProfit();
            }
        }

        return maxAsset;
    }

    /**
     * Getter.
     * returneaza coins
     */
    public int getCoins() {
        return coins;
    }

    /**
     * Setter.
     * Modifica coins
     */
    public void setCoins(final int newCoins) {
        this.coins = newCoins;
    }

    /**
     * Getter.
     * returneaza earnedMoney, adica banii
     * proveniti din bunurile de pe taraba
     */
    public int getEarnedMoney() {
        return earnedMoney;
    }

    /**
     * Setter.
     * Modifica earnedMoney
     */
    public void setEarnedMoney(final int newMoney) {
        this.earnedMoney = newMoney;
    }

    /**
     * Getter.
     * returneaza bonusul
     */
    public int getBonus() {
        return bonus;
    }

    /**
     * Setter.
     * Modifica bonusul
     */
    public void setBonus(final int newBonus) {
        this.bonus = newBonus;
    }

    /**
     * Getter.
     * Returneaza assetMap, colectia de obiecte
     * din mana
     */
    public Map<Asset, Integer> getAssetMap() {
        return assetMap;
    }


    /**
     * primeste un string si returneaza un player de tipul.
     * aferent string-ului
     */


    public static Player createPlayerFromName(final String playerName) {

        switch (playerName) {

            case "basic": return new BasicStrategyPlayer();

            case "greedy": return new GreedyStrategyPlayer();

            case "bribed": return new BribeStrategyPlayer();


            default: return null;

        }

    }

    /**
     * functia adauga un bun la colectia de asset-uri din mana.
     */
    public void addAsset(final Asset assetToAdd) {

        assetMap.put(assetToAdd, assetMap.getOrDefault(assetToAdd, 0) + 1);

    }

    public abstract SackOfAssets createAssetSack();

    public abstract void inspectAssetSack(Player player, SackOfAssets sack, List<Integer> assetsIds);

    public abstract void inspectionTime(List<Player> playersArray, List<Integer> assetsId);

    public abstract void printStats();

    /**
     * calculeaza scorul player-ului.
     */
    public int getScore() {
        return this.earnedMoney + this.coins + this.bonus;
    }

}


