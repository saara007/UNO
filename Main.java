
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Play.startGame();
    }

    static int handleChoice() {
        Scanner scan = new Scanner(System.in);
        return scan.nextInt();
    }
}