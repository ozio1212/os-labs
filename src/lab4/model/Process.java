package lab4.model;

import java.util.LinkedList;
import java.util.List;

public class Process {
    private final int id;
    private final int minPage;
    private final int maxPage;
    private final List<Integer> fullReferenceString;
    private int allocatedFrames;
    private final LinkedList<Integer> localMemory;
    private int pageFaults;
    private boolean isSuspended;

    public Process(int id, int minPage, int maxPage, List<Integer> fullReferenceString) {
        this.id = id;
        this.minPage = minPage;
        this.maxPage = maxPage;
        this.fullReferenceString = fullReferenceString;
        this.localMemory = new LinkedList<>();
        this.allocatedFrames = 0;
        this.pageFaults = 0;
        this.isSuspended = false;
    }

    public boolean processPageRequest(int page) {
        if (isSuspended) return false;

        boolean isFault = false;

        if (localMemory.contains(page)) {
            localMemory.remove(Integer.valueOf(page));
            localMemory.addLast(page);
        } else {
            pageFaults++;
            isFault = true;

            if (allocatedFrames > 0) {
                if (localMemory.size() >= allocatedFrames) {
                    localMemory.removeFirst();
                }
                localMemory.addLast(page);
            }
        }
        return isFault;
    }

    public void setAllocatedFrames(int frames) {
        this.allocatedFrames = frames;
        while (localMemory.size() > allocatedFrames && !localMemory.isEmpty()) {
            localMemory.removeFirst();
        }
    }

    public void suspend() {
        this.isSuspended = true;
        this.localMemory.clear();
    }

    public void resume() {
        this.isSuspended = false;
    }

    public void reset() {
        this.allocatedFrames = 0;
        this.localMemory.clear();
        this.pageFaults = 0;
        this.isSuspended = false;
    }


    public int getId() { return id; }
    public int getPageFaults() { return pageFaults; }
    public boolean isSuspended() { return isSuspended; }
    public int getAllocatedFrames() { return allocatedFrames; }
    public int getVirtualMemorySize() { return maxPage - minPage + 1; }
    public List<Integer> getFullReferenceString() { return fullReferenceString; }
}
