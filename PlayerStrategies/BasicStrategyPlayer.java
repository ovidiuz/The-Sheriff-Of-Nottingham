package main.PlayerStrategies;

import java.util.Map;
import main.Asset.Asset;
import main.Asset.SackOfAssets;
import java.util.List;

public class BasicStrategyPlayer extends Player {

    private static final int MAX_ASSETS = 5;

    /**
     * cream sacul cu bunuri alegand bunul legal cel mai frecvent.
     * sau cel ilegal cel mai profitabil, dupa caz.
     */

    public SackOfAssets createAssetSack() {

        SackOfAssets sack = new SackOfAssets();

        Asset maxLegalFreqAsset = getMaxFreqAsset(true);
        Asset maxIllegalProfitAsset = getMaxProfitAsset(false);

        if (maxLegalFreqAsset != null) {

            int maxLegalValue = this.getAssetMap().get(maxLegalFreqAsset);

            if (maxLegalValue > MAX_ASSETS) {
                maxLegalValue = MAX_ASSETS;
            }

            sack.setDeclaredAsset(maxLegalFreqAsset);
            sack.setBribe(0);
            sack.getAssetMap().put(maxLegalFreqAsset, maxLegalValue);
            sack.setNumberOfAssets(maxLegalValue);

            int amount = this.getAssetMap().get(maxLegalFreqAsset);

            if (maxLegalValue < amount) {
                this.getAssetMap().replace(
                        maxLegalFreqAsset,
                        amount - maxLegalValue
                );
            } else {
                this.getAssetMap().remove(maxLegalFreqAsset);
            }
        } else
            if (maxIllegalProfitAsset != null) {

                sack.setDeclaredAsset(new Asset("apple"));
                sack.setBribe(0);
                sack.getAssetMap().put(maxIllegalProfitAsset, 1);
                sack.setNumberOfAssets(1);

                int amount = this.getAssetMap().get(maxIllegalProfitAsset);

                if (amount > 1) {
                    this.getAssetMap().replace(
                            maxIllegalProfitAsset,
                            amount - 1
                    );
                } else {
                    this.getAssetMap().remove(maxIllegalProfitAsset);
                }
            }

        return sack;

    }

    /**
     * inspectam intreaga lista de playeri.
     */
    @Override
    public void inspectionTime(final List<Player> playersArray, final List<Integer> assetsIds) {

        for (Player playerIterator: playersArray) {

            if (playerIterator != this) {

                SackOfAssets sackToBeInspected =
                        playerIterator.createAssetSack();

                this.inspectAssetSack(
                        playerIterator,
                        sackToBeInspected,
                        assetsIds
                );

            }

        }


    }

    /**
     * aplicam prezumtia de nevinovatie si daca aceasta pica.
     * sheriff-ul va primi penalty
     * daca prezumtia se dovedeste a fi adevarata
     * player-ul va primi penalty de la sheriff
     */
    @Override
    public void inspectAssetSack(final Player player, final SackOfAssets sack, final List<Integer> assetsIds) {

        boolean isLegal = true;     // prezumtia de nevinovatie
        int sheriffPenalty = 0;
        int playerPenalty = 0;

        for (Map.Entry<Asset, Integer> entry: sack.getAssetMap().entrySet()) {

            Asset declaredAsset = sack.getDeclaredAsset();

            if (!declaredAsset.equals(entry.getKey())) {
                // daca un bun este ilegal, este automat si nedeclarat
                // din regulile jocului,
                // deci nu mai are sens sa testam legalitatea

                isLegal = false;
                playerPenalty += entry.getKey().getPenalty() * entry.getValue();
                assetsIds.add(entry.getKey().createIdFromAsset());

            } else {

                int penalty = entry.getKey().getPenalty() * entry.getValue();
                sheriffPenalty += penalty;
                Asset assetToAdd = entry.getKey();
                int countBonus = assetToAdd.getBonusAmount();
                int freq = player.getMerchantStand().getOrDefault(assetToAdd, 0);
                freq += entry.getValue();
                player.getMerchantStand().put(assetToAdd, freq);
                if (countBonus > 0) {
                    Asset bonusAsset = new Asset(assetToAdd.getBonusAsset());
                    freq = player.getMerchantStand().getOrDefault(bonusAsset, 0);
                    freq += countBonus * entry.getValue();
                    player.getMerchantStand().put(bonusAsset, freq);
                }
            }

        }

        if (isLegal) { // verificam daca prezumtia de nevinovatie a fost sau nu adevarata

            player.setCoins(player.getCoins() + sheriffPenalty);
            this.setCoins(this.getCoins() - sheriffPenalty);

        } else {

            player.setCoins(player.getCoins() - playerPenalty);
            this.setCoins(this.getCoins() + playerPenalty);
        }

    }

    /**
     *  afisam scorul player-ului.
     */
    public void printStats() {
        System.out.print("BASIC: ");
        System.out.println(this.getScore());
    }

}
