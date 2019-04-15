
package main.PlayerStrategies;

import main.Asset.SackOfAssets;
import main.Asset.Asset;

import java.util.List;
import java.util.Map;
import java.util.Set;

public final class BribeStrategyPlayer extends BasicStrategyPlayer {

    private static final int SMALL_BRIBE = 5;
    private static final int BIG_BRIBE = 10;
    private static final int MAX_ASSETS = 5;

    public SackOfAssets createAssetSack() {

        SackOfAssets sack = new SackOfAssets();

        int countIllegalAssets = 0;
        int bribe = 0;

        while (true) {

            // daca nu mai avem loc in sac ne oprim
            if (countIllegalAssets == MAX_ASSETS) {
                break;
            }

            // daca nu avem bani sa dam mita ne oprim

            if (this.getCoins() < SMALL_BRIBE) {
                break;
            }

            Asset maxIllegalProfitAsset = this.getMaxProfitAsset(false);

            // daca nu avem bunuri ilegale ne oprim
            if (maxIllegalProfitAsset == null) {
                break;
            }

            int amount = this.getAssetMap().get(maxIllegalProfitAsset);

            if (amount > MAX_ASSETS - countIllegalAssets) {
                amount = MAX_ASSETS - countIllegalAssets;
            }

            // daca avem mai multe bunuri de un tip
            // decat putem pune in sac actualizam cantitatea
            // pe care vrem sa o punem

            if (this.getCoins() >= BIG_BRIBE && amount + countIllegalAssets > 2) {
                bribe = BIG_BRIBE;
                sack.getAssetMap().put(maxIllegalProfitAsset, amount);
                int amountOnStand = this.getAssetMap().get(maxIllegalProfitAsset);
                if (amount < amountOnStand) {
                    this.getAssetMap().replace(maxIllegalProfitAsset,
                            amountOnStand - amount);
                } else {
                    this.getAssetMap().remove(maxIllegalProfitAsset);
                }
                countIllegalAssets += amount;
            } else {
                if (this.getCoins() >= SMALL_BRIBE
                        && amount + countIllegalAssets <= 2) {
                    bribe = SMALL_BRIBE;
                    sack.getAssetMap().put(maxIllegalProfitAsset, amount);
                    int amountOnStand =
                            this.getAssetMap().get(maxIllegalProfitAsset);
                    if (amount < amountOnStand) {
                        this.getAssetMap().replace(maxIllegalProfitAsset,
                                amountOnStand - amount);
                    } else {
                        this.getAssetMap().remove(maxIllegalProfitAsset);
                    }
                    countIllegalAssets += amount;

                }
            }

        }

        if (bribe != 0) {

            // daca am avut bani sa mituim si bunuri ilegale declaram
            // ca am bagat mere in sac

            sack.setDeclaredAsset(new Asset("apple"));
            sack.setBribe(bribe);
            sack.setNumberOfAssets(countIllegalAssets);
        } else {

            // daca nu apelam strategia de baza
            sack = super.createAssetSack();
        }
        return sack;

    }

    public void inspectAssetSack(final Player player, final SackOfAssets sack, final List<Integer> assetsIds) {

        // daca in sac se gaseste mita, o acceptam
        // si trecem toate bunurile player-ului in merchantStand-ul sau

        // in caz contrar apelam strategia de baza

        if (sack.getBribe() != 0) {

            this.setCoins(this.getCoins() + sack.getBribe());
            player.setCoins(player.getCoins() - sack.getBribe());

            Set<Map.Entry<Asset, Integer>> mapSet;
            mapSet = sack.getAssetMap().entrySet();

            for (Map.Entry<Asset, Integer> entry: mapSet) {
                Asset assetToAdd = entry.getKey();
                int val = player.getMerchantStand().getOrDefault(assetToAdd, 0);
                player.getMerchantStand().put(assetToAdd, val + entry.getValue());
                int countBonus = assetToAdd.getBonusAmount();
                if (countBonus > 0) {
                    Asset bonusAsset = new Asset(assetToAdd.getBonusAsset());
                    player.getMerchantStand().put(bonusAsset,
                            player.getMerchantStand().getOrDefault(bonusAsset, 0)
                                    + countBonus * entry.getValue());
                }
            }

        } else {
            super.inspectAssetSack(player, sack, assetsIds);
        }
    }

    public void inspectionTime(final List<Player> playersArray, final List<Integer> assetsIds) {

        int left;
        int right;

        int index = playersArray.indexOf(this);

        // calculam pozitiile din stanga si din dreapta

        if (index > 0) {
            left = index - 1;
        } else {
            left = playersArray.size() - 1;
        }

        if (index < playersArray.size() - 1) {
            right = index + 1;
        } else {
            right = 0;
        }

        SackOfAssets sack = playersArray.get(left).createAssetSack();
        super.inspectAssetSack(playersArray.get(left), sack, assetsIds);

        // verificam ca left sa nu coincida cu right

        if (left != right) {
            sack = playersArray.get(right).createAssetSack();
            super.inspectAssetSack(playersArray.get(right), sack, assetsIds);
        }

        // inspectam si restul listei de playeri
        for (int i = 0; i < playersArray.size(); i++) {
            if (i != left && i != right && i != index) {
                sack = playersArray.get(i).createAssetSack();
                inspectAssetSack(playersArray.get(i), sack, assetsIds);
            }
        }


    }

    public void printStats() {
        System.out.print("BRIBED: ");
        System.out.println(this.getScore());
    }

}
