package com.sandyagor.ball.utils;

import com.sandyagor.ball.beans.Coordinate;
import com.sandyagor.ball.beans.Line;

import java.util.ArrayList;
import java.util.List;

public class BallUtils {

    public static int MAX = 2;
    /**
     * 获取一个全量的田字格坐标值
     * @return
     */
   public static List<Coordinate> getCoordinates(){
       List<Coordinate> coordinates =new ArrayList<Coordinate>();
       for(int x=0;x<MAX+1;x++){
           for(int y=0;y<MAX+1;y++){
               coordinates.add(new Coordinate(x,y));
           }
       }
       return coordinates;
    }

    /**
     * 获取相邻的坐标值
     * @param coordinate
     * @return
     */
    public static List<Coordinate> getCloseCoordinate(Coordinate coordinate){
        List<Coordinate> coordinates =new ArrayList<Coordinate>();
       int x = coordinate.getX();
       int y = coordinate.getY();
       coordinates.addAll(getChange(x,y,true));
       coordinates.addAll(getChange(x,y,false));
        return coordinates;
    }

    private static List<Coordinate> getChange(int x,int y,boolean first){
        List<Coordinate> coordinates =new ArrayList<Coordinate>();
        if(first){//横坐标切换
            if(x+1<MAX || x+1==MAX){
                Coordinate coordinate = new Coordinate(x+1,y);
                coordinates.add(coordinate);
            }
            if(x-1>0 || x-1 == 0){
                Coordinate coordinate = new Coordinate(x-1,y);
                if(!coordinates.contains(coordinate)){
                    coordinates.add(coordinate);
                }
            }
        }else{
            if(y+1<MAX || y+1==MAX){
                Coordinate coordinate = new Coordinate(x,y+1);
                coordinates.add(coordinate);
            }
            if(y-1>0 || y-1 == 0){
                Coordinate coordinate = new Coordinate(x,y-1);
                if(!coordinates.contains(coordinate)){
                    coordinates.add(coordinate);
                }

            }
        }

        return coordinates;
    }

    /**
     * 获取最近的线直到没有线或者线已经被使用完毕
     * @param line
     * @return
     */
    public static List<Line> getCloseLine(Line line,List<Line> usedLines){
        Coordinate xCoord = line.getxCoord();
        Coordinate yCoord = line.getyCoord();
        //获取y左边最近的坐标值
        List<Coordinate> nextCoordinates = getCloseCoordinate(yCoord);
        nextCoordinates.remove(xCoord);
        List<Line> lines = new ArrayList<>();
        for (Coordinate coord :nextCoordinates){
            Line nextLine = new Line(yCoord,coord);
            if(!isSame(line,nextLine) && !isUsedOrRund(nextLine,usedLines)){//不能与开始的线相同，且不能是使用过的，且不能为回头路
                lines.add(nextLine);
            }
        }
        return lines;
    }

    /**
     * 判断这条线是否已经被使用了
     * @param nextLine
     * @param usedLines
     * @return
     */
    private static boolean isUsedOrRund(Line nextLine, List<Line> usedLines) {
        for (Line used : usedLines) {
            if (used.getxCoord().toString().equals(nextLine.getxCoord().toString())
                    && used.getyCoord().toString().equals(nextLine.getyCoord().toString())) {//is used
                return true;
            }
            if(used.getxCoord().toString().equals(nextLine.getyCoord().toString())
                    && used.getyCoord().toString().equals(nextLine.getxCoord().toString())){//is round
                return true;
            }
        }
        return false;
    }

    /**
     * 是否反向线
     * @param line
     * @param nextLine
     * @return
     */
    private static boolean isSame(Line line, Line nextLine) {
        if(line.getxCoord().toString().equals(nextLine.getyCoord().toString())
        && line.getyCoord().toString().equals(nextLine.getxCoord().toString())){
            return true;
        }
        return false;
    }

    public static List<Line> getAllLines(){
        List<Coordinate> coordinates = getCoordinates();//获取所有坐标
        List<Line> lines = new ArrayList<>();
        for(Coordinate coord:coordinates){//遍历坐标获取最近的坐标，组成相邻的线（双向线条）
            List<Coordinate> coords = getCloseCoordinate(coord);
            for (Coordinate nextCoor:coords) {
                Line xline = new Line(coord,nextCoor);
                Line yline = new Line(nextCoor,coord);
                if(!isIn(xline,lines)){
                    lines.add(xline);
                }
                if(!isIn(yline,lines)){
                    lines.add(yline);
                }
            }
        }
        return lines;
    }
    public static boolean isIn(Line line,List<Line> lines){
        for(Line li:lines){
            Coordinate xCoord = li.getxCoord();
            Coordinate yCoord = li.getyCoord();
            if(xCoord.toString().equals(line.getxCoord().toString())
            && yCoord.toString().equals(line.getyCoord().toString())){//横纵坐标相同
                return true;
            }
            if(xCoord.toString().equals(line.getyCoord().toString())
            && yCoord.toString().equals(line.getxCoord().toString())){

            }
        }
        return false;
    }

    static int i=0;
    public static void droupLine(Line line,List<Line> usedLines){
        usedLines.add(line);
        //System.out.println("当前画的线是："+line.toString()+"被使用的线有："+usedLines);
        List<Line> nextLines = getCloseLine(line,usedLines);
       // System.out.println((i++)+":当前画的线是："+line.toString()+"这条线相邻的线是："+nextLines);
        if(null != nextLines && nextLines.size()>0){//还存在相邻的线
            for (Line next : nextLines){
                //重置被使用的线
               // System.out.println("接上条线："+line.toString()+"\t准备开始画"+next.toString());
               // System.out.println("重置前被使用的线为：" + usedLines);
                usedLines = resetUsedLine(line,usedLines,next);
               // System.out.println("重置后被使用的线为：" + usedLines);
                droupLine(next,usedLines);
            }
        }else{
            System.out.println("第"+(++i)+"次画:"+usedLines+"\n");
            //i=0;
            if(usedLines.size()==MAX*6){
                System.out.println("田 ：" + usedLines);
            }
        }
    }

    /**
     * 重置被使用的线
     * @param line
     * @param usedLines
     */
    private static List<Line> resetUsedLine(Line line, List<Line> usedLines,Line next) {
        List<Line> useds = new ArrayList<>();
        for (Line used : usedLines){
            useds.add(used);
            if(used.getxCoord().toString().equals(line.getxCoord().toString()) &&
            used.getyCoord().toString().equals(line.getyCoord().toString())){//如果当前的线,所在的位置
               break;
            }
        }
        return useds;
    }

    /**
     * 第一根线是否和第二根线条相衔接
     * @param line
     * @param next
     * @return
     */
    private static boolean isNext(Line line, Line next) {
        if(line.getyCoord().toString().equals(next.getxCoord().toString())){
            return true;
        }
        return false;
    }

    public static void showLines(List<Line> lines){
        lines.forEach(line -> {
            System.out.printf(line.toString());
        });
        System.out.println("");
    }
    public static void main(String[] args) {
        List<Line> lines = getAllLines();
        System.out.println("lines = " + lines.size());
        lines.forEach(line ->{//遍历所有线条
            droupLine(line,new ArrayList<>());
        });
/*        Coordinate x = new Coordinate(0,0);
        Coordinate y = new Coordinate(0,1);
        Coordinate z = new Coordinate(1,1);
        Line a = new Line(x,y);
        Line b = new Line(y,z);
        List<Line> lines = new ArrayList<>();
        lines.add(b);
        List<Line> colseLine = getCloseLine(a,lines);
        System.out.println("colseLine = " + colseLine);*/
    }
}
