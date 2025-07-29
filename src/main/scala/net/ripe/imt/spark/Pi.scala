package pro.zavodnikov.spark

import scala.math.random
import org.apache.spark.sql.SparkSession

/**
 * See:
 *      https://github.com/apache/spark/blob/master/examples/src/main/scala/org/apache/spark/examples/SparkPi.scala
 */
object Pi {
    def main(args: Array[String]): Unit = {
        val slices = if (args.length > 0) args(0).toInt else 3 // Number of cluster nodes.
        val n = math.min(100000 * slices, Int.MaxValue).toInt // To avoid overflow.

        val spark = SparkSession.builder().getOrCreate()

        val count = spark.sparkContext.parallelize(1 until n, slices).map {
            i =>
                val x = random() * 2 - 1
                val y = random() * 2 - 1
                if (x*x + y*y <= 1) 1 else 0
        }.reduce(_ + _)

        println(s"Pi is roughly ${ 4.0 * count / (n - 1) }")
        spark.stop()
    }
}
