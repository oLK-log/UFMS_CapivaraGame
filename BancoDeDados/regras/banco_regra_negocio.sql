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
    p_lado_mesa INTEGER
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_lado1_peca INTEGER;
    v_lado2_peca INTEGER;
BEGIN
    -- Pega os lados da peça
    SELECT valorLado1, valorLado2 INTO v_lado1_peca, v_lado2_peca
    FROM domino.peca
    WHERE idpeca = p_idpeca;
    -- verifica se a jogada é possível
    IF p_lado_mesa IS NOT NULL THEN
        IF v_lado1_peca <> p_ponta_mesa AND v_lado2_peca <> p_ponta_mesa THEN
            RAISE EXCEPTION 'Jogada inválida: está peça não se encaixa em %.',p_ponta_mesa;
        END IF;
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
    v_qtd_pecas_na_mao INTEGER;
BEGIN
    SELECT count(*) INTO v_qtd_pecas_na_mao
    FROM domino.jogada j
    WHERE j.idpartida = NEW.idpartida
    AND j.idjogador = NEW.idjogador
    AND j.idpeca NOT IN (
        SELECT idpeca FROM domino.jogada
        WHERE idpartida = NEW.idpartida 
        AND idjogador = NEW.idjogador
        AND acao = 1
    ) AND j.acao IN (2,4); 

    IF v_qtd_pecas_na_mao = 0 THEN
        RAISE NOTICE ' Jogador % bateu!', NEW.jogador;
        FOR v_adversario IN
            SELECT idjogador FROM domino.jogador
            WHERE idjogador <> NEW.idjogador
            AND idJogo = (
                SELECT idjogo 
                FROM domino.partida 
                WHERE idpartida = NEW.idpartida
            )
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

        UPDATE domino.partida
        SET idjogadorfinalizou = NEW.idjogador
        WHERE idpartida = NEW.idpartida;
    END IF;
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
