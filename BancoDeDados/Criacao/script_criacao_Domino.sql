-- Script de Criacao do Banco de dados, Schema Domino e tabelas --
-- Programmer: Lorran Kaique
-- Date: 03/11/2025

CREATE DATABASE CapivaraGame;
CREATE SCHEMA domino;
--Criacao tabelas
create table domino.Jogo (
	idJogo SERIAL PRIMARY KEY
);
create table domino.Peca (
	idPeca SERIAL PRIMARY KEY,
	valorLado1 Integer NOT NULL,
	valorLado2 Integer NOT NULL
);

create table domino.Jogador (
	idJogador SERIAL PRIMARY KEY,
	posicao Integer NOT NULL,
	pontuacao Integer DEFAULT 0,
	idJogo Integer REFERENCES domino.Jogo(idJogo)
);

create table domino.Dupla (
	idDupla SERIAL Primary Key,
	idJogador1 Integer REFERENCES domino.Jogador(idJogador),
	idJogador2 Integer REFERENCES domino.Jogador(idJogador)
);

create table domino.Partida (
	idPartida SERIAL PRIMARY KEY,
	idJogo Integer REFERENCES domino.Jogo(idJogo),
	idJogadorFinalizou Integer REFERENCES domino.Jogador(idJogador)
);

create table domino.Monte (
	idMonte SERIAL PRIMARY KEY,
	idPartida Integer REFERENCES domino.Partida(idPartida)
);

create table domino.Jogada (
	idJogada SERIAL PRIMARY KEY,
	ordem Integer NOT NULL,
	idPartida Integer REFERENCES domino.Partida(idPartida),
	idJogador Integer REFERENCES domino.Jogador(idJogador),
	acao Integer NOT NULL CHECK (acao IN (1, 2, 3, 4)), -- Jogou = 1; Comprou = 2; Passou = 3; Distribuiu = 4;
	idPeca Integer REFERENCES domino.Peca(idPeca),
	ladoUtilizado Integer
)
