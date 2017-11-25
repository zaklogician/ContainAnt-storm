package stormscala

import com.containant._
import com.containant.heuristics._

// Generic module for configuring Apache Storm applications

case class FCPULoad(toInt: Int)
case class FMemoryLoad(toInt: Int)
case class FParallelism(toInt: Int)

case class FBolt(
  cpuLoad: Int,
  memoryLoadOnHeap: Int,
  memoryLoadOffHeap: Int,
  parallelismHint: Int
)

trait StormModule extends Module {
  val cpu10: FCPULoad = FCPULoad(10)
  val cpu20: FCPULoad = FCPULoad(20)
  val cpu30: FCPULoad = FCPULoad(30)
  val cpu40: FCPULoad = FCPULoad(40)
  val cpu50: FCPULoad = FCPULoad(50)
  val cpu60: FCPULoad = FCPULoad(60)
  val cpu70: FCPULoad = FCPULoad(70)
  val cpu80: FCPULoad = FCPULoad(80)
  val cpu90: FCPULoad = FCPULoad(90)
  val cpu100: FCPULoad = FCPULoad(100)

  val mem10: FMemoryLoad = FMemoryLoad(10)
  val mem20: FMemoryLoad = FMemoryLoad(20)
  val mem30: FMemoryLoad = FMemoryLoad(30)
  val mem40: FMemoryLoad = FMemoryLoad(40)
  val mem50: FMemoryLoad = FMemoryLoad(50)
  val mem60: FMemoryLoad = FMemoryLoad(60)
  val mem70: FMemoryLoad = FMemoryLoad(70)
  val mem80: FMemoryLoad = FMemoryLoad(80)
  val mem90: FMemoryLoad = FMemoryLoad(90)
  val mem100: FMemoryLoad = FMemoryLoad(100)

  val par1: FParallelism = FParallelism(1)
  val par2: FParallelism = FParallelism(2)
  val par3: FParallelism = FParallelism(3)
  val par4: FParallelism = FParallelism(4)
  val par5: FParallelism = FParallelism(5)
  val par6: FParallelism = FParallelism(6)
  val par7: FParallelism = FParallelism(7)
  val par8: FParallelism = FParallelism(8)
  val par9: FParallelism = FParallelism(9)
  val par10: FParallelism = FParallelism(10)
  val par11: FParallelism = FParallelism(11)
  val par12: FParallelism = FParallelism(12)
  
  def bolt(
    cl: FCPULoad,
    mlon: FMemoryLoad,
    mloff: FMemoryLoad,
    ph: FParallelism
  ): FBolt = FBolt(cl.toInt,mlon.toInt,mloff.toInt,ph.toInt)
}

// End ///////////////////////////////////////////////////////////////
