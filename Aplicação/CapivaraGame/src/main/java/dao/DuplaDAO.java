package dao;
import modeloTabelas.Dupla;
import java.sql.*;


public class DuplaDAO {
    public Dupla criarDupla(Connection conexao, Dupla dupla) throws SQLException {
        String inserirDupla = "Insert into domino.dupla (idJogador1, idJogador2) VALUES (?, ?) RETURNING idDupla";

        try(PreparedStatement stmt = conexao.prepareStatement(inserirDupla)){
            stmt.setInt(1, dupla.getIdJoagador1());
            stmt.setInt(2, dupla.getGetIdJoagador2());

            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                dupla.setIdDupla(rs.getInt("idDupla"));
                return dupla;
            }
        }
        return null;
    }
}/*Details:
Programmer: Lorran
Date: 18/11 (first version)
 */
