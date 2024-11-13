public class Training {
    private GameLogic game;

    public Training() {
        this.game = new GameLogic();
    }

    public int playGame(double[] chromosome, String gameDifficulty) {
        game.setNeuralNetwork(chromosome);

        int score = 0;

        while (true) {
            
            // jogada da rede
            int realMove = game.neuralNetworkMove();

            // se jogada invalida perde 10 pontos e acaba o jogo
            if (realMove == -1) {
                return -10;  
            }

            // verifica se jogo acabou depois da jogada da rede           
            if (isGameOver()) {
                return getScore("Xganha", score);  // rede ganha
            }

            // jogada do computador
            game.computerMove(gameDifficulty);

            // verifica se jogo acabou depois da jogada do computador
            if (isGameOver()) {
                return getScore("Oganha", score);  // computador ganha
            }
        }
    }

    private boolean isGameOver() {
        String result = game.checkWinner();
        return !result.equals("Temjogo");  // jogo acabou
    }

    private int getScore(String result, int score) {
        if (result.equals("Xganha")) {
            return score + 20;  // rede ganha
        } else if (result.equals("Oganha")) {
            return score + 5;  // computador ganha
        } else {
            return score + 10;  // empate
        }
    }
}
