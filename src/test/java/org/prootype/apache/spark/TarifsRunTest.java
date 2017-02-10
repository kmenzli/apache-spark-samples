package org.prootype.apache.spark;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.prootype.apache.spark.spark.examples.TarifsRun;

import java.util.Arrays;
import java.util.List;

import static org.apache.spark.sql.types.DataTypes.DoubleType;
import static org.apache.spark.sql.types.DataTypes.StringType;
import static org.apache.spark.sql.types.Metadata.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TarifsRunTest {

    private static final SparkSession spark = SparkSession.builder()
            .appName("TestableStatistiquesPartenaireTest")
            .master("local[*]")
            .getOrCreate();

    private Dataset<Row> tarifs;

    @BeforeEach
    public void before() {
        List<Row> rows = Arrays.asList(RowFactory.create("F1", 50d, "assureur"),
                RowFactory.create("F1", 100d, "assureur"),
                RowFactory.create("F1", 70d, "assureur"));

        StructField formule = new StructField("formule", StringType, false, empty());
        StructField prime = new StructField("prime", DoubleType, false, empty());
        StructField assureur = new StructField("assureur", StringType, false, empty());
        StructType structType = new StructType(
                new StructField[]{formule, prime, assureur});

        tarifs = spark.createDataFrame(rows, structType);
    }

    @Test
    public void should_average_tarif_return_correct_average() {
        Dataset<Row> averagePrime = TarifsRun.averagePrime(tarifs);

        assertEquals(2, averagePrime.count());
        assertEquals("FORMULE 1", averagePrime.first().getAs("formule"));
        assertEquals("formule 1", averagePrime.first().getAs("formuleReadable"));
        assertEquals(75, (double) averagePrime.first().<Double>getAs("average"));
    }

}