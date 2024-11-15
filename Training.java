public class Training {
    private GameLogic game;

    public Training() {
        this.game = new GameLogic();
    }

    public int playGame(double[] chromosome, String gameDifficulty) {
        // define a rede neural para o jogo atual
        game.setNeuralNetwork(chromosome);

        int score = 0;

        while (true) {
            // jogada da rede neural
            int realMove = game.neuralNetworkMove();

            // se a jogada for invalida, perde pontos e o jogo acaba
            if (realMove == -1) {
                return - 100;  
            }

            // recompensa por uma jogada valida
            score += 5;

            // verifica se o jogo acabou apos a jogada da rede neural
            if (isGameOver()) {
                return getScore("Xganha", score);  // rede neural venceu
            }

            // jogada do computador
            game.computerMove(gameDifficulty);

            // verifica se o jogo acabou apos a jogada do computador
            if (isGameOver()) {
                return getScore("Oganha", score);  // computador venceu
            }
        }
    }

    private boolean isGameOver() {
        // verifica o estado do jogo (se ainda esta em andamento)
        String result = game.checkWinner();
        return !result.equals("Temjogo");  // jogo acabou
    }

    private int getScore(String result, int score) {
        // ajusta a pontuacao com base no resultado do jogo
        if (result.equals("Xganha")) {
            return score + 50;  // recompensa por vitoria da rede
        } else if (result.equals("Oganha")) {
            return score - 10;  // penalizacao por derrota
        } else {
            return score + 25;  // recompensa por empate
        }
    }
}
