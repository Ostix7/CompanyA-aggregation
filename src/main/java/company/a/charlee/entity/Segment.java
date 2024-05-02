package company.a.charlee.entity;

public enum Segment {
    ENG, UKR, RUS;

    public String getSegmentAsString() {
        String segmentAsString;
        switch (this) {
            case ENG:
                segmentAsString = "western";
                break;
            case UKR:
                segmentAsString = "ukr";
                break;
            default:
                segmentAsString = "katsap";
                break;
        }
        return segmentAsString;
    }

}
