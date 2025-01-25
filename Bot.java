public class Bot extends Player {
    private final static String[] botNames = {
            "Zenith",
            "Vesper",
            "Cipher",
            "Aurora",
            "Valor",
            "Orion",
            "Seraph",
            "Nyx",
            "Jimmy"
    };
    Bot(Player prev, Player next, String name) {
        super(prev, next, name);
    }
    static Bot[] generateBots(int total) {
        Bot[] bots = new Bot[total];

        // Create bots
        for (int i = 0; i < bots.length; i++) {
            bots[i] = new Bot(null, null, "[BOT] " + botNames[i]);
        }

        // Link Bots
        for (int i = 0; i < bots.length - 1; i++) {
            bots[i].next = bots[i + 1];
            bots[i + 1].prev = bots[i];
        }

        return bots;
    }

    @Override
    void playCard(String colorToPlay) throws InterruptedException {
        System.out.printf("%s is currently playing... (Cards Left: %d)\n", this.name, this.hand.size());
        Thread.sleep(2000);
        Card chosenCard = null;
        Card topDiscardPile = Play.discardPile.peek();
        for (Card card: this.hand) {
            if (card.isPlayable(topDiscardPile, colorToPlay)){
                chosenCard = card;
                break;
            }
        }
        if (chosenCard instanceof WildCard) {
            switch (((WildCard) chosenCard).type) {
                case "Wild" -> {
                    System.out.printf("%s played a Wild Card.\n", this.name);
                    WildCard.playWild(this);
                    Thread.sleep(800);
                }
                case "Wild Draw 4" -> WildCard.attemptPlayWildDraw4(this, colorToPlay);
            }
            chosenCard.play(this);
        } else if (chosenCard instanceof ActionCard) {
            System.out.printf("%s played the card: ", this.name);
            Card.printCard(chosenCard);
            Thread.sleep(800);
            switch (((ActionCard) chosenCard).type) {
                case "Skip" -> ActionCard.playSkip();
                case "Reverse" -> ActionCard.playReverse();
                case "Draw 2" -> ActionCard.playDraw2(this);
            }
            chosenCard.play(this);
        } else if (chosenCard instanceof NumberCard) {
            System.out.printf("%s played the card: ", this.name);
            Card.printCard(chosenCard);
            Thread.sleep(800);

            chosenCard.play(this);
        }
    }

    static void printBotNames(Bot[] bots) throws InterruptedException {
        System.out.println("Bot Names:");
        Thread.sleep(500);
        for (Bot bot: bots) {
            System.out.printf("\t%s\n", bot.name);
            Thread.sleep(500);
        }
    }
}