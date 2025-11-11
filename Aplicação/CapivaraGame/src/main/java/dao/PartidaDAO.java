package dao;
import modeloTabelas.Partida;
import java.sql.*;

public class PartidaDAO{
    public Partida criarPartida(Connection conexao, Partida partida) throws SQLException {
        String sql = "Insert INTO domino.Partida (idJogo) VALUES (?) RETURNING idPartida";
        try(PreparedStatement stmt = conexao.prepareStatement(sql)){
            stmt.setInt(1, partida.getIdJogo());
            ResultSet resultado = stmt.executeQuery();

            if(resultado.next()){
                int idGerado = resultado.getInt("idPartida");
                partida.setIdPartida(idGerado);
                return partida;
            }
        }
        return null;
    }
}
