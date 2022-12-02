package it.itsar.simon;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.widget.Button;

public class Colore {
    String nome;
    int freqHz;
    int durationMs;
    Button button;

    public Colore(String nome, int freqHz, int durationMs) {
        this.nome = nome;
        this.freqHz = freqHz;
        this.durationMs = durationMs;
        this.button = null;
    }

    private AudioTrack generateTone(double freqHz, int durationMs)
    {
        int count = (int)(44100.0 * 2.0 * (durationMs / 1000.0)) & ~1;
        short[] samples = new short[count];
        for(int i = 0; i < count; i += 2){
            short sample = (short)(Math.sin(2 * Math.PI * i / (44100.0 / freqHz)) * 0x7FFF);
            samples[i + 0] = sample;
            samples[i + 1] = sample;
        }
        AudioTrack track = new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
                AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT,
                count * (Short.SIZE / 8), AudioTrack.MODE_STATIC);
        track.write(samples, 0, count);
        return track;
    }

    public void play() {
        AudioTrack tone = generateTone(this.freqHz, this.durationMs);
        tone.play();
    }

    public void changeColor() {
        this.button.setAlpha(1);
        setTimeout(() -> {
            this.button.setAlpha((float)0.6);
            }, 600
        );
    }

    public String getNome() {
        return nome;
    }

    public int getFreqHz() {
        return freqHz;
    }

    public int getDurationMs() {
        return durationMs;
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
}
