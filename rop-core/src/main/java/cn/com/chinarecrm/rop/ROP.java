package cn.com.chinarecrm.rop;

/**
 * @author 王贵源(wangguiyuan@chinarecrm.com.cn)
 */
public class ROP {
    public static final String DESCRIPTION = "chinare rest open platform";
    public static final int MAJOR_VERSION = 1;
    public static final int MINOR_VERSION = 0;
    public static final String NAME = "rop";
    public static final String RELEASE_LEVEL = "b";
    public static final boolean SNAPSHOT = false;

    public static int majorVersion() {
        return MAJOR_VERSION;
    }

    public static int minorVersion() {
        return MINOR_VERSION;
    }

    public static String releaseLevel() {
        return RELEASE_LEVEL;
    }

    public static String v() {
        return String.format("%d.%s.%d%s",
                             majorVersion(),
                             releaseLevel(),
                             minorVersion(),
                             SNAPSHOT ? ".SNAPSHOT" : "");
    }

    /**
     *
     */
    private ROP() {}
}
