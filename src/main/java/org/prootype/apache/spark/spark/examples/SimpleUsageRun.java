package org.prootype.apache.spark.spark.examples;

import org.prootype.apache.spark.spark.utils.SparkRunner;
import org.apache.spark.sql.SparkSession;

public class SimpleUsageRun extends SparkRunner {

    private static final SparkSession spark = SparkSession.builder()
            .master("local[*]")
            .getOrCreate();

    public static void main(String[] args) {
        spark.emptyDataFrame().show();
    }

}
