package main;

import main.Asset.Asset;
import main.PlayerStrategies.Player;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public final class GameLogic {

    private static final String[] ASSET_NAMES =
            {"apple", "bread", "cheese", "chicken"};

    private static final int MAX_ASSETS = 6;

    // redistribuim bunuri in lista de playeri

    public void redristibuteAssets(final GameInput gameInput, final List<Player> playersArray) {

        for (Player player: playersArray) {

            int countAssetsToAdd = MAX_ASSETS - player.countAssets();

            while (countAssetsToAdd > 0) {
                int itemId = gameInput.getAssetIds().remove(0);
                Asset itemToAdd = new Asset(itemId);
                player.addAsset(itemToAdd);
                countAssetsToAdd--;
            }

        }
    }

    public void action(final GameInput gameInput) {

        List<Player> playersArray = new ArrayList<Player>();

        // generam sirul de playeri

        for (String player: gameInput.getPlayerNames()) {

            player = player.replaceAll("\"", "");

            playersArray.add(Player.createPlayerFromName(player));

        }

        // distribuim bunuri
        redristibuteAssets(gameInput, playersArray);


        // in aceasta iteratie ne asiguram ca fiecare player va fi serif macar o data

        for (Player player: playersArray) {
            player.inspectionTime(
                    playersArray,
                    gameInput.getAssetIds()
            );
            redristibuteAssets(
                    gameInput,
                    playersArray
            );
        }

        // aici a doua oara

        for (Player player: playersArray) {
            player.inspectionTime(
                    playersArray,
                    gameInput.getAssetIds()
            );
            redristibuteAssets(
                    gameInput,
                    playersArray
            );
        }

        // acum fiecare player a fost serif cel putin de 2 ori
        // deci jocul s-a terminat si urmeaza clasamentul

        // adaugam bunurile de pe taraba la scor
        // mai precis la earnedMoney

        for (Player player: playersArray) {

            Set<Map.Entry<Asset, Integer>> mapSet;
            mapSet = player.getMerchantStand().entrySet();

            for (Map.Entry<Asset, Integer> entry: mapSet) {
                int earnedMoney = player.getEarnedMoney()
                        + entry.getKey().getProfit()
                        * entry.getValue();
                player.setEarnedMoney(earnedMoney);
            }

        }

        // acordam bonusurile

        for (int i = 0; i < GameLogic.ASSET_NAMES.length; i++) {

            Asset assetKey = new Asset(GameLogic.ASSET_NAMES[i]);

            TreeSet<Integer> freqArray = new TreeSet<>();

            for (Player player: playersArray) {
                int freq = player.getMerchantStand().getOrDefault(assetKey, 0);
                freqArray.add(freq);
            }

            // calculam frecventa pentru bonusul de King
            // si pentru cel de Queen

            Integer kingVal = freqArray.pollLast();

            if (kingVal == null) {
                continue;
            }

            // acordam bonusul de King player-ilor care
            // au bunuri de tipul acesta in catitatea
            // respectiva

            for (Player player: playersArray) {
                int freq = player.getMerchantStand().getOrDefault(assetKey, 0);
                if (freq == kingVal) {
                    int bonus = player.getBonus() + assetKey.getKingBonus();
                    player.setBonus(bonus);
                }
            }

            Integer queenVal = freqArray.pollLast();

            if (queenVal == null) {
                continue;
            }

            // acordam bonusul de Queen player-ilor care
            // au bunuri de tipul acesta in catitatea
            // respectiva

            for (Player player: playersArray) {
                int freq = player.getMerchantStand().getOrDefault(assetKey, 0);
                if (freq == queenVal) {
                    int bonus = player.getBonus() + assetKey.getQueenBonus();
                    player.setBonus(bonus);
                }
            }

        }

        // sortam lista de players dupa scor

        playersArray.sort(new Comparator<Player>() {
            @Override
            public int compare(final Player o1, final Player o2) {
                return o2.getScore() - o1.getScore();
            }
        });

        // afisam rezultatul

        printFinalStats(playersArray);

    }

    public void printFinalStats(final List<Player> playersArray) {

        playersArray.forEach((player) -> {
            player.printStats();
        });
    }
}
