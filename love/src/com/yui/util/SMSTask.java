package com.yui.util;

public class SMSTask {
    private static boolean flag = false;
    private static Thread t1;

    /**
     * 启动任务
     *
     * @param time        任务执行的间隔时间
     * @param name
     * @param phoneNumber
     * @param city
     */
    public static void start(long time, String name, String phoneNumber, String city) {
        if (!flag) {
            Thread t1 = new Thread() {
                @Override
                public void run() {
                    flag = true;
                    task:
                    while (true) {
                        String text = Util.send(phoneNumber, name, city);
                        if (!"OK".equals(text)) {
                            continue;
                        }
                        try {
                            Thread.sleep(time);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            break task;
                        }
                    }
                }
            };
            t1.start();
        }
    }


    public static void end() {
        flag = false;
        if (t1 != null) t1.interrupt();
    }


    public static void main(String[] args) {
        //需要填充昵称，手机号码，城市
        start(259200000,"", "", "");
    }
}

