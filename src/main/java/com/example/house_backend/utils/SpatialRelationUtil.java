package com.example.house_backend.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class SpatialRelationUtil {
    public static class Point {
        BigDecimal x;
        BigDecimal y;

        public Point(BigDecimal x, BigDecimal y) {
            this.x = x;
            this.y = y;
        }
    }
    /**
     * 返回一个点是否在一个多边形区域内
     *
     * @param mPoints 多边形坐标点列表
     * @param point   待判断点
     * @return true 多边形包含这个点,false 多边形未包含这个点。
     */
    public static boolean isPolygonContainsPoint(List<Point> mPoints, Point point) {
        int nCross = 0;
        for (int i = 0; i < mPoints.size(); i++) {
            Point p1 = mPoints.get(i);
            Point p2 = mPoints.get((i + 1) % mPoints.size());
            // 取多边形任意一个边,做点point的水平延长线,求解与当前边的交点个数
            // p1p2是水平线段,要么没有交点,要么有无限个交点
            if (p1.y.compareTo(p2.y) == 0)
                continue;
            // point 在p1p2 底部 --> 无交点
            if (point.y.compareTo((p1.y.compareTo(p2.y) < 0 ? p1.y : p2.y)) < 0)
                continue;
            // point 在p1p2 顶部 --> 无交点
            if (point.y.compareTo((p1.y.compareTo(p2.y) > 0 ? p1.y : p2.y)) > 0)
                continue;
            // 求解 point点水平线与当前p1p2边的交点的 X 坐标

            BigDecimal x = (point.y.subtract(p1.y)).multiply(p2.x.subtract(p1.x)).divide(p2.y.subtract(p1.y),6, RoundingMode.HALF_UP).add(p1.x);
            if (x.compareTo(point.x) > 0) // 当x>point.x时,说明point在p1p2线段上
                nCross++; // 只统计单边交点
        }
        // 单边交点为偶数，点在多边形之外 ---
        return (nCross % 2 == 1);
    }
}
