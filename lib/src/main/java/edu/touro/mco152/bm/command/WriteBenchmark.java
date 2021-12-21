package edu.touro.mco152.bm.command;

import edu.touro.mco152.bm.App;
import edu.touro.mco152.bm.DiskMark;
import edu.touro.mco152.bm.IDiskAppWorker;
import edu.touro.mco152.bm.Util;
import edu.touro.mco152.bm.persist.DiskRun;
import edu.touro.mco152.bm.persist.EM;
import edu.touro.mco152.bm.ui.Gui;
import jakarta.persistence.EntityManager;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import static edu.touro.mco152.bm.App.*;
import static edu.touro.mco152.bm.App.msg;
import static edu.touro.mco152.bm.DiskMark.MarkType.WRITE;

/**
 * class to do read benchmarks and made into a command
 * code was removed from diskworker and placed into this new class
 */

public class WriteBenchmark implements CommandInterface{

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

    public WriteBenchmark(IDiskAppWorker worker, int marks, int numBlocks,int sizeBlocks, DiskRun.BlockSequence sequence){
        this.worker = worker;
        this.marks = marks;
        this.numBlocks = numBlocks;
        this.sizeBlocks = sizeBlocks;
        this.sequence = sequence;
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

        int blockSize = sizeBlocks*KILOBYTE;
        byte [] blockArr = new byte [blockSize];
        for (int b=0; b<blockArr.length; b++) {
            if (b%2==0) {
                blockArr[b]=(byte)0xFF;
            }
        }

        DiskMark wMark;
        int startFileNum = App.nextMarkNumber;

        DiskRun run = new DiskRun(DiskRun.IOMode.WRITE, sequence);
        run.setNumMarks(marks);
        run.setNumBlocks(numBlocks);
        run.setBlockSize(sizeBlocks);
        run.setTxSize(App.targetTxSizeKb());
        run.setDiskInfo(Util.getDiskInfo(dataDir));

        // Tell logger and GUI to display what we know so far about the Run
        msg("disk info: (" + run.getDiskInfo() + ")");

        Gui.chartPanel.getChart().getTitle().setVisible(true);
        Gui.chartPanel.getChart().getTitle().setText(run.getDiskInfo());

        // Create a test data file using the default file system and config-specified location
        if (!App.multiFile) {
            testFile = new File(dataDir.getAbsolutePath() + File.separator + "testdata.jdm");
        }

            /*
              Begin an outer loop for specified duration (number of 'marks') of benchmark,
              that keeps writing data (in its own loop - for specified # of blocks). Each 'Mark' is timed
              and is reported to the GUI for display as each Mark completes.
             */
        for (int m = startFileNum; m < startFileNum + marks && !worker.wasCanceled(); m++) {

            if (App.multiFile) {
                testFile = new File(dataDir.getAbsolutePath()
                        + File.separator + "testdata" + m + ".jdm");
            }
            wMark = new DiskMark(WRITE);    // starting to keep track of a new bench Mark
            wMark.setMarkNum(m);
            long startTime = System.nanoTime();
            long totalBytesWrittenInMark = 0;

            String mode = "rw";
            if (App.writeSyncEnable) {
                mode = "rwd";
            }

            try {
                try (RandomAccessFile rAccFile = new RandomAccessFile(testFile, mode)) {
                    for (int b = 0; b < numBlocks; b++) {
                        if (sequence == DiskRun.BlockSequence.RANDOM) {
                            int rLoc = Util.randInt(0, numBlocks - 1);
                            rAccFile.seek((long) rLoc * blockSize);
                        } else {
                            rAccFile.seek((long) b * blockSize);
                        }
                        rAccFile.write(blockArr, 0, blockSize);
                        totalBytesWrittenInMark += blockSize;
                        wUnitsComplete++;
                        unitsComplete = rUnitsComplete + wUnitsComplete;
                        percentComplete = (float) unitsComplete / (float) unitsTotal * 100f;

                            /*
                              Report to GUI what percentage level of Entire BM (#Marks * #Blocks) is done.
                             */
                        worker.progressSetter((int) percentComplete);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }

                /*
                  Compute duration, throughput of this Mark's step of BM
                 */
            long endTime = System.nanoTime();
            long elapsedTimeNs = endTime - startTime;
            double sec = (double) elapsedTimeNs / (double) 1000000000;
            double mbWritten = (double) totalBytesWrittenInMark / (double) MEGABYTE;
            wMark.setBwMbSec(mbWritten / sec);
            msg("m:" + m + " write IO is " + wMark.getBwMbSecAsString() + " MB/s     "
                    + "(" + Util.displayString(mbWritten) + "MB written in "
                    + Util.displayString(sec) + " sec)");
            App.updateMetrics(wMark);

                /*
                  Let the GUI know the interim result described by the current Mark
                 */
            worker.publishData(wMark);

            // Keep track of statistics to be displayed and persisted after all Marks are done.
            run.setRunMax(wMark.getCumMax());
            run.setRunMin(wMark.getCumMin());
            run.setRunAvg(wMark.getCumAvg());
            run.setEndTime(new Date());
        } // END outer loop for specified duration (number of 'marks') for WRITE bench mark

            /*
              Persist info about the Write BM Run (e.g. into Derby Database) and add it to a GUI panel
             */
        EntityManager em = EM.getEntityManager();
        em.getTransaction().begin();
        em.persist(run);
        em.getTransaction().commit();

        Gui.runPanel.addRun(run);

        return true;
    }
}
