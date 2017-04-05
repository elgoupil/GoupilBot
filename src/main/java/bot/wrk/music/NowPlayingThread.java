/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bot.wrk.music;

/**
 *
 * @author renardn
 */
public class NowPlayingThread extends Thread {
    
    private NowPlaying np;

    public NowPlayingThread(NowPlaying np) {
        this.np = np;
    }
    
    @Override
    public void run() {
        while (true) {
            try {
                np.updateNowPlaying();
                sleep(900);
            } catch (InterruptedException ex) {
                System.out.println("afafkafakl");
            }
        }
    }
}
