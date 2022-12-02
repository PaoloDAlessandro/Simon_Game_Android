package it.itsar.simon;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.sql.SQLOutput;
import java.util.ArrayList;

import it.itsar.simon.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    protected ActivityMainBinding binding;

    private Colore[] colori = {new Colore("red", 329, 250), new Colore("green", 391, 250), new Colore("blue", 195, 250), new Colore("yellow", 261, 250)};

    private ArrayList<String> sequenceArray = new ArrayList<String>();

    private ArrayList<String> userSequence = new ArrayList<String>();

    private int score = 0;

    private int indice = 0;

    private int indiceUtente = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.greenButton.setBackgroundColor(Color.GREEN);
        binding.redButton.setBackgroundColor(Color.RED);
        binding.yellowButton.setBackgroundColor(Color.YELLOW);
        binding.blueButton.setBackgroundColor(Color.BLUE);

        binding.redButton.setAlpha((float)0.6);
        binding.yellowButton.setAlpha((float)0.6);
        binding.blueButton.setAlpha((float)0.6);
        binding.greenButton.setAlpha((float)0.6);

        binding.greenButton.setOnClickListener(view -> {
            binding.greenButton.setAlpha(1);
            colori[1].play();
            userSequence.add("green");
            setTimeout(() -> {
                        this.runOnUiThread(() -> {
                            binding.greenButton.setAlpha((float)0.6);
                        });
                    }, 600
            );
            Log.d("sequenceUser:", userSequence.toString());

        });

        binding.yellowButton.setOnClickListener(view -> {
            binding.yellowButton.setAlpha(1);
            colori[3].play();
            userSequence.add("yellow");
            Log.d("sequenceUser:", userSequence.toString());
            setTimeout(() -> {
                        this.runOnUiThread(() -> {
                            binding.yellowButton.setAlpha((float)0.6);
                        });
                    }, 600
            );
        });

        binding.blueButton.setOnClickListener(view -> {
            binding.blueButton.setAlpha(1);
            colori[2].play();
            userSequence.add("blue");
            Log.d("sequenceUser:", userSequence.toString());
            setTimeout(() -> {
                        this.runOnUiThread(() -> {
                            binding.blueButton.setAlpha((float)0.6);
                        });
                    }, 600
            );
        });

        binding.redButton.setOnClickListener(view -> {
            binding.redButton.setAlpha(1);
            colori[0].play();
            userSequence.add("red");
            Log.d("sequenceUser:", userSequence.toString());
            setTimeout(() -> {
                        this.runOnUiThread(() -> {
                            binding.redButton.setAlpha((float)0.6);
                        });
                    }, 600
            );
        });

        binding.playButton.setOnClickListener(view -> {
            binding.score.setText("Your score: " + score);
            binding.playButton.setEnabled(false);
            reset();
            play();
        });
    }

    private void play() {
            int index = (int)(Math.random() * colori.length) + 0;
            sequenceArray.add(colori[index].getNome());
            setTimeout(() -> {
                playSound(colori[findIndex(sequenceArray.get(indice))]);
                }, 800);
    }

    private void reset() {
        indice = 0;
        indiceUtente = 1;
        score = 0;
        sequenceArray.clear();
        userSequence.clear();
    }

    private void userSequence() {
        Log.d("sequenceUser:", userSequence.toString());
        Log.d("sequenceArray:", sequenceArray.toString());
        Log.d("indiceUtente:", String.valueOf(indiceUtente));

        if(userSequence.equals(sequenceArray) && userSequence.size() == sequenceArray.size()) {
            score++;
            this.runOnUiThread(() -> {
                binding.score.setText("Your score: " + score);
            });
            userSequence.clear();
            System.out.println("LETSGOOOOOOOOOO");
            indiceUtente = 1;
            indice = 0;
            play();
        }
        else {
            if(!(userSequence.equals(sequenceArray)) && userSequence.size() >= sequenceArray.size()) {
                gameOver();
            }
            else {
                setTimeout(() -> {
                    takeUserInput();
                }, 3000);
            }
        }
    }

    private void takeUserInput() {
        Log.d("indiceUtenteTAKEUSER:", String.valueOf(indiceUtente));

        if(userSequence.size() >= indiceUtente) {
            indiceUtente++;
            userSequence();
        }
        else {
            gameOver();
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

    private void playSound(Colore color) {
        color.play();
        switch(color.nome) {
            case "green":
                binding.greenButton.setAlpha(1);
                setTimeout(() -> {
                            this.runOnUiThread(() -> {
                                binding.greenButton.setAlpha((float)0.6);
                            });
                        }, 600
                );
                break;

            case "red":
                binding.redButton.setAlpha(1);
                setTimeout(() -> {
                            this.runOnUiThread(() -> {
                                binding.redButton.setAlpha((float)0.6);
                            });
                        }, 600
                );
                break;

            case "blue":
                binding.blueButton.setAlpha(1);
                setTimeout(() -> {
                            this.runOnUiThread(() -> {
                                binding.blueButton.setAlpha((float)0.6);
                            });
                        }, 600
                );
                break;

            case "yellow":
                binding.yellowButton.setAlpha(1);
                setTimeout(() -> {
                            this.runOnUiThread(() -> {
                                binding.yellowButton.setAlpha((float)0.6);
                            });
                        }, 600
                );
                break;
        }

        indice++;
        if(indice < sequenceArray.size()) {
            setTimeout(() -> {
                playSound(colori[findIndex(sequenceArray.get(indice))]);
            }, 800);
        }
        else {
            userSequence();
        }
    }
}

