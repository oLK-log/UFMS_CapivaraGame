package modeloTabelas;

public class Jogo {
    private int idJogo;
    public Jogo(){

    }
    public int getIdJogo(){
        return idJogo;
    }
    public void setIdJogo(int idJogo){
        this.idJogo = idJogo;
    }

    @Override
    public String toString(){
        return "Id" + idJogo;
    }
}
