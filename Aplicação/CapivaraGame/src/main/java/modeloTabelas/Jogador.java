package modeloTabelas;


public class Jogador {
    private int idJogador;
    private int posicao;
    private int pontuacao;
    private int idJogo;
    public Jogador(int idJogador, int posicao, int pontuacao, int idJogo){
        this.idJogador = idJogador;
        this.idJogo = idJogo;
        this.pontuacao = pontuacao;
        this.posicao = posicao;
    }

    public Jogador(int posicao, int pontuacao, int idJogo){
        this.idJogo = idJogo;
        this.posicao = posicao;
        this.pontuacao = pontuacao;
    }

    public Jogador(int posicao, int idJogo){
        this.posicao = posicao;
        this.idJogo = idJogo;
        this.pontuacao = 0;
    }

    //gets e sets
    public void setIdJogador(int idJogador) {
        this.idJogador = idJogador;
    }

    public void setIdJogo(int idJogo) {
        this.idJogo = idJogo;
    }

    public void setPosicao(int posicao) {
        this.posicao = posicao;
    }

    public int getIdJogador() {
        return idJogador;
    }

    public int getIdJogo() {
        return idJogo;
    }

    public int getPosicao() {
        return posicao;
    }

    @Override
    public String toString(){
        return "Jogador id "+ idJogador + " pos "+ posicao + " pontuacao = "+ pontuacao;
    }
}
