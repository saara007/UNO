import java.util.Stack;

public class WildCard extends Card {
    private static final Stack<WildCard> cards = new Stack<>();
    private static boolean showWarningMsg = true;
    String type;
    WildCard(String color, String type) {
        super(color);
        this.type = type;
    }

    @Override
    boolean isPlayable(Card topDiscardPile, String colorToPlay) {
        return true;
    }

    @Override
    void play(Player player) {
        // Transfer chosen card from hand to discard pile.
        Play.discardPile.push(this);
        player.hand.remove(this);
    }

    public static Stack<WildCard> getCards(){
        //Regular Wild Cards
        for (int i = 0; i < 4; i++) cards.push(new WildCard("wild", "Wild"));

        // Wild Draw 4 Cards
        for (int i = 0; i < 4; i++) cards.push(new WildCard("wild", "Wild Draw 4"));

        return cards;
    }
    static void playWild(Player player) throws InterruptedException {
        String color;
        if (player instanceof User) {
            color = userChooseColor();
        }
        else color = botChooseColor((Bot) player);

        Thread.sleep(600);
        System.out.printf("%s chose the color %s\n", player.name, color);
        Play.setColorToPlay(color);
        Thread.sleep(1500);
    }
    static void playWildDraw4(Player player, String colorToPlay) throws InterruptedException {
        Player nextPlayer;
        if (Play.getTurnReversed()) {
            nextPlayer = player.prev;
        } else nextPlayer = player.next;

        System.out.printf("%s played a Wild Draw 4.\n", player.name);
        Thread.sleep(1500);

        // If next player is a user
        if (nextPlayer instanceof User) {
            // prompt user if they want to challenge.
            System.out.print("""
                        Do you want to challenge?
                            [1] Yes
                            [2] No (Draw 4 and lose your turn)
                        """);
            int choice;
            while (true) {
                try {
                    choice = Main.handleChoice();
                    if (choice != 1 && choice != 2) throw new RuntimeException();
                    break;
                } catch (Exception e) {
                    System.out.println("Invalid choice!");
                }
            }

            if (choice == 1) {
                challenge(player, colorToPlay);
            } else {
                System.out.printf("%s draws 4 cards and loses turn.\n", nextPlayer.name);
                PlayMethods.drawCards(4, nextPlayer);
                Play.activateSkip();
                Thread.sleep(1500);
            }

            // Next player is a bot
        } else {
            int random = (int) (Math.random() * 10) + 1;
            // (30% chance of challenging) bot will not challenge if the card played was drawn from the deck. (my bots are not stupid :P).
            // Reverse cards act as a Skip card if there are only 2 players.
            if (random <= 3 && !player.hasDrawn1Card) {
                challenge(player, colorToPlay);
            } else {
                System.out.printf("%s draws 4 cards and loses turn.\n", nextPlayer.name);
                Thread.sleep(1500);
                PlayMethods.drawCards(4, nextPlayer);
                Play.activateSkip();
            }
        }

        String color;
        if (player instanceof User) color = userChooseColor();
        else color = botChooseColor((Bot) player);

        System.out.printf("%s chose color %s\n", player.name, color);
        Play.setColorToPlay(color);
        Thread.sleep(1500);
    }

    // Displays and executes challenge result.
    private static void challenge(Player player, String colorToPlay) throws InterruptedException {
        boolean isSafe = true;
        for (Card card: player.hand) {
            if (card.color.equals(colorToPlay)) {
                isSafe = false;
                break;
            }
        }

        Player challenger;
        if (Play.getTurnReversed()) {
            challenger = player.prev;
        } else challenger = player.next;

        System.out.printf("%s wants to challenge %s for playing a Wild Draw 4 card!\n", challenger.name, player.name);
        Thread.sleep(2500);
        PlayMethods.displayHand(player);
        // If challenger won they get to play next turn and the player draws 4 cards instead.
        if (!isSafe) {
            System.out.printf("%s won the challenge and gets to play next turn. %s gets to draw 4 cards instead.\n",
                    challenger.name, player.name);
            PlayMethods.drawCards(4, player);
        } else {
            //  They draw 6 cards instead and still lose their turn.
            System.out.printf("%s lost the challenge and gets to draw 6 cards in total while still losing turn.\n", challenger.name);
            PlayMethods.drawCards(6, challenger);
            Play.activateSkip();
        }
        Thread.sleep(2500);
    }

    static boolean attemptPlayWildDraw4(Player player, String colorToPlay) throws InterruptedException {
        boolean isSafe = true;
        boolean playSuccess = true;

        for (Card card: player.hand) {
            isSafe = !card.color.equals(colorToPlay);
            break;
        }
        //  Warn user playing wild draw 4 while still having cards with playable colors in their hand.
        if (!isSafe && player instanceof User) {
            if (showWarningMsg) {
                System.out.println("""
                    Caution: You are attempting to play a "Wild Draw 4" card while having other cards that match the current color being played.
                             The next player might choose to accept your play drawing their 4 cards and losing their turn, Or they may challenge you.
                             If they lose the challenge, they will draw and additional of 2 cards (6 total) and lose their turn.
                             If they win, you will draw 4 cards instead while they get to play their turn next.
                             You get to choose the next color to be played regardless of the outcome.
                             Do you still want to play the card?
                                [1] Yes
                                [2] No
                                [3] Yes (Don't show this message again)
                                [4] No (Don't show this message again)
                    """);
                int choice;
                while (true) {
                    try {
                        choice = Main.handleChoice();
                        if (choice < 1 || choice > 4) throw new RuntimeException();
                        break;
                    } catch (Exception e) {
                        System.out.println("Invalid choice!");
                    }
                }

                switch (choice) {
                    case 1 -> playWildDraw4(player, colorToPlay);
                    case 2 -> playSuccess =  false;
                    case 3 -> {
                        playWildDraw4(player, colorToPlay);
                        showWarningMsg = false;
                    }
                    case 4 -> {
                        playSuccess = false;
                        showWarningMsg = false;
                    }
                }
                // Play card directly if showMessage is false.
            } else playWildDraw4(player, colorToPlay);

            // Play card directly if player is a bot or play is safe.
        } else playWildDraw4(player, colorToPlay);
        return playSuccess;
    }

    static String userChooseColor() {
        System.out.print("""
                Enter choice of color to play next turn:
                [1] Blue
                [2] Green
                [3] Red
                [4] Yellow
                """);
        int choice;
        while (true) {
            try {
                choice = Main.handleChoice();
                if (choice < 1 || choice > 4) throw new RuntimeException();
                break;
            } catch (Exception e) {
                System.out.println("Invalid choice!");
            }
        }
        return switch (choice) {
            case 1 -> "Blue";
            case 2 -> "Green";
            case 3 -> "Red";
            case 4 -> "Yellow";
            default -> throw new IllegalStateException("Unexpected value: " + choice);
        };
    }

    static String botChooseColor(Bot bot) {
        int blueCardsCount = 0;
        int greenCardsCount = 0;
        int redCardsCount = 0;
        int yellowCardsCount = 0;

        for(Card card: bot.hand) {
            switch (card.color) {
                case "Blue" -> blueCardsCount++;
                case "Green" -> greenCardsCount++;
                case "Red" -> redCardsCount++;
                case "Yellow" -> yellowCardsCount++;
            }
        }

        int[] cardColorCount = {blueCardsCount, greenCardsCount, redCardsCount, yellowCardsCount};

        int max = blueCardsCount;
        int botChoice = 1;

        for (int i = 0; i < cardColorCount.length - 1; i++) {
            if (max < cardColorCount[i + 1]) {
                max = cardColorCount[i + 1];
                botChoice = i + 2;
            }
        }

        return switch (botChoice) {
            case 1 -> "Blue";
            case 2 -> "Green";
            case 3 -> "Red";
            case 4 -> "Yellow";
            default -> throw new IllegalStateException("Unexpected value: " + botChoice);
        };
    }
}