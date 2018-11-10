package com.sandyagor.ball.utils;

import com.sandyagor.ball.beans.Coordinate;
import com.sandyagor.ball.beans.Line;

import java.util.ArrayList;
import java.util.List;

public class BallUtils {
    /**
     * 获取一个全量的田字格坐标值
     * @return
     */
   public static List<Coordinate> getCoordinates(){
       List<Coordinate> coordinates =new ArrayList<Coordinate>();
       for(int x=0;x<3;x++){
           for(int y=0;y<3;y++){
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
    private static List<Coordinate> getChange(int a,int b){
        List<Coordinate> coordinates =new ArrayList<Coordinate>();
        if(a+1<2 || a+1==2){
            Coordinate coordinate = new Coordinate(a+1,b);
            coordinates.add(coordinate);
        }
        if(a-1>0 || a-1 == 0){
            Coordinate coordinate = new Coordinate(a-1,b);
            coordinates.add(coordinate);
        }
        return coordinates;
    }
    private static List<Coordinate> getChange(int x,int y,boolean first){
        List<Coordinate> coordinates =new ArrayList<Coordinate>();
        if(first){//横坐标切换
            if(x+1<2 || x+1==2){
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
            if(y+1<2 || y+1==2){
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
    public static List<Line> getCloseLine(Line line){
        Coordinate xCoord = line.getxCoord();
        Coordinate yCoord = line.getyCoord();
        //获取y左边最近的坐标值
        List<Coordinate> nextCoordinates = getCloseCoordinate(yCoord);
        nextCoordinates.remove(xCoord);
        List<Line> lines = new ArrayList<>();
        for (Coordinate coord :nextCoordinates){
            Line nextLine = new Line(yCoord,coord);
            if(!isSame(line,nextLine)){
                lines.add(nextLine);
            }
        }
        return lines;
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

    public static List<Line> droupLine(Line line,List<Line> usedLines){
        usedLines.add(line);//这个线已经被使用了
        List<Line> droupLine = new ArrayList<Line>();
        //判断是否已经组成一个田
        if(usedLines.size()==12){
            System.out.println("田：" + usedLines);
        }
        List<Line> nextLines = getCloseLine(line);
        nextLines.forEach(next -> {
            if(!isIn(next,usedLines)){
               droupLine(next,usedLines);
            }
            System.out.println("耗尽："+usedLines );
        });
        return usedLines;
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
            droupLine(line,new ArrayList<Line>());
        });
    }
}
