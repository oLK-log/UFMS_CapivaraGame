package modeloTabelas;

public class Partida {
    private int idPartida;
    private int idJogo;
    private Integer idJogadorFinalizou = null; //enquanto a partida ainda n tiver finalizada
    public Partida(int idPartida, int idJogo, int idJogadorFinalizou){
        this.idPartida = idPartida;
        this.idJogo = idJogo;
        this.idJogadorFinalizou = idJogadorFinalizou;
    }
    public Partida(int idPartida, int idJogo){
        this.idPartida = idPartida;
        this.idJogo = idJogo;
    }
    public Partida(int idJogo){
        this.idJogo = idJogo;
    }

    public int getIdJogadorFinalizou() {
        return idJogadorFinalizou;
    }

    public void setIdJogadorFinalizou(int idJogadorFinalizou) {
        this.idJogadorFinalizou = idJogadorFinalizou;
    }

    public int getIdJogo() {
        return idJogo;
    }

    public void setIdJogo(int idJogo) {
        this.idJogo = idJogo;
    }

    public int getIdPartida() {
        return idPartida;
    }

    public void setIdPartida(int idPartida) {
        this.idPartida = idPartida;
    }

    @Override
    public String toString(){
        return "Partida id= "+ idPartida + ", Jogo id= "+ idJogo ;
    }
}
/*Details:
Programmer: Lorran
Date: 10/11 (first version)
 */