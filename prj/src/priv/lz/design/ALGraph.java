package priv.lz.design;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ALGraph {
    public int vexNum; //结点数
    public int arcNum; //边数
    public List<String> vexes; //结点集 (下标与结点名对应,以下将用下标操作)
    public List<String> info;  //结点信息集 (下标与结点集的结点对应)
    public List<ArcNode> vertices; //邻接表

    public String path;

    static class ArcNode {
        public int node; //对应结点
        public int value; //权值(路径)
        public ArcNode next;
    }

    public ALGraph(String path) throws IOException {
        this.path = path;
        createGraph();
    }

    public List<String> getAdjVexes(int index) {
        ArcNode p = vertices.get(index);
        List<String> list = new ArrayList<>();
        while (p.next != null) {
            list.add(vexes.get(p.next.node));
            p = p.next;
        }
        return list;
    }

    public List<Integer> getValues(int index) {
        ArcNode p = vertices.get(index);
        List<Integer> list = new ArrayList<>();
        while (p.next != null) {
            list.add(p.next.value);
            p = p.next;
        }
        return list;
    }

    public void updateInfo(int index, String content) throws IOException {
        info.set(index, content);
        updateGraph();
    }

    /**
     * 从指定结点出发经过所有结点(可重复)且路径最短的路径
     * @param index 指定结点的序号
     * @param routeInfo 路径信息将导入该参数，具体包括路径串和对应权值串的共两个字符串
     */
    public void bestRoute(int index, List<String> routeInfo) {
        int[][] valueFloyd = new int[vexNum][vexNum];
        int[][] pathFloyd = new int[vexNum][vexNum];
        shortPathsByFloyd(valueFloyd, pathFloyd);

        int[] permutation = new int[vexNum];
        permutation[0] = index;
        int k = 0;
        for (int i = 1; i < vexNum; ++i) {
            if (k == index) ++k;
            permutation[i] = k++;
        }
        int value = permutationValue(permutation, valueFloyd);
        int[] shortPermu = Arrays.copyOf(permutation, permutation.length);
        while (true) {
            int i, j, valueTemp;
            for (i = vexNum-2; i > 0 && permutation[i] >= permutation[i+1]; --i) ;
            if (i <= 0) break;
            for (j = vexNum-1; permutation[j] <= permutation[i]; --j) ;
            permutation[i] = permutation[i] ^ permutation[j];
            permutation[j] = permutation[i] ^ permutation[j];
            permutation[i] = permutation[i] ^ permutation[j];
            Arrays.sort(permutation, i+1, vexNum);
            valueTemp = permutationValue(permutation, valueFloyd);
            if (valueTemp < value) {
                value = valueTemp;
                shortPermu = Arrays.copyOf(permutation, permutation.length);
            }
        }
        String path, temp;
        int vex1, vex2;
        path = vexes.get(shortPermu[0]);
        for (int i = 0, j = 1; j < vexNum; ++i, ++j) {
            vex1 = shortPermu[i];
            vex2 = shortPermu[j];
            temp = parseFloydPath(vex1, vex2, pathFloyd);
            int length = vexes.get(vex1).length();
            temp = temp.substring(length);
            path += temp;
        }
        routeInfo.add(path);
        routeInfo.add(String.valueOf(value));
    }

    public int permutationValue(int[] permutation, int[][] values) {
        int vex1, vex2;
        int value = 0;
        for (int i = 0, j = 1; j < permutation.length; ++i, ++j) {
            vex1 = permutation[i];
            vex2 = permutation[j];
            value += values[vex1][vex2];
        }
        return value;
    }

    public String parseFloydPath(int begin, int end, int[][] pathFloyd) {
        String path = vexes.get(begin);
        int k = begin;
        while (k != end) {
            k = pathFloyd[k][end];
            path += ("->" + vexes.get(k));
        }
        return path;
    }

    /**
     * 两结点的所有简单路径信息
     * @param begin 结点一序号
     * @param end   结点二序号
     * @param simplePaths 将两节点的所有简单路径导入该参数
     * @param values 将两节点简单路径对应权值集合导入该参数
     * @return 最短简单路径在simplePaths路径集中的索引
     */
    public int allPaths(int begin, int end, List<String> simplePaths, List<Integer> values) {
        String path = vexes.get(begin);
        int value = 0;
        boolean[] isVisit = new boolean[vexNum];
        dfs1(begin, end, path, value, simplePaths, values, isVisit);
        int min = Integer.MAX_VALUE;
        int index = 0;
        for (int i = 0; i < values.size(); ++i) {
            if (values.get(i) < min) {
                min = values.get(i);
                index = i;
            }
        }
        return index;
    }

    public void dfs1(int begin, int end, String path, int value, List<String> simplePaths, List<Integer> values, boolean[] isVisit) {
        if (begin == end) {
            simplePaths.add(path);
            values.add(value);
            return;
        }
        isVisit[begin] = true;
        ArcNode p = vertices.get(begin);
        while (p.next != null) {
            if (! isVisit[p.next.node]) {
                String strSeg = "->" + vexes.get(p.next.node);
                int num = p.next.value;
                dfs1(p.next.node, end, path+strSeg, value+num, simplePaths, values, isVisit);
            }
            p = p.next;
        }
        isVisit[begin] = false;
    }

    /**
     * 到指定位置的所有最短简单路径
     * @param dest 指定结点序号
     * @param paths 将最短路径集导出到该参数
     * @param values 将最短路径集对应的权值集合导入该参数
     */
    public void shortPathsToDest(int dest, List<String> paths, List<Integer> values) {
        int[][] value = new int[vexNum][vexNum];
        int[][] path = new int[vexNum][vexNum];
        shortPathsByFloyd(value, path);
        String temp;
        for (int i = 0; i < vexNum; ++i) {
            if (i == dest) continue;
            temp = vexes.get(i);
            int k = i;
            while (k != dest) {
                k = path[k][dest];
                temp += ("->" + vexes.get(k));
            }
            paths.add(temp);
            values.add(value[i][dest]);
        }
    }

    public void shortPathsByFloyd(int[][] dp, int[][] path) {
//        int[][] dp = new int[vexNum][vexNum];   //权值
//        int[][] path = new int[vexNum][vexNum]; //动态路径
        final int max = 1000000;
        for (int[] row : dp) {
            Arrays.fill(row, max);
        }
        for (int[] row : path) {
            for (int j = 0; j < vexNum; ++j)
                row[j] = -1;
        }
        for (int i = 0; i < vexNum; ++i) {  //邻接表转邻接矩阵
            ArcNode p = vertices.get(i);
            while (p.next != null) {
                dp[i][p.next.node] = p.next.value;
                path[i][p.next.node] = p.next.node;
                p = p.next;
            }
        }
        for (int k = 0; k < vexNum; ++k)
            for (int i = 0; i < vexNum; ++i)
                for (int j = 0; j < vexNum; ++j) {
                    if (k == i) break;
                    else if (k == j) continue;
                    else if (i == j) continue;

                    if (dp[i][k] + dp[k][j] < dp[i][j]) {
                        dp[i][j] = dp[i][k] + dp[k][j];
                        path[i][j] = path[i][k];
                    }
                }
    }

    public void updateGraph() throws IOException {
        saveFile();
        createGraph();
    }

    public void createGraph() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path, StandardCharsets.UTF_8));
        vexNum = Integer.parseInt(reader.readLine());
        arcNum = Integer.parseInt(reader.readLine());
        vexes = new ArrayList<>();
        vertices = new ArrayList<>();
        info = new ArrayList<>();
        for (int i = 0; i < vexNum; ++i) {
            vexes.add(reader.readLine());
            info.add(reader.readLine());
            vertices.add(new ArcNode()); //头结点
        }
        String[] temps;
        for (int i = 0; i < arcNum; ++i) {
            temps = reader.readLine().split(" ");
            ArcNode arcNode1 = new ArcNode();
            arcNode1.node = vexes.indexOf(temps[1]);
            arcNode1.value = Integer.parseInt(temps[2]);
            ArcNode head = vertices.get(vexes.indexOf(temps[0]));
            arcNode1.next = head.next;
            head.next = arcNode1;
            ArcNode arcNode2 = new ArcNode();
            arcNode2.node = vexes.indexOf(temps[0]);
            arcNode2.value = arcNode1.value;
            head = vertices.get(vexes.indexOf(temps[1]));
            arcNode2.next = head.next;
            head.next = arcNode2;
        }
        reader.close();
    }

    public void saveFile() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(path, StandardCharsets.UTF_8));
        writer.write(vexNum + "\n");
        writer.write(arcNum + "\n");
        for (int i = 0; i < vexNum; ++i) {
            writer.write(vexes.get(i) + "\n");
            writer.write(info.get(i) + "\n");
        }
        for (int i = 0; i < vexNum; ++i) {
            String node = vexes.get(i);
            ArcNode head = vertices.get(i);
            while (head.next != null) {
                if (head.next.node > i) {
                    String adjNode = vexes.get(head.next.node);
                    writer.write(node + " " + adjNode + " " + head.next.value + "\n");
                }
                head = head.next;
            }
        }
        writer.flush();
        writer.close();
    }

}
