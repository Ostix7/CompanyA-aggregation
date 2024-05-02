package company.a.charlee.entity;

public enum TimeUnit {
    HOUR, DAY;

    public int getSecondsFromTimeFrame() {
        int seconds;
        if (this == TimeUnit.HOUR) {
            seconds = 3600;
        } else {
            seconds = 86400;
        }
        return seconds;
    }

}
