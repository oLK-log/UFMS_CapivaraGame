package DAO;
import modeloTabelas.Jogo;
import java.sql.*;

public class JogoDAO {
    public Jogo criaJogo(Connection conexao) throws SQLException {
        String sql = "Insert INTO Domino.Jogo DEFAULT VALUES RETURNING idJogo";
        try (PreparedStatement stmt = conexao.prepareStratement(sql)){

        }
    }
}
