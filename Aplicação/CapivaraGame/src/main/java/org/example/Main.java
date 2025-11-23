package org.example;

import dao.*;
import modeloTabelas.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        System.out.println("-----------------------------------------------------------");
        System.out.println("------------------- CAPIVARAGAME - DOMINÓ ------------------");
        System.out.println("-----------------------------------------------------------");

        String url = "jdbc:postgresql://localhost:5454/capivaragame";
        String usuario = "postgres";
        String senha = "minhasenha";

        try (Connection conexao = DriverManager.getConnection(url, usuario, senha)) {
            System.out.println("Conectado ao banco!");

            Scanner input = new Scanner(System.in);
            JogoDAO jogoDAO = new JogoDAO();

            Jogo novoJogo = jogoDAO.criarJogo(conexao);
            verificarSeCriouUsandoGenerico(novoJogo, "Jogo");

            // jogadores
            List<Jogador> listaJogadores = criarJogadores(conexao, input, novoJogo);
            System.out.println("Jogadores criados!");

            // peças
            PecaDAO pecaDAO = new PecaDAO();
            pecaDAO.inicializarPecas(conexao);
            List<Peca> todasPecas = pecaDAO.listarTodasPecas(conexao);

            // partida
            PartidaDAO partidaDAO = new PartidaDAO();
            Partida partida = new Partida(novoJogo.getIdJogo());
            partidaDAO.criarPartida(conexao, partida);
            verificarSeCriouUsandoGenerico(partida, "Partida");

            // monte
            MonteDAO monteDAO = new MonteDAO();
            Monte monte = new Monte(partida.getIdPartida());
            monteDAO.criarMonte(conexao, monte);

            // distribuir
            Collections.shuffle(todasPecas);
            JogadaDAO jogadaDAO = new JogadaDAO();
            int idx = 0;

            System.out.println("Distribuindo 7 peças para cada jogador...");

            for (Jogador jogador : listaJogadores) {
                for (int k = 0; k < 7; k++) {
                    Peca p = todasPecas.get(idx);
                    jogadaDAO.criarJogada(conexao,
                            new Jogada(0, partida.getIdPartida(), jogador.getIdJogador(), 4, p.getIdPeca(), null));
                    idx++;
                }
            }

            System.out.println("Distribuição feita.");
            System.out.println("Peças restantes no monte: " + (todasPecas.size() - idx));

            // achar o jogador que tem a peça 6-6
            Jogador primeiro = acharQuemTemDouble6(conexao, partida);

            if (primeiro == null) {
                System.out.println("Ninguém recebeu o 6-6.");
            } else {
                System.out.println("Jogador que começa: Jogador " + primeiro.getIdJogador());
                jogarDouble6(conexao, partida, primeiro);
            }

        } catch (SQLException e) {
            System.out.println("Erro no banco:");
            e.printStackTrace();
        }
    }

    // ---------------------------- seus métodos ----------------------------

    public static List<Jogador> criarJogadores(Connection conexao, Scanner input, Jogo novoJogo) throws SQLException {
        List<Jogador> listaDeJogadores = new ArrayList<>();
        int quantidadeJogadores = 0;

        while (quantidadeJogadores < 2 || quantidadeJogadores > 4) {
            System.out.println("Digite a quantidade de Jogadores: ");

            try {
                if (input.hasNextInt()) {
                    quantidadeJogadores = input.nextInt();
                    input.nextLine();
                } else {
                    input.nextLine();
                }
            } catch (Exception e) {
                quantidadeJogadores = 0;
            }

            if (quantidadeJogadores > 4 || quantidadeJogadores < 2) {
                System.out.println("Valor inválido! Digite entre 2 e 4.");
            }
        }

        JogadorDAO jogadorDAO = new JogadorDAO();
        System.out.println("Criando " + quantidadeJogadores + " jogadores no jogo " + novoJogo);

        for (int i = 1; i <= quantidadeJogadores; i++) {
            Jogador objeto = new Jogador(i, novoJogo.getIdJogo());
            Jogador novoJogador = jogadorDAO.criarJogador(conexao, objeto);
            verificarSeCriouUsandoGenerico(novoJogador, "Jogador" + i);
            listaDeJogadores.add(novoJogador);
        }

        return listaDeJogadores;
    }

    public static Jogador acharQuemTemDouble6(Connection c, Partida p) throws SQLException {
        String sql = "select j.idjogador from domino.jogada jog "
                + "join domino.jogador j on j.idjogador = jog.idjogador "
                + "where jog.idpartida = ? and jog.acao = 4 "
                + "and jog.idpeca = (select idpeca from domino.peca where valorLado1 = 6 and valorLado2 = 6 limit 1) "
                + "limit 1";

        PreparedStatement st = c.prepareStatement(sql);
        st.setInt(1, p.getIdPartida());
        ResultSet r = st.executeQuery();

        if (r.next()) {
            int id = r.getInt("idjogador");
            return new Jogador(id, p.getIdJogo());
        }

        return null;
    }

    public static void jogarDouble6(Connection c, Partida p, Jogador j) throws SQLException {
        String q = "select idpeca from domino.peca where valorLado1 = 6 and valorLado2 = 6 limit 1";
        PreparedStatement st = c.prepareStatement(q);
        ResultSet r = st.executeQuery();

        int idPeca = -1;
        if (r.next()) {
            idPeca = r.getInt("idpeca");
        }

        if (idPeca == -1) {
            System.out.println("não achei a peca 6-6");
            return;
        }

        String ins = "insert into domino.jogada (ordem, idpartida, idjogador, acao, idpeca) "
                + "values ((select coalesce(max(ordem),0)+1 from domino.jogada where idpartida=?), ?, ?, 1, ?)";

        PreparedStatement st2 = c.prepareStatement(ins);
        st2.setInt(1, p.getIdPartida());
        st2.setInt(2, p.getIdPartida());
        st2.setInt(3, j.getIdJogador());
        st2.setInt(4, idPeca);
        st2.executeUpdate();

        System.out.println("Jogador " + j.getIdJogador() + " jogou o 6-6 e começou o jogo.");
    }

    public static void verificarSeCriou(Object objeto, String nomeTipo) {
        if (objeto != null) {
            System.out.println(nomeTipo + " criado com sucesso!");
            System.out.println("Detalhes do " + nomeTipo + ": " + objeto);
        } else {
            System.out.println("ERRO: retornou NULL");
        }
    }

    public static <T> boolean verificarSeCriouUsandoGenerico(T objeto, String nomeTipo) {
        if (objeto != null) {
            System.out.println("---------------------------" + nomeTipo + " criado com sucesso!" + "-------------------------------");
            System.out.println("Detalhes do " + nomeTipo + ": " + objeto);
            return true;
        } else {
            System.out.println("ERRO: retornou NULL");
            return false;
        }
    }
}