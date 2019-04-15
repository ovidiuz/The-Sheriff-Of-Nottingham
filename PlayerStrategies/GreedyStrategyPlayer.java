package main.PlayerStrategies;

import main.Asset.SackOfAssets;
import main.Asset.Asset;

import java.util.List;
import java.util.Map;
import java.util.Set;

public final class GreedyStrategyPlayer extends BasicStrategyPlayer {

    private static final int MAX_ASSETS = 5;
    private static int countRounds = 0;

    public SackOfAssets createAssetSack() {

        // cream sacul apeland strategia basic si incrementam numarul rundei
        SackOfAssets sack = super.createAssetSack();
        countRounds++;

        // daca runda este para si mai avem loc in sac
        // gasim bunul ilegal de profit maxim
        // si il adaugam daca acesta exista

        if (countRounds % 2 == 0 && sack.getNumberOfAssets() < MAX_ASSETS) {

            Asset maxProfitIllegalAsset = getMaxProfitAsset(false);

            if (maxProfitIllegalAsset != null) {

                sack.getAssetMap().put(
                        maxProfitIllegalAsset,
                        sack.getAssetMap().getOrDefault(
                                maxProfitIllegalAsset,
                                0
                        )
                                + 1
                );
                int amount = this.getAssetMap().get(maxProfitIllegalAsset);

                if (amount > 1) {
                    this.getAssetMap().replace(maxProfitIllegalAsset, amount - 1);
                } else {
                    this.getAssetMap().remove(maxProfitIllegalAsset);
                }
                sack.setNumberOfAssets(sack.getNumberOfAssets() + 1);

            }

        }

        return sack;
    }

    public  void inspectAssetSack(final Player player, final SackOfAssets sack, final List<Integer> assetsIds) {

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
                int freq = player.getMerchantStand().getOrDefault(assetToAdd, 0);
                freq += entry.getValue();
                player.getMerchantStand().put(assetToAdd, freq);
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

    public void printStats() {
        System.out.print("GREEDY: ");
        System.out.println(this.getScore());
    }
}



