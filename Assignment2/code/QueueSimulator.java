import java.lang.*;

public class QueueSimulator {
	public enum Event {
		ARRIVAL, DEPARTURE
	};

	private double currTime;
	// arrivalRate stores gamma (average rate of arrival)
	private double arrivalRate;
	// serviceTime stores 1/mu (how many seconds per packet to process)
	private double serviceTime;
	// arrival and depart from buffer queue
	private double timeForNextArrival;
	private double timeForNextDeparture;
	private double simTime;
	LinkedListQueue<Data> buffer = new LinkedListQueue<Data>();
	LinkedListQueue<Data> eventQueue = new LinkedListQueue<Data>();
	private Event e;

	public double getRandTime(double arrivalRate) {
		double num, time1, max = 1, min = 0, randNUM;
		randNUM = Math.random();
		time1 = (-1 / arrivalRate) * (Math.log(1 - randNUM));
		return time1;
	}

	public QueueSimulator(double aR, double servT, double simT) {
		arrivalRate = aR;
		serviceTime = servT;
		simTime = simT;
		timeForNextArrival = getRandTime(arrivalRate);
		timeForNextDeparture = timeForNextArrival + serviceTime;
	}

	public double calcAverageWaitingTime() {
		double NumEvents = 0;
		double totalWaittime = 0;

		while (!eventQueue.isEmpty()) {
			Data curr = eventQueue.dequeue();
			totalWaittime += (curr.getDepartureTime() - curr.getArrivalTime());

			NumEvents++;
		}
		return totalWaittime / NumEvents;
	}

	public double runSimulation() {

		while (currTime < this.simTime) {
			if (timeForNextArrival < timeForNextDeparture || buffer.isEmpty()) {

				e = Event.ARRIVAL;

			} else {

				e = Event.DEPARTURE;
			}
			Data obj = new Data();
			switch (e) {
				case ARRIVAL:

					currTime = timeForNextArrival;
					obj.setArrivalTime(timeForNextArrival);
					buffer.enqueue(obj);
					timeForNextArrival += getRandTime(arrivalRate);

					break;
				case DEPARTURE:

					currTime = timeForNextDeparture;
					obj = buffer.dequeue();
					obj.setDepartureTime(currTime);
					eventQueue.enqueue(obj);

					if (!buffer.isEmpty()) {
						timeForNextDeparture += serviceTime;
					} else {
						timeForNextDeparture = timeForNextArrival + serviceTime;
					}
					break;
			}
		}
		return calcAverageWaitingTime();
	}
}
