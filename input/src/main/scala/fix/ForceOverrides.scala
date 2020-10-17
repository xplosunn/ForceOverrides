/*
rule = ForceOverrides
*/
package fix

object ForceOverrides {
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
    override def toString: String = "StdOut"
  }
}
