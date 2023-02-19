package priv.lz.design;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        String path = System.getProperty("user.dir") + "\\src\\data.txt";
//        String path = "D:\\ing\\DScpp\\design\\prj\\src\\data.txt";
        ALGraph graph = new ALGraph(path);
        String order;
        mainMenu:
        while (true) {  //主菜单
            Window.mainMenu();
            order = Util.validInput("1", "2", "3", "4", Window.EXIT);
            Window.divideLine();
            switch (order) {
                case Window.EXIT: break mainMenu;
                //1 场馆展览详情
                case "1": {
                    detailMenu:
                    while (true) {
                        Window.detailMenu(graph.vexes);
                        order = Util.validInput(graph.vexNum);
                        Window.divideLine();
                        switch (order) {    //显示用户选择场馆的介绍信息及操作界面
                            case Window.EXIT: break mainMenu;
                            case Window.BACK: break detailMenu;
                            default:
                                int index = Integer.parseInt(order);
                                Window.detailDescription(graph.vexes.get(index), graph.info.get(index));
                                if (graph.info.get(index).equals(Window.EMPTY_CONTENT))
                                    order = Util.validInput(Window.EXIT, Window.BACK, Window.ADD);
                                else order = Util.validInput(Window.EXIT, Window.BACK, Window.EDIT, Window.DELETE);
                                //用户对选定场馆操作的响应
                                if (order.equals(Window.EXIT)) break mainMenu;
                                else if (order.equals(Window.BACK)) {Window.divideLine();}
                                else if (order.equals(Window.ADD)) {
                                    String content = Util.contentInput("新建");
                                    graph.updateInfo(index, content);
                                    Window.divideLine("操作成功");
                                } else if (order.equals(Window.EDIT)) {
                                    String content = Util.contentInput("编辑");
                                    graph.updateInfo(index, content);
                                    Window.divideLine("操作成功");
                                } //Window.DELETE
                                else {
                                    graph.updateInfo(index, Window.EMPTY_CONTENT);
                                    Window.divideLine("操作成功");
                                }
                        }
                    }
                    break;
                }
                //2 园区景点显示
                case "2":{
                    navMenu:
                    while (true) {
                        Window.navMenu(graph.vexes);
                        order = Util.validInput(graph.vexNum);
                        Window.divideLine();
                        switch (order) {    //显示用户具体选定景点的邻接信息
                            case Window.EXIT: break mainMenu;
                            case Window.BACK: break navMenu;
                            default:
                                int index = Integer.parseInt(order);
                                String curr = graph.vexes.get(index);
                                List<String> adjVexes = graph.getAdjVexes(index);
                                List<Integer> values = graph.getValues(index);
                                Window.adjDescription(curr, adjVexes, values);
                                order = Util.validInput(Window.EXIT, Window.BACK);
                                Window.divideLine();
                                if (order.equals(Window.EXIT))  break mainMenu;
                                else break;
                        }
                    }
                }
                break;
                //3 相关路径查询
                case "3":{
                    pathSearchMenu:
                    while (true) {
                        Window.pathSearchMenu();
                        order = Util.validInput("1", "2", Window.BACK, Window.EXIT);
                        Window.divideLine();
                        switch (order) {    //显示用户选择的具体查询服务界面
                            case Window.EXIT: break mainMenu;
                            case Window.BACK: break pathSearchMenu;
                            case "1": { //1 查询所有场馆到指定位置的最短路径及距离
                                service1:
                                while (true) {
                                    Window.vexSelectDescription(graph.vexes);
                                    order = Util.validInput(graph.vexNum);
                                    Window.divideLine();
                                    switch (order) {    //用户对服务1选定场馆的查询响应
                                        case Window.EXIT: break mainMenu;
                                        case Window.BACK: break service1;
                                        default:
                                            int index = Integer.parseInt(order);
                                            List<String> paths = new ArrayList<>();
                                            List<Integer> values = new ArrayList<>();
                                            graph.shortPathsToDest(index, paths, values);
                                            Window.shortPathsToDestDescription(graph.vexes.get(index), paths, values);
                                            order = Util.validInput(Window.EXIT, Window.BACK);
                                            Window.divideLine();
                                            if (order.equals(Window.EXIT))  break mainMenu;
                                            else break;
                                    }
                                }
                            }
                            break;
                            case "2":  {    //2 查询两个位置间的所有路径信息
                                while (true) {
                                    Window.vexSelectDescription(graph.vexes);
                                    Window.indentStr("选择第一个位置");
                                    String order1 = Util.validInput(graph.vexNum);
                                    if (order1.equals(Window.EXIT)) {
                                        Window.divideLine();
                                        break mainMenu;
                                    } else if (order1.equals(Window.BACK)) {
                                        Window.divideLine();
                                        break ;
                                    }
                                    Window.indentStr("选择第二个位置");
                                    String order2 = Util.excludeInput(graph.vexNum, order1);
                                    if (order2.equals(Window.EXIT)) {
                                        Window.divideLine();
                                        break mainMenu;
                                    } else if (order2.equals(Window.BACK)) {
                                        Window.divideLine();
                                        break ;
                                    }
                                    Window.divideLine();
                                    //用户对服务2选定的两个场馆的查询响应
                                    int index1 = Integer.parseInt(order1);
                                    int index2 = Integer.parseInt(order2);
                                    List<String> simplePaths = new ArrayList<>();
                                    List<Integer> values = new ArrayList<>();
                                    int index = graph.allPaths(index1, index2, simplePaths, values);
                                    Window.allPathsDescription(graph.vexes.get(index1), graph.vexes.get(index2), simplePaths, values, index);
                                    order = Util.validInput(Window.EXIT, Window.BACK);
                                    Window.divideLine();
                                    if (order.equals(Window.EXIT))  break mainMenu;
                                    else continue ;
                                }
                            }
                            break;
                        }
                    }
                }
                break;
                //4 最佳路线推荐
                case "4": {
                    bestRouteMenu:
                    while (true) {
                        Window.bestRouteMenu(graph.vexes);
                        order = Util.validInput(graph.vexNum);
                        Window.divideLine();
                        switch (order) {
                            case Window.EXIT: break mainMenu;
                            case Window.BACK: break bestRouteMenu;
                            default:
                                int index = Integer.parseInt(order);
                                List<String> routeInfo = new ArrayList<>();
                                graph.bestRoute(index, routeInfo);
                                String route = routeInfo.get(0);
                                int value = Integer.parseInt(routeInfo.get(1));
                                Window.bestRouteDescription(route, value);
                                order = Util.validInput(Window.EXIT, Window.BACK);
                                if (order.equals(Window.EXIT))  break mainMenu;
                                else continue ;
                        }
                    }
                }
                break ;
            }
        }
        Window.divideLine("成功退出");

    }
}
