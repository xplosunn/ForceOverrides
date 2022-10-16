/*
rule = fix.ForceOverrides
 */
package fix

import scala.annotation.tailrec
import scala.util.Random

object Examples {
  trait Printer {
    def print(s: String): Unit
  }

  abstract class Writer {
    def write(s: String): Unit
  }

  object StdOut extends Writer with Printer {
    def print(sx: String): Unit = System.out.print(sx)
    def myprint(sx: String): Unit = System.out.print(sx)
    def write(sx: String): Unit = System.out.print(sx)
    override def toString: String = "StdOut"
  }

  class StdOutImpl extends Writer with Printer {
    def print(sx: String): Unit = System.out.print(sx)
    def myprint(sx: String): Unit = System.out.print(sx)
    def write(sx: String): Unit = System.out.print(sx)
    override def toString: String = "StdOutImpl"
  }

  class StdOutOverrideImpl extends Writer with Printer {
    override def print(sx: String): Unit = System.out.print(sx)
    def myprint(sx: String): Unit = System.out.print(sx)
    override def write(sx: String): Unit = System.out.print(sx)
    override def toString: String = "StdOutOverrideImpl"
  }

  trait Super1 {
    def method1(): Unit
  }

  trait Super2 extends Super1 {
    def method2(): Unit
  }

  trait Super3 extends Super2 {
    def method3(): Unit
  }

  class ExtendsSuper extends Super3 {
    def method3(): Unit = ()
    def method2(): Unit = ()
    def method1(): Unit = ()
  }

  class TailRec extends Super1 {
    @tailrec
    final def method1(): Unit = if (Random.nextInt() == 0) method1()
  }
}
