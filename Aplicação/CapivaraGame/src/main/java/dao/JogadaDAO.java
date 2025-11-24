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
}
/*Details:
Programmer: Sergio
Date: 18/11 (first version)
Programmer: Lorran
Date : 23/11 (second version)
 */