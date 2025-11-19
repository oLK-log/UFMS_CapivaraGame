package dao;

import modeloTabelas.Peca;
import java.sql.*;

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
}
/*Details:
Programmer: Sergio
Date: 18/11 (first version)
 */