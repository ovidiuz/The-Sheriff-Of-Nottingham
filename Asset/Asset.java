package main.Asset;

public final class Asset {

    private static final int KING_BONUS_APPLE = 20;
    private static final int KING_BONUS_CHEESE = 15;
    private static final int KING_BONUS_CHICKEN = 10;
    private static final int QUEEN_BONUS_APPLE = 10;
    private static final int QUEEN_BONUS_CHICKEN = 5;
    private static final int APPLE_ID = 0;
    private static final int CHEESE_ID = 1;
    private static final int BREAD_ID = 2;
    private static final int CHICKEN_ID = 3;
    private static final int SILK_ID = 10;
    private static final int PEPPER_ID = 11;
    private static final int BARREL_ID = 12;
    private static final int APPLE_PROFIT = 2;
    private static final int CHEESE_PROFIT = 3;
    private static final int BREAD_PROFIT = 4;
    private static final int SILK_PROFIT = 9;
    private static final int PEPPER_PROFIT = 8;
    private static final int BARREL_PROFIT = 7;
    private static final int LEGAL_PENALTY = 2;
    private static final int ILLEGAL_PENALTY = 4;
    private static final int SILK_BONUS_COUNT = 3;
    private static final int OTHER_BONUS_COUNT = 2;

    private String assetName;

    // genereaza un Asset primind un string

    public Asset(final String newName) {
        this.assetName = newName;
    }

    // returneaza King's Bonus aferent asset-ului

    public int getKingBonus() {

        int bonus;

        switch (assetName) {

            case "apple":
                bonus = KING_BONUS_APPLE;
                break;
            case "cheese":
            case "bread":
                bonus = KING_BONUS_CHEESE;
                break;
            case "chicken":
                bonus = KING_BONUS_CHICKEN;
                break;
            default:
                bonus = 0;
                break;
        }

        return bonus;
    }

    // returneaza Queen's Bonus aferent asset-ului

    public int getQueenBonus() {

        int bonus;

        switch (assetName) {

            case "apple":
            case "cheese":
            case "bread":
                bonus = QUEEN_BONUS_APPLE;
                break;
            case "chicken":
                bonus = QUEEN_BONUS_CHICKEN;
                break;
            default:
                bonus = 0;
                break;
        }

        return bonus;
    }

    // returneaza true daca bunul este legal
    // si false daca este ilegal

    public boolean isLegal() {

        boolean legal;

        switch (this.assetName) {

            case "apple":
            case "cheese":
            case "bread":
            case "chicken":
                legal = true;
                break;

            case "silk":
            case "pepper":
            case "barrel":
            default:
                legal = false;
                break;

        }

        return legal;

    }

    /**
     *  Am facut Override functiilor hashCode si equals.
     *  pentru a le putea utiliza in Map
     *  Am ales sa returnez hashCode-ul campului assetName,
     *  de tip string, respectiv sa il compar in equals cu
     *  assetName-ul altui obiect
     *
     */

    @Override
    public int hashCode() {
        return this.assetName.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {

        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Asset)) {
            return false;
        }
        Asset assetToCompare = (Asset) obj;

        return this.assetName.equals(assetToCompare.assetName);

    }

    // constructor care genereaza un Aseet
    // pe baza unui ID

    public Asset(final int id) {

        switch (id) {

            case APPLE_ID:
                this.assetName = "apple";
                break;
            case CHEESE_ID:
                this.assetName = "cheese";
                break;
            case BREAD_ID:
                this.assetName = "bread";
                break;
            case CHICKEN_ID:
                this.assetName = "chicken";
                break;
            case SILK_ID:
                this.assetName = "silk";
                break;
            case PEPPER_ID:
                this.assetName = "pepper";
                break;
            case BARREL_ID:
                this.assetName = "barrel";
                break;
            default:
                this.assetName = null;
                break;
        }

    }

    // functia returneaza valoare id-ului
    // asset-ului

    public int createIdFromAsset() {

        int id;

        switch (this.assetName) {

            case "apple":
                id = APPLE_ID;
                break;
            case "cheese":
                id = CHEESE_ID;
                break;
            case "bread":
                id = BREAD_ID;
                break;
            case "chicken":
                id = CHICKEN_ID;
                break;
            case "silk":
                id = SILK_ID;
                break;
            case "pepper":
                id = PEPPER_ID;
                break;
            case "barrel":
                id = BARREL_ID;
                break;
            default:
                id = 0;
                break;
        }

        return id;

    }

    // returneaza profitul aferent bunului

    public int getProfit() {

        int profit;

        switch (this.assetName) {

            case "apple":
                profit = APPLE_PROFIT;
                break;
            case "cheese":
                profit = CHEESE_PROFIT;
                break;
            case "bread":
                profit = BREAD_PROFIT;
                break;
            case "chicken":
                profit = BREAD_PROFIT;
                break;
            case "silk":
                profit = SILK_PROFIT;
                break;
            case "pepper":
                profit = PEPPER_PROFIT;
                break;
            case "barrel":
                profit = BARREL_PROFIT;
                break;
            default:
                profit = 0;
                break;

        }

        return profit;
    }

    // returneaza penalitatea aferenta bunului

    public int getPenalty() {

        if (this.isLegal()) {
            return LEGAL_PENALTY;
        }
        return ILLEGAL_PENALTY;

    }

    // returneaza bonusul aferent asset-ului sub
    // forma de denumirea bunului reprezentat
    // de bonus

    public String getBonusAsset() {

        String bonus;

        switch (this.assetName) {

            case "silk":
                bonus = "cheese";
                break;
            case "pepper":
                bonus = "chicken";
                break;
            case "barrel":
                bonus = "bread";
                break;
            default:
                bonus = null;
                break;

        }

        return bonus;
    }

    // returneaza numarul de bunuri pe care
    // le primeste ca bonus

    public int getBonusAmount() {

        int count;

        switch (this.assetName) {

            case "silk":
                count = SILK_BONUS_COUNT;
                break;
            case "pepper":
            case "barrel":
                count = OTHER_BONUS_COUNT;
                break;
            default:
                count = 0;
                break;

        }

        return count;
    }



}
