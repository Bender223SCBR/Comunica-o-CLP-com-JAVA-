package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

public class DB {

    private static final String URL = "jdbc:mysql://localhost:3306/caldeira_dspi";
    private static final String USUARIO = "root";
    private static final String SENHA = ""; // Use sua senha real, se houver

    public static void inserirLeituraCaldeira(float temperatura, double nivelLiquido, double pressao) {
        String sql = "INSERT INTO leituras_caldeira (timestamp, temperatura, nivel_liquido, pressao) VALUES (?, ?, ?, ?)";
        Connection conexao = null;
        PreparedStatement stmt = null;

        try {
            // É necessário ter o driver do MySQL no projeto para esta linha funcionar
            conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
            stmt = conexao.prepareStatement(sql);

            stmt.setTimestamp(1, Timestamp.from(Instant.now()));
            stmt.setFloat(2, temperatura);
            stmt.setDouble(3, nivelLiquido);
            stmt.setDouble(4, pressao);

            int linhasAfetadas = stmt.executeUpdate();
            System.out.println("-------------------------------------------------");
            System.out.println("BANCO DE DADOS: " + linhasAfetadas + " linha(s) adicionada(s) à tabela 'leituras_caldeira'!");

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
}
