package modeloTabelas;

public class Jogada {

    private int idJogada;
    private int ordem;
    private int idPartida;
    private int idJogador;
    private int acao; // 1=Jogou, 2=Comprou, 3=Passou, 4=Distribuiu
    private Integer idPeca; // pode ser null quando passa a vez
    private Integer ladoUtilizado; // pode ser null se não jogou peça

    public Jogada(int idJogada, int ordem, int idPartida, int idJogador, int acao, Integer idPeca, Integer ladoUtilizado){
        this.idJogada = idJogada;
        this.ordem = ordem;
        this.idPartida = idPartida;
        this.idJogador = idJogador;
        this.acao = acao;
        this.idPeca = idPeca;
        this.ladoUtilizado = ladoUtilizado;
    }

    public Jogada(int ordem, int idPartida, int idJogador, int acao, Integer idPeca, Integer ladoUtilizado){
        this.ordem = ordem;
        this.idPartida = idPartida;
        this.idJogador = idJogador;
        this.acao = acao;
        this.idPeca = idPeca;
        this.ladoUtilizado = ladoUtilizado;
    }

    // Getters e Setters
    public int getIdJogada() {
        return idJogada;
    }

    public void setIdJogada(int idJogada) {
        this.idJogada = idJogada;
    }

    public int getOrdem() {
        return ordem;
    }

    public void setOrdem(int ordem) {
        this.ordem = ordem;
    }

    public int getIdPartida() {
        return idPartida;
    }

    public int getIdJogador() {
        return idJogador;
    }

    public int getAcao() {
        return acao;
    }

    public Integer getIdPeca() {
        return idPeca;
    }

    public Integer getLadoUtilizado() {
        return ladoUtilizado;
    }

    @Override
    public String toString(){
        return "Jogada id= " + idJogada +
                ", ordem=" + ordem +
                ", partida=" + idPartida +
                ", jogador=" + idJogador +
                ", acao=" + acao +
                ", peca=" + idPeca +
                ", ladoUsado=" + ladoUtilizado;
    }
}
