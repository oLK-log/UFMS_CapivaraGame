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
}
