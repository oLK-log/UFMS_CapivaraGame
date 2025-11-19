package dao;
import modeloTabelas.Monte;
import java.sql.*;

public class MonteDAO {
    public Monte criarMonte(Connection conexao, Monte monte) throws SQLException{
        String inserirMonte = "insert into domino.monte (idPartida) VALUES (?) RETURNING idMonte";

        try(PreparedStatement stmt = conexao.prepareStatement(inserirMonte)){
            stmt.setInt(1, monte.getIdPartida());

            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                monte.setIdMonte(rs.getInt("idMonte"));
                return monte;
            }
        }
        return null;
    }

}
/*Details:
Programmer: Lorran
Date: 18/11 (first version)
 */