package stormscala 

import java.util.concurrent.TimeUnit

import org.apache.storm.Config
import org.apache.storm.LocalCluster
import org.apache.storm.topology.TopologyBuilder
import org.apache.storm.topology.base.BaseWindowedBolt
import org.apache.storm.tuple.Fields
import org.apache.storm.bolt.JoinBolt

object Main {

  def runConfig(cfg: TopConfig): Double = {
    val builder: TopologyBuilder = new TopologyBuilder
    builder
      .setSpout("spout", new IdSpout, cfg.spoutParallelismHint)
      .setCPULoad(cfg.spoutCPULoad)
      .setMemoryLoad(cfg.spoutMemoryLoadOnHeap, cfg.spoutMemoryLoadOffHeap)
    builder
      .setBolt("gender", new GenderBolt, cfg.genderBoltParallelismHint)
      .fieldsGrouping("spout", new Fields("id"))
      .setCPULoad(cfg.genderBoltCPULoad)
      .setMemoryLoad(cfg.genderBoltMemoryLoadOnHeap, cfg.genderBoltMemoryLoadOffHeap)
    builder
      .setBolt("age", new AgeBolt, cfg.ageBoltParallelismHint)
      .fieldsGrouping("spout", new Fields("id"))
      .setCPULoad(cfg.ageBoltCPULoad)
      .setMemoryLoad(cfg.ageBoltMemoryLoadOnHeap, cfg.ageBoltMemoryLoadOffHeap)

    val joinBolt: JoinBolt = new JoinBolt("gender", "id")
      .join("age", "id", "gender")
      .select("gender:id,age:id,gender,age")
      .withTumblingWindow(
        new BaseWindowedBolt.Duration(300, TimeUnit.MILLISECONDS)
      )
    builder
      .setBolt("join", joinBolt ,1)
      .fieldsGrouping("gender", new Fields("id") )
      .fieldsGrouping("age", new Fields("id") )

    builder.setBolt("return", new ReturnBolt, 1).globalGrouping("join")

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
    Signal.finished = false
    Signal.throughput = 0L
    
    val cluster: LocalCluster = new LocalCluster
    cluster.submitTopology("wc", config, builder.createTopology)
    Thread.sleep(3000)
    Signal.finished = false
    Signal.throughput = 0L
    Thread.sleep(3000)
    cluster.killTopology("wc")
    cluster.shutdown

    Signal.throughput.toDouble
  }


  def main(args: Array[String]): Unit = {
    val seed = if (args.length > 0) args(0).toInt else 0xDEADBEEF
    import com.containant.ContainAnt
    object CA extends ContainAnt( WithSeed(seed) )
    val result = CA create (JoinModule, runConfig)
  }

}
