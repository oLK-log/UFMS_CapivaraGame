package org.example;
import dao.*;

import modeloTabelas.*;

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

            //Lógica de formar duplas
            if(listaDeJogadores.size() == 4){
                System.out.println("----------------------------------------------------------------------------------");
                System.out.println("                               Formando Duplas...");

                DuplaDAO duplaDAO = new DuplaDAO();
                Jogador p1 = listaDeJogadores.get(0);
                Jogador p2 = listaDeJogadores.get(1);
                Jogador p3 = listaDeJogadores.get(2);
                Jogador p4 = listaDeJogadores.get(3);
                Dupla dupla1 = new Dupla(p1.getIdJogador(), p3.getIdJogador());
                duplaDAO.criarDupla(conexao, dupla1);
                System.out.println("Dupla formada: Jogador " + p1.getIdJogador() + " e Jogador " + p3.getIdJogador());
                Dupla dupla2 = new Dupla(p2.getIdJogador(), p4.getIdJogador());
                duplaDAO.criarDupla(conexao, dupla2);
                System.out.println("Dupla formada: Jogador " + p2.getIdJogador() + " e Jogador " + p4.getIdJogador());
                System.out.println("----------------------------------------------------------------------------------");
            }

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
                int idJogadorQueComeca = -1;
                int idPecaB = -1;


                System.out.println("\n                          Distribuindo 7 pecas para cada jogador!\n");
                for(Jogador jogador : listaDeJogadores){
                    for(int k=0; k<7; k++){
                        Peca pecaUsada = listaDePecas.get(indicePeca);
                        //logica para peca 6-6
                        if(pecaUsada.getValorLado1() == 6 && pecaUsada.getValorLado2() == 6){
                            idJogadorQueComeca = jogador.getIdJogador();
                            idPecaB = pecaUsada.getIdPeca();
                            System.out.println("\n                          A peca 6-6 está com " + jogador.getIdJogador()+"!");
                        }
                        //È preciso salvar o registro
                        Jogada distribuicao = new Jogada(0, novaPartida.getIdPartida(), jogador.getIdJogador(), 4, pecaUsada.getIdPeca(), null);
                        jogadaDAO.criarJogada(conexao, distribuicao);
                        indicePeca++;
                    }
                }
                System.out.println("                          Pecas distribuidas\n                          Pecas restantes no Monte: "+ (listaDePecas.size() - indicePeca));
                if(idJogadorQueComeca != -1 && idPecaB != -1){
                    System.out.println("Jogador "+idJogadorQueComeca+" comeca a partida");
                    try{
                        jogadaDAO.jogarPeca(conexao, novaPartida.getIdPartida(), idJogadorQueComeca, idPecaB, 6, null);
                    }catch(SQLException e){
                        System.out.println("Erro ao registrar jogada" + e.getMessage());
                    }
                }

                //Jogadas
                int pontaEsquedaMesa = 6;
                int pontaDiretaMesa = 6;

                int posJogadorAtual = -1;
                for(int i=0; i<listaDeJogadores.size(); i++){
                    if(listaDeJogadores.get(i).getIdJogador() == idJogadorQueComeca){
                        posJogadorAtual = i;
                        break;
                    }
                }
                posJogadorAtual = (posJogadorAtual + 1) % listaDeJogadores.size();
                boolean jogoAcabou = false;

                System.out.println("\nINICIANDO JOGADAS!!!\n");
                while(!jogoAcabou){
                    Jogador jogadorDaVez = listaDeJogadores.get(posJogadorAtual);
                    System.out.println("Mesa atual: "+ pontaEsquedaMesa + " - "+ pontaDiretaMesa);
                    System.out.println("Vez do Jogador "+ jogadorDaVez.getIdJogador() + " da posicao "+ jogadorDaVez.getPosicao());

                    JogadaDAO jogadaDaoLoop = new JogadaDAO();

                    List<Peca> maoDoJogador = jogadaDaoLoop.buscarMaoDoJogador(conexao, novaPartida.getIdPartida(), jogadorDaVez.getIdJogador());
                    System.out.println("Sua mão: ");
                    for(int i=0; i<maoDoJogador.size(); i++){
                        System.out.println(i + " = " + maoDoJogador.get(i));
                    }

                    System.out.println("Passando a vez");
                    posJogadorAtual = (posJogadorAtual + 1) % listaDeJogadores.size();
                    break;
                }
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