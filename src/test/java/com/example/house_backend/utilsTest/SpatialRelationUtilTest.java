package com.example.house_backend.utilsTest;

import com.example.house_backend.utils.SpatialRelationUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SpatialRelationUtilTest {

    @Test
    public void isPolygonContainsPointTest() {
        System.out.println("execute isPolygonContainsPointTest");
        List<SpatialRelationUtil.Point> mPoints = new ArrayList<>();
        mPoints.add(new SpatialRelationUtil.Point(new BigDecimal("501"), new BigDecimal("56")));
        mPoints.add(new SpatialRelationUtil.Point(new BigDecimal("309"), new BigDecimal("112")));
        mPoints.add(new SpatialRelationUtil.Point(new BigDecimal("269"), new BigDecimal("262")));
        mPoints.add(new SpatialRelationUtil.Point(new BigDecimal("516"), new BigDecimal("482")));
        mPoints.add(new SpatialRelationUtil.Point(new BigDecimal("665"), new BigDecimal("333")));
        mPoints.add(new SpatialRelationUtil.Point(new BigDecimal("555"), new BigDecimal("200")));
        mPoints.add(new SpatialRelationUtil.Point(new BigDecimal("779"), new BigDecimal("112")));
        Assertions.assertTrue(SpatialRelationUtil.isPolygonContainsPoint
                (mPoints, new SpatialRelationUtil.Point(new BigDecimal("422"), new BigDecimal("160"))));
        Assertions.assertTrue(SpatialRelationUtil.isPolygonContainsPoint
                (mPoints, new SpatialRelationUtil.Point(new BigDecimal("560"), new BigDecimal("386"))));
        Assertions.assertTrue(SpatialRelationUtil.isPolygonContainsPoint
                (mPoints, new SpatialRelationUtil.Point(new BigDecimal("692"), new BigDecimal("120"))));
        Assertions.assertTrue(SpatialRelationUtil.isPolygonContainsPoint
                (mPoints, new SpatialRelationUtil.Point(new BigDecimal("542"), new BigDecimal("83"))));
        Assertions.assertTrue(SpatialRelationUtil.isPolygonContainsPoint
                (mPoints, new SpatialRelationUtil.Point(new BigDecimal("521"), new BigDecimal("475"))));
        Assertions.assertFalse(SpatialRelationUtil.isPolygonContainsPoint
                (mPoints, new SpatialRelationUtil.Point(new BigDecimal("598"), new BigDecimal("215"))));
        Assertions.assertFalse(SpatialRelationUtil.isPolygonContainsPoint
                (mPoints, new SpatialRelationUtil.Point(new BigDecimal("167"), new BigDecimal("73"))));
        Assertions.assertFalse(SpatialRelationUtil.isPolygonContainsPoint
                (mPoints, new SpatialRelationUtil.Point(new BigDecimal("556"), new BigDecimal("537"))));
        Assertions.assertFalse(SpatialRelationUtil.isPolygonContainsPoint
                (mPoints, new SpatialRelationUtil.Point(new BigDecimal("927"), new BigDecimal("152"))));
    }

}
