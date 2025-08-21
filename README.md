# Sistema de Leitura de CLP com Gravação em Banco de Dados

Este projeto consiste em uma aplicação Java desenvolvida para realizar a comunicação com um Controlador Lógico Programável (CLP) da família Siemens S7, ler variáveis de processo em tempo real e persistir esses dados em um banco de dados MySQL.

## Funcionalidades Principais

- **Conexão com CLP Siemens S7** via protocolo Ethernet.
- **Leitura de variáveis** de diferentes tipos (`BOOL`, `INT`, `REAL`) de um Data Block (DB) específico.
- **Conexão com banco de dados MySQL** utilizando o driver JDBC.
- **Inserção dos dados lidos** do CLP em uma tabela de leituras, com registro de data e hora (`timestamp`).

## Tecnologias Utilizadas

- **Linguagem:** Java 21
- **Gerenciador de Build:** Apache Maven
- **Comunicação CLP:** Biblioteca Moka7 (S7-Connector)
- **Banco de Dados:** MySQL
- **Driver de Conexão:** MySQL Connector/J

## Pré-requisitos

Antes de executar o projeto, certifique-se de ter os seguintes softwares instalados e configurados em seu ambiente:

-   JDK 21 (Java Development Kit)
-   Apache Maven
-   Um servidor de banco de dados MySQL (Ex: XAMPP, WAMP, Docker ou uma instalação standalone).
-   Um CLP Siemens S7 acessível na rede pela aplicação.
-   **Siemens TIA Portal v19** para programar o CLP.
-   Uma IDE de sua preferência (Ex: IntelliJ IDEA, Eclipse).

## Configuração do Ambiente

Siga os passos abaixo para configurar o projeto em uma nova máquina.

### 1. Banco de Dados

Conecte-se ao seu servidor MySQL e execute o seguinte comando SQL para criar a tabela necessária no banco de dados `caldeira_dspi`:

```sql
CREATE TABLE `leituras_caldeira` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `timestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `temperatura` FLOAT DEFAULT NULL,
  `nivel_liquido` DOUBLE DEFAULT NULL,
  `pressao` DOUBLE DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

### 2. Configuração da Aplicação Java

As configurações principais da aplicação estão centralizadas em dois arquivos. Abra-os e ajuste os valores conforme necessário:

-   **`src/main/java/org/example/DB.java`**: Contém as credenciais de acesso ao banco de dados.
    ```java
    private static final String URL = "jdbc:mysql://localhost:3306/caldeira_dspi";
    private static final String USUARIO = "root";
    private static final String SENHA = ""; // <-- Altere aqui sua senha do MySQL
    ```

-   **`src/main/java/org/example/Main.java`**: Contém os parâmetros de conexão com o CLP.
    ```java
    private static final String PLC_IP = "192.168.0.1"; // <-- Altere para o IP real do seu CLP
    private static final int RACK = 0;
    private static final int SLOT = 1;
    private static final int DB_NUMBER = 1; // O número do DB que será lido
    ```

### 3. Configuração do CLP (TIA Portal)

Este repositório inclui o projeto do TIA Portal (`Projeto caldeira Tia portal - DSPI 21.08.25 .zap19`) necessário para programar o CLP e permitir a comunicação com a aplicação Java. Siga os passos abaixo para configurá-lo.

**Passos:**

1.  **Restaurar o Projeto:**
    * Abra o TIA Portal.
    * No menu superior, vá em **Project > Retrieve**.
    * Navegue até a pasta deste repositório e selecione o arquivo `Projeto caldeira - IHM - funcional conection.zap19`.
    * Escolha uma pasta de destino para extrair o projeto e clique em **OK**. O TIA Portal irá descompactar e abrir o projeto.

2.  **Verificar os Endereços das Variáveis (Offsets):**
    * Com o projeto aberto, na árvore de projeto à esquerda, navegue até **PLC_1 > Program blocks**.
    * Abra o bloco de dados **"DB_DADOS_CALDEIRA" (DB1)**.
    * Você verá a tabela com as variáveis. A coluna **"Offset"** mostra o endereço inicial (em bytes) de cada variável. É este endereço que o código Java usa para a leitura.

    | Nome da Variável | Tipo de Dado | Offset | Descrição |
    | :--------------- | :----------- | :----- | :---------- |
    | `temperatura`    | `Real`       | `0.0`  | Temperatura da caldeira |
    | `nivel_liquido`  | `Real`       | `4.0`  | Nível de líquido |
    | `pressao`        | `Real`       | `8.0`  | Pressão interna |

3.  **Ajustar o IP do CLP:**
    * Na árvore do projeto, dê um duplo clique em **"Device configuration"** dentro de **PLC_1**.
    * Selecione a CPU do CLP e, na aba de propriedades abaixo, vá para **"PROFINET interface [X1]" > "Ethernet addresses"**.
    * Certifique-se de que o endereço IP configurado aqui é o mesmo que você definiu na variável `PLC_IP` no arquivo `Main.java` (Ex: `192.168.0.1`).

4.  **Compilar e Carregar no CLP:**
    * Com as configurações verificadas, clique com o botão direito sobre **PLC_1** na árvore de projeto.
    * Selecione **Compile > Hardware and Software (Rebuild all)**.
    * Após compilar sem erros, selecione **"Download to device"** para transferir o programa para o seu CLP físico.

## Como Executar

1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/Bender223SCBR/Comunica-o-CLP-com-JAVA-.git](https://github.com/Bender223SCBR/Comunica-o-CLP-com-JAVA-.git)
    cd Comunica-o-CLP-com-JAVA-
    ```

2.  **Compile o projeto com Maven:**
    Este comando irá baixar todas as dependências (`moka7`, `mysql-connector`) e compilar o código.
    ```bash
    mvn clean install
    ```

3.  **Execute a aplicação:**
    Após a compilação, você pode executar o programa de duas maneiras:

    -   **Via terminal (recomendado para produção):**
        ```bash
        java -jar target/CLP_DSPI-1.0-SNAPSHOT.jar
        ```

    -   **Diretamente pela sua IDE:**
        Abra o projeto na sua IDE, navegue até o arquivo `src/main/java/org/example/Main.java` e execute o método `main`.

## Estrutura do Projeto

-   **`Main.java`**: O "cérebro" da aplicação. É responsável por se conectar ao CLP, ler os dados, convertê-los e orquestrar a gravação no banco de dados.
-   **`DB.java`**: Uma classe utilitária responsável por toda a lógica de interação com o banco de dados MySQL, como conectar e inserir dados.
-   **`pom.xml`**: O arquivo de configuração do Maven, onde todas as dependências do projeto são declaradas.
