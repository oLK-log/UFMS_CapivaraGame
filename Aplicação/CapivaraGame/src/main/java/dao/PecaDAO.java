package dao;

import modeloTabelas.Peca;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PecaDAO {
    public Peca criarPeca(Connection conexao, Peca peca) throws SQLException {
        String sql = "INSERT INTO domino.peca (valorLado1, valorLado2) " +
                "VALUES (?, ?) RETURNING idPeca";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, peca.getValorLado1());
            stmt.setInt(2, peca.getValorLado2());

            ResultSet resultado = stmt.executeQuery();

            if (resultado.next()) {
                int idGerado = resultado.getInt("idPeca");
                peca.setIdPeca(idGerado);
                return peca;
            }
        }

        return null;
    }

    //Sergio, do modo que você fez, era criada apenas uma peça
    //Meu pensamento foi de já deixar as pecasd criadas no inicioa do JOGO e apenas irmos usando elas
    public void inicializarPecas(Connection conexao) throws SQLException {
        System.out.println("Criando Peças...");
        String sqlVerificaSeExistePecaCriada = "SELECT COUNT(*) FROM domino.peca";
        try (Statement stmt = conexao.createStatement() ; ResultSet rs = stmt.executeQuery(sqlVerificaSeExistePecaCriada)) {
            if(rs.next() && rs.getInt(1)>0) {
                return;
            }
        }
        System.out.println("Adicionando valor as peças, tenha paciência...");
        for(int i=0; i<=6; i++){
            for(int j=0; j<=6; j++){
                Peca novaPeca = new Peca(i, j);
                criarPeca(conexao, novaPeca);
            }
        }
        System.out.println("As peças foram criadas com sucesso!");
    }

    //Sergio, outro detalhe eh que a cada partida precisamos embaralhar as pecas
    //acho interessante ja fazer isso como um metodo da propra classe pra ja pegar as pecas para embaralhar
    public List<Peca> listarTodasPecas(Connection conexao) throws SQLException {
        List<Peca> listaDePecas = new ArrayList<>();
        String selecionaTodasPecas = "SELECT * FROM domino.peca";

        try(Statement stmt = conexao.createStatement();ResultSet rs = stmt.executeQuery(selecionaTodasPecas)) {
            while(rs.next()){
                Peca peca = new Peca(rs.getInt("IdPeca"), rs.getInt("valorLado1"), rs.getInt("valorLado2"));
                listaDePecas.add(peca);
            }
        }
        return listaDePecas;
    }
}
/*Details:
Programmer: Sergio
Date: 18/11 (first version)
- Second version
Lorran
date: 18/11
 */