package org.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {

    // Configurações do seu banco de dados
    private static final String URL = "jdbc:mysql://localhost:3306/caldeira_dspi";
    private static final String USUARIO = "root";
    private static final String SENHA = ""; // Use sua senha real, se houver

    public static Connection conectar() {
        Connection conexao = null; // A variável é inicializada aqui
        try {
            // Carrega o driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Estabelece a conexão com o banco de dados
            conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
            System.out.println("Conexão com o banco de dados 'caldeira_dipi' estabelecida com sucesso!");

        } catch (ClassNotFoundException e) {
            System.err.println("Driver do banco de dados não encontrado.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Erro ao conectar com o banco de dados.");
            e.printStackTrace();
        }
        return conexao; // A variável é retornada aqui, garantindo que sempre haja um retorno
    }

    public static void fecharConexao(Connection conexao) {
        if (conexao != null) {
            try {
                conexao.close();
                System.out.println("Conexão com o banco de dados fechada.");
            } catch (SQLException e) {
                System.err.println("Erro ao fechar a conexão.");
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Connection conexao = DB.conectar();
        // Lógica para usar a conexão...
        DB.fecharConexao(conexao);
    }
}