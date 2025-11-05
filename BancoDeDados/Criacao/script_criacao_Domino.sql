-- Script de Criacao do Banco de dados, Schema Domino e tabelas --
-- Programmer: Lorran Kaique
-- Date: 03/11/2025

CREATE DATABASE CapivaraGame;
CREATE SCHEMA Domino
--Criacao tabelas
create table Domino.Jogo (
	idJogo SERIAL PRIMARY KEY
);
create table Domino.Peca (
	idPeca SERIAL PRIMARY KEY,
	valorLado1 Integer NOT NULL,
	valorLado2 Integer NOT NULL
);

create table Domino.Jogador (
	idJogador SERIAL PRIMARY KEY,
	posicao Integer NOT NULL,
	pontuacao Integer DEFAULT 0,
	jogo Integer REFERENCES Domino.Jogo(idJogo)
);

create table Domino.Dupla (
	idDupla SERIAL Primary Key,
	Jogador1 Integer REFERENCES Domino.Jogador(idJogador),
	Jogador2 Integer REFERENCES Domino.Jogador(idJogador)
);

create table Domino.Partida (
	idPartida SERIAL PRIMARY KEY,
	jogo Integer REFERENCES Domino.Jogo(idJogo),
	finalizada_por Integer REFERENCES Domino.Jogador(idJogador)
);

create table Domino.Monte (
	idMonte SERIAL PRIMARY KEY,
	partida Integer REFERENCES Domino.Partida(idPartida)
);

create table Domino.Jogada (
	idJogada SERIAL PRIMARY KEY,
	ordem Integer NOT NULL,
	partida Integer REFERENCES Domino.Partida(idPartida),
	jogador Integer REFERENCES Domino.Jogador(idJogador),
	acao Integer NOT NULL CHECK (acao IN (1, 2, 3, 4)), -- Jogou = 1; Comprou = 2; Passou = 3; Distribuiu = 4;
	peca Integer REFERENCES Domino.Peca(idPeca),
	ladoUtilizado Integer
)
