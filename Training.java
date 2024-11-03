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
            // Movimento da Rede Neural
            int bestMove = game.network_move_minimax();
            int realMove = game.neuralNetworkMove();

            // System.out.println("Best move: " + (bestMove+1) + " Real move: " + (realMove+1));
            
            // Se a jogada não for válida, a rede neural perde automaticamente
            if (realMove == -1) {
                return -1;
            }

            // Verifica se a jogada da rede neural corresponde à jogada ideal
            if (realMove == bestMove) {
                score += 1;
            }

            // Verificar se o jogo terminou após a jogada da rede neural
            boolean result = isGameOver();
            if (result) {
                gameOver = true;
                return getScore("Xganha", score);
            }

            // Movimento do Computador
            game.computerMove(gameDifficulty);

            // Verificar se o jogo terminou após a jogada do computador
            result = isGameOver();
            if (result) {
                gameOver = true;
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
            score += 5;
            return score;
        } else if (result == "Oganha") {
            return score;
        } else {
            score += 2;
            return score;
        }
    }
}
