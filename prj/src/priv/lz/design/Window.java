package priv.lz.design;

import java.util.List;

public class Window {
    public static final String BACK = "#";
    public static final String EXIT = "*";
    public static final String ADD = "+";
    public static final String DELETE = "-";
    public static final String EDIT = "/";
    public static final String EMPTY_CONTENT = "该内容为空，为他加些内容吧！";

    public static void mainMenu() {
        edge();
        title("景点导游系统");
        indentStr("1 场馆展览详情");
        indentStr("2 园区景点显示");
        indentStr("3 相关路径查询");
        indentStr("4 最佳路线推荐");
        indentStr(EXIT + " 退出系统");
        edge();
    }

    /**
     *1 场馆展览详情菜单
     * @param vexes 顶点集
     */
    public static void detailMenu(List<String> vexes) {
        edge();
        title("场馆展览详情");
        System.out.println("请选择对应场馆查看详细信息");
        displayVexesMap(vexes);
        backAndExit();
        edge();
    }

    /**
     * 详情信息展示
     * @param name 标题
     * @param description 内容
     */
    public static void detailDescription(String name, String description) {
        edge();
        title(name);
        //内容
        boolean isEmptyContent = description.equals(EMPTY_CONTENT);
        System.out.print("   ");
        if (description.length() <= 18) {
            System.out.println(description);
        } else {
            System.out.println(description.substring(0, 18));
            description = description.substring(18);
            while (description.length() > 20) {
                System.out.println(description.substring(0, 20));
                description = description.substring(20);
            }
            System.out.println(description);
        }
        System.out.println();

        if (isEmptyContent) {
            indentStr(ADD + " 添加信息");
        } else {
            indentStr(EDIT + " 修改");
            indentStr(DELETE + " 删除");
        }
        backAndExit();
        edge();
    }

    /**
     * 2 园区景点显示菜单
     * @param vexes 顶点集
     */
    public static void navMenu(List<String> vexes) {
        edge();
        title("园区景点显示");
        System.out.println("请选择当前场馆");
        displayVexesMap(vexes);
        backAndExit();
        edge();
    }

    /**
     * 指定结点邻接信息展示
     * @param curr 选定结点名
     * @param adjVexes 邻接结点集合
     * @param values 邻接结点集对应的权值集合
     */
    public static void adjDescription(String curr, List<String> adjVexes, List<Integer> values) {
        edge();
        title("当前：" + curr);
        for (int i = 0; i < adjVexes.size(); ++i) {
            indentStr(values.get(i) + "米可直达" + adjVexes.get(i));
        }
        System.out.println();
        backAndExit();
        edge();
    }

    /**
     * 3 相关路径查询菜单
     */
    public static void pathSearchMenu() {
        edge();
        title("相关路径查询");
        indentStr("1 查询其他场馆到选定位置的最短简单路径及距离");
        indentStr("2 查询两个位置间的所有路径信息");
        backAndExit();
        edge();
    }

    /**
     * 结点映射窗口，提供用户便利选择结点
     * @param vexes 结点集
     */
    public static void vexSelectDescription(List<String> vexes) {
        edge();
        title("请选择指定场馆");
        displayVexesMap(vexes);
        backAndExit();
        edge();
    }

    /**
     * 所有结点到指定位置的最短简单路径及距离展示
     * @param dest 到指定结点名
     * @param paths 所有符合条件最短简单路径名集合
     * @param values 所有最短路径对应权值
     */
    public static void shortPathsToDestDescription(String dest, List<String> paths, List<Integer> values) {
        edge();
        title("前往 " + dest);
        for (int i = 0; i < paths.size(); ++i) {
            indentStr(paths.get(i) + " 共计" + values.get(i) + "米");
        }
        System.out.println();
        backAndExit();
        edge();
    }

    /**
     * 两位置间的所有路径信息展示（所有简单路径和距离，以及最短简单路径及距离）
     * @param begin 起始位置
     * @param end 终点位置
     * @param simplePaths 两位置间所有简单路径集合
     * @param values 两位置简单路径对应权值集合
     * @param index 最短简单路径在简单路径集中的索引
     */
    public static void allPathsDescription(String begin, String end, List<String> simplePaths, List<Integer> values, int index) {
        edge();
        title(begin + " - " + end);
        indentStr("所有简单路径信息：");
        for (int i = 0; i < simplePaths.size(); ++i) {
            indentStr(simplePaths.get(i) + " 共计" + values.get(i) + "米");
        }
        System.out.println();
        indentStr("最短简单路径：");
        indentStr(simplePaths.get(index) + " 共计" + values.get(index) + "米");
        System.out.println();
        backAndExit();
        edge();
    }

    /**
     * 4 最佳路线推荐
     * @param vexes 结点集
     */
    public static void bestRouteMenu(List<String> vexes) {
        edge();
        title("最佳路线推荐");
        System.out.println("请选择指定场馆");
        System.out.println("将为您匹配最佳游览路线");
        displayVexesMap(vexes);
        backAndExit();
        edge();
    }

    /**
     * 最佳游览路线展示
     * @param route 最佳路线
     * @param value 路线距离
     */
    public static void bestRouteDescription(String route, int value) {
        edge();
        title("最佳路线推荐");
        indentStr(route);
        indentStr("全长" + value + "米");
        indentStr("带你花最少的时间体验最多的欢乐");
        System.out.println();
        backAndExit();
        edge();
    }


    /**
     * 打印顶点序号映射表，提供用户参考选择
     * @param vexes 顶点集
     */
    public static void displayVexesMap(List<String> vexes) {
        int i = 0;
        for (String s : vexes) {
            indentStr(i + " " + s);
            i++;
        }
    }

    public static void edge() {
        System.out.println("++++++++++++++++++++++++++++++");
    }
    public static void indentStr(String str) {
        System.out.println("         " + str);
    }
    public static void title(String str) {
        System.out.println("         *" + str + "*");
        System.out.println();
    }
    public static void backAndExit() {
        indentStr(BACK + " 返回上级菜单");
        indentStr(EXIT + " 退出系统");
    }
    public static void divideLine() {
        System.out.println();
        System.out.println();
        System.out.println("————————————————————————————————————————");
        System.out.println();
        System.out.println();
    }
    public static void divideLine(String str) {
        System.out.println();
        System.out.println();
        System.out.println("—————————————————" + str + "—————————————————");
        System.out.println();
        System.out.println();
    }
}
