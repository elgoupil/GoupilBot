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
    private boolean working;

    public NowPlayingThread(NowPlaying np) {
        this.np = np;
        working = false;
    }
    
    public void npStop(){
        working = false;
    }
    
    public boolean npIsWorking(){
        return working;
    }
    
    @Override
    public void run() {
        working = true;
        while (working) {
            try {
                np.updateNowPlaying();
                sleep(2000);
            } catch (InterruptedException ex) {
                System.out.println("afafkafakl");
            }
        }
    }
}
