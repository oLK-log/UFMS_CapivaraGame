package modeloTabelas;

public class Peca {
    private int idPeca;
    private int valorLado1;
    private int valorLado2;

    public Peca(int idPeca, int valorLado1, int valorLado2){
        this.idPeca = idPeca;
        this.valorLado1 = valorLado1;
        this.valorLado2 = valorLado2;
    }

    public Peca(int valorLado1, int valorLado2){
        this.valorLado1 = valorLado1;
        this.valorLado2 = valorLado2;
    }

    public int getIdPeca() {
        return idPeca;
    }

    public void setIdPeca(int idPeca) {
        this.idPeca = idPeca;
    }

    public int getValorLado1() {
        return valorLado1;
    }

    public int getValorLado2() {
        return valorLado2;
    }

    @Override
    public String toString(){
        return "Peça id= " + idPeca + " [" + valorLado1 + "|" + valorLado2 + "]";
    }
}
