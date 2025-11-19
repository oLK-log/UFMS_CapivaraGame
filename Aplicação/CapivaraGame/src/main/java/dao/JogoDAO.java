package dao;
import modeloTabelas.Jogo;
import java.sql.*;

public class JogoDAO {
    public Jogo criarJogo(Connection conexao) throws SQLException {
        String sql = "Insert INTO domino.Jogo DEFAULT VALUES RETURNING idJogo";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)){
            ResultSet resultado = stmt.executeQuery();

            if(resultado.next()){
                int idGerado = resultado.getInt("idJogo");
                return new Jogo(idGerado);
            }
        }
        return null;
    }
}
/*Details:
Programmer: Lorran
Date: 09/11 (first version)
 */