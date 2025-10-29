# <p align="center"> UFMS_CapivaraGame <br/> </p>
<p align="center">

## Trabalho Final da matéria 'Laboratório de Banco de Dados' com fim de modelagem, projeto e construção de um banco de dados relacional, e aplicação por meio de um sistema Online de jogos.
* **Faculdade:** UFMS
* **Curso:** Engenharia de Software
* **Desenvolvedores:**
    - @Klevinryanr - Klevin Ryan Ramalho Matos
    - @oLK-log - Lorran Kaíque Silveira Fernandes
    - @sergio.lamarque - Sergio Lamarque
* **Ferramentas:**
  - [![draw.io](https://img.shields.io/badge/draw.io-F08705?style=for-the-badge&logo=diagramsdotnet&logoColor=white)](https://www.draw.io/)
  - [![PostgreSQL](https://img.shields.io/badge/PostgreSQL-336791?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/) 
 
## Descrição:
  Este trabalho prático da disciplina de Laboratório de Banco de Dados consiste na modelagem, projeto e construção de um banco de dados relacional, por meio do desenvolvimento de um banco de dados do sistema Online chamado de Capivara Game. Esse sistema terá como primeiro jogo o **'Dominó'** .
  #### Regras do Dominó:
  - Peças - 28 peças com lados variando de 0 a 6;
  - Distribuição - 7 peças para cada participante;
  - Objetivo - fazer 50 pontos.

  #### Contagem Pontos
    ' Caso algum jogador tenha batido o jogo, sua dupla leva todos os pontos das peças que estão nas mãos dos adversários. Caso o jogo fique trancado, conta-se todos os pontos conseguidos por cada dupla. A dupla que possuir menos pontos é a vencedora, e leva todos os pontos da dupla adversária. 
    Caso haja um empate nesta contagem de pontos, a dupla que trancou o jogo perde, e a dupla vencedora leva todos os pontos desta dupla. Os pontos da dupla vencedora são acumulados, e o jogo termina quando uma das duplas atinge a marca de 50 pontos. O valor em pontos de cada peça corresponde à 
    soma dos valores das duas pontas da peça. Dessa forma, a peça 0-0 vale 0 pontos, a peça 3-4 vale 7 pontos, a peça 6-6 vale 12 pontos e assim por diante.'

  #### Implementação
       Deve ser utilizada um linguagem de programação para gerar as jogadas e simular cada um dos jogadores, cadastrar partidas, etc. 
    Porém, algumas regras de negócio devem ser implementadas dentro do banco de dados, através de restrições, funções/procedimentos e gatilhos:
     • Gatilhos: Calcular pontos automaticamente ao bater/fechar;
     • Procedimentos: Comprar peça do monte, validar jogada;
     • Funções: Verificar jogadas possíveis, detectar jogo trancado;
     • Visão: Ranking de pontuação (por usuário), contando quantas partidas vencidas e quantos jogos vencidos;
     • Visão: Listagem de cada partida e seu vencedor.

        O banco de dados deve ser capaz de armazenar dados de usuários, que podem jogar diversos jogos. Jogos são compostos por diversas partidas. 
     Um jogo está completo quando o total de 50 pontos for atingido, somando todos os pontos das partidas pertencentes ao jogo em questão. 
     Além disso, o banco de dados deve possuir o histórico completo de todas as movimentações, de todos os jogadores em todas as partidas.
  
 ## Modelagem:
 * #### **Modelo Conceitual**-EER
 <!--* ![imagem modelo conceitual EER](./ERR.drawio.png);-->
<div align="center">
  <img src="./AgenciaTurismoWhite.drawio.png" alt="imagem modelo conceitual EER" width="700"/>
</div>

## Funcionalidades

<!-- Liste as principais funcionalidades do sistema implementado. Você pode usar de checkbox para indicar aquelas que foram, ou não, implementadas na versão atual. Por exemplo:

- [x] Funcionalidade 1
- [ ] Funcionalidade 2
- [ ] ...-->

## Instalação / Implantação

<!--*Nesta seção você deverá trazer um passo a passo do que é necessário para a implantação do sistema desenvolvido.

Preferencialmente, use de ferramentas que automatizem a instalação/implantação do sistema, ou partes dele. Por exemplo, se você precisa compilar os arquivos do sistema para utilizá-lo, uma ótima opção é o [Make](https://www.gnu.org/software/make/). Com ele é possível automatizar todo processo de preparação e compilaçãocom um simples comando no terminal:

```sh
make
```

Outras ferramentas podem, inclusive, automatizar o processo de inicialização dos serviços da ferramenta, como o [Docker Compose](https://docs.docker.com/compose/):

```sh
docker-compose up -d
```

Se o sistema precisa ser implantado manualmente, descreva detalhadamente os passos necessários para a correta instalação. Neste caso, u

1. Abra um terminal no diretório do projeto ....
2. Instale as dependências usando o comando xxxx ...
3. Compile o código fonte com o comando yyyy ...
4. ....

Por fim, lembre-se de destacar quando necessário quais variáveis de ambientes (do inglês _environment variables_) são utilizadas ou necessárias para o processo. Muitas vezes a falta destas variáveis pode causar erros e impedir a correta implantação do sistema.-->

## Primeiros passos

<!--* Use esta seção para mostrar os primeiros passos para usar a aplicação. Lembre-se que esta parte deve ser focada no uso pelos clientes finais da aplicação, portanto, seja objetivo e use _screenshots_ quando necessário.-->

 
⚠ **Atenção**: Material com fins de aprendizado, e assim sendo, pode conter **erros** e **insconsistências**.

* ### **Links e material de apoio** 📖
 - [Curso Banco de Dados - Metropole Digital]([./universidade.sql](https://materialpublic.imd.ufrn.br/curso/disciplina/3/73/8/6))
 - [Tipos de dados MySQL](https://pt.myservername.com/mysql-data-types-what-are-different-data-types-mysql#goog_rewarded)
 - **WELLING**, Luke; **THOMSON**, Laura. **PHP e MySQL: Desenvolvimento Web**. 5. ed. Rio de Janeiro: Campus/Elsevier, 2010.
 
    




