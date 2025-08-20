package org.example;

import com.sourceforge.snap7.moka7.S7Client;
import com.sourceforge.snap7.moka7.S7;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class Main {

    private static final String PLC_IP = "192.168.0.53";
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

                    // --- Conversão dos dados lidos ---

                    // Temperatura: Bytes 4, 5, 6, 7
                    byte[] realBytesTemp = {buffer[4], buffer[5], buffer[6], buffer[7]};
                    float temperaturaLida = ByteBuffer.wrap(realBytesTemp).order(ByteOrder.BIG_ENDIAN).getFloat();

                    // Nível do Líquido: Bytes 8, 9, 10, 11
                    byte[] realBytesNivel = {buffer[8], buffer[9], buffer[10], buffer[11]};
                    double nivelLiquido = ByteBuffer.wrap(realBytesNivel).order(ByteOrder.BIG_ENDIAN).getFloat();

                    // Pressão: Bytes 12, 13, 14, 15
                    byte[] realBytesPressao = {buffer[12], buffer[13], buffer[14], buffer[15]};
                    double pressao = ByteBuffer.wrap(realBytesPressao).order(ByteOrder.BIG_ENDIAN).getFloat();


                    System.out.println("-------------------------------------------------");
                    System.out.println("Valores Reais Lidos do CLP:");
                    System.out.println("Temperatura (REAL): " + temperaturaLida);
                    System.out.println("Nível do Líquido (REAL): " + nivelLiquido);
                    System.out.println("Pressão (REAL): " + pressao);


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
