package stormscala

import org.apache.storm.topology.BasicOutputCollector
import org.apache.storm.topology.OutputFieldsDeclarer
import org.apache.storm.topology.base.BaseBasicBolt
import org.apache.storm.tuple.Fields
import org.apache.storm.tuple.Tuple
import org.apache.storm.tuple.Values

object Signal {
  var throughput: Long = 0
  var finished: Boolean = false
}

class ReturnBolt extends BaseBasicBolt {
  var throughput: Long = 0

  override def execute(input: Tuple, collector: BasicOutputCollector): Unit = {
    val id1 = input.getLongByField("gender:id")
    val id2 = input.getLongByField("age:id")
    val gender = input.getStringByField("gender")
    val age = input.getLongByField("age")
    throughput = throughput + 1
  }

  override def declareOutputFields(declarer: OutputFieldsDeclarer): Unit = {
    // emits nothing
  }

  override def cleanup: Unit = {
    Signal.throughput = this.throughput
    Signal.finished = true
  }
}
