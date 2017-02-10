package org.prootype.apache.spark;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.prootype.apache.spark.spark.examples.WordCountRun;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class WordCountRunTest {

    private SparkSession spark = SparkSession
            .builder()
            .master("local[*]")
            .appName("LesFurets @ Snowcamp - CountTest")
            .getOrCreate();

    private Dataset<Row> text;

    @BeforeEach
    public void before() {
        Row row = RowFactory.create("this file has content, this file is nice, this is it");

        StructField field = new StructField("lines", DataTypes.StringType, false, Metadata.empty());
        StructType schema = new StructType(new StructField[]{field});

        text = spark.createDataFrame(singletonList(row), schema);
    }

    @Test
    public void should_file_split_return_ordered_word_count() {
        Dataset<Row> wordCount = WordCountRun.countWords(text);

        assertEquals(7, wordCount.count());
        assertEquals("this", wordCount.first().<String>getAs("word"));
        assertEquals(3, (long) wordCount.first().<Long>getAs("count"));
    }

}