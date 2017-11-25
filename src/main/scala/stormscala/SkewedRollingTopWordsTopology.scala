package stormscala 

import java.util.concurrent.TimeUnit

import org.apache.storm.Config
import org.apache.storm.LocalCluster
import org.apache.storm.topology.TopologyBuilder
import org.apache.storm.topology.base.BaseWindowedBolt
import org.apache.storm.tuple.Fields
import org.apache.storm.bolt.JoinBolt

import com.containant._
import com.containant.heuristics._

///////////////////////////////////

// Specific code for configuring the topology

case class SkewedRollingTopWordsConfig(
  wordSpout: FBolt,
  counterBolt: FBolt,
  aggBolt: FBolt,
  intermediateRankerBolt: FBolt,
  totalRankerBolt: FBolt,
  maxTaskParallelism: Int
)

object SkewedRollingTopWordsModule extends StormModule {
  def topConfig(
    wordSpout: FBolt,
    counterBolt: FBolt,
    aggBolt: FBolt,
    intermediateRankerBolt: FBolt,
    totalRankerBolt: FBolt,
    mtp: FParallelism
  ): SkewedRollingTopWordsConfig = SkewedRollingTopWordsConfig(
    wordSpout, counterBolt, aggBolt, intermediateRankerBolt, totalRankerBolt, mtp.toInt
  ) 
}

///////////////////////////////////

object SkewedRollingTopWordsTopology {

  def runConfig(cfg: SkewedRollingTopWordsConfig): Double = {
  
    val builder: TopologyBuilder = new TopologyBuilder

    builder
      .setSpout("wordSpout", new TestWordSpout, cfg.wordSpout.parallelismHint)
      .setCPULoad(cfg.wordSpout.cpuLoad)
      .setMemoryLoad(cfg.wordSpout.memoryLoadOnHeap, cfg.wordSpout.memoryLoadOffHeap)
    builder
      .setBolt("counterBolt", new RollingCountBolt(9, 3), cfg.counterBolt.parallelismHint)
      .partialKeyGrouping("wordSpout", new Fields("word"))
      .setCPULoad(cfg.counterBolt.cpuLoad)
      .setMemoryLoad(cfg.counterBolt.memoryLoadOnHeap, cfg.counterBolt.memoryLoadOffHeap)
    builder
      .setBolt("aggBolt", new RollingCountAggBolt, cfg.aggBolt.parallelismHint)
      .fieldsGrouping("counterBolt", new Fields("obj"))
      .setCPULoad(cfg.aggBolt.cpuLoad)
      .setMemoryLoad(cfg.aggBolt.memoryLoadOnHeap, cfg.aggBolt.memoryLoadOffHeap)
    builder
      .setBolt("intermediateRankerBolt", new IntermediateRankingsBolt(4), cfg.intermediateRankerBolt.parallelismHint)
      .fieldsGrouping("aggBolt", new Fields("obj"))
      .setCPULoad(cfg.intermediateRankerBolt.cpuLoad)
      .setMemoryLoad(cfg.intermediateRankerBolt.memoryLoadOnHeap, cfg.intermediateRankerBolt.memoryLoadOffHeap)
    builder
      .setBolt("totalRankerBolt", new TotalRankingsBolt(4), cfg.totalRankerBolt.parallelismHint)
      .globalGrouping("intermediateRankerBolt")
      .setCPULoad(cfg.totalRankerBolt.cpuLoad)
      .setMemoryLoad(cfg.totalRankerBolt.memoryLoadOnHeap, cfg.totalRankerBolt.memoryLoadOffHeap)

    val config = new Config
    config.setMaxTaskParallelism(cfg.maxTaskParallelism)
    config.setDebug(false)
    config.setTopologyStrategy(
      classOf[org.apache.storm.scheduler.resource.strategies.scheduling.DefaultResourceAwareStrategy]
    )

    var fitness = 0.0
    try {
      fitness = eval(builder,config) 
    } catch { case _: Throwable => fitness = -1.0 }
    println("Fitness: " + fitness)
    fitness
  }

  def eval(builder: TopologyBuilder, config: Config): Double = {
    TotalRankingsBolt.throughput = 0L
    
    val cluster: LocalCluster = new LocalCluster
    cluster.submitTopology("srtw", config, builder.createTopology)
    Thread.sleep(3000)
    TotalRankingsBolt.throughput = 0L
    Thread.sleep(3000)
    cluster.killTopology("srtw")
    cluster.shutdown

    TotalRankingsBolt.throughput.toDouble
  }


  def main(args: Array[String]): Unit = {
    val seed = if (args.length > 0) args(0).toInt else 0xDEADBEEF
    import com.containant.ContainAnt
    object CA extends ContainAnt( WithSeed(seed) )
    val result = CA create (SkewedRollingTopWordsModule, runConfig)
  }

}

// End ///////////////////////////////////////////////////////////////
