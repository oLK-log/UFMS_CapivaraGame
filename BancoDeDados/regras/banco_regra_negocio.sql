-- Script com as regras de negócio do projeto CapivaraGame - Dominó
-- Sérgio H., 2025

-- FUNCTION: verificar se o jogador tem alguma jogada possível

CREATE OR REPLACE FUNCTION domino.verificar_jogadas_possiveis(
    p_idpartida INTEGER,
    p_idjogador INTEGER,
    p_lado_esquerdo INTEGER,
    p_lado_direito INTEGER
)
RETURNS BOOLEAN
LANGUAGE plpgsql
AS $$
DECLARE
    v_peca RECORD;
BEGIN
    -- pega todas as peças que ainda estão com o jogador
    FOR v_peca IN
        SELECT p.*
        FROM domino.peca p
        JOIN domino.jogada j ON j.idpeca = p.idpeca
        WHERE j.idpartida = p_idpartida
          AND j.idjogador = p_idjogador
          AND j.idjogada = (
                SELECT j2.idjogada
                FROM domino.jogada j2
                WHERE j2.idpeca = p.idpeca AND j2.idpartida = p_idpartida
                ORDER BY j2.idjogada DESC LIMIT 1
          )
          AND j.acao IN (2,4) -- comprou ou recebeu na distribuição
    LOOP
        -- verifica se encaixa em alguma das pontas
        IF v_peca.valorLado1 = p_lado_esquerdo OR 
           v_peca.valorLado2 = p_lado_esquerdo OR
           v_peca.valorLado1 = p_lado_direito OR
           v_peca.valorLado2 = p_lado_direito THEN
            RETURN TRUE;
        END IF;
    END LOOP;

    RETURN FALSE;
END;
$$;



-- PROCEDURE: comprar peça do monte


CREATE OR REPLACE PROCEDURE domino.comprar_peca(
    p_idpartida INTEGER,
    p_idjogador INTEGER
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_peca INTEGER;
BEGIN
    -- pega uma peça que ainda não foi distribuída nem comprada
    SELECT p.idpeca INTO v_peca
    FROM domino.peca p
    WHERE p.idpeca NOT IN (
        SELECT idpeca FROM domino.jogada
        WHERE idpartida = p_idpartida
    )
    ORDER BY p.idpeca
    LIMIT 1;

    IF v_peca IS NULL THEN
        RAISE NOTICE 'Monte vazio';
        RETURN;
    END IF;

    -- registra a compra
    INSERT INTO domino.jogada (ordem, idpartida, idjogador, acao, idpeca)
    VALUES (
        (SELECT COALESCE(MAX(ordem),0)+1 FROM domino.jogada WHERE idpartida = p_idpartida),
        p_idpartida,
        p_idjogador,
        2,       -- acao: 2 = comprou
        v_peca
    );
END;
$$;

-- PROCEDURE: validar jogada


CREATE OR REPLACE PROCEDURE domino.validar_jogada(
    p_idpartida INTEGER,
    p_idjogador INTEGER,
    p_idpeca INTEGER,
    p_lado_escolhido INTEGER,
    p_lado_esquerdo INTEGER,
    p_lado_direito INTEGER
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_tem_jogada BOOLEAN;
BEGIN
    -- verifica se a jogada é possível
    v_tem_jogada := domino.verificar_jogadas_possiveis(
        p_idpartida, p_idjogador, p_lado_esquerdo, p_lado_direito
    );

    IF v_tem_jogada = FALSE THEN
        RAISE EXCEPTION 'Jogada inválida: jogador não tem peça que encaixa.';
    END IF;

    -- registra a jogada
    INSERT INTO domino.jogada (ordem, idpartida, idjogador, acao, idpeca, ladoUtilizado)
    VALUES (
        (SELECT COALESCE(MAX(ordem),0)+1 FROM domino.jogada WHERE idpartida = p_idpartida),
        p_idpartida,
        p_idjogador,
        1, -- acao = jogou
        p_idpeca,
        p_lado_escolhido
    );
END;
$$;


-- TRIGGER: calcular pontos quando jogador bate (acao = 1)


CREATE OR REPLACE FUNCTION domino.tr_bater_func()
RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
DECLARE
    v_adversario INTEGER;
    v_soma INTEGER := 0;
BEGIN
    -- pega adversários
    FOR v_adversario IN
        SELECT idjogador FROM domino.jogador
        WHERE idjogador <> NEW.idjogador
    LOOP
        -- soma peças na mão do adversário
        SELECT COALESCE(SUM(p.valorLado1 + p.valorLado2), 0)
        INTO v_soma
        FROM domino.peca p
        JOIN domino.jogada j ON j.idpeca = p.idpeca
        WHERE j.idjogador = v_adversario
        AND j.idjogada = (
            SELECT j2.idjogada FROM domino.jogada j2
            WHERE j2.idpeca = p.idpeca
            ORDER BY j2.idjogada DESC LIMIT 1
        )
        AND j.acao IN (2,4);
    END LOOP;

    -- adiciona pontos ao jogador que bateu
    UPDATE domino.jogador
    SET pontuacao = pontuacao + v_soma
    WHERE idjogador = NEW.idjogador;

    RETURN NEW;
END;
$$;


CREATE TRIGGER tr_bater
AFTER INSERT ON domino.jogada
FOR EACH ROW
WHEN (NEW.acao = 1)
EXECUTE FUNCTION domino.tr_bater_func();

-- VIEW: rankin


CREATE OR REPLACE VIEW domino.v_ranking AS
SELECT 
    j.idjogador,
    j.posicao,
    j.pontuacao
FROM domino.jogador j
ORDER BY j.pontuacao DESC;

-- VIEW: partidas e vencedor

CREATE OR REPLACE VIEW domino.v_partidas AS
SELECT 
    p.idpartida,
    p.idjogo,
    p.idjogadorfinalizou
FROM domino.partida p;
