package modeloTabelas;

public class Dupla {
    private int idDupla;
    private int idJoagador1;
    private int getIdJoagador2;

    public Dupla(int idJogador1, int idJoagador2){
        this.idJoagador1 = idJogador1;
        this.getIdJoagador2 = idJoagador2;
    }

    public Dupla(int idJoagador1, int idJoagador2, int idDupla){
        this.idJoagador1 = idJoagador1;
        this.getIdJoagador2 = idJoagador2;
        this.idDupla = idDupla;
    }

    public int getGetIdJoagador2() {
        return getIdJoagador2;
    }

    public void setGetIdJoagador2(int getIdJoagador2) {
        this.getIdJoagador2 = getIdJoagador2;
    }

    public int getIdJoagador1() {
        return idJoagador1;
    }

    public void setIdJoagador1(int idJoagador1) {
        this.idJoagador1 = idJoagador1;
    }

    public int getIdDupla() {
        return idDupla;
    }

    public void setIdDupla(int idDupla) {
        this.idDupla = idDupla;
    }

    @Override
    public String toString(){
        return "Dupla de id = "+ idDupla + " formada pelo Jogador " + idJoagador1 + " e " + getIdJoagador2;
    }
}
/*Details:
Programmer: Lorran
Date: 18/11 (first version)
 */