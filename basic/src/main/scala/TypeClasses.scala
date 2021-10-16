object TypeClasses {

  trait JSONSerializable[T]:
    def toJson(value: T): String
    extension (value: T) def toJs = toJson(value)

  given stringJSON: JSONSerializable[Int] with
    def toJson(value: Int): String = value.toString

  given listJSON[T] (using ts: JSONSerializable[T]): JSONSerializable[List[T]] with
    def toJson(value: List[T]): String =
      value.map(ts.toJson(_)).mkString("[", ",", "]")

  extension[T] (list: List[T]) {
    def toJson(using ls: JSONSerializable[List[T]]) =
      ls.toJson(list)
  }

  def main(args: Array[String]): Unit = {
    println(List(1, 2, 3).toJson)
  }
}
