package it.itsar.simon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import java.util.ArrayList;
import it.itsar.simon.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    protected ActivityMainBinding binding;

    private Colore[] colori;

    private ArrayList<String> sequenceArray = new ArrayList<>();
    private ArrayList<String> userSequence = new ArrayList<>();

    private int score = 0;
    private int indice = 0;
    private int indiceUtente = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        colori = new Colore[]{
                    new Colore("red", 329, 250, binding.redButton, Color.RED),
                    new Colore("green", 391, 250, binding.greenButton, Color.GREEN),
                    new Colore("blue", 195, 250, binding.blueButton, Color.BLUE),
                    new Colore("yellow", 261, 250, binding.yellowButton, Color.YELLOW)
        };

        setBackGroundColor();
        addClickListeners();

        binding.playButton.setOnClickListener(view -> {
            binding.score.setText("Your score: " + score);
            binding.playButton.setEnabled(false);
            reset();
            play();
        });
    }

    private void play() {
        int index = (int)(Math.random() * colori.length);
        sequenceArray.add(colori[index].getNome());
        setTimeout(() -> playSound(colori[findIndex(sequenceArray.get(indice))]), 500);
    }

    private void reset() {
        indice = 0;
        indiceUtente = 1;
        score = 0;
        sequenceArray.clear();
        userSequence.clear();
    }

    private void setBackGroundColor() {
        for (Colore colore : colori) {
            colore.getButton().setBackgroundColor(colore.getColor());
            colore.getButton().setAlpha((float) 0.5);
        }
    }

    private void userSequence() {
        if(userSequence.equals(sequenceArray)) {
            score++;
            this.runOnUiThread(() -> binding.score.setText("Your score: " + score));
            userSequence.clear();
            indiceUtente = 1;
            indice = 0;
            play();
        }
        else {
            if(userSequence.size() >= sequenceArray.size()) {
                gameOver();
            }
            else {
                setTimeout(() -> takeUserInput(), 1200);
            }
        }
    }

    private void takeUserInput() {
        if(userSequence.size() >= indiceUtente) {
            indiceUtente++;
            userSequence();
        }
        else {
            gameOver();
        }
    }

    private void addClickListeners() {
        for (int i = 0; i < colori.length; i++) {
            int finalI = i;
            colori[i].getButton().setOnClickListener(view -> {
                colori[finalI].getButton().setAlpha(1);
                colori[finalI].play();
                userSequence.add(colori[finalI].getNome());
                setTimeout(() -> this.runOnUiThread(()-> colori[finalI].getButton().setAlpha((float)0.5)), 500);
            });
        }
    }

    private void gameOver() {
        this.runOnUiThread(() -> {
            binding.score.setText("Game over");
            binding.playButton.setEnabled(true);
        });
        reset();
    }

    public int findIndex(String nome) {
        for (int i = 0; i < colori.length; i++) {
            if(colori[i].getNome().equals(nome)) {
                return i;
            }
        }
        return 0;
    }

    public static void setTimeout(Runnable runnable, int delay) {
        new Thread( () -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            }
            catch (Exception e) {
                System.err.println(e);
            }
        }).start();
    }

    private void changeButtonAlpha(Colore color) {
        color.getButton().setAlpha(1);
        setTimeout(() -> this.runOnUiThread(() -> color.getButton().setAlpha((float)0.5)), 500
        );
    }

    private void playSound(Colore color) {
        color.play();
        changeButtonAlpha(color);
        indice++;
        if(indice < sequenceArray.size()) {
            setTimeout(() -> playSound(colori[findIndex(sequenceArray.get(indice))]), 500);
        }
        else {
            setTimeout(() -> userSequence(), 1000);
        }
    }
}
