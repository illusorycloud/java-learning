public class singleton {
    public static class singletonHolder {
        private static final singleton instance = new singleton();
    }

    private singleton() {
    }

    public singleton getInstance() {
        return singletonHolder.instance;
    }
}
