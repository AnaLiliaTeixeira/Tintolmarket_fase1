# Tintolmarket - Fase 1 - Compilar e Executar

Este documento explica como compilar e executar o projeto Tintolmarket Fase 1. O projeto consiste em uma aplicação distribuída do tipo cliente-servidor para a compra e venda de vinhos, semelhante ao Vivino.  
Nesta primeira fase do trabalho, as questões de segurança da informação não são consideradas.

***

# Requisitos

Antes de começar, certifique-se de que possui as seguintes ferramentas instaladas:

+ Java Development Kit (JDK) versão 8 ou superior

***

# Compilar

1. Abra um terminal na pasta root do projeto  
2. Compile os arquivos Java do Servidor e do Cliente utilizando o seguinte comando:  
````
javac TintolmarketServer.java TintolmarketClient.java
````

***

# Executar

Após compilar o projeto, siga os passos abaixo para executar os programas.

## Executar o Servidor TintoImarketServer  

1. Abra um terminal e navegue até a pasta onde se encontram os arquivos Java compilados.  
2. Execute o seguinte comando para iniciar o servidor, substituindo ````<port>```` pelo número da porta (p.e. , 12345):

````
java TintolmarketServer <port>
````

## Executar a aplicação Cliente TintoImarket  

1. Abra um novo terminal e navegue até a pasta onde se encontram os arquivos Java compilados.  
2. Execute o seguinte comando para iniciar a aplicação cliente, substituindo ````<serverAddress>````, ````<userID>```` e ````[password]```` pelos valores correspondentes:  

````
java TintolmarketClient <serverAddress> <userID> [password]
````  
Agora pode começar a utilizar o sistema Tintolmarket para adicionar vinhos, indicar quantidades disponíveis, classificar vinhos e enviar mensagens privadas a outros utilizadores.

***

# Operações Disponíveis

A aplicação cliente oferece várias operações que pode executar. Alguns exemplos incluem:  

+ `add <wine> <image>`: Adicionar um novo vinho à lista.
+ `sell <wine> <value> <quantity>`: Colocar um vinho à venda com um preço e quantidade especificados.
+ `view <wine>`: Visualizar informações de um vinho específico, como imagem, classificação média e disponibilidade.
+ `buy <wine> <seller> <quantity>`: Comprar uma quantidade específica de um vinho de outro utilizador.
+ `wallet` : Devolver o saldo atual da carteira
+ `classify <wine> <stars>`: Atribuir uma classificação de 1 a 5 a um vinho específico.
+ `talk <user> <message>`: Enviar uma mensagem privada a outro utilizador.
+ `read`: Ler as novas mensagens recebidas e apresentar a identificação do remetente e a respectiva mensagem.
+ `exit`: Terminar o programa cliente.

***

# Limitações

+ Assume que o Servidor tem permissão para escrever no diretório em que está a ser executado.  
  
+ Não inclui qualquer mecanismo de autenticação ou criptografia na transferência de ficheiros.

***

# Autores

Ana Teixeira 56336  
Francisco Abreu 56277  
João Matos 56292

***

# Data

19/03/2023
