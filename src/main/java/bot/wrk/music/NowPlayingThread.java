/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bot.wrk.music;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author renardn
 */
public class NowPlayingThread extends Thread {

    private NowPlaying np;
    private volatile boolean working;
    private volatile boolean alive;

    public NowPlayingThread(NowPlaying np) {
        this.np = np;
        working = false;
        alive = true;
    }

    public void npStop() {
        working = false;
    }

    public boolean npIsWorking() {
        return working;
    }
    
    public void npWork(){
        working = true;
    }

    public void diePotato() {
        alive = false;
    }

    @Override
    public void run() {
        while (alive) {
            while (working) {
                np.updateNowPlaying();
                try {
                    sleep(2000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(NowPlayingThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
