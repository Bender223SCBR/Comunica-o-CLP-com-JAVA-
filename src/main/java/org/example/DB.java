package org.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {

    // Configurações do seu banco de dados
    private static final String URL = "jdbc:mysql://localhost:3306/nome_do_seu_banco";
    private static final String USUARIO = "seu_usuario";
    private static final String SENHA = "sua_senha";

    public static Connection conectar() {
        Connection conexao = null;
        try {
            // Carrega o driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Estabelece a conexão com o banco de dados
            conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
            System.out.println("Conexão com o banco de dados estabelecida com sucesso!");

        } catch (ClassNotFoundException e) {
            System.err.println("Driver do banco de dados não encontrado.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Erro ao conectar com o banco de dados.");
            e.printStackTrace();
        }
        return conexao;
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

        // Você pode executar operações de banco de dados aqui
        // Ex: Statement stmt = conexao.createStatement();
        //     ResultSet rs = stmt.executeQuery("SELECT * FROM sua_tabela");

        fecharConexao(conexao);
    }
}