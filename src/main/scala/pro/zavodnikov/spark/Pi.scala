/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2023-2025 Dmitry Zavodnikov
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package pro.zavodnikov.spark

import scala.math.random
import org.apache.spark.sql.SparkSession

/** See:
  * https://github.com/apache/spark/blob/master/examples/src/main/scala/org/apache/spark/examples/SparkPi.scala
  */
object Pi {
  def main(args: Array[String]): Unit = {
    val slices =
      if (args.length > 0) args(0).toInt else 3 // Number of cluster nodes.
    val n = math.min(100000 * slices, Int.MaxValue).toInt // To avoid overflow.

    val spark = SparkSession.builder().getOrCreate()

    val count = spark.sparkContext
      .parallelize(1 until n, slices)
      .map { i =>
        val x = random() * 2 - 1
        val y = random() * 2 - 1
        if (x * x + y * y <= 1) 1 else 0
      }
      .reduce(_ + _)

    println(s"Pi is roughly ${4.0 * count / (n - 1)}")
    spark.stop()
  }
}
