package dao;
import modeloTabelas.Jogador;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JogadorDAO {
    public Jogador criarJogador(Connection conexao, Jogador jogador) throws SQLException {
        String sql = "INSERT INTO domino.jogador (posicao, idJogo) VALUES (?, ?) RETURNING idJogador";

        try(PreparedStatement stmt = conexao.prepareStatement(sql)){
            stmt.setInt(1, jogador.getPosicao());
            stmt.setInt(2, jogador.getIdJogo());

            ResultSet resultado = stmt.executeQuery();
            if(resultado.next()){
                int idGerado = resultado.getInt("idJogador");
                jogador.setIdJogador(idGerado);
                return jogador;
            }
        }
        return null;
    }
}

/*Details:
Programmer: Lorran
Date: 09/11 (first version)
 */