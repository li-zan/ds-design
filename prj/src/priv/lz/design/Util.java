package priv.lz.design;

import java.util.Scanner;

public class Util {
    /**
     * 提供用户输入有效指令
     * @param strings 指定有效指令集string[]
     * @return 有效指令
     */
    public static String validInput(String... strings) throws InterruptedException {
        System.out.print("请输入服务前对应【符号】,【回车】确认：");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String str = "";
            if (scanner.hasNextLine())
                str = scanner.nextLine();
            else safeExit();
            str = str.trim();
            for (String data : strings) {
                if (data.equals(str)) {
                    return data;
                }
            }
            System.out.print("输入无效,请重新输入,回车确认：");
        }
    }

    /**
     * 提供用户输入并验证有效指令
     * @param maxRange [0,maxRange)为有效指令
     * @return 有效指令
     */
    public static String validInput(int maxRange) throws InterruptedException {
        String[] strings = new String[maxRange +2];
        int i;
        for (i = 0; i < maxRange; ++i) {
            strings[i] = "" + i;
        }
        strings[maxRange] = Window.EXIT;
        strings[maxRange +1] = Window.BACK;
        return validInput(strings);
    }

    /**
     * 提供用户输入并验证有效指令且自定义非法指令
     * @param maxRange [0,maxRange)为有效指令
     * @param invalidStrings 指定非法指令集
     * @return 有效指令
     */
    public static String excludeInput(int maxRange, String... invalidStrings) throws InterruptedException {
        String[] strings = new String[maxRange +2];
        int i;
        for (i = 0; i < maxRange; ++i) {
            strings[i] = "" + i;
        }
        strings[maxRange] = Window.EXIT;
        strings[maxRange +1] = Window.BACK;
        System.out.print("请输入服务前对应【符号】,【回车】确认：");
        Scanner scanner = new Scanner(System.in);
        outer:
        while (true) {
            String str = "";
            if (scanner.hasNextLine())
                str = scanner.nextLine();
            else safeExit();
            str = str.trim();
            for (String invalidStr : invalidStrings) {
                if (str.equals(invalidStr)) {
                    System.out.print("输入无效,请重新输入,回车确认：");
                    continue outer;
                }
            }
            for (String data : strings) {
                if (data.equals(str)) {
                    return data;
                }
            }
            System.out.print("输入无效,请重新输入,回车确认：");
        }
    }

    /**
     * 验证用户输入内容，空内容转换指定内容
     * @param str 打印 ”请输入${str}内容 [输入单行,回车确认] :“
     * @return 返回用户输入内容
     */
    public static String contentInput(String str) throws InterruptedException {
        System.out.println("请输入" + str + "内容 [输入单行,回车确认] :");
        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNextLine())
            str = scanner.nextLine();
        else safeExit();
        str = str.trim();
        if (str.isEmpty())  str = Window.EMPTY_CONTENT;
        return str;
    }

    /**
     * 用在输入流的读取上，针对用户输入DOS指令传入EOF导致程序抛出异常
     */
    public static void safeExit() throws InterruptedException {
        System.out.println();
        System.out.println();
        System.out.println("收到退出信号");
        System.out.println();
        System.out.println("    正在安全退出");
        System.out.println();
        Thread.sleep(1500);
        System.out.println("        退出成功");
        System.exit(0);
        System.out.println();
    }
}
