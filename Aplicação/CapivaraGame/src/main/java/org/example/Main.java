package org.example;
import dao.JogadorDAO;
import dao.JogoDAO;
import modeloTabelas.Jogador;
import modeloTabelas.Jogo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5454/capivaragame";//meninos, pra voces provavelmente a porta será 5432- por problemas locais a minha é diferente
        String usuario = "postgres";
        String senha = "minhasenha";

        System.out.println("Conectando ao banco de dados...");

        try(Connection conexao = DriverManager.getConnection(url, usuario, senha)){
            System.out.println("Conexão estabelecida!");
            Scanner input = new Scanner(System.in);
            JogoDAO jogoDAO = new JogoDAO();
            System.out.println("Criação de novo Jogo...");
            Jogo novoJogo = jogoDAO.criarJogo(conexao);

            int quantidadeJogadores = 0;

            verificarSeCriou(novoJogo, "Jogo");
            verificarSeCriouUsandoGenerico(novoJogo, "Jogo");

            //iniciando criacao dos jogadores
            while(quantidadeJogadores < 2 || quantidadeJogadores >4){
                System.out.println("Digite a quantidade de Jogadores: ");
                quantidadeJogadores = input.nextInt();
                if(quantidadeJogadores >4 || quantidadeJogadores < 2){
                    System.out.println("Valor inválido! Digite uma quantidade de jogadores de no minimo 2 e no maximo 4!");
                }
            }

            JogadorDAO jogadorDAO = new JogadorDAO();
            System.out.println("Criando "+quantidadeJogadores+ " jogadores no jogo "+novoJogo);
            for(int i=1; i<=quantidadeJogadores; i++){
                Jogador objetoJogador = new Jogador(i, novoJogo.getIdJogo());
                Jogador novoJogador = jogadorDAO.criarJogador(conexao, objetoJogador);
                verificarSeCriouUsandoGenerico(novoJogador, "Jogador" + i);
            }
            System.out.println("Jogadores criados com sucesso!");

            // primeiro estou fazendo o geralzao e depois vou implementar a lógica de iniciar jogo / escolher quantos jogadores




        } catch(SQLException e){
            System.out.println("Falha no banco de dados");
            e.printStackTrace();
        }
    }
    //usando a objecti
    public static void verificarSeCriou(Object objeto, String nomeTipo){
        if(objeto!= null){
            System.out.println(nomeTipo + " criado com sucesso!");
            System.out.println("Detalhes do "+ nomeTipo + ": "+ objeto);
        } else{
            System.out.println("ERRO: retornou NULL");
        }
    }
    //usando metodo generico(solicitado para LPOO)
    public static <T> boolean verificarSeCriouUsandoGenerico( T objeto, String nomeTipo){
        if(objeto != null){
            System.out.println(nomeTipo + " criado com sucesso!");
            System.out.println("Detalhes do "+ nomeTipo + ": "+ objeto);
            return true;
        }else{
            System.out.println("ERRO: retornou NULL");
            return false;
        }
    }
}