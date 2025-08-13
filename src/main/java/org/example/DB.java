package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp; // Import para usar o timestamp
import java.time.Instant;   // Import para pegar o tempo atual

public class DB {

    // Configurações do banco de dados
    private static final String URL = "jdbc:mysql://localhost:3306/caldeira_dspi";
    private static final String USUARIO = "root";
    private static final String SENHA = "";

    /**
     * Insere os dados da caldeira no banco de dados.
     * @param temperatura O valor da temperatura.
     * @param nivelLiquido O valor do nível do líquido.
     * @param pressao O valor da pressão.
     */
    public static void inserirLeituraCaldeira(float temperatura, double nivelLiquido, double pressao) {
        // SQL para a tabela `leituras_caldeira`, incluindo o timestamp
        String sql = "INSERT INTO leituras_caldeira (timestamp, temperatura, nivel_liquido, pressao) VALUES (?, ?, ?, ?)";

        Connection conexao = null;
        PreparedStatement stmt = null;

        try {
            conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
<<<<<<< Updated upstream
            System.out.println("Conexão com o banco de dados 'caldeira_dspi' estabelecida com sucesso!");
=======
            stmt = conexao.prepareStatement(sql);

            // Define os valores para cada '?' no comando SQL
            stmt.setTimestamp(1, Timestamp.from(Instant.now())); // Pega o horário atual
            stmt.setFloat(2, temperatura);
            stmt.setDouble(3, nivelLiquido);
            stmt.setDouble(4, pressao);

            int linhasAfetadas = stmt.executeUpdate();
            System.out.println("-------------------------------------------------");
            System.out.println("BANCO DE DADOS: " + linhasAfetadas + " linha(s) adicionada(s) à tabela 'leituras_caldeira'!");
>>>>>>> Stashed changes

        } catch (SQLException e) {
            System.err.println("Erro ao inserir dados na tabela 'leituras_caldeira'.");
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conexao != null) conexao.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
<<<<<<< Updated upstream

    public static void main(String[] args) {
        Connection conexao = DB.conectar();
        // Lógica para usar a conexão...
        DB.fecharConexao(conexao);
    }
}
=======
}
>>>>>>> Stashed changes
