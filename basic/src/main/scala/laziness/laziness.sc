def eagerBoth(f: (Int, Int) => Int): Int =
  f({println("one"); 1}, {println("two"); 2})

eagerBoth(_ + _)

eagerBoth((a, _) => a)

eagerBoth((_, b) => b)

def lazySecond(f: (Int, => Int) => Int): Int =
  f({println("one"); 1}, {println("two"); 2})

lazySecond(_ + _)

lazySecond((a, _) => a)

lazySecond((_, b) => b)

def lazyFirst(f: (=> Int, Int) => Int): Int =
  f({println("one"); 1}, {println("two"); 2})

lazyFirst(_ + _)

lazyFirst((a, _) => a)

lazyFirst((_, b) => b)

def lazyBoth(f: (=> Int, => Int) => Int): Int =
  f({println("one"); 1}, {println("two"); 2})

lazyBoth(_ + _)

lazyBoth((a, _) => a)

lazyBoth((_, b) => b)
