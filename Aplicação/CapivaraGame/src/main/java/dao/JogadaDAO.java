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
        String url = "jdbc:postgresql://localhost:5454/capivaragame";
        String usuario = "postgres";
        String senha = "minhasenha";

        System.out.println("                                Iniciando Jogo");
        System.out.println("                         Conectando ao banco de dados...");
        System.out.println("----------------------------------------------------------------------------------");

        try(Connection conexao = DriverManager.getConnection(url, usuario, senha)){

            System.out.println("                             Conexão estabelecida!");
            Scanner input = new Scanner(System.in);

            JogoDAO jogoDAO = new JogoDAO();
            Jogo novoJogo = jogoDAO.criarJogo(conexao);

            verificarSeCriouUsandoGenerico(novoJogo, "Jogo");
            System.out.println("                         Criação de novo Jogo Feita ...");

            List<Jogador> listaDeJogadores = criarJogadores(conexao, input, novoJogo);
            System.out.println("\n                          Jogadores criados com sucesso!\n");

            // Pecas
            PecaDAO pecaDAO = new PecaDAO();
            pecaDAO.inicializarPecas(conexao);
            List<Peca> listaDePecas = pecaDAO.listarTodasPecas(conexao);

            System.out.println("\n                        " + listaDePecas.size() + " Pecas criadas com sucesso!\n");

            System.out.println("----------------------------------------------------------------------------------");

            // Partida
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

                JogadaDAO jogadaDAO = new JogadaDAO();
                int indicePeca = 0;

                System.out.println("\n                          Distribuindo 7 pecas para cada jogador!\n");

                for(Jogador jogador : listaDeJogadores){
                    for(int k=0; k<7; k++){
                        Peca pecaUsada = listaDePecas.get(indicePeca);

                        Jogada distribuicao = new Jogada(
                                0,
                                novaPartida.getIdPartida(),
                                jogador.getIdJogador(),
                                4,
                                pecaUsada.getIdPeca(),
                                null
                        );

                        jogadaDAO.criarJogada(conexao, distribuicao);
                        indicePeca++;
                    }
                }

                System.out.println("                          Pecas distribuidas\n                          Pecas restantes no Monte: "+ (listaDePecas.size() - indicePeca));

                // =============================
                //    JOGADA DO 6-6 AUTOMÁTICA
                // =============================

                Jogador primeiro = acharQuemTemDouble6(conexao, novaPartida);

                if (primeiro != null) {
                    jogarDouble6(conexao, novaPartida, primeiro);
                    System.out.println("\nPrimeira jogada automática: o jogador "
                            + primeiro.getIdJogador() + " iniciou com 6-6.\n");
                }
            }

        } catch(SQLException e){
            System.out.println("Falha no banco de dados");
            e.printStackTrace();
        }
    }

    // ======================================================================
    // MÉTODO: CRIAR JOGADORES (SEU ORIGINAL)
    // ======================================================================
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

    // ======================================================================
    // MÉTODO NOVO 1: ACHAR JOGADOR QUE POSSUI A PEÇA 6–6
    // ======================================================================
    public static Jogador acharQuemTemDouble6(Connection conexao, Partida partida) throws SQLException {
        String sql = """
                SELECT jog.idJogador
                FROM domino.jogada jog
                JOIN domino.peca p ON p.idPeca = jog.idPeca
                WHERE jog.idPartida = ?
                  AND jog.acao = 4
                  AND p.valorLado1 = 6
                  AND p.valorLado2 = 6
                LIMIT 1;
                """;

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, partida.getIdPartida());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int idJogador = rs.getInt("idJogador");
                return new Jogador(idJogador, partida.getIdJogo());
            }
        }
        return null;
    }

    // ======================================================================
    // MÉTODO NOVO 2: INSERIR JOGADA AUTOMÁTICA DO 6–6
    // ======================================================================
    public static void jogarDouble6(Connection conexao, Partida partida, Jogador jogador) throws SQLException {

        String pegarPeca = """
                SELECT idPeca
                FROM domino.peca
                WHERE valorLado1 = 6 AND valorLado2 = 6
                LIMIT 1;
                """;

        Integer idPeca = null;

        try (PreparedStatement stmt = conexao.prepareStatement(pegarPeca)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                idPeca = rs.getInt("idPeca");
            }
        }

        if (idPeca == null) return;

        String sql = """
                INSERT INTO domino.jogada 
                (ordem, idPartida, idJogador, acao, idPeca, ladoUtilizado)
                VALUES (
                    (SELECT COALESCE(MAX(ordem),0)+1 FROM domino.jogada WHERE idPartida = ?),
                    ?, ?, 1, ?, 0
                );
                """;

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, partida.getIdPartida());
            stmt.setInt(2, partida.getIdPartida());
            stmt.setInt(3, jogador.getIdJogador());
            stmt.setInt(4, idPeca);
            stmt.executeUpdate();
        }
    }

    // ======================================================================
    // MÉTODOS DE VERIFICAÇÃO (SEUS ORIGINAIS)
    // ======================================================================
    public static void verificarSeCriou(Object objeto, String nomeTipo){
        if(objeto!= null){
            System.out.println(nomeTipo + " criado com sucesso!");
            System.out.println("Detalhes do "+ nomeTipo + ": "+ objeto);
        } else{
            System.out.println("ERRO: retornou NULL");
        }
    }

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

Programmer: Sérgio
Date: 20/11 - 23/11 (second version)
 */
