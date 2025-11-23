package modeloTabelas;

public class Monte {
    private int idMonte;
    private int idPartida;

    public Monte(int idPartida){
        this.idPartida = idPartida;
    }

    public Monte(int idPartida, int idMonte){
        this.idPartida = idPartida;
        this.idMonte = idMonte;
    }

    public int getIdPartida() {
        return idPartida;
    }

    public void setIdPartida(int idPartida) {
        this.idPartida = idPartida;
    }

    public int getIdMonte() {
        return idMonte;
    }

    public void setIdMonte(int idMonte) {
        this.idMonte = idMonte;
    }

    @Override
    public String toString(){
        return "Monte id = "+ idMonte + " na partida id = "+ idPartida;
    }
}
/*Details:
Programmer: Lorran
Date: 18/11 (first version)
 */