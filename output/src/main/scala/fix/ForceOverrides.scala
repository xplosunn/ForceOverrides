package fix

object ForceOverrides {
  trait Printer {
    def print(s: String): Unit
  }

  object StdOut extends Printer {
    override def print(sx: String): Unit = System.out.print(sx)
    def myprint(sx: String): Unit = System.out.print(sx)
    override def toString: String = "StdOut"
  }
}
