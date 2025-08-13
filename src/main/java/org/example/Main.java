package org.example;

import com.sourceforge.snap7.moka7.S7Client;
import com.sourceforge.snap7.moka7.S7;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class Main {

    private static final String PLC_IP = "192.168.0.1";
    private static final int RACK = 0;
    private static final int SLOT = 1;
    private static final int DB_NUMBER = 1;
    private static final int START_ADDRESS = 0;
    private static final int READ_LENGTH = 20;

    public static void main(String[] args) {
        S7Client client = new S7Client();

        try {
            int result = client.ConnectTo(PLC_IP, RACK, SLOT);
            if (result == 0) {
                System.out.println("Conexão com o CLP estabelecida com sucesso!");

                byte[] buffer = new byte[READ_LENGTH];
                result = client.ReadArea(S7.S7AreaDB, DB_NUMBER, START_ADDRESS, READ_LENGTH, buffer);

                if (result == 0) {
                    System.out.println("Leitura do DB" + DB_NUMBER + " bem-sucedida.");

                    // Convertendo os dados
                    byte[] realBytesTemp = {buffer[4], buffer[5], buffer[6], buffer[7]};
                    float temperaturaLida = ByteBuffer.wrap(realBytesTemp).order(ByteOrder.BIG_ENDIAN).getFloat();

                    System.out.println("-------------------------------------------------");
                    System.out.println("Valor Real Lido do CLP:");
                    System.out.println("Temperatura (REAL): " + temperaturaLida);

                    // SUBSTITUIR ESTAS LINHAS PELA LEITURA REAL DO CLP.
                    double nivelLiquido = 85.5;  // Valor de exemplo
                    double pressao = 10.25;    // Valor de exemplo


                    // Chamar o método do DB para inserir os dados na tabela correta
                    DB.inserirLeituraCaldeira(temperaturaLida, nivelLiquido, pressao);

                } else {
                    System.err.println("Erro ao ler DB: " + client.ErrorText(result));
                }
            } else {
                System.err.println("Erro ao conectar com o CLP: " + client.ErrorText(result));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (client.Connected) {
                client.Disconnect();
                System.out.println("Desconectado do CLP.");
            }
        }
    }
}