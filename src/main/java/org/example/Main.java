package org.example;
import com.sourceforge.snap7.moka7.S7Client;
import com.sourceforge.snap7.moka7.S7;
import java.util.Arrays;

public class Main{

    // Endereço IP do seu CLP
    private static final String PLC_IP = "192.168.0.1";

    // Rack e Slot do CLP (para S7-1200, é 0 e 1)
    private static final int RACK = 0;
    private static final int SLOT = 1;

    // Número do Data Block (DB) que você quer ler
    private static final int DB_NUMBER = 1;

    // Posição inicial no DB para começar a leitura
    private static final int START_ADDRESS = 0;

    // Quantidade de bytes que você quer ler
    private static final int READ_LENGTH = 10;

    public static void main(String[] args) {
        S7Client client = new S7Client();
        int result;

        try {
            // Conecta o CLP
            result = client.ConnectTo(PLC_IP, RACK, SLOT);
            if (result == 0) {
                System.out.println("Conexão com o CLP estabelecida com sucesso!");

                // Cria um buffer de bytes para armazenar os dados lidos
                byte[] buffer = new byte[READ_LENGTH];

                // Lê o bloco de dados (DB)
                result = client.ReadArea(S7.S7AreaDB, DB_NUMBER, START_ADDRESS, READ_LENGTH, buffer);

                if (result == 0) {
                    System.out.println("Leitura do DB" + DB_NUMBER + " bem-sucedida.");
                    System.out.println("Dados lidos: " + Arrays.toString(buffer));

                    // Você pode processar os dados lidos aqui
                    // Por exemplo: int valorInteiro = S7.GetWordAt(buffer, 0);

                } else {
                    System.err.println("Erro ao ler DB: " + client.ErrorText(result));
                }

            } else {
                System.err.println("Erro ao conectar com o CLP: " + client.ErrorText(result));
            }

        } finally {
            // Desconecta do CLP para liberar o recurso
            if (client.Connected) {
                client.Disconnect();
                System.out.println("Desconectado do CLP.");
            }
        }
    }
}
