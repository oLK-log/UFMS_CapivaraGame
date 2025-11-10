package org.example;
import dao.JogoDAO;
import modeloTabelas.Jogo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5454/capivaragame";//meninos, pra voces provavelmente a porta será 5432- por problemas locais a minha é diferente
        String usuario = "postgres";
        String senha = "minhasenha";

        System.out.println("Conectando ao banco de dados...");

        try(Connection conexao = DriverManager.getConnection(url, usuario, senha)){
            System.out.println("Conexão estabelecida!");

            JogoDAO jogoDAO = new JogoDAO();
            System.out.println("Criação de novo Jogo...");
            Jogo novoJogo = jogoDAO.criarJogo(conexao);

            if(novoJogo!=null){
                System.out.println("\n Jogo criado com sucesso!");
                System.out.println("Detalhes do jogo:"+ novoJogo);
            }else{
                System.out.println("ERRO: retornou NULL");
            }
        } catch(SQLException e){
            System.out.println("Falha no banco de dados");
            e.printStackTrace();
        }
    }
}