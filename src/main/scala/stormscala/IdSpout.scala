package stormscala

import org.apache.storm.spout.SpoutOutputCollector
import org.apache.storm.task.TopologyContext
import org.apache.storm.topology.OutputFieldsDeclarer
import org.apache.storm.topology.base.BaseRichSpout
import org.apache.storm.tuple.Fields
import org.apache.storm.tuple.Values

import scala.util.Random

class IdSpout extends BaseRichSpout {
  var _collector: SpoutOutputCollector = _
  var _rand: Random = _
  var nextId: Long = 0L;
 
  override def nextTuple: Unit = {
    Thread.sleep(75)
    if (nextId == 0L) nextId = _rand.nextLong % 100 else nextId = nextId + 1L
    _collector.emit( new Values( new java.lang.Long(nextId) ) )
  }

  override def open(conf: java.util.Map[_, _], context: TopologyContext, collector: SpoutOutputCollector): Unit = {
    _collector = collector
    _rand = new Random(0xDEADBEEF)
  }

  override def declareOutputFields(declarer: OutputFieldsDeclarer): Unit = {
    declarer.declare(new Fields("id"))
  }

}
