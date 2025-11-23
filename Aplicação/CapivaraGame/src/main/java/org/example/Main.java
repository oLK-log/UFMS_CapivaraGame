package org.example;
import dao.JogadorDAO;
import dao.JogoDAO;
import dao.PartidaDAO;
import dao.PecaDAO;
import dao.DuplaDAO;
import dao.MonteDAO;
import dao.JogadaDAO;

import modeloTabelas.Jogador;
import modeloTabelas.Jogo;
import modeloTabelas.Partida;
import modeloTabelas.Peca;
import modeloTabelas.Dupla;
import modeloTabelas.Monte;
import modeloTabelas.Jogada;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Collections;

public class Main {
    public static void main(String[] args) {
        System.out.println("----------------------------------------------------------------------------------");
        System.out.println("--------------------------Seja bem Vindo ao CapivaraGame--------------------------");
        System.out.println("                           Você escolheu o Jogo DOMINÓ.");
        String url = "jdbc:postgresql://localhost:5454/capivaragame";//meninos, pra voces provavelmente a porta será 5432- por problemas locais a minha é diferente
        String usuario = "postgres";//verifiquem se no de vcs eh a mesma
        String senha = "minhasenha";//verifiquem se no de vcs eh a mesma
        System.out.println("----------------------------------------------------------------------------------");
        System.out.println("                                Iniciando Jogo");
        System.out.println("                         Conectando ao banco de dados...");

        try(Connection conexao = DriverManager.getConnection(url, usuario, senha)){
            System.out.println("                             Conexão estabelecida!");
            Scanner input = new Scanner(System.in);
            JogoDAO jogoDAO = new JogoDAO();
            Jogo novoJogo = jogoDAO.criarJogo(conexao);
            //verificarSeCriou(novoJogo, "Jogo");
            verificarSeCriouUsandoGenerico(novoJogo, "Jogo");
            System.out.println("                         Criação de novo Jogo Feita ...");
            //iniciando criacao dos jogadores
            List<Jogador> listaDeJogadores = new ArrayList<>();
            listaDeJogadores = criarJogadores(conexao, input, novoJogo);
            System.out.println("\n                          Jogadores criados com sucesso!\n");

            // primeiro estou fazendo o geralzao e depois vou implementar a lógica de iniciar jogo / escolher quantos jogadores
            //Pecas
            PecaDAO pecaDAO = new PecaDAO();
            pecaDAO.inicializarPecas(conexao);
            List<Peca> listaDePecas = pecaDAO.listarTodasPecas(conexao);
            System.out.println("\n                        " + listaDePecas.size() + " Pecas criadas com sucesso!\n");

            System.out.println("----------------------------------------------------------------------------------");
            //Partidas
            //antes de iniciar a partida ou na primeira partida teriamos que distribuir as cartas?
            System.out.println("Iniciando partidas");
            PartidaDAO partidaDAO = new PartidaDAO();
            Partida novaPartida = new Partida(novoJogo.getIdJogo());
            partidaDAO.criarPartida(conexao, novaPartida);

            if(verificarSeCriouUsandoGenerico(novaPartida, "Partida 1")){
                MonteDAO monteDAO = new MonteDAO();
                Monte novoMonte = new Monte(novaPartida.getIdPartida());
                monteDAO.criarMonte(conexao, novoMonte);
                System.out.println("\n                          Embaralhando e distribuindo!\n");
                Collections.shuffle(listaDePecas);
                //primeira jogada(ou a 0) onde é distribuido
                JogadaDAO jogadaDAO = new JogadaDAO();
                int indicePeca = 0;
                System.out.println("\n                          Distribuindo 7 pecas para cada jogador!\n");
                for(Jogador jogador : listaDeJogadores){
                    for(int k=0; k<7; k++){
                        Peca pecaUsada = listaDePecas.get(indicePeca);
                        //È preciso salvar o registro
                        Jogada distribuicao = new Jogada(0, novaPartida.getIdPartida(), jogador.getIdJogador(), 4, pecaUsada.getIdPeca(), null);
                        jogadaDAO.criarJogada(conexao, distribuicao);
                        indicePeca++;
                    }
                }


                System.out.println("                          Pecas distribuidas\n                          Pecas restantes no Monte: "+ (listaDePecas.size() - indicePeca));
            }

        } catch(SQLException e){
            System.out.println("Falha no banco de dados");
            e.printStackTrace();
        }
    }
    public static List<Jogador> criarJogadores(Connection conexao, Scanner input, Jogo novoJogo) throws SQLException{
        List<Jogador> listaDeJogadores = new ArrayList<>();
        int quantidadeJogadores = 0;
        while(quantidadeJogadores < 2 || quantidadeJogadores >4){
            System.out.println("Digite a quantidade de Jogadores: ");
            try{
                if(input.hasNextInt()){
                    quantidadeJogadores = input.nextInt();
                    input.nextLine();
                } else{
                    input.nextLine();
                }
            } catch (Exception e){
                quantidadeJogadores = 0;
            }
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
            listaDeJogadores.add(novoJogador);
        }
        return listaDeJogadores;
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
    //Criar objetos

    //usando metodo generico(solicitado para LPOO)
    public static <T> boolean verificarSeCriouUsandoGenerico( T objeto, String nomeTipo){
        if(objeto != null){
            System.out.println("---------------------------"+nomeTipo + " criado com sucesso!"+"-------------------------------");
            System.out.println("Detalhes do "+ nomeTipo + ": "+ objeto);
            return true;
        }else{
            System.out.println("ERRO: retornou NULL");
            return false;
        }
    }
}
/*Details:
Programmer: Lorran
Date: 09/11 (first version)
 */