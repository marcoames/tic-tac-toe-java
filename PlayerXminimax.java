public class PlayerXminimax {
    private GameLogic game;

    public PlayerXminimax() {
        this.game = new GameLogic();
    }

    public void playGame() {
        
        boolean gameOver = false;

        while (!gameOver) {

            // usuario joga
            System.out.println("Current Board:");
            game.printBoard();
            game.userMovexMinimax();
            game.printBoard();

            // verifica se jogo acabou
            if (checkGameOver()) {
                gameOver = true;
                break;
            }
 
            // computador joga com minimax
            game.computerMove("HARD"); // HARD: 100% minimax

            // verifica se jogo acabou
            if (checkGameOver()) {
                gameOver = true;
                System.out.println("Current Board:");
                game.printBoard();
                break;
            }
        }
    }

    private boolean checkGameOver() {
        String realResult = game.checkWinner();

        if (!realResult.equals("Temjogo")) {
            game.printBoard();
            System.out.println("Game result: " + realResult);
            return true;  
        }
        return false; 
    }

    public static void main(String[] args) {
        PlayerXminimax game = new PlayerXminimax();
        game.playGame();
    }
}
