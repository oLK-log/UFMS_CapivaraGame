package dao;
import modeloTabelas.Jogada;
import java.sql.*;

public class JogadaDAO {

    public Jogada criarJogada(Connection conexao, Jogada jogada) throws SQLException {
        String sql = "INSERT INTO domino.Jogada " +
                "(ordem, idPartida, idJogador, acao, idPeca, ladoUtilizado) " +
                "VALUES (?, ?, ?, ?, ?, ?) RETURNING idJogada";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, jogada.getOrdem());
            stmt.setInt(2, jogada.getIdPartida());
            stmt.setInt(3, jogada.getIdJogador());
            stmt.setInt(4, jogada.getAcao());

            if (jogada.getIdPeca() != null)
                stmt.setInt(5, jogada.getIdPeca());
            else
                stmt.setNull(5, Types.INTEGER);

            if (jogada.getLadoUtilizado() != null)
                stmt.setInt(6, jogada.getLadoUtilizado());
            else
                stmt.setNull(6, Types.INTEGER);

            ResultSet resultado = stmt.executeQuery();

            if (resultado.next()) {
                jogada.setIdJogada(resultado.getInt("idJogada"));
                return jogada;
            }
        }
        return null;
    }
    public void jogarPeca(Connection conexao, int idPartida, int idJogador, int idPeca, int lado, Integer pontaMesa) throws SQLException {
        String sql = "CALL domino.validar_jogada(?, ?, ?, ?, ?)";
        try (CallableStatement cs = conexao.prepareCall(sql)) {
            cs.setInt(1, idPartida);
            cs.setInt(2, idJogador);
            cs.setInt(3, idPeca);
            cs.setInt(4, lado);

            if(pontaMesa == null){
                cs.setNull(5, Types.INTEGER);
            }else{
                cs.setInt(5, pontaMesa);
            }
            cs.execute();
        }
    }
    public java.util.List<modeloTabelas.Peca> buscarMaoDoJogador(Connection conexao, int idPartida, int idJogador) throws SQLException {
        java.util.List<modeloTabelas.Peca> mao = new java.util.ArrayList<>();
        String sql = """
            SELECT p.* FROM domino.peca p
            JOIN domino.jogada j ON j.idpeca = p.idpeca
            WHERE j.idpartida = ? AND j.idjogador = ?
              AND j.acao IN (2, 4) 
              AND NOT EXISTS (
                  SELECT 1 FROM domino.jogada j2 
                  WHERE j2.idpeca = p.idpeca 
                    AND j2.idpartida = j.idpartida 
                    AND j2.acao = 1 
              )
            ORDER BY p.idpeca
        """;
        try(PreparedStatement stmt = conexao.prepareStatement(sql)){
            stmt.setInt(1, idPartida);
            stmt.setInt(2, idJogador);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                modeloTabelas.Peca p = new modeloTabelas.Peca(
                        rs.getInt("idpeca"),
                        rs.getInt("valorlado1"),
                        rs.getInt("valorlado2")
                );
                mao.add(p);
            }
        }
        return mao;
    }
}
/*Details:
Programmer: Sergio
Date: 18/11 (first version)
Programmer: Lorran
Date : 23/11 (second version)
 */