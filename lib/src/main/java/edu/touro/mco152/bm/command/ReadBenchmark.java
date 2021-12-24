package edu.touro.mco152.bm.command;

import edu.touro.mco152.bm.*;
import edu.touro.mco152.bm.persist.DiskRun;
import edu.touro.mco152.bm.persist.EM;
import edu.touro.mco152.bm.ui.Gui;
import jakarta.persistence.EntityManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import static edu.touro.mco152.bm.App.*;
import static edu.touro.mco152.bm.App.msg;
import static edu.touro.mco152.bm.DiskMark.MarkType.READ;

/**
 * class to do read benchmarks and made into a command
 * code was removed from diskworker and placed into this new class
 */
public class ReadBenchmark implements CommandInterface{

        public ArrayList<ObserverInterface> observerList = new ArrayList();
        private IDiskAppWorker worker;
        private int marks, numBlocks,sizeBlocks;
        private DiskRun.BlockSequence sequence;

    /**
     * we remove control from app and set the things here
     * @param worker take in a IDiskAppWorker
     * @param marks takes in the marks instead of numOfMarks from app
     * @param numBlocks takes in the numblocks instead of numOfBlock from app
     * @param sizeBlocks takes in the sizeBlocks instead of blockSizeKb from app
     * @param sequence takes in a Enum BlockSequence instead of blockSequence from app
     */

        public ReadBenchmark(IDiskAppWorker worker, int marks, int numBlocks,int sizeBlocks, DiskRun.BlockSequence sequence){
            this.worker = worker;
            this.marks = marks;
            this.numBlocks = numBlocks;
            this.sizeBlocks = sizeBlocks;
            this.sequence = sequence;
        }

    public void register(ObserverInterface observer){
        observerList.add(observer);

    }


    private void updateEveryone(DiskRun run){
        for (ObserverInterface o : observerList) {
            o.update(run);
        }
    }
    @Override
    public boolean execute() throws IOException {
        // declare local vars formerly in DiskWorker

        int wUnitsComplete = 0,
                rUnitsComplete = 0,
                unitsComplete;

        int wUnitsTotal = App.writeTest ? numBlocks * marks : 0;
        int rUnitsTotal = App.readTest ? numBlocks * marks : 0;
        int unitsTotal = wUnitsTotal + rUnitsTotal;
        float percentComplete;

        int blockSize = blockSizeKb*KILOBYTE;
        byte [] blockArr = new byte [blockSize];
        for (int b=0; b<blockArr.length; b++) {
            if (b%2==0) {
                blockArr[b]=(byte)0xFF;
            }
        }

        DiskMark rMark;
        int startFileNum = App.nextMarkNumber;

        DiskRun run = new DiskRun(DiskRun.IOMode.READ, sequence);
        run.setNumMarks(marks);
        run.setNumBlocks(numBlocks);
        run.setBlockSize(sizeBlocks);
        run.setTxSize((long)marks * numBlocks * sizeBlocks);
        run.setDiskInfo(Util.getDiskInfo(dataDir));

        msg("disk info: (" + run.getDiskInfo() + ")");

        Gui.chartPanel.getChart().getTitle().setVisible(true);
        Gui.chartPanel.getChart().getTitle().setText(run.getDiskInfo());

        for (int m = startFileNum; m < startFileNum + marks && !worker.wasCanceled(); m++) {

            if (App.multiFile) {
                testFile = new File(dataDir.getAbsolutePath()
                        + File.separator + "testdata" + m + ".jdm");
            }
            rMark = new DiskMark(READ);
            rMark.setMarkNum(m);
            long startTime = System.nanoTime();
            long totalBytesReadInMark = 0;

            try {
                try (RandomAccessFile rAccFile = new RandomAccessFile(testFile, "r")) {
                    for (int b = 0; b < numBlocks; b++) {
                        if (sequence == DiskRun.BlockSequence.RANDOM) {
                            int rLoc = Util.randInt(0, numBlocks - 1);
                            rAccFile.seek((long) rLoc * blockSize);
                        } else {
                            rAccFile.seek((long) b * blockSize);
                        }
                        rAccFile.readFully(blockArr, 0, blockSize);
                        totalBytesReadInMark += blockSize;
                        rUnitsComplete++;
                        unitsComplete = rUnitsComplete + wUnitsComplete;
                        percentComplete = (float) unitsComplete / (float) unitsTotal * 100f;
                        worker.progressSetter((int) percentComplete);
                    }
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }
            long endTime = System.nanoTime();
            long elapsedTimeNs = endTime - startTime;
            double sec = (double) elapsedTimeNs / (double) 1000000000;
            double mbRead = (double) totalBytesReadInMark / (double) MEGABYTE;
            rMark.setBwMbSec(mbRead / sec);
            msg("m:" + m + " READ IO is " + rMark.getBwMbSec() + " MB/s    "
                    + "(MBread " + mbRead + " in " + sec + " sec)");
            App.updateMetrics(rMark);

            worker.publishData(rMark);

            run.setRunMax(rMark.getCumMax());
            run.setRunMin(rMark.getCumMin());
            run.setRunAvg(rMark.getCumAvg());
            run.setEndTime(new Date());

        }

        updateEveryone(run);
        return true;
    }
}
