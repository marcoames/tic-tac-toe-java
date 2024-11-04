public class Training {
    private GameLogic game;

    public Training() {
        this.game = new GameLogic();
    }

    public int playGame(double[] chromosome, String gameDifficulty) {

        game.setNeuralNetwork(chromosome);

        boolean gameOver = false;
        int score = 0;

        while (!gameOver) {

            // movimento da Rede Neural
            int bestMove = game.network_move_minimax();
            int realMove = game.neuralNetworkMove();

            // System.out.println("Best move: " + (bestMove+1) + " Real move: " + (realMove+1));
            
            // se a jogada nao for valida a rede neural perde automaticamente
            if (realMove == -1) {
                return -1;
            }

            // verifica se a jogada da rede neural corresponde a melhor jogada gerada pelo minimax da rede, se sim adiciona 1 ponto
            if (realMove == bestMove) {
                score += 1;
            }

            // verifica se o jogo terminou depois da jogada da rede neural
            boolean result = isGameOver();
            if (result) {
                gameOver = true;
                // this.game.printBoard();
                // System.out.println("Rede Neural ganhou! score: " + score);
                return getScore("Xganha", score);
            }

            // movimento do Computador
            game.computerMove(gameDifficulty);

            // verifica se o jogo terminou depois da jogada do computador
            result = isGameOver();
            if (result) {
                gameOver = true;
                // this.game.printBoard();
                // System.out.println("Computador ganhou! score: " + score);
                return getScore("Oganha", score);
            }
        }
        
        return score;
    }

    private boolean isGameOver() {
        String result = game.checkWinner();
        if (result != "Temjogo") {
            return true;
        }
        return false;
    }

    private int getScore(String result, int score) {
        if (result == "Xganha") {
            score += 10;
            return score;
        } else if (result == "Oganha") {
            return score;
        } else {
            score += 5;
            return score;
        }
    }
}
