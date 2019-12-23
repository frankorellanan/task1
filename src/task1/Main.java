package task1;

public class Main {

	public static void main(String[] args) throws Exception {

		JobLogger log = new JobLogger(true, true, true, 
				true, true, true, "F:");
		log.LogMessage("Error Msg", true, true, true);
	}

}
