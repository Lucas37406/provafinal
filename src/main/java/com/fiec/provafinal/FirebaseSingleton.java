package com.fiec.provafinal;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.FileInputStream;
import java.io.IOException;

public class FirebaseSingleton {
    private static FirebaseApp firebaseApp;

    private FirebaseSingleton() {
    }

    public static void getInstance() {
        if (firebaseApp == null) {
            try {
                // Caminho para o arquivo de credenciais do Firebase
                FileInputStream serviceAccount = new FileInputStream("src/main/resources/firebase/serviceAccountKey.json");

                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .setStorageBucket("seu-bucket.appspot.com") // Substitua pelo nome do seu bucket no Firebase
                        .build();

                // Inicializa o Firebase se não foi inicializado
                firebaseApp = FirebaseApp.initializeApp(options);
                System.out.println("Firebase inicializado com sucesso!");
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Erro ao inicializar o Firebase: " + e.getMessage());
            }
        }
    }
}
