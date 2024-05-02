package company.a.charlee.entity;

public enum TimeFrame {
    HOUR, DAY;

    public int getSecondsFromTimeFrame() {
        int seconds;
        if (this == TimeFrame.HOUR) {
            seconds = 3600;
        } else {
            seconds = 86400;
        }
        return seconds;
    }

}
